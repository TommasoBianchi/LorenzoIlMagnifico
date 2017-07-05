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
import it.polimi.ingsw.LM45.model.cards.Excommunication;
import it.polimi.ingsw.LM45.model.cards.LeaderCard;
import it.polimi.ingsw.LM45.model.cards.PeriodType;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.PersonalBonusTile;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.network.client.ClientController;
import it.polimi.ingsw.LM45.util.Pair;
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
	private GridPane familiarPane;

	@FXML
	private GridPane bonusActionPane;

	@FXML
	private Button endTurnButton;

	private Stage stage;
	private String myUsername;
	private ClientController clientController;
	private Map<String, PersonalBoardController> usersPersonalBoards = new HashMap<>();
	private Set<String> coveredSlotsIDs;
	// Pair<Integer,Integer> refers to the position of the token player inside the excommunication Grid
	private Map<PlayerColor, Pair<Integer, Integer>> playerExcommunicationPosition = new EnumMap<>(PlayerColor.class);
	// Map<PlayerColor, String PlayerName>
	private Map<PlayerColor, String> playerColorName = new HashMap<>();
	// Map<String cardName, String towerslotID>
	private Map<String, String> cardPosition = new HashMap<>();

	private FamiliarColor selectedFamiliarColor = null;
	private boolean familiarSelected = false;

	public GameBoardController(String[] playersUsername, PlayerColor[] playerColors, ClientController clientController,
			Excommunication[] excommunications) {

		stage = new Stage();
		this.clientController = clientController;
		myUsername = clientController.getUsername();

		for (int i = 0; i < playersUsername.length; i++) {
			usersPersonalBoards.put(playersUsername[i],
					new PersonalBoardController(new Stage(), playersUsername[i], clientController));
			playerColorName.put(playerColors[i], playersUsername[i]);
		}

		for (int i = 0; i < 2; i++)
			for (int j = 0; j < 2; j++){
				int index = i * 2 + j;
				if (index < playerColors.length)
					playerExcommunicationPosition.put(playerColors[index], new Pair<Integer, Integer>(i, j));
			}

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GameBoardController.class.getResource("GameBoardView.fxml"));
			loader.setController(this);
			AnchorPane gameBoard = (AnchorPane) loader.load();
			Scene scene = new Scene(gameBoard);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.getIcons().add(new Image("/Image/Cards/LEADER/LeaderCard Cover.jpg"));
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setTitle("Lorenzo il Magnifico");
			stage.show();
			coverSlots(playersUsername.length);
			setUsernames(playersUsername);
			setServantCost(1);
			setFamiliarsColors(playersUsername, playerColors);
			disableGameBoard();
			placeExcommunications(excommunications);
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
					familiarImage
							.setImage(new Image("/Image/Familiars/" + playerColors[i] + "/" + familiarColor + ".png"));
				}
			}
			usersPersonalBoards.get(playersUsername[i]).setFamiliarsColors(playerColors[i]);
		}
	}

	private void coverSlots(int numPlayers) {
		coveredSlotsIDs = new HashSet<>();

		if (numPlayers < 4) {
			coverableMarketSlot2.setStyle("-fx-background-image : url(/Image/GameBoard/CoverMarketSlot2.png);"
					+ "-fx-background-size : cover;");
			coverableMarketSlot2.setDisable(true);
			coveredSlotsIDs.add(coverableMarketSlot2.getId());
			coverableMarketSlot3.setStyle("-fx-background-image : url(/Image/GameBoard/CoverMarketSlot3.png);"
					+ "-fx-background-size : cover;");
			coverableMarketSlot3.setDisable(true);
			coveredSlotsIDs.add(coverableMarketSlot3.getId());
			if (numPlayers < 3) {
				coverableProductionSlot.setStyle("-fx-background-image : url(/Image/GameBoard/CoverProduce.png);"
						+ "-fx-background-size : cover;");
				coverableProductionSlot.setDisable(true);
				coveredSlotsIDs.add(coverableProductionSlot.getId());
				coverableHarvestSlot.setStyle("-fx-background-image : url(/Image/GameBoard/CoverHarvest.png);"
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
		if (username.equals(myUsername)) {
			Label familiarValue = (Label) stage.getScene().lookup("#VALUE" + color.toString());
			familiarValue.setText(Integer.toString(value));
		}
		usersPersonalBoards.get(username).setFamiliarValue(color, value);
	}

	public void setResources(Resource[] resources, String username) {
		if (username.equals(myUsername))
			for (Resource resource : resources)
				setResourceGameboard(resource);
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
			clientController.placeFamiliar(selectedFamiliarColor, slotType, position);
			writeInDialogBox(slotType + " slot " + position);
		}
	}

	public void doBonusAction(SlotType slotType, int value) {
		writeInDialogBox("Do Bonus Action: " + slotType + " of value " + value);
		selectedFamiliarColor = FamiliarColor.BONUS;
		familiarSelected = true;
		familiarPane.setDisable(true);
		bonusActionPane.setDisable(false);
		bonusActionPane.setOpacity(1);
		Label bonusValue = (Label) stage.getScene().lookup("#VALUEBONUS");
		bonusValue.setText(Integer.toString(value));
	}

	public void pickCard(Card card, String username) {
		String position = cardPosition.get(card.getName());
		ImageView cardView = (ImageView) stage.getScene().lookup(position);
		Image cardImage = cardView.getImage();
		usersPersonalBoards.get(username).addCard(cardImage, card.getCardType());
		cardView.setImage(null);
		cardView.setDisable(true);
	}

	public void addFamiliar(SlotType slotType, int position, FamiliarColor familiarColor, PlayerColor playerColor) {
		if (familiarColor == FamiliarColor.BONUS) {
			bonusActionPane.setDisable(true);
			bonusActionPane.setOpacity(0);
			familiarPane.setDisable(false);
		} else {
			FlowPane slot = (FlowPane) stage.getScene().lookup("#" + slotType + position);
			String pathFamiliar = "/Image/Familiars/" + playerColor + "/" + familiarColor + ".png";
			ImageView familiar = new ImageView(new Image(pathFamiliar));
			familiar.setFitHeight(25);
			familiar.setFitWidth(25);
			slot.getChildren().add(familiar);
			usersPersonalBoards.get(playerColorName.get(playerColor)).familiarUsed(familiarColor);
			if (playerColorName.get(playerColor).equals(myUsername)) {
				familiarUsed(familiarColor);
				endTurnButton.setDisable(false);
				familiarPane.setDisable(true);
				if(selectedFamiliarColor != FamiliarColor.BONUS)
					familiarSelected = false;
			}
		}
	}

	public void familiarSelected(MouseEvent event) {
		ImageView image = (ImageView) event.getSource();
		selectedFamiliarColor = FamiliarColor.valueOf(image.getId().substring(8));
		familiarSelected = true;
		writeInDialogBox(selectedFamiliarColor + " familiar selected !");
	}

	private void familiarUsed(FamiliarColor familiarColor) {
		ImageView familiarView = (ImageView) stage.getScene().lookup("#FAMILIAR" + familiarColor);
		familiarView.setOpacity(0);
		familiarView.setDisable(true);
		Label familiarValue = (Label) stage.getScene().lookup("#VALUE" + familiarColor);
		familiarValue.setOpacity(0);
		ImageView servantView = (ImageView) stage.getScene().lookup("#" + familiarColor);
		servantView.setOpacity(0);
		servantView.setDisable(true);
	}

	public void zoomImage(MouseEvent event) {
		ImageView image = (ImageView) event.getSource();
		image.setScaleX(2);
		image.setScaleY(2);
		image.setTranslateY(50);
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
		familiarSelected = false;
		clientController.endTurn();
		disableGameBoard();
		bonusActionPane.setDisable(true);
		bonusActionPane.setOpacity(0);
	}

	public void disableGameBoard() {
		familiarPane.setDisable(true);
		setSlotsDisabled(true);
		endTurnButton.setDisable(true);
	}

	public void myTurn() {
		writeInDialogBox("It's my turn!");
		familiarPane.setDisable(false);
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
			image.setDisable(false);
			image.setImage(new Image("/Image/Cards/" + slotType + "/" + cards[i].getName() + ".png"));
			cardPosition.put(cards[i].getName(), "#VIEW" + slotType + i);
		}
		clearSlots();
		showFamiliars();
	}

	private void clearSlots() {
		// Remove all familiars from slots
		for (FlowPane slot : getSlots())
			slot.getChildren().clear();
	}

	private void showFamiliars() {
		for (FamiliarColor familiarColor : new FamiliarColor[] { FamiliarColor.BLACK, FamiliarColor.ORANGE,
				FamiliarColor.UNCOLORED, FamiliarColor.WHITE }) {
			ImageView familiarView = (ImageView) stage.getScene().lookup("#FAMILIAR" + familiarColor);
			familiarView.setOpacity(1);
			familiarView.setDisable(false);
			Label familiarValue = (Label) stage.getScene().lookup("#VALUE" + familiarColor);
			familiarValue.setOpacity(1);
			ImageView servantView = (ImageView) stage.getScene().lookup("#" + familiarColor);
			servantView.setOpacity(1);
			servantView.setDisable(false);
		}
		for (PersonalBoardController personalController : usersPersonalBoards.values())
			personalController.showFamiliars();
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
	private FlowPane[] slots;

	private FlowPane[] getSlots() {
		if (slots != null)
			return slots;

		List<FlowPane> nodes = new ArrayList<>();
		for (SlotType slotType : new SlotType[] { SlotType.BUILDING, SlotType.CHARACTER, SlotType.COUNCIL,
				SlotType.HARVEST, SlotType.MARKET, SlotType.PRODUCTION, SlotType.TERRITORY, SlotType.VENTURE }) {
			for (int i = 0; i < 4; i++) {
				String slotID = "#" + slotType + i;
				Node slot = stage.getScene().lookup(slotID);
				if (slot != null && !coveredSlotsIDs.contains(slotID.substring(1)))
					nodes.add((FlowPane) slot);
			}
		}

		slots = nodes.stream().toArray(FlowPane[]::new);
		return slots;
	}

	private void placeExcommunications(Excommunication[] excommunications) {
		String path = "/Image/Excommunication/";
		for (Excommunication excom : excommunications) {
			ImageView excommunication = (ImageView) stage.getScene()
					.lookup("#VIEWEXCOMMUNICATION" + excom.getPeriodType().name());
			excommunication.setImage(new Image(path + excom.getName() + ".png"));
		}
	}

	public void placeExcommunicationToken(PlayerColor playerColor, PeriodType periodType) {
		GridPane excommunication = (GridPane) stage.getScene().lookup("#EXCOMMUNICATION" + periodType.name());
		String tokenPath = "/Image/ExcommunicationToken/";
		ImageView token = new ImageView(new Image(tokenPath + "/" + playerColor + ".png"));
		token.setFitHeight(15);
		token.setFitWidth(15);
		Pair<Integer, Integer> pair = playerExcommunicationPosition.get(playerColor);
		excommunication.add(token, pair._1(), pair._2());
	}

	public void setLeaderCards(LeaderCard[] leaders) {
		usersPersonalBoards.get(myUsername).setLeaderCards(leaders);
	}

	public void discardLeaderCard(String username, LeaderCard leader) {
		usersPersonalBoards.get(username).discardLeaderCard(leader);
	}

	public void playLeaderCard(String username, LeaderCard leader) {
		usersPersonalBoards.get(username).playLeaderCard(leader);
	}

	public void activateLeaderCard(String username, LeaderCard leader) {
		usersPersonalBoards.get(username).activateLeaderCard(leader);
	}
	
	public void enableLeaderCard(String username, LeaderCard leader) {
		usersPersonalBoards.get(username).enableLeaderCard(leader);
	}

	public void setPersonalBonusTile(String username, PersonalBonusTile personalBonusTile) {
		Resource[] productionBonus = personalBonusTile.getProductionBonuses();
		Resource[] harvestBonus = personalBonusTile.getHarvestBonuses();
		if (username.equals(myUsername)) {
			for(int i=0; i<2; i++){
				ImageView resourceView = (ImageView) stage.getScene().lookup("#PRODUCTIONTILE" + i);
				Image resource = new Image("/Image/Resources/" + productionBonus[i].getResourceType() + ".png");
				resourceView.setImage(resource);
				Label resourceValue = (Label) stage.getScene().lookup("#LABELPRODUCTION" + i);
				resourceValue.setText(Integer.toString(productionBonus[i].getAmount()));
			}
			for(int i=0; i<3; i++){
				ImageView resourceView = (ImageView) stage.getScene().lookup("#HARVESTTILE" + i);
				Image resource = new Image("/Image/Resources/" + harvestBonus[i].getResourceType() + ".png");
				resourceView.setImage(resource);
				Label resourceValue = (Label) stage.getScene().lookup("#LABELHARVEST" + i);
				resourceValue.setText(Integer.toString(harvestBonus[i].getAmount()));
			}
		} else
			usersPersonalBoards.get(username).setPersonalBonusTile(productionBonus, harvestBonus);
	}
	
	public void close() {
		for(PersonalBoardController personalBoard : usersPersonalBoards.values())
			personalBoard.getStage().close();
		stage.close();
	}

}
