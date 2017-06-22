package it.polimi.ingsw.LM45.view.gui.personalBoard;

import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.model.cards.LeaderCard;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.PersonalBonusTile;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.network.client.ClientController;
import javafx.event.ActionEvent;
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

	private Stage stage;
	private ClientController clientController;

	private Map<CardType, FlowPane> cardFlowPanes = new EnumMap<>(CardType.class);
	private Map<ResourceType, Text> resourceTexts = new EnumMap<>(ResourceType.class);
	private Map<String, Integer> leaderPositonHand = new HashMap<>();

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
			resourceTexts.get(resource.getResourceType()).setText(resource.getAmount() + "");
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
		for (int i=0; i<4; i++) {
			ImageView leaderView = (ImageView) stage.getScene().lookup("#HAND" + i);
			leaderView.setImage(new Image(path + leaderCard[i].getName() +".jpg"));
			leaderView.setId(leaderCard[i].getName());
			Button play = (Button) stage.getScene().lookup("#PLAY" + i);
			play.setId("PLAY" + leaderCard[i].getName());
			play.setDisable(false);
			play.setOpacity(1);
			Button discard = (Button) stage.getScene().lookup("#DISCARD" + i);
			discard.setId("DISCARD" + leaderCard[i].getName());
			discard.setDisable(false);
			discard.setOpacity(1);
		}
	}

	public void playLeader(ActionEvent event) {
		Button button = (Button)event.getSource();
		String leaderName = button.getId().substring(5);
		clientController.playLeaderCard(leaderName);
	}

	public void discardLeader(MouseEvent event) {
		Button button = (Button)event.getSource();
		String leaderName = button.getId().substring(8);
		clientController.discardLeaderCard(leaderName);
	}
	
	public void discardLeaderCard(LeaderCard leader) {
		if(stage.getScene().lookup("#" + leader.getName()) != null) {
			ImageView leaderView = (ImageView) stage.getScene().lookup("#" + leader.getName());
			leaderView.setImage(null);
			leaderView.setId(null);
			leaderView.setDisable(true);
			Button play = (Button) stage.getScene().lookup("#PLAY" + leader.getName());
			play.setDisable(true);
			play.setOpacity(0);
			Button discard = (Button) stage.getScene().lookup("#DISCARD" + leader.getName());
			discard.setDisable(true);
			discard.setOpacity(0);
		} else {
			for(int i=0; i<4 ; i++){
				if(stage.getScene().lookup("#HAND" + i) != null) {
					ImageView cover = (ImageView) stage.getScene().lookup("#HAND" + i);
					cover.setDisable(true);
					cover.setImage(null);
					cover.setId(null);
					return;
				}
			}
		}
	}
	
	public void playLeaderCard(LeaderCard leader) {
		if(stage.getScene().lookup("#" + leader.getName()) != null) {
			ImageView leaderView = (ImageView) stage.getScene().lookup("#" + leader.getName());
		} else {
			for(int i=0; i<4 ; i++){
				if(stage.getScene().lookup("#HAND" + i) != null) {
					ImageView cover = (ImageView) stage.getScene().lookup("#HAND" + i);
					cover.setDisable(true);
					cover.setOpacity(0);
					return;
				}
			}
		}
	}

	public void activateLeaderCard(LeaderCard leader) {
		// TODO Auto-generated method stub
	}
	
	public void setPersonalBonusTile(PersonalBonusTile personalBonusTile) {
		// TODO Auto-generated method stub
	}

	public Stage getStage() {
		return stage;
	}

}
