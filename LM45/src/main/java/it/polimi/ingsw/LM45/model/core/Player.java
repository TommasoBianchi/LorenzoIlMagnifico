package it.polimi.ingsw.LM45.model.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.polimi.ingsw.LM45.exceptions.IllegalActionException;
import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.LeaderCard;
import it.polimi.ingsw.LM45.model.effects.ActionModifier;
import it.polimi.ingsw.LM45.model.effects.CardEffect;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;

public class Player {

	private String username;
	private PlayerColor playerColor;
	private List<LeaderCard> leaderCards;
	private PersonalBoard personalBoard;
	private List<Familiar> familiars;
	private PersonalBonusTile personalBonusTile;
	private boolean payIfTowerIsOccupied;
	private List<Resource> churchSupportBonuses;
	private boolean hasToSkipFirstTurn;
	private Resource[] bonusFamiliarDiscount;

	/**
	 * @param username
	 *            the username of this player
	 * @param color
	 *            the color of this player (i.e. of his pawns)
	 */
	public Player(String username, PlayerColor playerColor) {
		this.username = username;
		this.playerColor = playerColor;
		this.leaderCards = new ArrayList<>();
		this.personalBoard = new PersonalBoard();
		this.familiars = new ArrayList<>();
		this.familiars.add(new Familiar(this, FamiliarColor.BLACK));
		this.familiars.add(new Familiar(this, FamiliarColor.WHITE));
		this.familiars.add(new Familiar(this, FamiliarColor.ORANGE));
		this.familiars.add(new Familiar(this, FamiliarColor.UNCOLORED));
		this.personalBonusTile = null; // This needs to be chosen later on by the player
		this.payIfTowerIsOccupied = true;
		this.churchSupportBonuses = new ArrayList<>();
		this.hasToSkipFirstTurn = false;
	}

	/**
	 * @param effectResolutor
	 *            the effectResolutor used to check for this card
	 * @param card
	 *            the card to check
	 * @param actionModifier
	 *            the actionModifier for the action the player is trying to do (in this case, picking a card of a specific cardType)
	 * @return true if we can add that card to this player's personalBoard (including check about territoryRequisites and about card's cost)
	 */
	public boolean canAddCard(EffectResolutor effectResolutor, Card card, ActionModifier actionModifier) {
		return card.canPick(effectResolutor, actionModifier) && personalBoard.canAddCard(card);
	}

	/**
	 * @param card
	 *            the card we want to add to this player's personalBoard
	 */
	public void addCard(Card card, ActionModifier actionModifier) {
		personalBoard.addCard(card);
	}

	/**
	 * @param leaderCard
	 *            the leaderCard we want to add
	 */
	public void addLeaderCard(LeaderCard leaderCard) {
		leaderCards.add(leaderCard);
	}

	/**
	 * @param leaderCard
	 *            the leaderCard we want to play
	 * @throws IllegalActionException
	 *             if the player does not have the requisites for the leaderCard or if it does not have the leaderCard itself
	 */
	public void playLeaderCard(LeaderCard leaderCard) throws IllegalActionException {
		if (leaderCards.contains(leaderCard)) {
			leaderCard.play();
		}
		else {
			throw new IllegalActionException("You cannot play LeaderCard " + leaderCard.getName() + " because you do not have it");
		}
	}

	/**
	 * @param leaderCard
	 *            the leaderCard we want to discard
	 * @throws IllegalActionException
	 *             if the player does not have the leaderCard itself
	 */
	public void discardLeaderCard(LeaderCard leaderCard) throws IllegalActionException {
		if (!leaderCards.remove(leaderCard))
			throw new IllegalActionException("You cannot discard LeaderCard " + leaderCard.getName() + " because you do not have it");
	}

	/**
	 * @param leaderCard
	 *            the leaderCard we want to activate
	 * @throws IllegalActionException
	 *             if the player does not have played yet the leaderCard or if it does not have the leaderCard itself
	 */
	public void activateLeaderCard(LeaderCard leaderCard, EffectResolutor effectResolutor) throws IllegalActionException {
		if (leaderCards.contains(leaderCard)) {
			leaderCard.activate(effectResolutor);
		}
		else {
			throw new IllegalActionException("You cannot activate LeaderCard " + leaderCard.getName() + " because you do not have it");
		}
	}

