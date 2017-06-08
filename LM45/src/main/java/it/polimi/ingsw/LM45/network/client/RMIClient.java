package it.polimi.ingsw.LM45.network.client;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import it.polimi.ingsw.LM45.exceptions.GameException;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.network.server.RMIRemoteFactory;
import it.polimi.ingsw.LM45.network.server.RemoteServerInterface;
import it.polimi.ingsw.LM45.network.server.ServerInterface;

public class RMIClient implements RemoteClientInterface, ServerInterface {

	private ClientController clientController;
	private RemoteServerInterface remoteServer;

	public RMIClient(String host, ClientController clientController) throws RemoteException, NotBoundException {
		this.clientController = clientController;
		
		Registry registry = LocateRegistry.getRegistry(host);
		remoteServer = ((RMIRemoteFactory) registry.lookup("RMIFactory"))
				.getServerInterface((RemoteClientInterface) UnicastRemoteObject.exportObject(this, 0));
	}

	@Override
	public void login(String username) throws IOException {
		remoteServer.login(username);
	}

	@Override
	public void placeFamiliar(FamiliarColor familiarColor, SlotType slotType, Integer slotID) throws IOException {
		remoteServer.placeFamiliar(familiarColor, slotType, slotID);
	}

	@Override
	public void increaseFamiliarValue(FamiliarColor familiarColor) throws IOException {
		remoteServer.increaseFamiliarValue(familiarColor);
	}

	@Override
	public void playLeaderCard(String leaderCardName) throws IOException {
		remoteServer.playLeaderCard(leaderCardName);
	}

	@Override
	public void activateLeaderCard(String leaderCardName) throws IOException {
		remoteServer.activateLeaderCard(leaderCardName);
	}

	@Override
	public void discardLeaderCard(String leaderCardName) throws IOException {
		remoteServer.discardLeaderCard(leaderCardName);
	}

	@Override
	public void endTurn() throws IOException {
		remoteServer.endTurn();
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
	
}
