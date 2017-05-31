package it.polimi.ingsw.LM45.network.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.network.client.ClientInterface;
import it.polimi.ingsw.LM45.network.client.ClientMessages;

public class SocketServer implements ClientInterface, ServerInterface, Runnable {
	
	private Socket socket;
	private ObjectOutputStream outStream;
	private ObjectInputStream inStream;
	private ServerController serverController;
	private boolean isRunning;
	private String username;
	
	public SocketServer(Socket socket, ServerController serverController) throws IOException {
		this.serverController = serverController;
		this.socket = socket;

		outStream = new ObjectOutputStream(socket.getOutputStream());
		inStream = new ObjectInputStream(socket.getInputStream());
		
		new Thread(this).start();
		
		isRunning = true;
	}

	@Override
	public void run() {
		while (isRunning) {
			try {
				ServerMessages messageType = (ServerMessages) inStream.readObject();
				handleMessage(messageType);
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
				String username = (String)inStream.readObject();
				login(username);
				break;
			case PLACE_FAMILIAR:
				FamiliarColor familiarColor = (FamiliarColor)inStream.readObject();
				Integer slotID = (Integer)inStream.readObject();
				placeFamiliar(familiarColor, slotID);
				break;
			case INCREASE_FAMILIAR_VALUE:
				familiarColor = (FamiliarColor)inStream.readObject();
				increaseFamiliarValue(familiarColor);
				break;
			case PLAY_LEADER:
				String leaderCardName = (String)inStream.readObject();
				playLeaderCard(leaderCardName);
				break;
			case ACTIVATE_LEADER:
				leaderCardName = (String)inStream.readObject();
				activateLeaderCard(leaderCardName);
				break;
			case DISCARD_LEADER:
				leaderCardName = (String)inStream.readObject();
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
		serverController.login(username, this);
	}

	@Override
	public void placeFamiliar(FamiliarColor familiarColor, Integer slotID) {
		serverController.placeFamiliar(username, familiarColor, slotID);		
	}

	@Override
	public void increaseFamiliarValue(FamiliarColor familiarColor) {
		serverController.increaseFamiliarValue(username, familiarColor);
	}

	@Override
	public void playLeaderCard(String leaderCardName) {
		serverController.playLeaderCard(username, leaderCardName);
	}

	@Override
	public void activateLeaderCard(String leaderCardName) {
		serverController.activateLeaderCard(username, leaderCardName);
	}

	@Override
	public void discardLeaderCard(String leaderCardName) {
		serverController.discardLeaderCard(username, leaderCardName);
	}

	@Override
	public void endTurn() {
		serverController.endTurn(username);
	}

}