	/**
	 * @param resource
	 *            the resource we want to add (or remove if amount < 0)
	 */
	public void addResources(Resource resource) {
		if (resource.getAmount() > 0)
			personalBoard.addResources(resource);
		else
			personalBoard.removeResources(resource.multiply(-1)); // To add a negative amount, remove its inverse
	}

	/**
	 * @param resourceType
	 *            the type of resource to count
	 * @return the amount of resources of the given ResourceType
	 */
	public int getResourceAmount(ResourceType resourceType) {
		return personalBoard.getResourceAmount(resourceType);
	}

	/**
	 * @return an array containing all the resources this player has
	 */
	public Resource[] getAllResources() {
		return personalBoard.getAllResources();
	}

	/**
	 * @param resource
	 *            the type of resource to check
	 * @return true if the amount of resource of the same type of the parameter resource is at least the same as the amount of the parameter resource
	 */
	public boolean hasResources(Resource resource) {
		return personalBoard.hasResources(resource);
	}

	/**
	 * @param color
	 *            the familiarColor of the familiar to add the bonus to
	 * @param bonus
	 *            the bonus to add (not including servants bonus)
	 */
	public void addFamiliarBonus(FamiliarColor color, int bonus) {
		for (Familiar familiar : familiars) {
			if (familiar.getFamiliarColor() == color)
				familiar.addBonus(bonus);
		}
	}

	/**
	 * @param color
	 *            the familiarColor of the familiar we want to set the value
	 * @param value
	 *            the value to set
	 */
	public void setFamiliarValue(FamiliarColor color, int value) {
		for (Familiar familiar : familiars) {
			if (familiar.getFamiliarColor() == color)
				familiar.setValue(value);
		}
	}

	/**
	 * @param color
	 *            the familiarColor of the familiar we want to increase the value by paying servants
	 * @throws IllegalActionException
	 */
	public void increaseFamiliarValue(FamiliarColor color) throws IllegalActionException {
		for (Familiar familiar : familiars) {
			if (familiar.getFamiliarColor() == color)
				familiar.addServantsBonus();
		}
	}

	/**
	 * @param cost
	 *            the cost (in servants) of increasing by one the value of this player's familiars
	 */
	public void modifyServantCost(int cost) {
		for (Familiar familiar : familiars) {
			familiar.setServantBonusCost(cost);
		}
	}

	/**
	 * Sets to zero the requisites for the territory cards by setting them all to 0 military points
	 */
	public void noTerritoryRequisites() {
		personalBoard.clearTerritoryRequisites();
	}

	/**
	 * @param resource
	 *            the resource gained when supporting the church
	 */
	public void addChurchSupportBonus(Resource resource) {
		churchSupportBonuses.add(resource);
	}

	/**
	 * @return the username of this player
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return true if this player has to skip the first round of every turn
	 */
	public boolean getHasToSkipFirstRound() {
		return hasToSkipFirstTurn;
	}

	/**
	 * Signals this player will have to skip the first round of every turn
	 */
	public void setHasToSkipFirstRound() {
		hasToSkipFirstTurn = true;
	}

	/**
	 * This method can be used for both setting the permanentEffect of a card and setting the permanentEffect of an excommunication
	 * 
	 * @param permanentEffect
	 *            the CardEffect to add as a permanent effects
	 */
	public void addPermanentEffect(CardEffect permanentEffect) {
		personalBoard.addPermanentEffect(permanentEffect);
	}

	/**
	 * @param effectResolutor
	 *            the effectResolutor we need in order to resolve the harvest effects
	 * @param value
	 *            the value of the harvest action
	 */
	public void harvest(EffectResolutor effectResolutor, int value) {
		personalBonusTile.harvest(effectResolutor, value);
		personalBoard.harvest(effectResolutor, value);
	}

