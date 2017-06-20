package it.polimi.ingsw.LM45.network.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.omg.CORBA.portable.IndirectionException;

import it.polimi.ingsw.LM45.exceptions.GameException;
import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.Excommunication;
import it.polimi.ingsw.LM45.model.cards.LeaderCard;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.PersonalBonusTile;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.network.server.ServerInterface;
import it.polimi.ingsw.LM45.network.server.ServerMessages;
import it.polimi.ingsw.LM45.util.CheckedAction;
import javafx.scene.layout.ConstraintsBase;

public class SocketClient implements ClientInterface, ServerInterface, Runnable {

	private Socket socket;
	private ObjectOutputStream outStream;
	private ObjectInputStream inStream;
	private ClientController clientController;
	private boolean isRunning;
	private ExecutorService executorService;

	public SocketClient(String host, int port, ClientController clientController) throws IOException {
		this.clientController = clientController;
		this.executorService = Executors.newFixedThreadPool(3);

		socket = new Socket(host, port);

		outStream = new ObjectOutputStream(socket.getOutputStream());
		inStream = new ObjectInputStream(socket.getInputStream());

		new Thread(this).start();

		isRunning = true;
	}

	@Override
	public void run() {
		while (isRunning) {
			try {
				ClientMessages messageType = (ClientMessages) inStream.readObject();
				handleMessage(messageType);
			}
			catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e) {
				// here it means the socket has been closed
				try {
					outStream.close();
					inStream.close();
					socket.close();
					isRunning = false;
				}
				catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
		}
	}

	private void handleMessage(ClientMessages messageType) throws ClassNotFoundException, IOException {
		switch (messageType) {
			case SET_USERNAME:
				String username = (String) inStream.readObject();
				performAsync(() -> setUsername(username));
				break;
			case NOTIFY_TURN:
				String player = (String) inStream.readObject();
				performAsync(() -> notifyPlayerTurn(player));
				break;
			case THROW_EXCEPTION:
				GameException gameException = (GameException) inStream.readObject();
				performAsync(() -> throwGameException(gameException));
				break;
			case CHOOSE:
				String[] alternatives = (String[]) inStream.readObject();
				performAsync(() -> {
					Integer index = new Integer(chooseFrom(alternatives));
					if(index >= 0 && index < alternatives.length)
						outStream.writeObject(index);
				});
				break;
			case PICK_CARD:
				Card card = (Card) inStream.readObject();
				username = (String) inStream.readObject();
				performAsync(() -> pickCard(card, username)); 
				break;
			case SETUP_TOWER:
				Card[] cards = (Card[]) inStream.readObject();
				SlotType slotType = (SlotType) inStream.readObject();
				performAsync(() -> addCardsOnTower(cards, slotType)); 
				break;
			case ADD_FAMILIAR:
				slotType = (SlotType) inStream.readObject();
				Integer position = (Integer) inStream.readObject();
				FamiliarColor familiarColor = (FamiliarColor) inStream.readObject();
				PlayerColor playerColor = (PlayerColor) inStream.readObject();				
				performAsync(() -> addFamiliar(slotType, position, familiarColor, playerColor)); 
				break;
			case SETUP_LEADERS:
				LeaderCard[] leaders = (LeaderCard[]) inStream.readObject();
				performAsync(() -> setLeaderCards(leaders)); 
				break;
			case SET_FAMILIAR:
				username = (String) inStream.readObject();
				FamiliarColor color = (FamiliarColor) inStream.readObject();
				Integer value = (Integer) inStream.readObject();
				performAsync(() -> setFamiliar(username, color, value)); 
				break;
			case BONUS_ACTION:
				slotType = (SlotType) inStream.readObject();
				value = (Integer) inStream.readObject();
				performAsync(() -> doBonusAction(slotType, value)); 
				break;
			case SET_RESOURCES:
				Resource[] resources = (Resource[]) inStream.readObject();
				username = (String) inStream.readObject();
				performAsync(() -> setResources(resources, username)); 
				break;
			case SET_PERSONALTILE:
				PersonalBonusTile personalBonusTile = (PersonalBonusTile) inStream.readObject();
				username = (String) inStream.readObject();
				performAsync(() -> setPersonalBonusTile(username, personalBonusTile)); 
				break;
			case INIT_GAMEBOARD:
				String[] playersUsername = (String[]) inStream.readObject();
				PlayerColor[] playerColors = (PlayerColor[]) inStream.readObject();
				Excommunication[] excommunications = (Excommunication[]) inStream.readObject();
				performAsync(() -> initializeGameBoard(playersUsername, playerColors, excommunications));
			default:
				break;
		}
	}

