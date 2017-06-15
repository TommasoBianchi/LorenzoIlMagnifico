package it.polimi.ingsw.LM45.network.client;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.Arrays;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

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
		}
		catch (IOException e) {
			manageIOException(e);
		}
		catch (NotBoundException e) {
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
		}
		else {
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

	// TEST
	private Queue<String> inQueue = new ConcurrentLinkedQueue<>();
	private Integer threadCounter = 0;
	// TEST

	public int chooseFrom(String[] alternatives) {
		// TODO: implement in a sensible way
		// maybe with a call to the view interface

		// TEST		
		int chosenNumber = viewChooseFrom(alternatives);		
		// TEST

		return chosenNumber;
	}
	
	// TEST: this function mimic the work of the view interface (in particular of the CLI)
	private int viewChooseFrom(String[] alternatives){
		int oldThreadCounter = 0;
		synchronized (threadCounter) {
			threadCounter++;
			oldThreadCounter = threadCounter;
		}

		System.out.println("");
		System.out.println("Choose between this things: ");
		System.out.println(Arrays.stream(alternatives).map(s -> "- " + s).reduce("", (a, b) -> a + "\n" + b));
		int chosenNumber = alternatives.length > 0 ? new Random().nextInt(alternatives.length) : 0;

		synchronized (this) {
			String s = (inQueue.isEmpty()) ? ClientMain.scanner.nextLine() : inQueue.remove();
			int newThreadCounter = 0;
			synchronized (threadCounter) {
				newThreadCounter = threadCounter;
			}
			if (oldThreadCounter == newThreadCounter) {
				System.out.println("You have chosen " + alternatives[chosenNumber]);
				System.out.println("");
				return chosenNumber;
			}
			else {
				inQueue.add(s);
				return -1;
			}
		}
	}
	// TEST

	private void manageIOException(IOException e) {
		// TODO: implement better
		e.printStackTrace();

		// Maybe try to reconnect
		// serverInterface = ServerInterfaceFactory.create(connectionType, host, this);
	}

}
