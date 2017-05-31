package it.polimi.ingsw.LM45.network.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.LM45.network.client.RemoteClientInterface;

public interface RMIRemoteFactory extends Remote {

	public RemoteServerInterface getServerInterface(RemoteClientInterface remoteClient) throws RemoteException;
	
}
