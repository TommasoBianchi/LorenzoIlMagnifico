package it.polimi.ingsw.LM45.view.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import it.polimi.ingsw.LM45.config.BoardConfiguration;
import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.model.cards.Excommunication;
import it.polimi.ingsw.LM45.model.cards.LeaderCard;
import it.polimi.ingsw.LM45.model.cards.PeriodType;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.PersonalBonusTile;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.network.client.ClientController;
import it.polimi.ingsw.LM45.util.Pair;
import it.polimi.ingsw.LM45.view.cli.ConsoleWriter.ConsoleColor;
import it.polimi.ingsw.LM45.view.cli.GameBoardCliOptions.Stage;

public class GameBoardCli {

	private String myUsername;
	private ClientController clientController;
	private Map<String, PersonalBoardCli> usersPersonalBoards;
	private Map<Excommunication, List<String>> playersExcommunications;
	private Map<PlayerColor, String> playerColorName;

	private Map<SlotType, TowerCli> towers;
	private Map<SlotType, SlotCli[]> otherSlots;
	private Excommunication[] excommunications;
	private String currentPlayer;
	private boolean familiarPlacedThisTurn = false;

	public GameBoardCli(String[] playersUsername, PlayerColor[] playerColors, Excommunication[] excommunications,
			BoardConfiguration boardConfiguration, ClientController clientController) {
		this.myUsername = clientController.getUsername();
		this.clientController = clientController;
		this.usersPersonalBoards = new HashMap<>();
		this.playersExcommunications = new HashMap<>();
		this.playerColorName = new HashMap<>();

		for (int i = 0; i < playersUsername.length; i++) {
			usersPersonalBoards.put(playersUsername[i], new PersonalBoardCli(playersUsername[i], playerColors[i], clientController));
			playerColorName.put(playerColors[i], playersUsername[i]);
		}

		this.excommunications = excommunications.clone();

		for (Excommunication excommunication : this.excommunications)
			this.playersExcommunications.put(excommunication, new ArrayList<>());

		this.towers = new HashMap<>();

		for (CardType cardType : new CardType[] { CardType.TERRITORY, CardType.CHARACTER, CardType.BUILDING, CardType.VENTURE })
			towers.put(cardType.toSlotType(), new TowerCli(cardType, boardConfiguration));

		this.otherSlots = new HashMap<>();

		otherSlots.put(SlotType.COUNCIL, new SlotCli[] { new SlotCli(SlotType.COUNCIL, 0, boardConfiguration.getSlotBonuses(SlotType.COUNCIL, 0)) });

		List<SlotCli> marketSlots = new ArrayList<>();
		marketSlots.add(new SlotCli(SlotType.MARKET, 0, boardConfiguration.getSlotBonuses(SlotType.MARKET, 0)));
		marketSlots.add(new SlotCli(SlotType.MARKET, 1, boardConfiguration.getSlotBonuses(SlotType.MARKET, 1)));
		if (playersUsername.length > 3) {
			marketSlots.add(new SlotCli(SlotType.MARKET, 2, boardConfiguration.getSlotBonuses(SlotType.MARKET, 2)));
			marketSlots.add(new SlotCli(SlotType.MARKET, 3, boardConfiguration.getSlotBonuses(SlotType.MARKET, 3)));
		}
		otherSlots.put(SlotType.MARKET, marketSlots.stream().toArray(SlotCli[]::new));

		List<SlotCli> harvestSlots = new ArrayList<>();
		List<SlotCli> productionSlots = new ArrayList<>();
		harvestSlots.add(new SlotCli(SlotType.HARVEST, 0, boardConfiguration.getSlotBonuses(SlotType.HARVEST, 0)));
		productionSlots.add(new SlotCli(SlotType.PRODUCTION, 0, boardConfiguration.getSlotBonuses(SlotType.PRODUCTION, 0)));
		if (playersUsername.length > 2) {
			harvestSlots.add(new SlotCli(SlotType.HARVEST, 1, boardConfiguration.getSlotBonuses(SlotType.HARVEST, 1)));
			productionSlots.add(new SlotCli(SlotType.PRODUCTION, 1, boardConfiguration.getSlotBonuses(SlotType.PRODUCTION, 1)));
		}
		otherSlots.put(SlotType.HARVEST, harvestSlots.stream().toArray(SlotCli[]::new));
		otherSlots.put(SlotType.PRODUCTION, productionSlots.stream().toArray(SlotCli[]::new));
	}

