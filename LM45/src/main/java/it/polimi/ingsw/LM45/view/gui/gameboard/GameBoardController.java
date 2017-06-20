package it.polimi.ingsw.LM45.view.gui.gameboard;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.PersonalBoard;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.serialization.FileManager;
import it.polimi.ingsw.LM45.view.controller.Main;
import it.polimi.ingsw.LM45.view.gui.personalBoard.PersonalBoardController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

	private Stage stage;
	private PersonalBoardController[] personalBoards;

	private FamiliarColor familiarColor = FamiliarColor.BONUS;
	private boolean familiarSelected = false;

	public GameBoardController(Stage stage, PlayerColor playerColor, String[] playersUsername) {
		
		this.stage = stage;
		
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("../gui/gameboard/GameBoardView.fxml"));
			loader.setController(this);
			AnchorPane gameBoard = (AnchorPane) loader.load();
			Scene scene = new Scene(gameBoard);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.getIcons().add(new Image("file:Assets/Image/Cards/LEADER/LeaderCard Cover.jpg"));
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setTitle("Lorenzo il Magnifico");
			stage.show();
			this.coverSlots(playersUsername.length);
			this.setFamiliars(playerColor, new int[] { 1, 2, 3, 4 });
			this.setUsernames(playersUsername);
			this.setServantCost(1);
		} catch (IOException e) { // TODO sistemare
			e.printStackTrace();
		}
		
		personalBoards = new PersonalBoardController[playersUsername.length];
		for(int i=0;i<personalBoards.length;i++){
			personalBoards[i] = new PersonalBoardController(new Stage(), playersUsername[i]);
		}
	}

	public void setServantCost(int cost) {
		servantCost.setText(Integer.toString(cost));
	}

	public void setFamiliars(PlayerColor color, int[] values) {
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

	public void coverSlots(int numPlayers) {
		if (numPlayers < 4) {
			coverableMarketSlot2
					.setStyle("-fx-background-image : url(file:Assets/Image/GameBoard/CoverMarketSlot2.png);"
							+ "-fx-background-size : cover;");
			coverableMarketSlot2.setDisable(true);
			coverableMarketSlot3
					.setStyle("-fx-background-image : url(file:Assets/Image/GameBoard/CoverMarketSlot3.png);"
							+ "-fx-background-size : cover;");
			coverableMarketSlot3.setDisable(true);
			if (numPlayers < 3) {
				coverableProductionSlot
						.setStyle("-fx-background-image : url(file:Assets/Image/GameBoard/CoverProduce.png);"
								+ "-fx-background-size : cover;");
				coverableProductionSlot.setDisable(true);
				coverableHarvestSlot
						.setStyle("-fx-background-image : url(file:Assets/Image/GameBoard/CoverHarvest.png);"
								+ "-fx-background-size : cover;");
				coverableHarvestSlot.setDisable(true);
			}
		}
	}

	public void setUsernames(String[] usernames) {
		username0.setText(usernames[0]);
		username1.setText(usernames[1]);
		username2.setText(usernames[2]);
		username3.setText(usernames[3]);
		personalBoard0.setId(usernames[0]);
		personalBoard1.setId(usernames[1]);
		personalBoard2.setId(usernames[2]);
		personalBoard3.setId(usernames[3]);
	}

	public void slotAction(MouseEvent event) {
		FlowPane slot = (FlowPane) event.getSource();
		String slotType = new String(slot.getId().substring(0, slot.getId().length() - 1));
		int position = Integer.parseInt(slot.getId().substring(slot.getId().length() - 1));
		System.out.println(slotType + " " + position);
		// TODO method to give to the server slotType and position
	}

	public void slotModify(String slotType, Integer position) {
		FlowPane slot = (FlowPane) stage.getScene().lookup("#" + slotType + position);
		slot.setStyle("-fx-background-color: black;");
		// TODO method to addFamiliar
	}

	public void zoomImage(MouseEvent event) {
		ImageView image = (ImageView) event.getSource();
		image.setScaleX(2);
		image.setScaleY(2);
		image.setTranslateY(50);
	}

	public void resetZoomImage(MouseEvent event) {
		ImageView image = (ImageView) event.getSource();
		image.setScaleX(1);
		image.setScaleY(1);
		image.setTranslateY(0);
	}

	public void showPersonalBoard(MouseEvent event) {
		Button button = (Button) event.getSource();
		for(PersonalBoardController personalBoard : personalBoards)
			if(personalBoard.getUsername() == button.getId())
				personalBoard.getStage().show();
	}

	public void endTurn() {
		// TODO call endTurn on ClientController
	}

	public void setDialog(String text) {
		dialogBox.setText(text);
		;
	}

	public void spendServant(MouseEvent event) {
		ImageView addIcon = (ImageView) event.getSource();
		// TODO ClientController.spendServant(addIcon.getId());
	}

	public void addCard(String username, Card card) {
		for(PersonalBoardController personalBoard : personalBoards)
			if(personalBoard.getUsername() == username)
				personalBoard.addCard(card);
	}

	public void newPeriod(Map<CardType, List<Card>> towerCards, int[] familiarsValues) {

		// Add new towers cards of the new Period
		for (CardType cardType : new CardType[] { CardType.BUILDING, CardType.CHARACTER, CardType.TERRITORY,
				CardType.VENTURE }) {
			for (int i = 0; i < 4; i++) {
				Card card = towerCards.get(cardType).get(i);
				ImageView image = (ImageView) stage.getScene().lookup("#VIEW" + cardType + i);
				image.setImage(new Image("file:Assets/Image/Cards/" + cardType + "/" + card.getName() + ".jpg"));
			}
		}

		// Remove all familiars from slots
		for (SlotType slotType : new SlotType[] { SlotType.BUILDING, SlotType.CHARACTER, SlotType.COUNCIL,
				SlotType.HARVEST, SlotType.MARKET, SlotType.PRODUCTION, SlotType.TERRITORY, SlotType.VENTURE }) {
			for (int i = 0; i < 4; i++) {
				FlowPane slot = (FlowPane) stage.getScene().lookup("#" + slotType + i);
				slot.getChildren().clear();
			}
		}

		// TODO setFamiliars(familiarValues)
	}

}
