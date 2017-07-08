package it.polimi.ingsw.LM45.view.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.model.cards.LeaderCard;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.PersonalBonusTile;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.network.client.ClientController;
import it.polimi.ingsw.LM45.util.Pair;

public class PersonalBoardCli {

	private Map<CardType, List<Card>> cards = new EnumMap<>(CardType.class);
	private Map<ResourceType, Integer> resources = new EnumMap<>(ResourceType.class);
	private PersonalBonusTile personalBonusTile;

	// LeaderCardMode is used to save LeaderCard status : HAND, PLAYED or ACTIVE
	private Map<LeaderCard, LeaderCardMode> leaders = new HashMap<>();
	private int numOfLeadersInHand = 4;

	// Boolean is used to save Familiar status : USED = TRUE; while Integer is the Value of the Familiar
	private Map<FamiliarColor, Pair<Integer, Boolean>> familiars = new EnumMap<>(FamiliarColor.class);

	public PersonalBoardCli(String username, PlayerColor playerColor, ClientController clientController) {

		initialize();
	}

	/**
	 * Used to initialize cards, resources, familiars and leaders Maps
	 */
	private void initialize() {
		setFamiliarsUnused();

		cards.put(CardType.TERRITORY, new ArrayList<>());
		cards.put(CardType.BUILDING, new ArrayList<>());
		cards.put(CardType.CHARACTER, new ArrayList<>());
		cards.put(CardType.VENTURE, new ArrayList<>());

		resources.put(ResourceType.COINS, 0);
		resources.put(ResourceType.STONE, 0);
		resources.put(ResourceType.WOOD, 0);
		resources.put(ResourceType.SERVANTS, 0);
		resources.put(ResourceType.VICTORY, 0);
		resources.put(ResourceType.MILITARY, 0);
		resources.put(ResourceType.FAITH, 0);
	}
	
	private enum LeaderCardMode {
		HAND, ACTIVE, PLAYED
	}

	/**
	 * Sets all familiars' booleans to False and values to 0
	 */
	public void setFamiliarsUnused() {
		for (FamiliarColor familiarColor : new FamiliarColor[] { FamiliarColor.BLACK, FamiliarColor.ORANGE, FamiliarColor.UNCOLORED,
				FamiliarColor.WHITE }) {
			familiars.put(familiarColor, new Pair<Integer, Boolean>(0, false));
		}
	}

	/**
	 * @param familiarColor the familiar you want to set as used
	 */
	public void setFamiliarUsed(FamiliarColor familiarColor) {
		int value = familiars.get(familiarColor)._1();
		familiars.put(familiarColor, new Pair<Integer, Boolean>(value, true));
	}

	/**
	 * @param familiarColor color of the familiar
	 * @param value new value of the familiar
	 */
	public void setFamiliarValue(FamiliarColor familiarColor, int value) {
		Boolean status = familiars.get(familiarColor)._2();
		familiars.put(familiarColor, new Pair<Integer, Boolean>(value, status));
	}

	/**
	 * @return a list of all the familiars that can be placed with their value
	 */
	public List<Pair<FamiliarColor, Integer>> getUsableFamiliars() {
		return familiars.entrySet().stream().filter(entry -> entry.getValue()._2() == false)
				.map(entry -> new Pair<>(entry.getKey(), entry.getValue()._1())).collect(Collectors.toList());
	}

	/**
	 * @param card the Card to add
	 */
	public void addCard(Card card) {
		cards.get(card.getCardType()).add(card);
	}

	/**
	 * @param resourcesToSet resources to set
	 */
	public void setResources(Resource[] resourcesToSet) {
		for(Resource resource : resourcesToSet)
			resources.put(resource.getResourceType(), resource.getAmount());
	}

	/**
	 * @param leaders leaders to set in HAND
	 */
	public void setLeaderCards(LeaderCard[] leaders) {
		this.leaders = Arrays.stream(leaders).collect(Collectors.toMap(leader -> leader, leader -> LeaderCardMode.HAND));
	}

