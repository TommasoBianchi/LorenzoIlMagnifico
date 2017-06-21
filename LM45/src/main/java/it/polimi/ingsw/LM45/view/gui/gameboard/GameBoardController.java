package it.polimi.ingsw.LM45.view.gui.gameboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.model.cards.Excommunication;
import it.polimi.ingsw.LM45.model.cards.PeriodType;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.network.client.ClientController;
import it.polimi.ingsw.LM45.util.Pair;
import it.polimi.ingsw.LM45.view.controller.Main;
import it.polimi.ingsw.LM45.view.gui.personalBoard.PersonalBoardController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GameBoardController {

	@FXML
	private Label servantCost;

	@FXML
	private TextArea dialogBox;

	@FXML
	private FlowPane coverableMarketSlot2;

	@FXML
	private FlowPane coverableMarketSlot3;

	@FXML
	private FlowPane coverableProductionSlot;

	@FXML
	private FlowPane coverableHarvestSlot;

	@FXML
	private AnchorPane rightPane;

	private Stage stage;
	private String myUsername;
	private ClientController clientController;
	private Map<String, PersonalBoardController> usersPersonalBoards = new HashMap<>();
	private Set<String> coveredSlotsIDs;
	private Map<PlayerColor, Pair<Integer, Integer>> playerExcommunicationPosition = new EnumMap<>(PlayerColor.class);

	private FamiliarColor familiarColor = FamiliarColor.BONUS;
	private boolean familiarSelected = false;

	public GameBoardController(String[] playersUsername, PlayerColor[] playerColors, ClientController clientController,
			Excommunication[] excommunications) {

		this.stage = new Stage();
		this.clientController = clientController;
		myUsername = clientController.getUsername();
		System.out.println("HEY!");

		for (int i = 0; i < playersUsername.length; i++) {
			usersPersonalBoards.put(playersUsername[i],
					new PersonalBoardController(new Stage(), playersUsername[i], playerColors[i], clientController));
		}

		for (int i = 0, l = 0; i < 2; i++)
			for (int j = 0; j < 2; j++, l++)
				if (l < playerColors.length)
					playerExcommunicationPosition.put(playerColors[l], new Pair<Integer, Integer>(i, j));

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
			// stage.getScene().getRoot().setDisable(true);
			stage.show();
			this.coverSlots(playersUsername.length);
			this.setUsernames(playersUsername);
			this.setServantCost(1);
			this.setFamiliarsColors(playersUsername, playerColors);
			this.disableGameBoard();
			this.placeExcommunications(excommunications);
		} catch (IOException e) { // TODO sistemare
			e.printStackTrace();
		}

	}

	private void setFamiliarsColors(String[] playersUsername, PlayerColor[] playerColors) {
		for (int i = 0; i < playersUsername.length; i++) {
			if (playersUsername[i].equals(myUsername)) {
				for (FamiliarColor familiarColor : new FamiliarColor[] { FamiliarColor.BLACK, FamiliarColor.ORANGE,
						FamiliarColor.UNCOLORED, FamiliarColor.WHITE }) {
					ImageView familiarImage = (ImageView) stage.getScene().lookup("#FAMILIAR" + familiarColor);
					familiarImage.setImage(
							new Image("file:Assets/Image/Familiars/" + playerColors[i] + "/" + familiarColor + ".png"));
				}
			}
			System.out.println(usersPersonalBoards.containsKey(playersUsername[i]));
			usersPersonalBoards.get(playersUsername[i]).setFamiliarsColors(playerColors[i]);
		}
	}

	private void coverSlots(int numPlayers) {
		coveredSlotsIDs = new HashSet<>();

		if (numPlayers < 4) {
			coverableMarketSlot2
					.setStyle("-fx-background-image : url(file:Assets/Image/GameBoard/CoverMarketSlot2.png);"
							+ "-fx-background-size : cover;");
			coverableMarketSlot2.setDisable(true);
			coveredSlotsIDs.add(coverableMarketSlot2.getId());
			coverableMarketSlot3
					.setStyle("-fx-background-image : url(file:Assets/Image/GameBoard/CoverMarketSlot3.png);"
							+ "-fx-background-size : cover;");
			coverableMarketSlot3.setDisable(true);
			coveredSlotsIDs.add(coverableMarketSlot3.getId());
			if (numPlayers < 3) {
				coverableProductionSlot
						.setStyle("-fx-background-image : url(file:Assets/Image/GameBoard/CoverProduce.png);"
								+ "-fx-background-size : cover;");
				coverableProductionSlot.setDisable(true);
				coveredSlotsIDs.add(coverableProductionSlot.getId());
				coverableHarvestSlot
						.setStyle("-fx-background-image : url(file:Assets/Image/GameBoard/CoverHarvest.png);"
								+ "-fx-background-size : cover;");
				coverableHarvestSlot.setDisable(true);
				coveredSlotsIDs.add(coverableHarvestSlot.getId());
			}
		}
	}

	private void setUsernames(String[] usernames) {
		for (int i = 0; i < usernames.length; i++) {
			Label userText = (Label) stage.getScene().lookup("#USERNAME" + i);
			if (usernames[i].equals(myUsername))
				userText.setText(usernames[i] + " (Me)");
			else
				userText.setText(usernames[i]);
			Button personalButton = (Button) stage.getScene().lookup("#BUTTONPERSONAL" + i);
			personalButton.setId(usernames[i]);
			personalButton.setOpacity(1);
			personalButton.setDisable(false);
		}
	}

	public void setServantCost(int cost) {
		servantCost.setText(Integer.toString(cost));
	}

	public void setFamiliarValue(String username, FamiliarColor color, int value) {
		if (username == myUsername) {
			Label familiarValue = (Label) stage.getScene().lookup("#VALUE" + color.toString());
			familiarValue.setText(Integer.toString(value));
		}
		usersPersonalBoards.get(username).setFamiliarValue(color, value);
	}

	public void setResources(Resource[] resources, String username) {
		if (username == myUsername)
			for (Resource resource : resources)
				this.setResourceGameboard(resource);
		for (Resource resource : resources)
			usersPersonalBoards.get(username).setResource(resource);
	}

	private void setResourceGameboard(Resource resource) {
		Label value = (Label) stage.getScene().lookup("#" + resource.getResourceType());
		value.setText(Integer.toString(resource.getAmount()));
	}

	public void doAction(MouseEvent event) {
		if (!familiarSelected)
			writeInDialogBox("Familiar not selected yet !");
		else {
			FlowPane slot = (FlowPane) event.getSource();
			SlotType slotType = SlotType.valueOf((slot.getId().substring(0, slot.getId().length() - 1)));
			int position = Integer.parseInt(slot.getId().substring(slot.getId().length() - 1));
			clientController.placeFamiliar(familiarColor, slotType, position);
			writeInDialogBox(slotType + " slot " + position);
			familiarSelected = false;
		}
	}
	
	public void pickCard(Card card, String username){
		/*ImageView cardView = (ImageView) stage.getScene().lookup("#VIEW" + cardType + position);
		Image card = cardView.getImage();
		usersPersonalBoards.get(username).addCard(card, cardType);
		cardView.setImage(null);*/
	}

	public void familiarSelected(MouseEvent event) {
		ImageView image = (ImageView) event.getSource();
		familiarColor = FamiliarColor.valueOf(image.getId().substring(8));
		familiarSelected = true;
		writeInDialogBox(familiarColor + " familiar selected !");
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
		image.setTranslateZ(-2);
		image.toFront();
	}

	public void resetZoomImage(MouseEvent event) {
		ImageView image = (ImageView) event.getSource();
		image.setScaleX(1);
		image.setScaleY(1);
		image.setTranslateY(0);
	}

	public void showPersonalBoard(MouseEvent event) {
		Button button = (Button) event.getSource();
		usersPersonalBoards.get(button.getId()).getStage().show();
	}

	public void endTurn() {
		clientController.endTurn();
		this.disableGameBoard();
	}

	public void disableGameBoard() {
		rightPane.setDisable(true);
		setSlotsDisabled(true);
	}

	public void myTurn() {
		writeInDialogBox("It's my turn!");
		rightPane.setDisable(false);
		setSlotsDisabled(false);
	}

	public void spendServant(MouseEvent event) {
		ImageView addIcon = (ImageView) event.getSource();
		clientController.increaseFamiliarValue(FamiliarColor.valueOf(addIcon.getId()));
	}

	public void addCardsOnTower(Card[] cards, SlotType slotType) {
		// Add new towers cards of the new Period
		for (int i = 0; i < 4; i++) {
			ImageView image = (ImageView) stage.getScene().lookup("#VIEW" + slotType + i);
			System.out.println(cards[i] == null);
			System.out.println(cards[i].getName());
			System.out.println("file:Assets/Image/Cards/" + slotType + "/" + cards[i].getName() + ".png");
			image.setImage(new Image("file:Assets/Image/Cards/" + slotType + "/" + cards[i].getName() + ".png"));
		}
	}

	public void clearSlots() {
		// Remove all familiars from slots
		for (SlotType slotType : new SlotType[] { SlotType.BUILDING, SlotType.CHARACTER, SlotType.COUNCIL,
				SlotType.HARVEST, SlotType.MARKET, SlotType.PRODUCTION, SlotType.TERRITORY, SlotType.VENTURE }) {
			for (int i = 0; i < 4; i++) {
				FlowPane slot = (FlowPane) stage.getScene().lookup("#" + slotType + i);
				slot.getChildren().clear();
			}
		}
	}

	public void writeInDialogBox(String message) {
		if (dialogBox.getText().equals(""))
			dialogBox.appendText("> " + message);
		else
			dialogBox.appendText("\n> " + message);
		// Scroll to bottom
		dialogBox.setScrollTop(Double.MAX_VALUE);
	}

	private void setSlotsDisabled(boolean enabled) {
		for (Node slot : getSlots()) {
			slot.setDisable(enabled);
		}
	}

	// Cache slots to avoid a lot of slow calls to scene::lookup
	private Node[] slots;

	private Node[] getSlots() {
		if (slots != null)
			return slots;

		List<Node> nodes = new ArrayList<>();
		for (SlotType slotType : new SlotType[] { SlotType.BUILDING, SlotType.CHARACTER, SlotType.COUNCIL,
				SlotType.HARVEST, SlotType.MARKET, SlotType.PRODUCTION, SlotType.TERRITORY, SlotType.VENTURE }) {
			for (int i = 0; i < 4; i++) {
				String slotID = "#" + slotType + i;
				Node slot = stage.getScene().lookup(slotID);
				if (slot != null && !coveredSlotsIDs.contains(slotID))
					nodes.add(slot);
			}
		}

		slots = nodes.stream().toArray(Node[]::new);
		return slots;
	}

	private void placeExcommunications(Excommunication[] excommunications) {
		String path = "file:Assets/Image/Excommunication/";
		for (Excommunication excom : excommunications) {
			ImageView excommunication = (ImageView) stage.getScene()
					.lookup("#VIEWEXCOMMUNICATION" + excom.getPeriodType().name());
			excommunication.setImage(new Image(path + excom.getName() + ".png"));
		}
	}

	public void placeExcommunicationToken(PlayerColor playerColor, PeriodType periodType) {
		GridPane excommunication = (GridPane) stage.getScene().lookup("#EXCOMMUNICATION" + periodType.name());
		String tokenPath = "file:Assets/Image/ExcommunicationToken/";
		ImageView token = new ImageView(new Image(tokenPath + "/" + playerColor + ".png"));
		token.setFitHeight(15);
		token.setFitWidth(15);
		Pair<Integer, Integer> pair = playerExcommunicationPosition.get(playerColor);
		excommunication.add(token, pair._1(), pair._2());
	}

}
