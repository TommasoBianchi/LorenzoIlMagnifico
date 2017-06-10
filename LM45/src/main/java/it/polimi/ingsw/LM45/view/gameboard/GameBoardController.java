package it.polimi.ingsw.LM45.view.gameboard;

import it.polimi.ingsw.LM45.view.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;

public class GameBoardController {
	
	@FXML
	private Label uncoloredValue;
	
	@FXML
	private Label whiteValue;
	
	@FXML
	private Label orangeValue;
	
	@FXML
	private Label blackValue;
	
	@FXML
	private ImageView uncoloredFamiliar;
	
	@FXML
	private ImageView whiteFamiliar;
	
	@FXML
	private ImageView orangeFamiliar;
	
	@FXML
	private ImageView blackFamiliar;
	
	@FXML
	private Label dialogBox;
	
	@FXML
	private Label myUsername;
	
	@FXML
	private Label username1;
	
	@FXML
	private Label username2;
	
	@FXML
	private Label username3;
	
	@FXML
	private Button myPersonalBoard;
	
	@FXML
	private Button PersonalBoard1;
	
	@FXML
	private Button PersonalBoard2;
	
	@FXML
	private Button PersonalBoard3;
	
	@FXML
	private Label coins;
	
	@FXML
	private Label stone;
	
	@FXML
	private Label wood;
	
	@FXML
	private Label servants;
		
	@FXML
	private Label victory;
	
	@FXML
	private Label military;
	
	@FXML
	private Label faith;
	
	@FXML
	private FlowPane marketSlot0;
	
	@FXML
	private FlowPane marketSlot1;
	
	@FXML
	private FlowPane coverableMarketSlot2;
	
	@FXML
	private FlowPane coverableMarketSlot3;
	
	@FXML
	private FlowPane productionSlot;
	
	@FXML
	private FlowPane coverableProductionSlot;
	
	@FXML
	private FlowPane harvestSlot;
	
	@FXML
	private FlowPane coverableHarvestSlot;
	
	private Main main;
	
	public GameBoardController(){
	}
	
	public void setMain(Main main){
		this.main = main;
	}
	
	public void showFamiliars(String color){
		String path = "file:Assets/Image/Familiars/" + color + "/";
		uncoloredFamiliar.setImage(new Image(path + "UNCOLORED.png"));
		whiteFamiliar.setImage(new Image(path + "WHITE.png"));
		orangeFamiliar.setImage(new Image(path + "ORANGE.png"));
		blackFamiliar.setImage(new Image(path + "BLACK.png"));
	}
	
	public void coverSlots(int numPlayers){
		if(numPlayers < 4){
			coverableMarketSlot2.setStyle("-fx-background-image : url(file:Assets/Image/GameBoard/CoverMarketSlot2.png);"
					+ "-fx-background-size : cover;");
			coverableMarketSlot2.setDisable(true);
			coverableMarketSlot3.setStyle("-fx-background-image : url(file:Assets/Image/GameBoard/CoverMarketSlot3.png);"
					+ "-fx-background-size : cover;");
			coverableMarketSlot3.setDisable(true);
			if(numPlayers < 3){
				coverableProductionSlot.setStyle("-fx-background-image : url(file:Assets/Image/GameBoard/CoverProduce.png);"
						+ "-fx-background-size : cover;");
				coverableProductionSlot.setDisable(true);
				coverableHarvestSlot.setStyle("-fx-background-image : url(file:Assets/Image/GameBoard/CoverHarvest.png);"
						+ "-fx-background-size : cover;");
				coverableHarvestSlot.setDisable(true);
			}
		}
	}
	
	public void setUsernames(){
		//TODO
	}
	
}
