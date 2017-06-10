package it.polimi.ingsw.LM45.network.client;

import java.io.IOException;
import java.rmi.NotBoundException;

import it.polimi.ingsw.LM45.network.server.ServerInterface;

public class ServerInterfaceFactory {
	
	private ServerInterfaceFactory(){}

	public static ServerInterface create(ConnectionType connectionType, String host, ClientController clientController) throws IOException, NotBoundException {
		switch (connectionType) {
		case SOCKET:
			return new SocketClient(host, 7000, clientController);
		case RMI:
			return new RMIClient(host, clientController);
		default:
			return new SocketClient(host, 7000, clientController);
		}
	}
	
}
