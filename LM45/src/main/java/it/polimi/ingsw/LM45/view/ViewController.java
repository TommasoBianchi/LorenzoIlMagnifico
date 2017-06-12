package it.polimi.ingsw.LM45.view;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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

import com.sun.glass.ui.Screen;

import it.polimi.ingsw.LM45.view.gameboard.GameBoardController;
import it.polimi.ingsw.LM45.view.lobby.LobbyController;
import it.polimi.ingsw.LM45.view.personalBoard.PersonalBoardController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ViewController extends Application {

	private Stage primaryStage;

	public void start(Stage stage) {
		this.primaryStage = stage;
		this.primaryStage.setTitle("Lorenzo il Magnifico");
		// showLobbyView();
		// showPersonalBoard();
		// showPersonalBoard();
		showGameBoard();
	}

	public void showLobbyView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ViewController.class.getResource("lobby/LobbyView.fxml"));
			AnchorPane lobby = (AnchorPane) loader.load();
			Scene scene = new Scene(lobby);
			primaryStage.setScene(scene);
			primaryStage.setHeight(Screen.getMainScreen().getHeight());
			double ratio = lobby.getPrefWidth() / lobby.getPrefHeight();
			primaryStage.setWidth(ratio * Screen.getMainScreen().getHeight());
			lobby.lookup("#grid").prefWidth(ratio * primaryStage.getHeight());
			lobby.lookup("#grid").maxWidth(ratio * primaryStage.getHeight());
			primaryStage.setResizable(false);
			primaryStage.show();
			LobbyController controller = loader.getController();
			controller.setMain(this);
		} catch (IOException e) { // TODO sistemare
			e.printStackTrace();
		}
	}

	public void showPersonalBoard() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ViewController.class.getResource("personalBoard/PersonalBoardScene.fxml"));
		PersonalBoardController controller = new PersonalBoardController(false);
		loader.setController(controller);

		try {
			Scene scene = new Scene(loader.load());

			Stage stage = new Stage();
			stage.setResizable(false);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// TEST
		try {
			Map<CardType, List<Card>> deck = FileManager.loadCards();

			for (int i = 0; i < 12; i++) {
				CardType cardType = CardType.values()[(new Random()).nextInt(CardType.values().length - 1)];
				Card card = deck.get(cardType).get(0);
				controller.addCard(card);
			}

			for (ResourceType resourceType : ResourceType.values())
				controller.setResource(new Resource(resourceType, new Random().nextInt(20)));

			for (int i = 0; i < 4; i++) {
				controller.addLeaderCard(null);
			}
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TEST

	}

	public void showGameBoard() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ViewController.class.getResource("gameboard/GameBoardView.fxml"));
			AnchorPane gameBoard = (AnchorPane) loader.load();
			Scene scene = new Scene(gameBoard);
			primaryStage.setScene(scene);
			primaryStage.show();
			GameBoardController controllerGameBoard = loader.getController();
			controllerGameBoard.setScene(scene);
			controllerGameBoard.coverSlots(2);
			controllerGameBoard.showFamiliars("BLUE");
			controllerGameBoard.slotModify("TERRITORY", 0);
		} catch (IOException | NullPointerException e) { // TODO sistemare
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
