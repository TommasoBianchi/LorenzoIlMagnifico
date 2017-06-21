package it.polimi.ingsw.LM45.network.client;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.Arrays;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.sun.media.jfxmedia.events.NewFrameEvent;

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
import it.polimi.ingsw.LM45.view.controller.ViewInterface;

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
		// TODO: implement in a sensible way
		// maybe with a call to the view interface

		// TEST		
		//int chosenNumber = viewChooseFrom(alternatives);		

		boolean fastTest = true;
		if(fastTest) return 0;
		// TEST
		
		int chosenNumber = viewInterface.chooseFrom(alternatives);

		return chosenNumber;
	}
	
	// TEST: this function mimic the work of the view interface (in particular of the CLI)
	/*private Queue<String> inQueue = new ConcurrentLinkedQueue<>();
	private Integer threadCounter = 0;
	private Object threadCounterLockToken = new Object();s
	private int viewChooseFrom(String[] alternatives){
		// See big explanation below
		int oldThreadCounter = 0;
		synchronized (threadCounterLockToken) {
			threadCounter++;
			oldThreadCounter = threadCounter;
		}

		System.out.println("");
		System.out.println("Choose between this things: ");
		System.out.println(Arrays.stream(alternatives).map(s -> "- " + s).reduce("", (a, b) -> a + "\n" + b));
		int chosenNumber = alternatives.length > 0 ? new Random().nextInt(alternatives.length) : 0;

		// All this is to make sure that if more then one thread has come to this point (let's say thread1, thread2, thread3) and 
		// the first one of those has naturally put himself in wait for something from the scanner, then what should happen is this:
		// - thread1 reads from the scanner at a certain point, realized he's no more the most recent thread and so adds the line read
		//		to the inQueue; then he returns -1 which is outside of the valid range of chosable index.
		// - one between thread2 and thread3 will gain the lock on this; in the first case thread2 will read from the inQueue, realize
		// 		he's the wrong thread and so re-add it to the inQueue, while in the second one thread3 will correctly get the line read
		// 		from the inQueue and will process it to return the choice to the caller.
		//
		// NOTE: main assumption here is that if chooseFrom is called a second time while another thread is waiting to resolve a chooseFrom
		// 		 call, then only the last choice will be the relevant one (probably the server has already decided we were too slow to
		//		 choose and so already handled for us the previous choice).
		synchronized (this) {
			String s = (inQueue.isEmpty()) ? ClientMain.scanner.nextLine() : inQueue.remove();
			int newThreadCounter = 0;
			synchronized (threadCounterLockToken) {
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
	}*/
	// TEST
	
	public void pickCard(SlotType slotType, int position, String username) {
		viewInterface.pickCard(slotType, position, username);
	}

	public void addCardsOnTower(Card[] cards, SlotType slotType) {
		viewInterface.addCardsOnTower(cards, slotType);
	}

	public void addFamiliar(SlotType slotType, int position, FamiliarColor familiarColor, PlayerColor playerColor) {
		viewInterface.addFamiliar(slotType, position, familiarColor, playerColor);
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
	
	public void setPersonalBonusTile(String username, PersonalBonusTile personalBonusTile){
		viewInterface.setPersonalBonusTile(username, personalBonusTile);
	}
	
	public void initializeGameBoard(String[] playersUsername, PlayerColor[] playerColors, Excommunication[] excommunications){
		viewInterface.initializeGameBoard(playersUsername, playerColors, excommunications);
	}
	
	public void placeExcommunicationToken(PlayerColor playerColor, PeriodType periodType){
		viewInterface.placeExcommunicationToken(playerColor, periodType);
	}

	private void manageIOException(IOException e) {
		// TODO: implement better
		e.printStackTrace();

		// Maybe try to reconnect
		// serverInterface = ServerInterfaceFactory.create(connectionType, host, this);
	}

}
