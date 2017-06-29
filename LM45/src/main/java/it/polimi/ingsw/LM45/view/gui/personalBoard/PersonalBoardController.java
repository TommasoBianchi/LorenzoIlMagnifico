package it.polimi.ingsw.LM45.view.gui.personalBoard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.model.cards.LeaderCard;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.network.client.ClientController;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PersonalBoardController {

	@FXML
	private FlowPane territories;

	@FXML
	private FlowPane buildings;

	@FXML
	private FlowPane characters;

	@FXML
	private FlowPane ventures;

	@FXML
	private Text coinsText;

	@FXML
	private Text woodText;

	@FXML
	private Text stoneText;

	@FXML
	private Text servantsText;

	@FXML
	private Text victoryPointsText;

	@FXML
	private Text militaryPointsText;

	@FXML
	private Text faithPointsText;

	@FXML
	private GridPane productionGrid;

	@FXML
	private GridPane harvestGrid;

	private Stage stage;
	private ClientController clientController;

	private Map<CardType, FlowPane> cardFlowPanes = new EnumMap<>(CardType.class);
	private Map<ResourceType, Text> resourceTexts = new EnumMap<>(ResourceType.class);
	private List<LeaderCard> leaders = new ArrayList<LeaderCard>();

	public PersonalBoardController(Stage stage, String username, ClientController clienteController) {
		this.stage = stage;
		this.clientController = clienteController;

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(PersonalBoardController.class.getResource("PersonalBoardScene.fxml"));
		loader.setController(this);
		try {
			Scene scene = new Scene(loader.load());
			stage.setResizable(false);
			stage.setScene(scene);
			stage.getIcons().add(new Image("/Image/Cards/LEADER/LeaderCard Cover.jpg"));
			stage.setTitle(username + " - Personal Board");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@FXML
	public void initialize() {
		cardFlowPanes.put(CardType.TERRITORY, territories);
		cardFlowPanes.put(CardType.BUILDING, buildings);
		cardFlowPanes.put(CardType.CHARACTER, characters);
		cardFlowPanes.put(CardType.VENTURE, ventures);

		resourceTexts.put(ResourceType.COINS, coinsText);
		resourceTexts.put(ResourceType.STONE, stoneText);
		resourceTexts.put(ResourceType.WOOD, woodText);
		resourceTexts.put(ResourceType.SERVANTS, servantsText);
		resourceTexts.put(ResourceType.VICTORY, victoryPointsText);
		resourceTexts.put(ResourceType.MILITARY, militaryPointsText);
		resourceTexts.put(ResourceType.FAITH, faithPointsText);
	}

	public void addCard(Image cardView, CardType cardType) {
		ImageView card = new ImageView(cardView);
		card.setFitWidth(90);
		card.setFitHeight(130);
		card.setCursor(Cursor.HAND);
		card.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
		card.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				zoomImage(event);
			}
		});
		card.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				resetZoomImage(event);
			}
		});
		if (cardFlowPanes.containsKey(cardType)) {
			cardFlowPanes.get(cardType).getChildren().add(card);
		}
	}

	public void zoomImage(MouseEvent event) {
		ImageView image = (ImageView) event.getSource();
		image.setScaleX(2);
		image.setScaleY(2);
		image.getParent().getParent().toFront();
		image.setTranslateX(-45);
		image.setTranslateY(-50);
	}

	public void resetZoomImage(MouseEvent event) {
		ImageView image = (ImageView) event.getSource();
		image.setScaleX(1);
		image.setScaleY(1);
		image.setTranslateX(0);
		image.setTranslateY(0);
	}

	public void setResource(Resource resource) {
		if (resourceTexts.containsKey(resource.getResourceType())) {
			resourceTexts.get(resource.getResourceType()).setText(Integer.toString(resource.getAmount()));
		}
	}

	public void setFamiliarValue(FamiliarColor color, int value) {
		Label familiarValue = (Label) stage.getScene().lookup("#VALUE" + color.toString());
		familiarValue.setText(Integer.toString(value));
	}

	public void setFamiliarsColors(PlayerColor playerColor) {
		for (FamiliarColor familiarColor : new FamiliarColor[] { FamiliarColor.BLACK, FamiliarColor.ORANGE,
				FamiliarColor.UNCOLORED, FamiliarColor.WHITE }) {
			ImageView familiarImage = (ImageView) stage.getScene().lookup("#FAMILIAR" + familiarColor);
			familiarImage.setImage(new Image("/Image/Familiars/" + playerColor + "/" + familiarColor + ".png"));
		}
	}

	public void familiarUsed(FamiliarColor familiarColor) {
		ImageView familiarView = (ImageView) stage.getScene().lookup("#FAMILIAR" + familiarColor);
		familiarView.setOpacity(0);
		Label familiarValue = (Label) stage.getScene().lookup("#VALUE" + familiarColor);
		familiarValue.setOpacity(0);
	}

	public void showFamiliars() {
		for (FamiliarColor familiarColor : new FamiliarColor[] { FamiliarColor.BLACK, FamiliarColor.ORANGE,
				FamiliarColor.UNCOLORED, FamiliarColor.WHITE }) {
			ImageView familiarView = (ImageView) stage.getScene().lookup("#FAMILIAR" + familiarColor);
			familiarView.setOpacity(1);
			familiarView.setDisable(false);
			Label familiarValue = (Label) stage.getScene().lookup("#VALUE" + familiarColor);
			familiarValue.setOpacity(1);
		}
	}

	public void setLeaderCards(LeaderCard[] leaderCard) {
		String path = "/Image/Cards/LEADER/";
		for (int i = 0; i < 4; i++) {
			ImageView leaderView = (ImageView) stage.getScene().lookup("#HAND" + i);
			leaderView.setImage(new Image(path + leaderCard[i].getName() + ".jpg"));
			leaderView.setId("HAND" + leaderCard[i].getName().replaceAll(" ", "_"));
			Button play = (Button) stage.getScene().lookup("#PLAY" + i);
			play.setId("PLAY" + leaderCard[i].getName().replaceAll(" ", "_"));
			play.setDisable(false);
			play.setOpacity(1);
			Button discard = (Button) stage.getScene().lookup("#DISCARD" + i);
			discard.setId("DISCARD" + leaderCard[i].getName().replaceAll(" ", "_"));
			discard.setDisable(false);
			discard.setOpacity(1);
		}
	}

	public void playLeader(Event event) {
		Button button = (Button) event.getSource();
		String leaderName = button.getId().substring(4);
		clientController.playLeaderCard(leaderName.replaceAll("_", " "));
	}

	public void discardLeader(Event event) {
		Button button = (Button) event.getSource();
		String leaderName = button.getId().substring(7);
		clientController.discardLeaderCard(leaderName.replaceAll("_", " "));
	}

	public void activateLeader(Event event) {
		Button button = (Button) event.getSource();
		String leaderName = button.getId().substring(8);
		clientController.activateLeaderCard(leaderName.replaceAll("_", " "));
	}

	public void discardLeaderCard(LeaderCard leader) {
		if (stage.getScene().lookup("#HAND" + leader.getName().replaceAll(" ", "_")) != null) {
			eliminateCardFromHand(leader.getName());
		} else {
			eliminateFirstCover();
		}
	}

	public int findFirstFreeId(String id) {
		for (int i = 0; i < 4; i++) {
			if (stage.getScene().lookup("#" + id + i) != null)
				return i;
		}
		return -1;
	}

	private void eliminateFirstCover() {
		int i = findFirstFreeId("HAND");
		if (i != -1) {
			ImageView cover = (ImageView) stage.getScene().lookup("#HAND" + i);
			cover.setDisable(true);
			cover.setImage(null);
			cover.setId(null);
		}
	}

	private void eliminateCardFromHand(String leaderName) {
		ImageView leaderView = (ImageView) stage.getScene().lookup("#HAND" + leaderName.replaceAll(" ", "_"));
		leaderView.setImage(null);
		leaderView.setId(null);
		leaderView.setDisable(true);
		Button play = (Button) stage.getScene().lookup("#PLAY" + leaderName.replaceAll(" ", "_"));
		play.setId(null);
		play.setDisable(true);
		play.setOpacity(0);
		Button discard = (Button) stage.getScene().lookup("#DISCARD" + leaderName.replaceAll(" ", "_"));
		discard.setId(null);
		discard.setDisable(true);
		discard.setOpacity(0);
	}

	private void putLeaderCardInField(String leaderName, boolean activateActivateButton) {
		int i = findFirstFreeId("FIELD");
		if (i != -1) {
			ImageView fieldView = (ImageView) stage.getScene().lookup("#FIELD" + i);
			fieldView.setImage(new Image("/Image/Cards/LEADER/" + leaderName + ".jpg"));
			fieldView.setDisable(false);
			fieldView.setId("FIELD" + leaderName.replaceAll(" ", "_"));
			Label activeLabel = (Label) stage.getScene().lookup("#ACTIVELABEL" + i);
			activeLabel.setId("ACTIVELABEL" + leaderName.replaceAll(" ", "_"));
			if (activateActivateButton) {
				Button activate = (Button) stage.getScene().lookup("#ACTIVATE" + i);
				activate.setDisable(false);
				activate.setOpacity(1);
				activate.setId("ACTIVATE" + leaderName.replaceAll(" ", "_"));
			}
		}
	}

	public void playLeaderCard(LeaderCard leader) {
		if (stage.getScene().lookup("#HAND" + leader.getName().replaceAll(" ", "_")) != null) {
			putLeaderCardInField(leader.getName(), true);
			eliminateCardFromHand(leader.getName());
		} else {
			eliminateFirstCover();
			putLeaderCardInField(leader.getName(), false);
		}
	}

	public void activateLeaderCard(LeaderCard leader) {
		if (stage.getScene().lookup("#FIELD" + leader.getName().replaceAll(" ", "_")) != null) {
			leaders.add(leader);
			Label activeLabel = (Label) stage.getScene().lookup("#ACTIVELABEL" + leader.getName().replaceAll(" ", "_"));
			activeLabel.setOpacity(1);
			Button activate = (Button) stage.getScene().lookup("#ACTIVATE" + leader.getName().replaceAll(" ", "_"));
			activate.setDisable(true);
		}
	}
	
	public void deactivateLeaderCards() {
		leaders.forEach(leader -> {
			Label activeLabel = (Label) stage.getScene().lookup("#ACTIVELABEL" + leader.getName().replaceAll(" ", "_"));
			activeLabel.setOpacity(0);
		});
	}

	public void setPersonalBonusTile(Resource[] productionBonus, Resource[] harvestBonus) {
		productionGrid.setOpacity(1);
		for (int i = 0; i < 2; i++) {
			ImageView resourceView = (ImageView) stage.getScene().lookup("#PRODUCTION" + i);
			Image resource = new Image("/Image/Resources/" + productionBonus[i].getResourceType() + ".png");
			resourceView.setImage(resource);
			Label resourceValue = (Label) stage.getScene().lookup("#LABELPRODUCTION" + i);
			resourceValue.setText(Integer.toString(productionBonus[i].getAmount()));
		}
		harvestGrid.setOpacity(1);
		for (int i = 0; i < 2; i++) {
			ImageView resourceView = (ImageView) stage.getScene().lookup("#HARVEST" + i);
			Image resource = new Image("/Image/Resources/" + harvestBonus[i].getResourceType() + ".png");
			resourceView.setImage(resource);
			Label resourceValue = (Label) stage.getScene().lookup("#LABELHARVEST" + i);
			resourceValue.setText(Integer.toString(harvestBonus[i].getAmount()));
		}
	}

	public Stage getStage() {
		return stage;
	}

}
