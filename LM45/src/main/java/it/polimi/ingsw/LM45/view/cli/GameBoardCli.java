package it.polimi.ingsw.LM45.view.cli;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.LM45.model.cards.Excommunication;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.network.client.ClientController;
import it.polimi.ingsw.LM45.view.gui.personalBoard.PersonalBoardController;

public class GameBoardCli {
	
	private String myUsername;
	private ClientController clientController;
	private Map<String, PersonalBoardController> usersPersonalBoards = new HashMap<>();
	private Map<Excommunication, List<String>> playersExcommunications = new HashMap<>();
	private Map<PlayerColor, String> playerColorName = new HashMap<>();
	

}
