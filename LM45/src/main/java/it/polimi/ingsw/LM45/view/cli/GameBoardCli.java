package it.polimi.ingsw.LM45.view.cli;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.LM45.config.BoardConfiguration;
import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.model.cards.Excommunication;
import it.polimi.ingsw.LM45.model.cards.PeriodType;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.network.client.ClientController;
import it.polimi.ingsw.LM45.view.cli.ConsoleWriter.ConsoleColor;
import it.polimi.ingsw.LM45.view.cli.GameBoardCliOptions.Stage;

public class GameBoardCli {

	private String myUsername;
	private ClientController clientController;
	private Map<String, PersonalBoardCli> usersPersonalBoards;
	private Map<Excommunication, List<String>> playersExcommunications;
	private Map<PlayerColor, String> playerColorName;

	private Map<SlotType, TowerCli> towers;
	private List<SlotCli> otherSlots;
	private Excommunication[] excommunications;
	private String currentPlayer;

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

		for (Excommunication excommunication : excommunications)
			this.playersExcommunications.put(excommunication, new ArrayList<>());

		this.towers = new HashMap<>();

		for (CardType cardType : new CardType[] { CardType.TERRITORY, CardType.CHARACTER, CardType.BUILDING, CardType.VENTURE })
			towers.put(cardType.toSlotType(), new TowerCli(cardType, boardConfiguration));

		this.otherSlots = new ArrayList<>();

		otherSlots.add(new SlotCli(SlotType.COUNCIL, boardConfiguration.getSlotBonuses(SlotType.COUNCIL, 0)));
		otherSlots.add(new SlotCli(SlotType.MARKET, boardConfiguration.getSlotBonuses(SlotType.MARKET, 0)));
		otherSlots.add(new SlotCli(SlotType.MARKET, boardConfiguration.getSlotBonuses(SlotType.MARKET, 1)));
		if (playersUsername.length > 3) {
			otherSlots.add(new SlotCli(SlotType.MARKET, boardConfiguration.getSlotBonuses(SlotType.MARKET, 2)));
			otherSlots.add(new SlotCli(SlotType.MARKET, boardConfiguration.getSlotBonuses(SlotType.MARKET, 3)));
		}
		otherSlots.add(new SlotCli(SlotType.PRODUCTION, boardConfiguration.getSlotBonuses(SlotType.PRODUCTION, 0)));
		otherSlots.add(new SlotCli(SlotType.HARVEST, boardConfiguration.getSlotBonuses(SlotType.HARVEST, 0)));
		if (playersUsername.length > 2) {
			otherSlots.add(new SlotCli(SlotType.PRODUCTION, boardConfiguration.getSlotBonuses(SlotType.PRODUCTION, 1)));
			otherSlots.add(new SlotCli(SlotType.HARVEST, boardConfiguration.getSlotBonuses(SlotType.HARVEST, 1)));
		}
	}

	public void showMain() {
		printTitle("GameBoard");
		GameBoardCliOptions.navigate(Stage.MAIN, this);
	}

	public void showTowers() {
		printTitle("Towers");
		GameBoardCliOptions.navigate(Stage.TOWERS, this);
	}

	public void showTower(CardType cardType) {
		printTitle(cardType + " tower");
		towers.get(cardType.toSlotType()).print();
		GameBoardCliOptions.navigate(Stage.SINGLE_TOWER, this);
	}

	public void showOtherSlots() {
		printTitle("Other slots");
		otherSlots.stream().forEach(slot -> {
			slot.print();
			ConsoleWriter.println("");
		});
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
						"Players that have taken this: " + playersExcommunications.get(excommunication).stream().reduce(String::concat));
			}
		}
		ConsoleWriter.println("--------------------");
		GameBoardCliOptions.navigate(Stage.EXCOMMUNICATIONS, this);
	}

	public void showPersonalBoards() {

	}

	public void myTurn() {
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
		towers.get(card.getCardType()).removeCard(card);
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
		// TODO addFamiliar if not BONUS color to the correct Slot
		// TODO set familiar used on personalBoard
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

	private void printTitle(String title) {
		ConsoleWriter.println("");
		if (currentPlayer.equals(myUsername))
			ConsoleWriter.println(title + " (my turn)", ConsoleColor.BLUE, ConsoleColor.WHITE);
		else
			ConsoleWriter.println(title + " (" + currentPlayer + "'s turn)", ConsoleColor.BLUE, ConsoleColor.WHITE);
		ConsoleWriter.println("");
	}
}
