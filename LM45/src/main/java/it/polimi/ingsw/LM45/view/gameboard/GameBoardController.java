package it.polimi.ingsw.LM45.view.gameboard;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.CardType;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;

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
	private Label username0;
	
	@FXML
	private Label username1;
	
	@FXML
	private Label username2;
	
	@FXML
	private Label username3;
	
	@FXML
	private Button personalBoard0;
	
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
	
	private Scene scene;
	
	public GameBoardController(){
	}
	
	public void setScene(Scene scene){
		this.scene = scene;
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
		//TODO set usernames and ID on Buttons
	}
	
	public void slotAction(MouseEvent event) {
		FlowPane slot = (FlowPane)event.getSource();
		String slotType = new String(slot.getId().substring(0, slot.getId().length()-1));
		int position = Integer.parseInt(slot.getId().substring(slot.getId().length()-1));
		System.out.println(slotType + " " + position);
		//TODO method to give to the server slotType and position
	}
	
	public void slotModify(String slotType, Integer position){
		FlowPane slot = (FlowPane)scene.lookup("#"+slotType+position);
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
	
	public void showPersonalBoard(MouseEvent event) {
		Button button = (Button)event.getSource();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(InitializeViewController.class.getResource("../personalBoard/PersonalBoardScene.fxml"));
		PersonalBoardController controller = new PersonalBoardController(false);
		loader.setController(controller);

		try {
			Scene scene = new Scene(loader.load());

			Stage stage = new Stage();
			stage.setResizable(false);
			stage.setScene(scene);
			stage.setTitle(button.getId());
			stage.show();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// TEST
		try {
			Map<CardType, List<Card>> deck = FileManager.loadCards();

			for (int i = 0; i < 12; i++) {
				CardType cardType = CardType.values()[(new Random()).nextInt(CardType.values().length - 1)];
				Card card = deck.get(cardType).get(0);
				controller.addCard(card);
			}

			for (ResourceType resourceType : ResourceType.values())
				controller.setResource(new Resource(resourceType, new Random().nextInt(20)));

			for (int i = 0; i < 4; i++) {
				controller.addLeaderCard(null);
			}
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TEST

	}
	
}
