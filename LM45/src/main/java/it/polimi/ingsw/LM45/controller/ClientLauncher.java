package it.polimi.ingsw.LM45.controller;

import it.polimi.ingsw.LM45.network.client.ClientController;
import it.polimi.ingsw.LM45.network.client.ConnectionType;
import it.polimi.ingsw.LM45.view.controller.ViewInterface;
import it.polimi.ingsw.LM45.view.controller.ViewInterfaceFactory;
import it.polimi.ingsw.LM45.view.controller.ViewType;

public class ClientLauncher {

	public static void launch(String username, String host, int port, boolean useRMI, boolean useGUI){
		ConnectionType connectionType = useRMI ? ConnectionType.RMI : ConnectionType.SOCKET;
		ViewType viewType = useGUI ? ViewType.GUI : ViewType.CLI;
		
		ViewInterface viewInterface = ViewInterfaceFactory.create(viewType);
		ClientController clientController = new ClientController(connectionType, host, port, viewInterface);
		viewInterface.setClientController(clientController);
		clientController.login(username);
	}
	
}
