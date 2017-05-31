package it.polimi.ingsw.LM45.network.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

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
	public void login(String username) throws RemoteException {
		remoteServer.login(username);
	}

	@Override
	public void placeFamiliar(FamiliarColor familiarColor, Integer slotID) throws RemoteException {
		remoteServer.placeFamiliar(familiarColor, slotID);
	}

	@Override
	public void increaseFamiliarValue(FamiliarColor familiarColor) throws RemoteException {
		remoteServer.increaseFamiliarValue(familiarColor);
	}

	@Override
	public void playLeaderCard(String leaderCardName) throws RemoteException {
		remoteServer.playLeaderCard(leaderCardName);
	}

	@Override
	public void activateLeaderCard(String leaderCardName) throws RemoteException {
		remoteServer.activateLeaderCard(leaderCardName);
	}

	@Override
	public void discardLeaderCard(String leaderCardName) throws RemoteException {
		remoteServer.discardLeaderCard(leaderCardName);
	}

	@Override
	public void endTurn() throws RemoteException {
		remoteServer.endTurn();
	}
	
}
