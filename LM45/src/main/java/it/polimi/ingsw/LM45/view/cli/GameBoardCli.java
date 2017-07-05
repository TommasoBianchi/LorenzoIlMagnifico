package it.polimi.ingsw.LM45.view.cli;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.LM45.config.BoardConfiguration;
import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.model.cards.Excommunication;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
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
			usersPersonalBoards.put(playersUsername[i], new PersonalBoardCli());
			playerColorName.put(playerColors[i], playersUsername[i]);
		}

		this.excommunications = excommunications.clone();
		this.towers = new HashMap<>();

		for (CardType cardType : new CardType[] { CardType.TERRITORY, CardType.CHARACTER, CardType.BUILDING, CardType.VENTURE })
			towers.put(cardType.toSlotType(), new TowerCli(cardType, boardConfiguration));
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

	public void pickCard(Card card, String username) {
		towers.get(card.getCardType()).removeCard(card);
		// TODO: add card into the correct player's personalBoard
	}

	public void addCardsOnTower(Card[] cards, SlotType slotType) {
		towers.get(slotType).addCards(cards);
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
