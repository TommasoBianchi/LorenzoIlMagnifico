package it.polimi.ingsw.LM45.view.controller;

import java.io.IOException;

import com.sun.glass.ui.Screen;

import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.view.gameboard.GameBoardController;
import it.polimi.ingsw.LM45.view.leadercard.LeaderCardChoiceController;
import it.polimi.ingsw.LM45.view.lobby.LobbyController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class InitializeViewController extends Application {

	private Stage primaryStage;
	private GuiController guiController;

	public void start(Stage stage) {
		this.primaryStage = stage;
		this.primaryStage.setTitle("Lorenzo il Magnifico");
		this.primaryStage.getIcons().add(new Image("file:Assets/Image/Cards/LEADER/LeaderCard Cover.jpg"));
		showLobbyView();
		//showLeaderCardChoice();
		//showGameBoard(2, PlayerColor.RED, new String[]{"Pippo", "Cucu", "Lulu"});
	}

	public void showLobbyView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(InitializeViewController.class.getResource("../lobby/LobbyView.fxml"));
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
			//guiController.setLobbyController(controller);
			controller.setMain(this);
		} catch (IOException e) { // TODO sistemare
			e.printStackTrace();
		}
	}
	
	public void showLeaderCardChoice() {
		try {
			FXMLLoader loader2 = new FXMLLoader();
			loader2.setLocation(InitializeViewController.class.getResource("../leadercard/LeaderCardChoiceView.fxml"));
			AnchorPane leaderChoice = (AnchorPane) loader2.load();
			Scene scene = new Scene(leaderChoice);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setWidth(Screen.getMainScreen().getVisibleWidth());
			primaryStage.setHeight(Screen.getMainScreen().getVisibleHeight());
			primaryStage.show();
			LeaderCardChoiceController controller = loader2.getController();
			guiController.setLeaderChoiceController(controller);
			controller.setGuiController(guiController);
		} catch (IOException | NullPointerException e) { //TODO sistemare
			e.printStackTrace();
		}
	}

	public void showGameBoard(int numOfPlayers, PlayerColor playerColor, String[] playersUsername) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(InitializeViewController.class.getResource("../gameboard/GameBoardView.fxml"));
			AnchorPane gameBoard = (AnchorPane) loader.load();
			Scene scene = new Scene(gameBoard);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setWidth(Screen.getMainScreen().getVisibleWidth());
			primaryStage.setHeight(Screen.getMainScreen().getVisibleHeight());
			primaryStage.show();
			GameBoardController controllerGameBoard = loader.getController();
			//guiController.setGameBoardController(controllerGameBoard);
			controllerGameBoard.setScene(scene);
			controllerGameBoard.coverSlots(numOfPlayers);
			controllerGameBoard.setFamiliars(playerColor, new int[]{1,2,3,4});
			controllerGameBoard.setUsernames(playersUsername);
			controllerGameBoard.setMyUsername("JOH");
			controllerGameBoard.setServantCost(1);
			primaryStage.getScene().setOnMouseClicked(null);
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
