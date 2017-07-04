package it.polimi.ingsw.LM45.network.server;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import it.polimi.ingsw.LM45.network.client.RemoteClientInterface;

public class RMIFactory implements RMIRemoteFactory {
	
	private Registry registry;
	
	public RMIFactory() throws RemoteException {
		registry = LocateRegistry.createRegistry(1099);
		try {
			registry.bind("RMIFactory", (RMIRemoteFactory)UnicastRemoteObject.exportObject(this, 0));
		} catch (AlreadyBoundException e) {
			System.err.println("RMIFactory unable to bind himself on the registry");
			e.printStackTrace();
		}
	}

	@Override
	public RemoteServerInterface getServerInterface(RemoteClientInterface remoteClient) throws RemoteException {
		RMIServer rmiServer = new RMIServer(remoteClient);
		return (RemoteServerInterface)UnicastRemoteObject.exportObject(rmiServer, 0);
	}
	
	public void shutdown(){
		try {
			registry.unbind("RMIFactory");
			UnicastRemoteObject.unexportObject(registry, true);	
		}
		catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}	
	}

}