	public void showMain() {
		printTitle("GameBoard");
		if (familiarPlacedThisTurn)
			GameBoardCliOptions.navigate(Stage.MAIN, this,
					Arrays.asList(new Pair<Consumer<GameBoardCli>, String>(GameBoardCli::endTurn, "End turn")));
		else
			GameBoardCliOptions.navigate(Stage.MAIN, this);
	}

	public void showTowers() {
		printTitle("Towers");
		GameBoardCliOptions.navigate(Stage.TOWERS, this);
	}

	public void showTower(CardType cardType) {
		printTitle(cardType + " tower");
		towers.get(cardType.toSlotType()).print();

		if (currentPlayer.equals(myUsername))
			GameBoardCliOptions.navigate(Stage.SINGLE_TOWER, this,
					towers.get(cardType.toSlotType()).getNonEmptySlots().stream()
							.map(slot -> new Pair<Consumer<GameBoardCli>, String>(
									gameBoard -> gameBoard.placeFamiliar(slot, gb -> gb.showTower(cardType)),
									"Place a familiar on slot " + slot.getNamedID()))
							.collect(Collectors.toList()));
		else
			GameBoardCliOptions.navigate(Stage.SINGLE_TOWER, this);
	}

	public void showOtherSlots() {
		printTitle("Other slots");
		otherSlots.values().stream().flatMap(Arrays::stream).forEach(slot -> {
			slot.print();
			ConsoleWriter.println("");
		});

		if (currentPlayer.equals(myUsername))
			GameBoardCliOptions.navigate(Stage.OTHER_SLOTS, this,
					otherSlots.values().stream().flatMap(Arrays::stream)
							.map(slot -> new Pair<Consumer<GameBoardCli>, String>(
									gameBoard -> gameBoard.placeFamiliar(slot, GameBoardCli::showOtherSlots),
									"Place a familiar on slot " + slot.getNamedID()))
							.collect(Collectors.toList()));
		else
			GameBoardCliOptions.navigate(Stage.OTHER_SLOTS, this);
	}

	public void showExcommunications() {
		printTitle("Excommunications");
		for (Excommunication excommunication : excommunications) {
			ConsoleWriter.println("--------------------");
			ConsoleWriter.printShowInfo(excommunication.toString());
			if (playersExcommunications.get(excommunication).size() > 0) {
				ConsoleWriter.println("");
				ConsoleWriter.printShowInfo(
						"Players that have taken this: " + playersExcommunications.get(excommunication).stream().reduce("", String::concat));
			}
		}
		ConsoleWriter.println("--------------------");
		GameBoardCliOptions.navigate(Stage.EXCOMMUNICATIONS, this);
	}

	public void showPersonalBoards() {
		printTitle("Personal Boards");
		GameBoardCliOptions.navigate(Stage.PERSONAL_BOARDS, this,
				usersPersonalBoards.keySet().stream()
						.map(username -> new Pair<Consumer<GameBoardCli>, String>(gameBoard -> gameBoard.showPersonalBoard(username),
								"Show " + username + "'s Personal Board"))
						.collect(Collectors.toList()));
	}

	public void showPersonalBoard(String username) {
		printTitle(username + "'s Personal Board");
		usersPersonalBoards.get(username).print();
		List<Pair<Consumer<GameBoardCli>, String>> options = new ArrayList<>();
		for (CardType cardType : new CardType[] { CardType.TERRITORY, CardType.CHARACTER, CardType.BUILDING, CardType.VENTURE })
			options.add(new Pair<Consumer<GameBoardCli>, String>(gameBoard -> {
				usersPersonalBoards.get(username).printCards(cardType);
				gameBoard.showPersonalBoard(username);
			}, "Show " + cardType + " cards"));
		options.add(new Pair<Consumer<GameBoardCli>, String>(gameBoard -> {
			usersPersonalBoards.get(username).printLeaderCards();
			gameBoard.showPersonalBoard(username);
		}, "Show leader cards"));
		GameBoardCliOptions.navigate(Stage.SINGLE_PERSONAL_BOARD, this, options);
	}

	public void placeFamiliar(SlotCli slot, Consumer<GameBoardCli> backCallback) {
		ConsoleWriter.println("");
		ConsoleWriter.printChoice("What familiar do you want to place?");
		List<Pair<Consumer<GameBoardCli>, String>> options = usersPersonalBoards.get(myUsername).getUsableFamiliars().stream()
				.map(pair -> new Pair<Consumer<GameBoardCli>, String>(gameBoard -> gameBoard.placeFamiliar(pair._1(), slot),
						"Place familiar " + pair._1() + " (value " + pair._2() + ")"))
				.collect(Collectors.toList());
		options.add(new Pair<Consumer<GameBoardCli>, String>(backCallback, "Back"));
		GameBoardCliOptions.navigate(this, options);
	}