	@Override
	public void login(String username) throws IOException {
		outStream.writeObject(ServerMessages.LOGIN);
		outStream.writeObject(username);
	}

	@Override
	public void placeFamiliar(FamiliarColor familiarColor, SlotType slotType, Integer slotID) throws IOException {
		outStream.writeObject(ServerMessages.PLACE_FAMILIAR);
		outStream.writeObject(familiarColor);
		outStream.writeObject(slotType);
		outStream.writeObject(slotID);
	}

	@Override
	public void increaseFamiliarValue(FamiliarColor familiarColor) throws IOException {
		outStream.writeObject(ServerMessages.INCREASE_FAMILIAR_VALUE);
		outStream.writeObject(familiarColor);
	}

	@Override
	public void playLeaderCard(String leaderCardName) throws IOException {
		outStream.writeObject(ServerMessages.PLAY_LEADER);
		outStream.writeObject(leaderCardName);
	}

	@Override
	public void activateLeaderCard(String leaderCardName) throws IOException {
		outStream.writeObject(ServerMessages.ACTIVATE_LEADER);
		outStream.writeObject(leaderCardName);
	}

	@Override
	public void discardLeaderCard(String leaderCardName) throws IOException {
		outStream.writeObject(ServerMessages.DISCARD_LEADER);
		outStream.writeObject(leaderCardName);
	}

	@Override
	public void endTurn() throws IOException {
		outStream.writeObject(ServerMessages.END_TURN);
	}

	@Override
	public void setUsername(String username) throws IOException {
		clientController.setUsername(username);
	}

	@Override
	public void notifyPlayerTurn(String player) throws IOException {
		clientController.notifyPlayerTurn(player);
	}

	@Override
	public void throwGameException(GameException gameException) throws IOException {
		clientController.throwGameException(gameException);
	}

	@Override
	public int chooseFrom(String[] alternatives) throws IOException {
		return clientController.chooseFrom(alternatives);
	}

	/**
	 * Perform some asynchronous processing. Useful to free as soon as possible the thread
	 * waiting on the inStream
	 * 
	 * @param action the function to execute asynchronously (IOException already handled)
	 */
	private void performAsync(CheckedAction<IOException> action) {
		executorService.submit(() -> {
			try {
				action.apply();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	@Override
	public void pickCard(Card card, String username) {
		clientController.pickCard(card, username);
	}

	@Override
	public void addCardsOnTower(Card[] cards, SlotType slotType) {
		clientController.addCardsOnTower(cards, slotType);
	}

	@Override
	public void addFamiliar(SlotType slotType, int position, FamiliarColor familiarColor, PlayerColor playerColor) {
		clientController.addFamiliar(slotType, position, familiarColor, playerColor);
	}

	@Override
	public void setLeaderCards(LeaderCard[] leaders) {
		clientController.setLeaderCards(leaders);
	}

	@Override
	public void setFamiliar(String username, FamiliarColor color, int value) {
		clientController.setFamiliar(username, color, value);
	}

	@Override
	public void doBonusAction(SlotType slotType, int value) {
		clientController.doBonusAction(slotType, value);
	}

	@Override
	public void setResources(Resource[] resources, String username) {
		clientController.setResources(resources, username);
	}

	@Override
	public void setPersonalBonusTile(String username, PersonalBonusTile personalBonusTile) throws IOException {
		clientController.setPersonalBonusTile(username, personalBonusTile);
	}

	@Override
	public void initializeGameBoard(String[] playersUsername, PlayerColor[] playerColors, Excommunication[] excommunications) throws IOException {
		clientController.initializeGameBoard(playersUsername, playerColors, excommunications);
	}

}
