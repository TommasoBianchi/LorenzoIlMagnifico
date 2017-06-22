package it.polimi.ingsw.LM45.network.client;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.LM45.network.server.ServerInterface;

public class ServerInterfaceFactory {
	
	private static List<SocketClient> createdSocketClients = new ArrayList<>();
	private static List<RMIClient> createdRMIClients = new ArrayList<>();

	private ServerInterfaceFactory() {}

	public static ServerInterface create(ConnectionType connectionType, String host, int port, ClientController clientController)
			throws IOException, NotBoundException {
		switch (connectionType) {
			case SOCKET:
				SocketClient socketClient = new SocketClient(host, port, clientController);
				createdSocketClients.add(socketClient);
				return socketClient;
			case RMI:
				RMIClient rmiClient = new RMIClient(host, clientController);
				createdRMIClients.add(rmiClient);
				return rmiClient;
			default:
				socketClient = new SocketClient(host, port, clientController);
				createdSocketClients.add(socketClient);
				return socketClient;
		}
	}
	
	public static void stop(){
		createdSocketClients.forEach(SocketClient::stop);
		createdRMIClients.forEach(RMIClient::stop);
	}

}