	public void placeFamiliar(FamiliarColor familiarColor, SlotCli slot) {
		clientController.placeFamiliar(familiarColor, slot.getSlotType(), slot.getID());
	}

	public void myTurn() {
		familiarPlacedThisTurn = false;
		currentPlayer = myUsername;
		showMain();
	}

	public void playerTurn(String username) {
		boolean firstTurnEver = currentPlayer == null;
		this.currentPlayer = username;
		if (firstTurnEver)
			showMain();
	}

	/**
	 * @param card
	 *            the card that player has picked
	 * @param username
	 *            the username of the player who picked the card
	 */
	public void pickCard(Card card, String username) {
		towers.get(card.getCardType().toSlotType()).removeCard(card);
		usersPersonalBoards.get(username).addCard(card);
	}

	/**
	 * @param cards
	 *            cards to add to the tower
	 * @param slotType
	 *            the type of tower : BUILDING, TERRITORY, VENTURE and CHARACTER
	 */
	public void addCardsOnTower(Card[] cards, SlotType slotType) {
		towers.get(slotType).addCards(cards);
	}

	/**
	 * @param resources
	 *            resources to set
	 * @param username
	 *            username of the player to set resources
	 */
	public void setResources(Resource[] resources, String username) {
		usersPersonalBoards.get(username).setResources(resources);
	}

	/**
	 * @param username
	 *            username of the player
	 * @param familiarColor
	 *            color of the familiar
	 * @param value
	 *            new value to set to the familiar
	 */
	public void setFamiliarValue(String username, FamiliarColor familiarColor, int value) {
		usersPersonalBoards.get(username).setFamiliarValue(familiarColor, value);
	}

	public void addFamiliar(SlotType slotType, int position, FamiliarColor familiarColor, PlayerColor playerColor) {
		if (towers.containsKey(slotType)) {
			towers.get(slotType).addFamiliar(position, familiarColor, playerColor);
		}
		else {
			otherSlots.get(slotType)[position].placeFamiliar(familiarColor, playerColor);
		}
		usersPersonalBoards.get(playerColorName.get(playerColor)).setFamiliarUsed(familiarColor);

		if (playerColorName.get(playerColor).equals(myUsername)) {
			familiarPlacedThisTurn = true;
			showMain();
		}
		else
			ConsoleWriter
					.printShowInfo("Player " + playerColor + " has placed the " + familiarColor + " familiar on slot" + slotType + " " + position);
	}

	public void setServantCost(int cost) {
		// TODO
	}

	public void doAction() {
		// TODO when select print "Write : familiarColor slotType slotPosition"
	}

	public void doBonusAction(SlotType slotType, int value) {
		// TODO like doAction()
	}

	public void placeExcommunicationToken(PlayerColor playerColor, PeriodType periodType) {
		Excommunication excommunication = excommunications[periodType.ordinal()];
		playersExcommunications.get(excommunication).add(playerColorName.get(playerColor));
	}

	public void setLeaderCards(LeaderCard[] leaders) {
		usersPersonalBoards.get(myUsername).setLeaderCards(leaders);
	}

	public void discardLeaderCard(String username, LeaderCard leader) {
		usersPersonalBoards.get(username).discardLeaderCard(leader);
	}

	public void playLeaderCard(String username, LeaderCard leader) {
		usersPersonalBoards.get(username).playLeaderCard(leader);
	}

	public void activateLeaderCard(String username, LeaderCard leader) {
		usersPersonalBoards.get(username).activateLeaderCard(leader);
	}

	public void enableLeaderCard(String username, LeaderCard leader) {
		usersPersonalBoards.get(username).enableLeaderCard(leader);
	}

	public void setPersonalBonusTile(String username, PersonalBonusTile personalBonusTile) {
		usersPersonalBoards.get(username).setPersonalBonusTile(personalBonusTile);
	}

	public void endTurn() {
		familiarPlacedThisTurn = false;
		clientController.endTurn();
		showMain();
	}

	private void printTitle(String title) {
		ConsoleWriter.println("");
		ConsoleWriter.println("");
		ConsoleWriter.println("============================================================");
		ConsoleWriter.println("============================================================");
		if (currentPlayer.equals(myUsername))
			ConsoleWriter.println(title + " (my turn)", ConsoleColor.BLUE, ConsoleColor.WHITE);
		else
			ConsoleWriter.println(title + " (" + currentPlayer + "'s turn)", ConsoleColor.BLUE, ConsoleColor.WHITE);
		ConsoleWriter.println("");
	}
}
