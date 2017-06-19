package it.polimi.ingsw.LM45.network.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

import it.polimi.ingsw.LM45.exceptions.GameException;
import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.Excommunication;
import it.polimi.ingsw.LM45.model.cards.LeaderCard;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.network.client.ClientInterface;
import it.polimi.ingsw.LM45.network.client.ClientMessages;

public class SocketServer implements ClientInterface, ServerInterface, Runnable {

	private Socket socket;
	private ObjectOutputStream outStream;
	private ObjectInputStream inStream;
	private Queue<Object> inputQueue;
	private ReentrantLock inputStreamLock;

	private ServerController serverController;
	private boolean isRunning;
	private String username;

	public SocketServer(Socket socket) throws IOException {
		this.socket = socket;
		this.inputQueue = new ConcurrentLinkedQueue<>();
		this.inputStreamLock = new ReentrantLock();

		outStream = new ObjectOutputStream(socket.getOutputStream());
		inStream = new ObjectInputStream(socket.getInputStream());

		new Thread(this).start();

		isRunning = true;
	}

	@Override
	public void run() {
		while (isRunning) {
			try {
				inputStreamLock.lock();
				Object inputObject = inStream.readObject();
				inputStreamLock.unlock();
				try {
					ServerMessages messageType = (ServerMessages) inputObject;
					handleMessage(messageType);
				} catch (ClassCastException e) {
					boolean queueWasEmpty = inputQueue.isEmpty();
					inputQueue.add(inputObject);
					if (queueWasEmpty) {
						synchronized (inputQueue) {
							inputQueue.notifyAll();
						}
					}
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// here it means the socket has been closed
				try {
					outStream.close();
					inStream.close();
					socket.close();
					isRunning = false;
					serverController.removeUser(username);
					// Release resources that were waiting on the inputQueue
					synchronized (inputQueue) {
						inputQueue.notifyAll();
					}
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
		}
	}

	private void handleMessage(ServerMessages messageType) throws ClassNotFoundException, IOException {
		switch (messageType) {
		case LOGIN:
			String username = (String) inStream.readObject();
			login(username);
			break;
		case PLACE_FAMILIAR:
			FamiliarColor familiarColor = (FamiliarColor) inStream.readObject();
			SlotType slotType = (SlotType) inStream.readObject();
			Integer slotID = (Integer) inStream.readObject();
			placeFamiliar(familiarColor, slotType, slotID);
			break;
		case INCREASE_FAMILIAR_VALUE:
			familiarColor = (FamiliarColor) inStream.readObject();
			increaseFamiliarValue(familiarColor);
			break;
		case PLAY_LEADER:
			String leaderCardName = (String) inStream.readObject();
			playLeaderCard(leaderCardName);
			break;
		case ACTIVATE_LEADER:
			leaderCardName = (String) inStream.readObject();
			activateLeaderCard(leaderCardName);
			break;
		case DISCARD_LEADER:
			leaderCardName = (String) inStream.readObject();
			discardLeaderCard(leaderCardName);
			break;
		case END_TURN:
			endTurn();
			break;

		default:
			break;
		}
	}

	@Override
	public void login(String username) {
		this.username = username;
		serverController = ServerControllerFactory.getServerControllerInstance(username);
		serverController.login(username, this);
	}

	@Override
	public void placeFamiliar(FamiliarColor familiarColor, SlotType slotType, Integer slotID) {
		if (serverController != null) {
			serverController.placeFamiliar(username, familiarColor, slotType, slotID);
		}
	}

	@Override
	public void increaseFamiliarValue(FamiliarColor familiarColor) {
		if (serverController != null) {
			serverController.increaseFamiliarValue(username, familiarColor);
		}
	}

	@Override
	public void playLeaderCard(String leaderCardName) {
		if (serverController != null) {
			serverController.playLeaderCard(username, leaderCardName);
		}
	}

	@Override
	public void activateLeaderCard(String leaderCardName) {
		if (serverController != null) {
			serverController.activateLeaderCard(username, leaderCardName);
		}
	}

	@Override
	public void discardLeaderCard(String leaderCardName) {
		if (serverController != null) {
			serverController.discardLeaderCard(username, leaderCardName);
		}
	}

	@Override
	public void endTurn() {
		if (serverController != null) {
			serverController.endPlayerRound(username);
		}
	}

	@Override
	public void setUsername(String username) throws IOException {
		outStream.writeObject(ClientMessages.SET_USERNAME);
		outStream.writeObject(username);
	}

	@Override
	public void notifyPlayerTurn(String player) throws IOException {
		outStream.writeObject(ClientMessages.NOTIFY_TURN);
		outStream.writeObject(player);
	}

	@Override
	public void throwGameException(GameException gameException) throws IOException {
		outStream.writeObject(ClientMessages.THROW_EXCEPTION);
		outStream.writeObject(gameException);
	}

	@Override
	public int chooseFrom(String[] alternatives) throws IOException {
		outStream.writeObject(ClientMessages.CHOOSE);
		outStream.writeObject(alternatives);
		Integer index = 0;

		// This code is needed to synchronize the inStream.readObject() we need to do here with the fact that there may be
		// another thread (the "socket listener" implemented by the run method) waiting on the same inputStream.
		// The fact that we also may be called from the "socket listener" thread forces us to also consider the case
		// in which no one is waiting on inStream so we have to call readObject on our own.
		if (inputStreamLock.tryLock()) {
			try {
				index = (Integer) inStream.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			inputStreamLock.unlock();
		} else {
			if (inputQueue.isEmpty()) {
				synchronized (inputQueue) {
					try {
						inputQueue.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						System.err.println("SocketServer::chooseFrom() -- " +
								"An InterruptedException occurred while waiting on inputQueue. Propagating interrupt and returning 0");
						e.printStackTrace();
						Thread.currentThread().interrupt();
						return 0;
					}
				}
			}
			index = (Integer) inputQueue.poll();
			if(index == null)
				index = 0;
		}

		return index;
	}

	@Override
	public void pickCard(Card card, String username) throws IOException {
		outStream.writeObject(ClientMessages.PICK_CARD);
		outStream.writeObject(card);
		outStream.writeObject(username);
	}

	@Override
	public void addCardsOnTower(Card[] cards, SlotType slotType) throws IOException {
		outStream.writeObject(ClientMessages.SETUP_TOWER);
		outStream.writeObject(cards);
		outStream.writeObject(slotType);
	}

	@Override
	public void addFamiliar(SlotType slotType, int position, FamiliarColor familiarColor, PlayerColor playerColor) throws IOException {
		outStream.writeObject(ClientMessages.ADD_FAMILIAR);
		outStream.writeObject(slotType);
		outStream.writeObject(new Integer(position));
		outStream.writeObject(familiarColor);
		outStream.writeObject(playerColor);
	}

	@Override
	public void setExcommunications(Excommunication[] excommunications) throws IOException {
		outStream.writeObject(ClientMessages.SETUP_EXCOMMUNICATIONS);
		outStream.writeObject(excommunications);
	}

	@Override
	public void setLeaderCards(LeaderCard[] leaders) throws IOException {
		outStream.writeObject(ClientMessages.SETUP_LEADERS);
		outStream.writeObject(leaders);
	}

	@Override
	public void setFamiliar(String username, FamiliarColor color, int value) throws IOException {
		outStream.writeObject(ClientMessages.SET_FAMILIAR);
		outStream.writeObject(username);
		outStream.writeObject(color);
		outStream.writeObject(new Integer(value));
	}

	@Override
	public void doBonusAction(SlotType slotType, int value) throws IOException {
		outStream.writeObject(ClientMessages.BONUS_ACTION);
		outStream.writeObject(slotType);
		outStream.writeObject(new Integer(value));
	}

	@Override
	public void setResources(Resource[] resources, String username) throws IOException {
		outStream.writeObject(ClientMessages.SET_RESOURCES);
		outStream.writeObject(resources);
		outStream.writeObject(username);
	}

}
