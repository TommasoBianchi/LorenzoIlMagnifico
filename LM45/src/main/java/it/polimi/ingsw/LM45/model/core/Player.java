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
	private Color color; // FIXME: probably unneeded here
	private List<LeaderCard> leaderCards;
	private PersonalBoard personalBoard;
	private Familiar[] familiars;
	private PersonalBonusTile personalBonusTile;
	private boolean payIfTowerIsOccupied;
	private List<Resource> churchSupportBonuses;
	private boolean hasToSkipFirstTurn;

	/**
	 * @param username the username of this player
	 * @param color the color of this player (i.e. of his pawns)
	 */
	public Player(String username, Color color) {
		this.username = username;
		this.color = color;
		this.leaderCards = new ArrayList<LeaderCard>();
		this.personalBoard = new PersonalBoard();
		this.familiars = Arrays.stream(FamiliarColor.values()).map(familiarColor -> new Familiar(this, familiarColor)).toArray(Familiar[]::new);
		this.personalBonusTile = null; // This needs to be chosen later on by the player
		this.payIfTowerIsOccupied = true;
		this.churchSupportBonuses = new ArrayList<Resource>();
		this.hasToSkipFirstTurn = false;
	}

	/**
	 * @param card the card to check
	 * @return true if we can add that card to this player's personalBoard (including check about territoryRequisites and about card's cost)
	 */
	public boolean canAddCard(Card card) {
		return card.canPick(this) && personalBoard.canAddCard(card);
	}

	/**
	 * @param card the card we want to add to this player's personalBoard
	 */
	public void addCard(Card card) {
		personalBoard.addCard(card);
	}

	/**
	 * @param leaderCard the leaderCard we want to add
	 */
	public void addLeaderCard(LeaderCard leaderCard) {
		if (leaderCards == null)
			leaderCards = new ArrayList<LeaderCard>();
		leaderCards.add(leaderCard);
	}

	/**
	 * @param leaderCard the leaderCard we want to play
	 * @throws IllegalActionException if the player does not have the requisites for the leaderCard or if it does not have
	 * the leaderCard itself
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
	 * @param leaderCard the leaderCard we want to discard
	 * @throws IllegalActionException if the player does not have the leaderCard itself
	 */
	public void discardLeaderCard(LeaderCard leaderCard) throws IllegalActionException {
		if (!leaderCards.remove(leaderCard))
			throw new IllegalActionException("You cannot discard LeaderCard " + leaderCard.getName() + " because you do not have it");
	}
	
	/**
	 * @param leaderCard the leaderCard we want to activate
	 * @throws IllegalActionException if the player does not have played yet the leaderCard or if it does not have
	 * the leaderCard itself
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
	 * @param resource the resource we want to add (or remove if amount < 0)
	 */
	public void addResources(Resource resource) {
		if (resource.getAmount() > 0)
			personalBoard.addResources(resource);
		else
			personalBoard.removeResources(resource);
	}

	/**
	 * @param resourceType the type of resource to count
	 * @return the amount of resources of the given ResourceType
	 */
	public int getResourceAmount(ResourceType resourceType) {
		return personalBoard.getResourceAmount(resourceType);
	}

	/**
	 * @param resource the type of resource to check
	 * @return true if the amount of resource of the same type of the parameter resource
	 * is at least the same as the amount of the parameter resource
	 */
	public boolean hasResources(Resource resource) {
		return personalBoard.hasResources(resource);
	}

	/**
	 * @param color the familiarColor of the familiar to add the bonus to
	 * @param bonus the bonus to add (not including servants bonus)
	 */
	public void addFamiliarBonus(FamiliarColor color, int bonus) {
		for (Familiar familiar : familiars) {
			if (familiar.getFamiliarColor() == color)
				familiar.addBonus(bonus);
		}
	}

	/**
	 * @param color the familiarColor of the familiar we want to set the value
	 * @param value the value to set
	 */
	public void setFamiliarValue(FamiliarColor color, int value) {
		for (Familiar familiar : familiars) {
			if (familiar.getFamiliarColor() == color)
				familiar.setValue(value);
		}
	}

	/**
	 * @param color the familiarColor of the familiar we want to increase the value by paying servants
	 */
	public void increaseFamiliarValue(FamiliarColor color) {
		for (Familiar familiar : familiars) {
			if (familiar.getFamiliarColor() == color)
				familiar.addServantsBonus();
		}
	}

	/**
	 * @param cost the cost (in servants) of increasing by one the value of this player's familiars 
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
	 * @param resource the resource gained when supporting the church
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
	 * This method can be used for both setting the permanentEffect of a card
	 * and setting the permanentEffect of an excommunication
	 * @param permanentEffect the CardEffect to add as a permanent effects
	 */
	public void addPermanentEffect(CardEffect permanentEffect) {
		personalBoard.addPermanentEffect(permanentEffect);
	}

	/**
	 * @param effectResolutor the effectResolutor we need in order to resolve the harvest effects
	 * @param value the value of the harvest action
	 */
	public void harvest(EffectResolutor effectResolutor, int value) {
		personalBonusTile.harvest(effectResolutor, value);
		personalBoard.harvest(effectResolutor, value);
	}

	/**
	 * @param effectResolutor the effectResolutor we need in order to resolve the production effects
	 * @param value the value of the production action
	 */
	public void produce(EffectResolutor effectResolutor, int value) {
		personalBonusTile.produce(effectResolutor, value);
		personalBoard.produce(effectResolutor, value);
	}

	/**
	 * @param familiarColor the familiarColor of the familiar we want to retrieve
	 * @return a familiar of the given familiarColor (it should be unique, but this method does nothing about that)
	 * @throws IllegalActionException if no familiar of the given familiarColor is found
	 */
	public Familiar getFamiliarByColor(FamiliarColor familiarColor) throws IllegalActionException {
		return Arrays.stream(familiars).filter(familiar -> !familiar.getIsPlaced() && familiar.getFamiliarColor() == familiarColor).findFirst()
				.orElseThrow(() -> new IllegalActionException("Familiar of color " + familiarColor + " does not exists or has already been used"));
	}

}
