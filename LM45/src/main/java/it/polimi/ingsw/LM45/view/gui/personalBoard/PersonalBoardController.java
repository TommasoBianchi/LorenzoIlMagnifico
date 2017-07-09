package it.polimi.ingsw.LM45.view.gui.personalBoard;

import java.io.IOException;
import java.util.EnumMap;
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

/**
 * The controller of "PersonalBoardScene.fxml" where there is displayed
 * the player personalBoard
 * 
 * It handles all actions that concerns the personal board like
 * all leaders actions (Discard, Play and Activate) or like pickCard
 * and other familiars actions
 * 
 * @author Kostandin
 *
 */
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

	/**
	 * @param stage stage where to set the PersonalBoard scene
	 * @param username the name of the PersonalBoard's player
	 * @param clienteController the Client Controller
	 */
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

	/**
	 * @param cardView the Image of the Card to add
	 * @param cardType the CardType of the Card to add
	 */
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

	/**
	 * Changes Image's size when player press mouse button over an image
	 * so it looks like a zoom effect
	 * 
	 * @param event when player press mouse button over an image
	 */
	public void zoomImage(MouseEvent event) {
		ImageView image = (ImageView) event.getSource();
		image.setScaleX(2);
		image.setScaleY(2);
		image.getParent().getParent().toFront();
		image.setTranslateX(-45);
		image.setTranslateY(-50);
	}

	/**
	 * Reset the Image size when mouse button is released after a zoom effect
	 * 
	 * @param event when player releases mouse button after zoomImage's event
	 */
	public void resetZoomImage(MouseEvent event) {
		ImageView image = (ImageView) event.getSource();
		image.setScaleX(1);
		image.setScaleY(1);
		image.setTranslateX(0);
		image.setTranslateY(0);
	}

	/**
	 * @param resource the resource to modify in the PersonalBoard
	 */
	public void setResource(Resource resource) {
		if (resourceTexts.containsKey(resource.getResourceType())) {
			resourceTexts.get(resource.getResourceType()).setText(Integer.toString(resource.getAmount()));
		}
	}

	/**
	 * @param color the color of the familiar
	 * @param value the new value of the familiar
	 */
	public void setFamiliarValue(FamiliarColor color, int value) {
		Label familiarValue = (Label) stage.getScene().lookup("#VALUE" + color.toString());
		familiarValue.setText(Integer.toString(value));
	}

	/**
	 * Sets the images of the Familiars using playerColor to build the Path's String
	 * 
	 * @param playerColor the color of the player
	 */
	public void setFamiliarsColors(PlayerColor playerColor) {
		for (FamiliarColor familiarColor : new FamiliarColor[] { FamiliarColor.BLACK, FamiliarColor.ORANGE,
				FamiliarColor.UNCOLORED, FamiliarColor.WHITE }) {
			ImageView familiarImage = (ImageView) stage.getScene().lookup("#FAMILIAR" + familiarColor);
			familiarImage.setImage(new Image("/Image/Familiars/" + playerColor + "/" + familiarColor + ".png"));
		}
	}

	/**
	 * Hides familiar's Image to show that familiar is already used
	 * 
	 * @param familiarColor the color of the familiar
	 */
	public void familiarUsed(FamiliarColor familiarColor) {
		ImageView familiarView = (ImageView) stage.getScene().lookup("#FAMILIAR" + familiarColor);
		familiarView.setOpacity(0);
		Label familiarValue = (Label) stage.getScene().lookup("#VALUE" + familiarColor);
		familiarValue.setOpacity(0);
	}

	/**
	 * Shows up again all hidden familiars and their values resetting their opacity
	 */
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

	/**
	 * Takes an array of leaderCards and puts them in the "HAND" ImageViews
	 * and enables play and discard buttons
	 * 
	 * @param leaderCard an array of leader Cards to set
	 */
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

	/**
	 * @param event when Play button is clicked
	 */
	public void playLeader(Event event) {
		Button button = (Button) event.getSource();
		String leaderName = button.getId().substring(4);
		clientController.playLeaderCard(leaderName.replaceAll("_", " "));
	}

	/**
	 * @param event when Discard button is clicked
	 */
	public void discardLeader(Event event) {
		Button button = (Button) event.getSource();
		String leaderName = button.getId().substring(7);
		clientController.discardLeaderCard(leaderName.replaceAll("_", " "));
	}

	/**
	 * @param event when Activate button is clicked
	 */
	public void activateLeader(Event event) {
		Button button = (Button) event.getSource();
		String leaderName = button.getId().substring(8);
		clientController.activateLeaderCard(leaderName.replaceAll("_", " "));
	}

	/**
	 * @param leader the leader to discard
	 */
	public void discardLeaderCard(LeaderCard leader) {
		if (stage.getScene().lookup("#HAND" + leader.getName().replaceAll(" ", "_")) != null) {
			eliminateCardFromHand(leader.getName());
		} else {
			eliminateFirstCover();
		}
	}

	/**
	 * @param id string that represents part of the id i want to find
	 * @return the position of the first unused element with that id
	 */
	public int findFirstFreeId(String id) {
		for (int i = 0; i < 4; i++) {
			if (stage.getScene().lookup("#" + id + i) != null)
				return i;
		}
		return -1;
	}

	/**
	 * Discards the first Covered LeaderCard in hand
	 */
	private void eliminateFirstCover() {
		int i = findFirstFreeId("HAND");
		if (i != -1) {
			ImageView cover = (ImageView) stage.getScene().lookup("#HAND" + i);
			cover.setDisable(true);
			cover.setImage(null);
			cover.setId(null);
		}
	}

	/**
	 * Finds the leader's image, play and discard buttons thanks to leader's name
	 * and hides and disables them
	 * 
	 * @param leaderName the name of the leader to eliminate
	 */
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

	/**
	 * Creates a leader's image and puts it on the field and if activateActivateButton is true
	 * means that this is my PersonalBoard so it also enables the Button that let's me
	 * to activate that leader card
	 * 
	 * @param leaderName name of the leader to put in the field
	 * @param activateActivateButton a boolean : TRUE to activate "Activate" button
	 */
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

	/**
	 * @param leader leader card to play
	 */
	public void playLeaderCard(LeaderCard leader) {
		if (stage.getScene().lookup("#HAND" + leader.getName().replaceAll(" ", "_")) != null) {
			putLeaderCardInField(leader.getName(), true);
			eliminateCardFromHand(leader.getName());
		} else {
			eliminateFirstCover();
			putLeaderCardInField(leader.getName(), false);
		}
	}

	/**
	 * It shows this text next to the activated leader card "Active !"
	 * 
	 * @param leader leader card to activate
	 */
	public void activateLeaderCard(LeaderCard leader) {
		if (stage.getScene().lookup("#FIELD" + leader.getName().replaceAll(" ", "_")) != null) {
			Label activeLabel = (Label) stage.getScene().lookup("#ACTIVELABEL" + leader.getName().replaceAll(" ", "_"));
			activeLabel.setOpacity(1);
			Button activate = (Button) stage.getScene().lookup("#ACTIVATE" + leader.getName().replaceAll(" ", "_"));
			if(activate != null){
				activate.setDisable(true);
			}
		}
	}
	
	/**
	 * It means that leader card is not more active so it hides the "Active !" text
	 * and shows again the Activate Button
	 * 
	 * @param leader leader card to enable
	 */
	public void enableLeaderCard(LeaderCard leader) {
		if (stage.getScene().lookup("#FIELD" + leader.getName().replaceAll(" ", "_")) != null) {
			Label activeLabel = (Label) stage.getScene().lookup("#ACTIVELABEL" + leader.getName().replaceAll(" ", "_"));
			activeLabel.setOpacity(0);
			Button activate = (Button) stage.getScene().lookup("#ACTIVATE" + leader.getName().replaceAll(" ", "_"));
			if(activate != null){
				activate.setDisable(false);
			}
		}
	}

	/**
	 * @param productionBonus an array of (bonus) resources that player gains when produces
	 * @param harvestBonus an array of (bonus) resources that player gains when harvest
	 */
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

	/**
	 * @return the stage of this PersonalBoard
	 */
	public Stage getStage() {
		return stage;
	}

}
