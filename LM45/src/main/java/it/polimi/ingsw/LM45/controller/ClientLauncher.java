package it.polimi.ingsw.LM45.controller;

import java.io.IOException;

import it.polimi.ingsw.LM45.network.client.ClientController;
import it.polimi.ingsw.LM45.network.client.ConnectionType;
import it.polimi.ingsw.LM45.network.client.ServerInterfaceFactory;
import it.polimi.ingsw.LM45.view.ViewInterface;
import it.polimi.ingsw.LM45.view.ViewInterfaceFactory;
import it.polimi.ingsw.LM45.view.ViewType;

public class ClientLauncher {

	private static ViewType launchedViewType;

	private ClientLauncher() {
	}

	public static void launch(String username, String host, int port, boolean useRMI, boolean useGUI) throws IOException {
		ConnectionType connectionType = useRMI ? ConnectionType.RMI : ConnectionType.SOCKET;
		ViewType viewType = useGUI ? ViewType.GUI : ViewType.CLI;
		launchedViewType = viewType;

		ViewInterface viewInterface = ViewInterfaceFactory.create(viewType);
		ClientController clientController = new ClientController(connectionType, host, port, viewInterface);
		viewInterface.setClientController(clientController);
		clientController.login(username);
		viewInterface.showLeaderCardChoiceView();
	}

	public static void stop(ViewType viewTypeToStop) {
		if (launchedViewType == viewTypeToStop) {
			ServerInterfaceFactory.stop();
			System.out.println("I have been stopped");
		}
	}

}
