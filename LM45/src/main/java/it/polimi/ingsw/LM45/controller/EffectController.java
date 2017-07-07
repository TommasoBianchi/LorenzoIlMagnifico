package it.polimi.ingsw.LM45.controller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.model.cards.LeaderCard;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.model.effects.CardEffect;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;
import it.polimi.ingsw.LM45.model.effects.modifiers.NilModifier;
import it.polimi.ingsw.LM45.model.effects.modifiers.ResourceModifier;
import it.polimi.ingsw.LM45.network.server.ServerController;

public class EffectController implements EffectResolutor {

	private Player player;
	private ServerController serverController;

	public EffectController(Player player, ServerController serverController) {
		this.player = player;
		this.serverController = serverController;
	}

	public void addResources(Resource resource) {
		Map<ResourceType, ResourceModifier> gainModifiers = player.getActionModifier(SlotType.ANY, this).getGainModifiers();
		Resource modifiedResource = resource.getAmount() > 0
				? gainModifiers.getOrDefault(resource.getResourceType(), new NilModifier(resource.getResourceType())).modify(resource) : resource;

		if (modifiedResource.getResourceType() == ResourceType.COUNCIL_PRIVILEGES) {
			Set<ResourceType> chosenResourcesTypes = new HashSet<>();
			Resource[][] resourcesToChooseFrom = new Resource[][] {
					new Resource[] { new Resource(ResourceType.WOOD, 1), new Resource(ResourceType.STONE, 1) },
					new Resource[] { new Resource(ResourceType.SERVANTS, 2) }, new Resource[] { new Resource(ResourceType.COINS, 2) },
					new Resource[] { new Resource(ResourceType.MILITARY, 2) }, new Resource[] { new Resource(ResourceType.FAITH, 1) } };

			for (int i = 0; i < modifiedResource.getAmount(); i++) {
				int chosenIndex = serverController.chooseFrom(player.getUsername(), Arrays.stream(resourcesToChooseFrom)
						.map(resources -> Arrays.stream(resources).map(Resource::toString).reduce("", (a, b) -> a + " " + b)).toArray(String[]::new));
				Resource[] choosenResources = resourcesToChooseFrom[chosenIndex];
				Arrays.stream(choosenResources).forEach(res -> {
					player.addResources(res);
					chosenResourcesTypes.add(res.getResourceType());
				});
				resourcesToChooseFrom = Arrays.stream(resourcesToChooseFrom).filter(resources -> resources != choosenResources)
						.toArray(Resource[][]::new);
			}

			// Notify all players only of the resources that have changed (because the player chosed to take them in
			// exchange of a COUNCIL_PRIVILEGE)
			Resource[] changedResources = chosenResourcesTypes.stream()
					.map(resourceType -> new Resource(resourceType, player.getResourceAmount(resourceType))).toArray(Resource[]::new);
			serverController.notifyPlayers(clientInterface -> clientInterface.setResources(changedResources, player.getUsername()));
		}
		else {
			player.addResources(modifiedResource);

			// Notify all players only of the resource that has changed
			serverController.notifyPlayers(clientInterface -> clientInterface.setResources(
					new Resource[] { new Resource(modifiedResource.getResourceType(), player.getResourceAmount(modifiedResource.getResourceType())) },
					player.getUsername()));
		}
	}

	public int getResourceAmount(ResourceType resourceType) {
		return player.getResourceAmount(resourceType);
	}

	public boolean hasResources(Resource resource) {
		return player.hasResources(resource);
	}

	public void addChurchSupportBonus(Resource resource) {
		player.addChurchSupportBonus(resource);
	}

	public void addFamiliarBonus(FamiliarColor color, int bonus) {
		player.addFamiliarBonus(color, bonus);
		serverController.notifyPlayers(clientInterface -> clientInterface.setFamiliar(player.getUsername(), color, player.getFamiliarValue(color)));
	}

	public void setFamiliarValue(FamiliarColor color, int bonus) {
		player.setFamiliarValue(color, bonus);
		serverController.notifyPlayers(clientInterface -> clientInterface.setFamiliar(player.getUsername(), color, player.getFamiliarValue(color)));
	}

	public void modifyServantCost(int servantBonusCostModifier) {
		player.modifyServantCost(servantBonusCostModifier);
		serverController.notifyPlayers(clientInterface -> clientInterface.setServantCost(servantBonusCostModifier),
				username -> username.equals(player.getUsername()));
	}

	public void setHasToSkipFirstRound() {
		player.setHasToSkipFirstRound();
	}

	public void noTerritoryRequisites() {
		player.noTerritoryRequisites();
	}

	@Override
	public void setPayIfTowerIsOccupied(boolean value) {
		player.setPayIfTowerIsOccupied(value);
	}

	public void addPermanentEffect(CardEffect permanentEffect) {
		player.addPermanentEffect(permanentEffect);
	}

	public boolean canAddCard(Card card) {
		return player.canAddCard(card);
	}

	public void addCard(Card card) {
		player.addCard(card);
		serverController.notifyPlayers(clientInterface -> clientInterface.pickCard(card, player.getUsername()));
	}

	public void doBonusAction(SlotType slotType, int diceNumber, Resource[] discount) {
		serverController.doBonusAction(player.getUsername(), slotType, diceNumber, discount);
	}

	public void harvest(int value) {
		player.harvest(this, value);
	}

	public void produce(int value) {
		player.produce(this, value);
	}

	public Resource[] getCardsTotalCost(CardType cardType) {
		return player.getCardsTotalCost(cardType);
	}

	public CardEffect copyEffect() {
		LeaderCard[] playedLeaderCards = serverController.getAllPlayedLeaderCards();
		List<LeaderCard> myPlayerPlayedLeaderCards = Arrays.asList(player.getPlayedLeaderCards());
		LeaderCard[] chosableLeaderCards = Arrays.stream(playedLeaderCards).filter(leaderCard -> !myPlayerPlayedLeaderCards.contains(leaderCard))
				.toArray(LeaderCard[]::new);
		LeaderCard chosenLeaderCard = chooseFrom(chosableLeaderCards);
		return chosenLeaderCard.getEffect();
	}

	public <T> T chooseFrom(T[] alternatives) {
		if (serverController.isMyTurn(player)) {
			int index = serverController.chooseFrom(player.getUsername(), Arrays.stream(alternatives).map(Object::toString).toArray(String[]::new));
			if (index < 0 || index >= alternatives.length)
				index = new Random().nextInt(alternatives.length);
			return alternatives[index];
		}
		else
			return alternatives[new Random().nextInt(alternatives.length)];
	}

}
