package it.polimi.ingsw.LM45.network.client;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.Arrays;
import java.util.Random;

import it.polimi.ingsw.LM45.exceptions.GameException;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.network.server.ServerInterface;
import it.polimi.ingsw.LM45.view.controller.ViewInterface;

public class ClientController {

	private ConnectionType connectionType;
	private String host;
	private int port;
	private ServerInterface serverInterface;
	private ViewInterface viewInterface;
	private String username;

	public ClientController(ConnectionType connectionType, String host, int port, ViewInterface viewInterface) {
		this.connectionType = connectionType;
		this.host = host;
		this.port = port;
		this.viewInterface = viewInterface;
		
		try {
			serverInterface = ServerInterfaceFactory.create(connectionType, host, port, this);
		} catch (IOException e) {
			manageIOException(e);
		} catch (NotBoundException e) {
			// Wrap the NotBoundException in a IOException and manage it
			manageIOException(new IOException(e));
		}
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void notifyPlayerTurn(String player) {
		if (this.username.equals(player)) {
			viewInterface.myTurn();
			System.out.println("It's my turn");
		} else {
			viewInterface.playerTurn(player);
			System.out.println("It's " + player + " turn");
		}
	}

	public void throwGameException(GameException gameException) {
		System.err.println("-- Server sent an exception --");
		System.err.println(gameException.getMessage());
	}

	public void login(String username) {
		try {
			serverInterface.login(username);
		} catch (IOException e) {
			manageIOException(e);
		}
	}

	public void placeFamiliar(FamiliarColor familiarColor, SlotType slotType, Integer slotID) {
		try {
			serverInterface.placeFamiliar(familiarColor, slotType, slotID);
		} catch (IOException e) {
			manageIOException(e);
		}
	}

	public void increaseFamiliarValue(FamiliarColor familiarColor) {
		try {
			serverInterface.increaseFamiliarValue(familiarColor);
		} catch (IOException e) {
			manageIOException(e);
		}
	}

	public void playLeaderCard(String leaderCardName) {
		try {
			serverInterface.playLeaderCard(leaderCardName);
		} catch (IOException e) {
			manageIOException(e);
		}
	}

	public void activateLeaderCard(String leaderCardName) {
		try {
			serverInterface.activateLeaderCard(leaderCardName);
		} catch (IOException e) {
			manageIOException(e);
		}
	}

	public void discardLeaderCard(String leaderCardName) {
		try {
			serverInterface.discardLeaderCard(leaderCardName);
		} catch (IOException e) {
			manageIOException(e);
		}
	}

	public void endTurn() {
		try {
			serverInterface.endTurn();
		} catch (IOException e) {
			manageIOException(e);
		}
	}
	
	public int chooseFrom(String[] alternatives) {
		// TODO: implement in a sensible way
		// maybe with a call to the view interface
		
		// TEST
		System.out.println("");
		System.out.println("Choose between this things: ");
		System.out.println(Arrays.stream(alternatives).reduce("", (a, b) -> a + "\n" + b));
		int chosenNumber = alternatives.length > 0 ? new Random().nextInt(alternatives.length) : 0;
		System.out.println("");
		System.out.println("You have chosen " + alternatives[chosenNumber]);
		System.out.println("");
		// TEST
		
		return chosenNumber;
	}

	private void manageIOException(IOException e) {		
		// TODO: implement better
		e.printStackTrace();
		
		// Maybe try to reconnect
		//serverInterface = ServerInterfaceFactory.create(connectionType, host, this);
	}

}
