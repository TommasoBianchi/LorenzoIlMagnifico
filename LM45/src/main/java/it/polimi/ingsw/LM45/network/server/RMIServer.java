package it.polimi.ingsw.LM45.network.server;

import java.io.IOException;
import java.rmi.RemoteException;

import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.network.client.ClientInterface;
import it.polimi.ingsw.LM45.network.client.RemoteClientInterface;

public class RMIServer implements RemoteServerInterface, ClientInterface {
	
	private ServerController serverController;
	private RemoteClientInterface remoteClient;
	private String username;
	
	public RMIServer(ServerController serverController, RemoteClientInterface remoteClient) throws RemoteException {
		this.serverController = serverController;
		this.remoteClient = remoteClient;
	}

	@Override
	public void login(String username) throws RemoteException {
		this.username = username;
		serverController.login(username, this);
	}

	@Override
	public void placeFamiliar(FamiliarColor familiarColor, Integer slotID) throws RemoteException {
		serverController.placeFamiliar(username, familiarColor, slotID);
	}

	@Override
	public void increaseFamiliarValue(FamiliarColor familiarColor) throws RemoteException {
		serverController.increaseFamiliarValue(username, familiarColor);
	}

	@Override
	public void playLeaderCard(String leaderCardName) throws RemoteException {
		serverController.playLeaderCard(username, leaderCardName);
	}

	@Override
	public void activateLeaderCard(String leaderCardName) throws RemoteException {
		serverController.activateLeaderCard(username, leaderCardName);
	}

	@Override
	public void discardLeaderCard(String leaderCardName) throws RemoteException {
		serverController.discardLeaderCard(username, leaderCardName);
	}

	@Override
	public void endTurn() throws RemoteException {
		serverController.endTurn(username);
	}

	@Override
	public void setUsername(String username) throws IOException {
		remoteClient.setUsername(username);
	}

	@Override
	public void notifyPlayerTurn(String player) throws IOException {
		remoteClient.notifyPlayerTurn(player);
	}

}
