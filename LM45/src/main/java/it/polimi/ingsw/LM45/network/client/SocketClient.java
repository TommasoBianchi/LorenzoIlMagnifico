package it.polimi.ingsw.LM45.network.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.network.server.ServerInterface;
import it.polimi.ingsw.LM45.network.server.ServerMessages;

public class SocketClient implements ClientInterface, ServerInterface, Runnable {

	private Socket socket;
	private ObjectOutputStream outStream;
	private ObjectInputStream inStream;
	private ClientController clientController;
	private boolean isRunning;

	public SocketClient(String host, int port, ClientController clientController) throws UnknownHostException, IOException {
		this.clientController = clientController;
		
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
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
		}
	}

	private void handleMessage(ClientMessages messageType) {
		switch (messageType) {
			// FIXME: fix this 
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
	public void placeFamiliar(FamiliarColor familiarColor, Integer slotID) throws IOException {
		outStream.writeObject(ServerMessages.PLACE_FAMILIAR);	
		outStream.writeObject(familiarColor);			
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

}
