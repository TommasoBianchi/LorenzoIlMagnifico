package it.polimi.ingsw.LM45.controller;

import java.io.IOException;

import it.polimi.ingsw.LM45.view.ViewType;
import it.polimi.ingsw.LM45.view.lobby.LobbyController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	private Stage primaryStage;

	public void start(Stage stage) {
		this.primaryStage = stage;

		try {
			new LobbyController(primaryStage);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		ClientLauncher.stop(ViewType.GUI);
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
