package it.polimi.ingsw.LM45.view.controller;

import java.io.IOException;

import it.polimi.ingsw.LM45.controller.ClientLauncher;
import it.polimi.ingsw.LM45.view.lobby.LobbyController;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
	
	private static final boolean FAST_TESTING = false;

	private Stage primaryStage;

	public void start(Stage stage) {
		this.primaryStage = stage;
		this.primaryStage.setTitle("Lorenzo il Magnifico");
		this.primaryStage.getIcons().add(new Image("file:Assets/Image/Cards/LEADER/LeaderCard Cover.jpg"));
		this.primaryStage.initStyle(StageStyle.TRANSPARENT);
		
		try {
			if(FAST_TESTING)
				ClientLauncher.launch("Tommy", "127.0.0.1", 7000, false, true);
			else
				new LobbyController(primaryStage);			
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
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
