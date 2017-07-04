	package it.polimi.ingsw.LM45.view.controller;

import java.io.IOException;

import it.polimi.ingsw.LM45.controller.ClientLauncher;
import it.polimi.ingsw.LM45.view.lobby.LobbyController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	
	private static final boolean FAST_TESTING = true;

	private Stage primaryStage;

	public void start(Stage stage) {
		this.primaryStage = stage;
		
		try {
			if(FAST_TESTING)
				ClientLauncher.launch("Tommy", "127.0.0.1", 7000, false, true);
			else
				new LobbyController(primaryStage);			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stop(){
		ClientLauncher.stop();
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
