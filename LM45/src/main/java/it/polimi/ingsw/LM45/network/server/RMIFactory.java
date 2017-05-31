package it.polimi.ingsw.LM45.network.server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import it.polimi.ingsw.LM45.network.client.RemoteClientInterface;

public class RMIFactory implements RMIRemoteFactory {
	
private ServerController serverController;
	
	public RMIFactory(ServerController serverController) throws RemoteException {
		this.serverController = serverController;

		Registry registry = LocateRegistry.createRegistry(1099);
		try {
			registry.bind("RMIFactory", (RMIRemoteFactory)UnicastRemoteObject.exportObject(this, 0));
		} catch (AlreadyBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public RemoteServerInterface getServerInterface(RemoteClientInterface remoteClient) throws RemoteException {
		RMIServer rmiServer = new RMIServer(serverController, remoteClient);
		return (RemoteServerInterface)UnicastRemoteObject.exportObject(rmiServer, 0);
	}

}
