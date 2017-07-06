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
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.network.client.ClientController;
import it.polimi.ingsw.LM45.util.Pair;

public class PersonalBoardCli {

	private String username;
	private PlayerColor playerColor;
	private ClientController clientController;

	private Map<CardType, List<Card>> cards = new EnumMap<>(CardType.class);
	private Map<ResourceType, Integer> resources = new EnumMap<>(ResourceType.class);

	// String is used to save LeaderCard status : HAND, PLAYED or ACTIVE
	private Map<LeaderCard, String> leaders = new HashMap<>();

	// Boolean is used to save Familiar status : USED = TRUE; while Integer is the Value of the Familiar
	private Map<FamiliarColor, Pair<Integer, Boolean>> familiars = new EnumMap<>(FamiliarColor.class);

	public PersonalBoardCli(String username, PlayerColor playerColor, ClientController clientController) {

		this.username = username;
		this.playerColor = playerColor;
		this.clientController = clientController;

		initialize();

	}

	/**
	 * Used to initialize cards, resources and familiarUsed Maps
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
	public void setFamiliarUsed(FamiliarColor familiarColor){
		int value = familiars.get(familiarColor)._1();
		familiars.put(familiarColor, new Pair<Integer, Boolean>(value, true));
	}

	/**
	 * @param familiarColor
	 *            color of the familiar
	 * @param value
	 *            new value of the familiar
	 */
	public void setFamiliarValue(FamiliarColor familiarColor, int value) {
		Boolean status = familiars.get(familiarColor)._2();
		familiars.put(familiarColor, new Pair<Integer, Boolean>(value, status));
	}

	/**
	 * @return a list of all the familiars that can be placed with their value
	 */
	public List<Pair<FamiliarColor, Integer>> getUsableFamiliars() {
		return familiars.entrySet().stream().filter(entry -> entry.getValue()._2() == false).map(entry -> new Pair<>(entry.getKey(), entry.getValue()._1()))
				.collect(Collectors.toList());
	}

	/**
	 * @param card
	 *            the Card to add
	 */
	public void addCard(Card card) {
		cards.get(card.getCardType()).add(card);
	}

	/**
	 * @param resourcesToSet
	 *            resources to set
	 */
	public void setResources(Resource[] resourcesToSet) {
		Arrays.stream(resourcesToSet).map(resource -> resources.put(resource.getResourceType(), resource.getAmount()));
	}

}
