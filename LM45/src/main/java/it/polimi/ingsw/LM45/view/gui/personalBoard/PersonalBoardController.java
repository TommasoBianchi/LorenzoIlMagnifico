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
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.serialization.FileManager;
import it.polimi.ingsw.LM45.view.gui.gameboard.GameBoardController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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

	private Map<CardType, FlowPane> cardFlowPanes = new EnumMap<CardType, FlowPane>(CardType.class);
	private Map<ResourceType, Text> resourceTexts = new EnumMap<ResourceType, Text>(ResourceType.class);
	private boolean isLocalPlayer;

	public PersonalBoardController(Stage stage, String username) {
		this.stage = stage;
		this.username = username;

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

		// TEST
		try {
			Map<CardType, List<Card>> deck = FileManager.loadCards();

			for (int j = 0; j < 12; j++) {
				CardType cardType = CardType.values()[(new Random()).nextInt(CardType.values().length - 1)];
				Card card = deck.get(cardType).get(0);
				this.addCard(card);
			}

			for (ResourceType resourceType : ResourceType.values()) {
				this.setResource(new Resource(resourceType, new Random().nextInt(20)));
			}
			this.addLeaderCard(null);
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TEST
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

	public void addCard(Card card) {
		String path = "file:Assets/Image/Cards/" + card.getCardType() + "/" + card.getName() + ".png";
		ImageView imageView = new ImageView(new Image(path));
		imageView.setPreserveRatio(true);
		imageView.setFitWidth(200);
		imageView.setFitHeight(130);
		if (cardFlowPanes.containsKey(card.getCardType())) {
			System.out.println("New " + card.getCardType());
			cardFlowPanes.get(card.getCardType()).getChildren().add(imageView);
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
	
	public Stage getStage(){
		return stage;
	}

}
