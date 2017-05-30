package it.polimi.ingsw.LM45.view;

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
import it.polimi.ingsw.LM45.view.lobby.LobbyController;
import it.polimi.ingsw.LM45.view.personalBoard.PersonalBoardController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
	
	private Stage primaryStage;
	private AnchorPane lobby;
	
	public void start(Stage stage) {
		this.primaryStage = stage;
		this.primaryStage.setTitle("Lorenzo il Magnifico");
		//showLobbyView();
		showPersonalBoard();
	}
	
	public void showLobbyView() {
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("lobby/LobbyView.fxml"));
			lobby = (AnchorPane) loader.load();
			Scene scene = new Scene(lobby);
			/*BackgroundImage cover = new BackgroundImage(new Image("file:Assets/Image/Lobby/Cover.jpg"), BackgroundRepeat.NO_REPEAT,
					BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100, 100, false, false, true, false));
			lobby.setBackground(new Background(cover));*/
			primaryStage.setScene(scene);
			/*primaryStage.minWidthProperty().bind(scene.heightProperty());
		    primaryStage.minHeightProperty().bind(scene.widthProperty());*/
			primaryStage.show();
			LobbyController controller = loader.getController();
			controller.setMain(this);
			} catch (IOException e) {				// TODO sistemare
				e.printStackTrace();
		}
	}
	
	public void showPersonalBoard(){
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("personalBoard/PersonalBoardScene.fxml"));
			PersonalBoardController controller = new PersonalBoardController(false);
			loader.setController(controller);
			Scene scene = new Scene(loader.load());
			
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();

			Map<CardType, List<Card>> deck = null;
			try {
				deck = FileManager.loadCards();
			} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for(int i = 0; i < 12; i++){
				CardType cardType = CardType.values()[(new Random()).nextInt(CardType.values().length - 1)];
				Card card = deck.get(cardType).get(0);
				controller.addCard(card);
			}
			
			for(ResourceType resourceType : ResourceType.values())
				controller.setResource(new Resource(resourceType, new Random().nextInt(20)));
			
			for(int i = 0; i < 4; i++){
				controller.addLeaderCard(null);
			}
			
			} catch (IOException e) {				// TODO sistemare
				e.printStackTrace();
		}
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
