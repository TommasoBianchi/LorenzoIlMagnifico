package it.polimi.ingsw.LM45.network.client;

import java.io.IOException;
import java.rmi.NotBoundException;

import it.polimi.ingsw.LM45.config.BoardConfiguration;
import it.polimi.ingsw.LM45.controller.ClientLauncher;
import it.polimi.ingsw.LM45.exceptions.GameException;
import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.Excommunication;
import it.polimi.ingsw.LM45.model.cards.LeaderCard;
import it.polimi.ingsw.LM45.model.cards.PeriodType;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.PersonalBonusTile;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.network.server.ServerInterface;
import it.polimi.ingsw.LM45.view.ViewInterface;

public class ClientController {

	private ConnectionType connectionType;
	private String host;
	private int port;
	private ServerInterface serverInterface;
	private ViewInterface viewInterface;
	private String username;

	public ClientController(ConnectionType connectionType, String host, int port, ViewInterface viewInterface) throws IOException {
		this.connectionType = connectionType;
		this.host = host;
		this.port = port;
		this.viewInterface = viewInterface;

		try {
			serverInterface = ServerInterfaceFactory.create(connectionType, host, port, this);
		}
		catch (NotBoundException e) {
			throw new IOException(e);
		}
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void notifyPlayerTurn(String player) {
		if (this.username.equals(player)) {
			viewInterface.myTurn();
		}
		else {
			viewInterface.playerTurn(player);
		}
	}

	public void throwGameException(GameException gameException) {
		viewInterface.notifyError(gameException.getMessage());
	}

	public void login(String username) {
		try {
			serverInterface.login(username);
		}
		catch (IOException e) {
			manageIOException(e);
		}
	}

	public void placeFamiliar(FamiliarColor familiarColor, SlotType slotType, Integer slotID) {
		try {
			serverInterface.placeFamiliar(familiarColor, slotType, slotID);
		}
		catch (IOException e) {
			manageIOException(e);
		}
	}

	public void increaseFamiliarValue(FamiliarColor familiarColor) {
		try {
			serverInterface.increaseFamiliarValue(familiarColor);
		}
		catch (IOException e) {
			manageIOException(e);
		}
	}

	public void playLeaderCard(String leaderCardName) {
		try {
			serverInterface.playLeaderCard(leaderCardName);
		}
		catch (IOException e) {
			manageIOException(e);
		}
	}

	public void activateLeaderCard(String leaderCardName) {
		try {
			serverInterface.activateLeaderCard(leaderCardName);
		}
		catch (IOException e) {
			manageIOException(e);
		}
	}

	public void discardLeaderCard(String leaderCardName) {
		try {
			serverInterface.discardLeaderCard(leaderCardName);
		}
		catch (IOException e) {
			manageIOException(e);
		}
	}

	public void endTurn() {
		try {
			serverInterface.endTurn();
		}
		catch (IOException e) {
			manageIOException(e);
		}
	}

	public int chooseFrom(String[] alternatives) {
		int chosenNumber = viewInterface.chooseFrom(alternatives);

		return chosenNumber;
	}

	public void pickCard(Card card, String username) {
		viewInterface.pickCard(card, username);
	}

	public void addCardsOnTower(Card[] cards, SlotType slotType) {
		viewInterface.addCardsOnTower(cards, slotType);
	}

	public void addFamiliar(SlotType slotType, int position, FamiliarColor familiarColor, PlayerColor playerColor) {
		viewInterface.addFamiliar(slotType, position, familiarColor, playerColor);
	}
	
	public void setServantCost(int cost){
		viewInterface.setServantCost(cost);
	}

	public void setLeaderCards(LeaderCard[] leaders) {
		viewInterface.setLeaderCards(leaders);
	}

	public void setFamiliar(String username, FamiliarColor color, int value) {
		viewInterface.setFamiliar(username, color, value);
	}

	public void doBonusAction(SlotType slotType, int value) {
		viewInterface.doBonusAction(slotType, value);
	}

	public void setResources(Resource[] resources, String username) {
		viewInterface.setResources(resources, username);
	}

	public void setPersonalBonusTile(String username, PersonalBonusTile personalBonusTile) {
		viewInterface.setPersonalBonusTile(username, personalBonusTile);
	}

	public void initializeGameBoard(String[] playersUsername, PlayerColor[] playerColors, Excommunication[] excommunications,
			BoardConfiguration boardConfiguration) {
		viewInterface.initializeGameBoard(playersUsername, playerColors, excommunications, boardConfiguration);
	}

	public void placeExcommunicationToken(PlayerColor playerColor, PeriodType periodType) {
		viewInterface.placeExcommunicationToken(playerColor, periodType);
	}

	public void playLeaderCard(String username, LeaderCard leader) {
		viewInterface.playLeaderCard(username, leader);
	}

	public void activateLeaderCard(String username, LeaderCard leader) {
		viewInterface.activateLeaderCard(username, leader);
	}

	public void discardLeaderCard(String username, LeaderCard leader) {
		viewInterface.discardLeaderCard(username, leader);
	}

	public void enableLeaderCard(String username, LeaderCard leader) {
		viewInterface.enableLeaderCard(username, leader);
	}

	public void showFinalScore(String[] playersUsername, PlayerColor[] playerColors, int[] scores) {
		viewInterface.showFinalScore(playersUsername, playerColors, scores);
	}

	private void manageIOException(IOException e) {
		try {
			// Try to reconnect
			serverInterface = ServerInterfaceFactory.create(connectionType, host, port, this);
		}
		catch (IOException | NotBoundException e1) {
			// If we fail again, then just stop the client
			e1.initCause(e);
			e1.printStackTrace();
			ClientLauncher.stop();
		}
	}

}
