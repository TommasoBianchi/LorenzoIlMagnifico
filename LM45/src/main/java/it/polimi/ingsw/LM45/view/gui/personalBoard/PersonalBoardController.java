package it.polimi.ingsw.LM45.view.gui.personalBoard;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.model.cards.LeaderCard;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.network.client.ClientController;
import it.polimi.ingsw.LM45.serialization.FileManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
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
	private FlowPane leaderCardsInHand;

	@FXML
	private FlowPane activeLeaderCards;

	private Stage stage;
	private String username;
	private String playerColor;
	private ClientController clientController;

	private Map<CardType, FlowPane> cardFlowPanes = new EnumMap<>(CardType.class);
	private Map<ResourceType, Text> resourceTexts = new EnumMap<>(ResourceType.class);
	private boolean isLocalPlayer;

	public PersonalBoardController(Stage stage, String username, PlayerColor playerColor,
			ClientController clienteController) {
		this.stage = stage;
		this.username = username;
		this.playerColor = playerColor.toString();
		this.clientController = clienteController;

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(PersonalBoardController.class.getResource("PersonalBoardScene.fxml"));
		loader.setController(this);
		try {
			Scene scene = new Scene(loader.load());
			stage.setResizable(false);
			stage.setScene(scene);
			stage.getIcons().add(new Image("file:Assets/Image/Cards/LEADER/LeaderCard Cover.jpg"));
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

		leaderCardsInHand.getChildren().clear();
	}

	public void addCard(Image card, CardType cardType) {
		ImageView imageView = new ImageView();
		imageView.setImage(card);
		imageView.setFitWidth(200);
		imageView.setFitHeight(130);
		if (cardFlowPanes.containsKey(cardType)) {
			cardFlowPanes.get(cardType).getChildren().add(imageView);
		}
	}

	public void setResource(Resource resource) {
		if (resourceTexts.containsKey(resource.getResourceType())) {
			resourceTexts.get(resource.getResourceType()).setText(resource.getAmount() + "");
		}
	}

	public void addLeaderCard(LeaderCard leaderCard) {
		String coverImageFileName = "LeaderCard Cover";
		String path = "file:Assets/Image/Cards/LEADER/" + (isLocalPlayer ? "leader.getName()" : coverImageFileName)
				+ ".jpg";
		ImageView imageView = new ImageView(new Image(path));
		imageView.setPreserveRatio(true);
		imageView.setFitWidth(100);
		imageView.setFitHeight(150);
		leaderCardsInHand.getChildren().add(imageView);
		System.out.println(leaderCardsInHand.getChildren().size());
	}

	public void setFamiliarValue(FamiliarColor color, int value) {
		Label familiarValue = (Label) stage.getScene().lookup("#VALUE" + color.toString());
		familiarValue.setText(Integer.toString(value));
	}

	public void setFamiliarsColors(PlayerColor playerColor) {
		for (FamiliarColor familiarColor : new FamiliarColor[] { FamiliarColor.BLACK, FamiliarColor.ORANGE,
				FamiliarColor.UNCOLORED, FamiliarColor.WHITE }) {
			ImageView familiarImage = (ImageView) stage.getScene().lookup("#FAMILIAR" + familiarColor);
			familiarImage
					.setImage(new Image("file:Assets/Image/Familiars/" + playerColor + "/" + familiarColor + ".png"));
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
			ImageView servantView = (ImageView) stage.getScene().lookup("#" + familiarColor);
			servantView.setOpacity(1);
			servantView.setDisable(false);
		}
	}

	public Stage getStage() {
		return stage;
	}

}
