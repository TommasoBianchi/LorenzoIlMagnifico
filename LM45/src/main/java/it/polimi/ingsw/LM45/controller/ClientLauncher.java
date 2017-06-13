package it.polimi.ingsw.LM45.controller;

import it.polimi.ingsw.LM45.network.client.ClientController;
import it.polimi.ingsw.LM45.network.client.ConnectionType;

public class ClientLauncher {

	public static void launch(String username, String host, int port, boolean useRMI, boolean useGUI){
		ConnectionType connectionType = useRMI ? ConnectionType.RMI : ConnectionType.SOCKET;
		ClientController clientController = new ClientController(connectionType, host, port);
		clientController.login(username);
	}
	
}
