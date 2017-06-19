package it.polimi.ingsw.LM45.view.controller;

import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.SlotType;
import javafx.application.Application;
import javafx.stage.Stage;

public class TESTforGameboard extends Application {
	
	private Stage stage;
	
	public void start(Stage stage) {
		GuiController controller = new GuiController();
		controller.showGameBoard(PlayerColor.RED, new String[]{"BUBU", "LULU", "KUKU", "SUSU"});
		controller.gameBoardController.slotModify("BUILDING", 0);
		controller.gameBoardController.slotModify("TERRITORY", 0);
		controller.gameBoardController.slotModify("VENTURE", 0);
		controller.gameBoardController.slotModify("BUILDING", 3);
		controller.gameBoardController.slotModify("COUNCIL", 0);
		controller.gameBoardController.slotModify("HARVEST", 1);
		controller.gameBoardController.slotModify("HARVEST", 0);
	}
	
	public static void main(String[] args){
		launch(args);
	}

}
