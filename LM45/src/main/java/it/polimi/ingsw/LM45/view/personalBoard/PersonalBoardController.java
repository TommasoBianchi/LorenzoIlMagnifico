package it.polimi.ingsw.LM45.view.personalBoard;

import java.util.HashMap;
import java.util.Map;

import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.model.cards.LeaderCard;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

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

	private Map<CardType, FlowPane> cardFlowPanes = new HashMap<CardType, FlowPane>();
	private Map<ResourceType, Text> resourceTexts = new HashMap<ResourceType, Text>();
	private boolean isLocalPlayer;
	
	public PersonalBoardController(boolean isLocalPlayer){
		this.isLocalPlayer = isLocalPlayer;
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
	
	public void setResource(Resource resource){
		if(resourceTexts.containsKey(resource.getResourceType())){
			resourceTexts.get(resource.getResourceType()).setText(resource.getAmount() + "");
		}
	}
	
	public void addLeaderCard(LeaderCard leaderCard){
		String coverImageFileName = "leaders_b_c_00";
		String path = "file:Assets/Image/Cards/LEADER/" + (isLocalPlayer ? "leader.getName()" : coverImageFileName) + ".jpg";
		ImageView imageView = new ImageView(new Image(path));
		imageView.setPreserveRatio(true);
		imageView.setFitWidth(100);
		imageView.setFitHeight(150);
		leaderCardsInHand.getChildren().add(imageView);
		System.out.println(leaderCardsInHand.getChildren().size());
	}

}
