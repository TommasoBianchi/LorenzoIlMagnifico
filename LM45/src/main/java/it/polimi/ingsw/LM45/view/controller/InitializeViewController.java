package it.polimi.ingsw.LM45.view.controller;

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

public class InitializeViewController extends Application {

	private Stage primaryStage;

	public void start(Stage stage) {
		this.primaryStage = stage;
		this.primaryStage.setTitle("Lorenzo il Magnifico");
		// showLobbyView();
		showGameBoard();
	}

	public void showLobbyView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(InitializeViewController.class.getResource("lobby/LobbyView.fxml"));
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

	public void showGameBoard() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(InitializeViewController.class.getResource("gameboard/GameBoardView.fxml"));
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
