package it.polimi.ingsw.LM45.network.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.polimi.ingsw.LM45.exceptions.GameException;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.network.server.ServerInterface;
import it.polimi.ingsw.LM45.network.server.ServerMessages;
import it.polimi.ingsw.LM45.util.CheckedAction;

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
					outStream.writeObject(index);
				});
				break;
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

}