	/**
	 * @param effectResolutor
	 *            the effectResolutor we need in order to resolve the production effects
	 * @param value
	 *            the value of the production action
	 */
	public void produce(EffectResolutor effectResolutor, int value) {
		personalBonusTile.produce(effectResolutor, value);
		personalBoard.produce(effectResolutor, value);
	}


	/**
	 * This has to be called at the end of the game to collect victory points from venture cards
	 * 
	 * @param effectResolutor the effectResolutor needed in order to resolve venture's effects
	 */
	public void resolveVentures(EffectResolutor effectResolutor){
		personalBoard.resolveVentures(effectResolutor);
	}

	/**
	 * @param familiarColor
	 *            the familiarColor of the familiar we want to retrieve
	 * @return a familiar of the given familiarColor (it should be unique, but this method does nothing about that)
	 * @throws IllegalActionException
	 *             if no familiar of the given familiarColor is found
	 */
	public Familiar getFamiliarByColor(FamiliarColor familiarColor) throws IllegalActionException {
		return familiars.stream().filter(familiar -> !familiar.getIsPlaced() && familiar.getFamiliarColor() == familiarColor).findFirst()
				.orElseThrow(() -> new IllegalActionException("Familiar of color " + familiarColor + " does not exists or has already been used"));
	}

	/**
	 * @param familiarColor
	 *            the familiarColor of the familiar we want to retrieve
	 * @return the value of the requested familiar (-1 if the familiar is not present -- should never happen)
	 */
	public int getFamiliarValue(FamiliarColor familiarColor) {
		return familiars.stream().filter(familiar -> familiar.getFamiliarColor() == familiarColor).map(Familiar::getValue).findFirst().orElse(-1);
	}

	/**
	 * @return an array containing all this player's familiars
	 */
	public Familiar[] getFamiliars() {
		return familiars.stream().toArray(Familiar[]::new);
	}

	/**
	 * @return true if this player must pay 3 COINS every time he places a familiar in a tower already occupied by another player
	 */
	public boolean getPayIfTowerIsOccupied() {
		return payIfTowerIsOccupied;
	}

	/**
	 * @return the color this player has on the board
	 */
	public PlayerColor getColor() {
		return playerColor;
	}

	/**
	 * @param personalBonusTile
	 *            the personalBonusTile you want to set to this player
	 */
	public void setPersonalBonusTile(PersonalBonusTile personalBonusTile) {
		this.personalBonusTile = personalBonusTile;
	}

	/**
	 * @return an array of resources containing all the additional resources gained upon church support
	 */
	public Resource[] getChurchSupportBonuses() {
		return churchSupportBonuses.stream().toArray(Resource[]::new);
	}

	/**
	 * @param slotType the slotType the bonus familiar can be placed into
	 * @param value the value of the bonus familiar
	 * @param discount the discount received when picking a card with this bonus familiar
	 */
	public void addBonusFamiliar(SlotType slotType, int value, Resource[] discount) {
		Familiar bonusFamiliar = new Familiar(this, FamiliarColor.BONUS);
		bonusFamiliar.setValue(value);
		this.familiars.add(bonusFamiliar);
		this.bonusFamiliarDiscount = Arrays.stream(discount).map(resource -> resource.getAmount() > 0 ? resource.multiply(-1) : resource)
				.toArray(Resource[]::new); // Make sure the discounts are expressed as negative resources
	}

	/**
	 * removes the bonus familiar because it has been "placed" on the board
	 */
	public void removeBonusFamiliar() {
		familiars.removeIf(familiar -> familiar.getFamiliarColor() == FamiliarColor.BONUS);
	}

	/**
	 * @param slotType
	 *            the slotType in which we are placing a familiar
	 * @param effectResolutor
	 *            the effectResolutor of this player
	 * @return an actionModifier describing all the modifiers from permanentEffects of this player that have to be applied on this action
	 */
	public ActionModifier getActionModifier(SlotType slotType, EffectResolutor effectResolutor) {
		ActionModifier actionModifier = personalBoard.getActionModifier(slotType, effectResolutor);
		if (bonusFamiliarDiscount != null)
			actionModifier.merge(new ActionModifier(bonusFamiliarDiscount));
		return actionModifier;
	}

}