	/**
	 * @param leader leader to discard
	 */
	public void discardLeaderCard(LeaderCard leader) {
		leaders.remove(leader);
		numOfLeadersInHand--;
	}

	/**
	 * @param leader leader to play
	 */
	public void playLeaderCard(LeaderCard leader) {
		leaders.put(leader, LeaderCardMode.PLAYED);
	}

	/**
	 * @param leader leader to activate
	 */
	public void activateLeaderCard(LeaderCard leader) {
		leaders.put(leader, LeaderCardMode.ACTIVE);
	}

	/**
	 * @param leader leader to enable
	 */
	public void enableLeaderCard(LeaderCard leader) {
		leaders.put(leader, LeaderCardMode.PLAYED);
	}
	
	/**
	 * @return the list of LeaderCards
	 */
	public List<LeaderCard> getLeaderCards() {
		return leaders.entrySet().stream().map(entry -> entry.getKey()).collect(Collectors.toList());
	}
	
	/**
	 * @param leader the leader card
	 * @return the status of the leader card : HAND, PLAYED or ACTIVE
	 */
	public LeaderCardMode getLeaderStatus(LeaderCard leader) {
		return leaders.get(leader);
	}

	/**
	 * @param personalBonusTile the personalBonusTile of the player
	 */
	public void setPersonalBonusTile(PersonalBonusTile personalBonusTile) {
		this.personalBonusTile = personalBonusTile;
	}

	public void print() {
		ConsoleWriter.printShowInfo(
				resources.entrySet().stream().map(entry -> entry.getValue() + " " + entry.getKey()).reduce("", (a, b) -> a + " -- " + b));
		ConsoleWriter.println("");
		ConsoleWriter.printShowInfo(personalBonusTile.toString());
		ConsoleWriter.println("");
		ConsoleWriter.println("Familiars : " + familiars.entrySet().stream()
				.map(entry -> entry.getKey() + " (value " + entry.getValue()._1() + (entry.getValue()._2() ? ", used)" : ")"))
				.reduce("", (a, b) -> a + " " + b));
		ConsoleWriter.println("");
	}

	/**
	 * @param cardType the type of cards to print
	 */
	public void printCards(CardType cardType) {
		ConsoleWriter.printShowInfo(cards.get(cardType).stream().map(Card::toString).reduce("", (a, b) -> a + "\n\n" + b));
		ConsoleWriter.println("");
	}

	/**
	 * @param areMine boolean that indicates if this is my personal board
	 */
	public void printLeaderCards(Boolean areMine) {
		if(areMine) {
			ConsoleWriter.printShowInfo(
					leaders.entrySet().stream().map(entry -> entry.getValue() + " -- " + entry.getKey()).reduce("", (a, b) -> a + "\n\n" + b));
			ConsoleWriter.println("");
		} else {
			ConsoleWriter.printShowInfo("\n\nHAND -- " + numOfLeadersInHand + " Leaders\n\n");
			if(leaders.containsValue(LeaderCardMode.PLAYED)) {
				ConsoleWriter.printShowInfo(
						leaders.entrySet().stream().filter(entry -> entry.getValue() == LeaderCardMode.PLAYED)
						.map(entry -> entry.getValue() + " -- " + entry.getKey()).reduce("", (a, b) -> a + "\n\n" + b));
				ConsoleWriter.println("");
			}
			if(leaders.containsValue(LeaderCardMode.ACTIVE)) {
				ConsoleWriter.printShowInfo(
						leaders.entrySet().stream().filter(entry -> entry.getValue() == LeaderCardMode.ACTIVE)
						.map(entry -> entry.getValue() + " -- " + entry.getKey()).reduce("", (a, b) -> a + "\n\n" + b));
				ConsoleWriter.println("");
			}
		}
	}

}