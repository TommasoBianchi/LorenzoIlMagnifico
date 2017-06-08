package it.polimi.ingsw.LM45.model.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.scene.paint.Color;
import it.polimi.ingsw.LM45.exceptions.IllegalActionException;
import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.LeaderCard;
import it.polimi.ingsw.LM45.model.effects.CardEffect;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;

public class Player {

	private String username;
	private Color color;
	private List<LeaderCard> leaderCards;
	private PersonalBoard personalBoard;
	private Familiar[] familiars;
	private PersonalBonusTile personalBonusTile;
	private boolean payIfTowerIsOccupied;
	private List<Resource> churchSupportBonuses;
	private boolean hasToSkipFirstTurn;

	public Player(String username, Color color) {
		this.username = username;
		this.color = color;
		this.leaderCards = new ArrayList<LeaderCard>();
		this.personalBoard = new PersonalBoard();
		this.familiars = Arrays.stream(FamiliarColor.values()).map(familiarColor -> new Familiar(this, familiarColor))
				.toArray(Familiar[]::new);
		this.personalBonusTile = null; // This needs to be chosen later on by
										// the player
		this.payIfTowerIsOccupied = true;
		this.churchSupportBonuses = new ArrayList<Resource>();
		this.hasToSkipFirstTurn = false;
	}

	public boolean canAddCard(Card card) {
		return card.canPick(this) && personalBoard.canAddCard(card);
	}

	public void addCard(Card card) {
		personalBoard.addCard(card);
	}

	public void addLeaderCard(LeaderCard leaderCard) {
		if (leaderCards == null)
			leaderCards = new ArrayList<LeaderCard>();
		leaderCards.add(leaderCard);
	}

	public void playLeaderCard(LeaderCard leaderCard) {
		// TODO: implement
	}
	
	public void discardLeaderCard(LeaderCard leaderCard){
		leaderCards.remove(leaderCard);
		// TODO: add a council privilege (maybe we can do this in the controller)
	}
	
	/*public activateLeaderCard(leaderCard: LeaderCard) : void	*/

	public void addResources(Resource resource) {
		if (resource.getAmount() > 0)
			personalBoard.addResources(resource);
		else
			personalBoard.removeResources(resource);
	}

	public int getResourceAmount(ResourceType resourceType) {
		return personalBoard.getResourceAmount(resourceType);
	}

	public boolean hasResources(Resource resource) {
		return personalBoard.hasResources(resource);
	}

	public void addFamiliarBonus(FamiliarColor color, int bonus) {
		for (Familiar familiar : familiars) {
			if (familiar.getFamiliarColor() == color)
				familiar.addBonus(bonus);
		}
	}

	public void setFamiliarValue(FamiliarColor color, int value) {
		for (Familiar familiar : familiars) {
			if (familiar.getFamiliarColor() == color)
				familiar.setValue(value);
		}
	}

	public void increaseFamiliarValue(FamiliarColor color) {
		for (Familiar familiar : familiars) {
			if (familiar.getFamiliarColor() == color)
				familiar.addServantsBonus();
		}
	}

	public void modifyServantCost(int cost) {
		for (Familiar familiar : familiars) {
			familiar.setServantBonusCost(cost);
		}
	}
		
	public void noTerritoryRequisites() {
		personalBoard.clearTerritoryRequisites();
	}
	
	public void addChurchSupportBonus(Resource resource) {
		churchSupportBonuses.add(resource);
	}
	
	public String getUsername() {
		return username;
	}
	
	public boolean getHasToSkipFirstTurn() {
		return hasToSkipFirstTurn;
	}
	
	public void setHasToSkipFirstTurn() {
		hasToSkipFirstTurn = true;
	}
	
	// Use this also to add excommunications
	public void addPermanentEffect(CardEffect permanentEffect){
		personalBoard.addPermanentEffect(permanentEffect);
	}
	
	public void harvest(EffectResolutor effectResolutor, int value){
		personalBoard.harvest(effectResolutor, value);
	}
	
	public void produce(EffectResolutor effectResolutor, int value){
		personalBoard.produce(effectResolutor, value);
	}	

	public Familiar getFamiliarByColor(FamiliarColor familiarColor) throws IllegalActionException {
		return Arrays.stream(familiars)
				.filter(familiar -> !familiar.getIsPlaced() && familiar.getFamiliarColor() == familiarColor).findFirst()
				.orElseThrow(() -> new IllegalActionException(
						"Familiar of color " + familiarColor + " does not exists or has already been used"));
	}

}
