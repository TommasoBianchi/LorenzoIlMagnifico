package it.polimi.ingsw.LM45.view.gameboard;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.sun.glass.ui.Screen;

import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.serialization.FileManager;
import it.polimi.ingsw.LM45.view.controller.InitializeViewController;
import it.polimi.ingsw.LM45.view.personalBoard.PersonalBoardController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class GameBoardController {
	
	@FXML
	private Label servantCost;
	
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
	private Button personalBoard1;
	
	@FXML
	private Button personalBoard2;
	
	@FXML
	private Button personalBoard3;
	
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
	private FlowPane coverableMarketSlot2;
	
	@FXML
	private FlowPane coverableMarketSlot3;
	
	@FXML
	private FlowPane coverableProductionSlot;
	
	@FXML
	private FlowPane coverableHarvestSlot;
	
	private Stage myPersonalStage = new Stage();
	private Stage personalStage1 = new Stage();
	private Stage personalStage2 = new Stage();
	private Stage personalStage3 = new Stage();
	
	private PersonalBoardController myPersonalController = new PersonalBoardController();
	private PersonalBoardController personalController1 = new PersonalBoardController();
	private PersonalBoardController personalController2 = new PersonalBoardController();
	private PersonalBoardController personalController3 = new PersonalBoardController();
	
	private Scene gameScene;
	
	@FXML
	public void initialize() {
		Stage[] stages = {myPersonalStage, personalStage1, personalStage2, personalStage3};
		PersonalBoardController[] personalControllers = {myPersonalController, personalController1, personalController2,
				personalController3};
		for(int i=0; i<4; i++){
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GameBoardController.class.getResource("../personalBoard/PersonalBoardScene.fxml"));
			loader.setController(personalControllers[i]);
			try { Scene scene = new Scene(loader.load());
					stages[i].setResizable(false);
					stages[i].setScene(scene);
					stages[i].setWidth(Screen.getMainScreen().getVisibleWidth());
					stages[i].setHeight(Screen.getMainScreen().getVisibleHeight());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			// TEST
			try {
				Map<CardType, List<Card>> deck = FileManager.loadCards();

				for (int j = 0; j < 12; j++) {
					CardType cardType = CardType.values()[(new Random()).nextInt(CardType.values().length - 1)];
					Card card = deck.get(cardType).get(0);
					personalControllers[i].addCard(card);
				}

				for (ResourceType resourceType : ResourceType.values())
					personalControllers[i].setResource(new Resource(resourceType, new Random().nextInt(20)));

					personalControllers[i].addLeaderCard(null);
			} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// TEST
		}
	}
	
	public GameBoardController(){
	}
	
	public void setScene(Scene scene){
		this.gameScene = scene;
	}
	
	public void setServantCost(int cost) {
		servantCost.setText(Integer.toString(cost));
	}
	
	public void setFamiliars(PlayerColor color, int[] values){
		String path = "file:Assets/Image/Familiars/" + color.toString() + "/";
		uncoloredFamiliar.setImage(new Image(path + "UNCOLORED.png"));
		whiteFamiliar.setImage(new Image(path + "WHITE.png"));
		orangeFamiliar.setImage(new Image(path + "ORANGE.png"));
		blackFamiliar.setImage(new Image(path + "BLACK.png"));
		
		uncoloredValue.setText(Integer.toString(values[0]));
		whiteValue.setText(Integer.toString(values[1]));
		orangeValue.setText(Integer.toString(values[2]));
		blackValue.setText(Integer.toString(values[3]));
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
	
	public void setMyUsername(String username) {
		myPersonalBoard.setId(username);
	}
	
	public void setUsernames(String[] usernames){
		username1.setText(usernames[0]);
		username2.setText(usernames[1]);
		username3.setText(usernames[2]);
		personalBoard1.setId(usernames[0]);
		personalBoard2.setId(usernames[1]);
		personalBoard3.setId(usernames[2]);
	}
	
	public void slotAction(MouseEvent event) {
		FlowPane slot = (FlowPane)event.getSource();
		String slotType = new String(slot.getId().substring(0, slot.getId().length()-1));
		int position = Integer.parseInt(slot.getId().substring(slot.getId().length()-1));
		System.out.println(slotType + " " + position);
		//TODO method to give to the server slotType and position
	}
	
	public void slotModify(String slotType, Integer position){
		FlowPane slot = (FlowPane)gameScene.lookup("#"+slotType+position);
		slot.setStyle("-fx-background-color: black;");
		//TODO method to addFamiliar
	}
	
	public void zoomImage(MouseEvent event){
		ImageView image = (ImageView)event.getSource();
		image.setScaleX(2);
		image.setScaleY(2);
		image.setTranslateY(50);
	}
	
	public void resetZoomImage(MouseEvent event){
		ImageView image = (ImageView)event.getSource();
		image.setScaleX(1);
		image.setScaleY(1);
		image.setTranslateY(0);
	}
	
	//TODO add servantsCost
	
	//TODO button to spend Servant
	
	public void showPersonalBoard(MouseEvent event) {
		Button button = (Button)event.getSource();
		if(button.getId() == myPersonalBoard.getId())
			myPersonalStage.show();
		else if(button.getId() == personalBoard1.getId())
			personalStage1.show();
		else if(button.getId() == personalBoard2.getId())
			personalStage2.show();
		else
			personalStage3.show();

	}
	
	public void endTurn() {
		// TODO call endTurn on ClientController
	}
	
	public void setDialog(String text) {
		dialogBox.setText(text);;
	}
	
	public void spendServant(MouseEvent event) {
		ImageView addIcon = (ImageView)event.getSource();
		//TODO ClientController.spendServant(addIcon.getId());
	}
	
}
