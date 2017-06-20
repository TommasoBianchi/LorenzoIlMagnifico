package it.polimi.ingsw.LM45.view.controller;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import it.polimi.ingsw.LM45.model.cards.Excommunication;
import it.polimi.ingsw.LM45.model.cards.PeriodType;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.serialization.FileManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class TESTforGameboard extends Application {
	
	private Stage stage;
	
	public void start(Stage stage) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		GuiController controller = new GuiController();
		Map<PeriodType, List<Excommunication>> excommunications = FileManager.loadExcommunications();
		Excommunication excom0 = excommunications.get(PeriodType.I).get(0);
		Excommunication excom1 = excommunications.get(PeriodType.II).get(0);
		Excommunication excom2 = excommunications.get(PeriodType.III).get(0);
		controller.initializeGameBoard(new String[]{"BUBU", "LULU", "KUKU", "SUSU"}, new PlayerColor[] {PlayerColor.BLUE, PlayerColor.GREEN,
				PlayerColor.RED, PlayerColor.YELLOW}, new Excommunication[]{excom0, excom1, excom2});
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
