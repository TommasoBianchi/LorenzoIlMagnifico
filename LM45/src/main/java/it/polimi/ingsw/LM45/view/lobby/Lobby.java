package it.polimi.ingsw.LM45.view.lobby;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Lobby extends Application {
	
	private Stage primaryStage;
	private AnchorPane lobby;
	
	public void start(Stage primarystage) {
		this.primaryStage = primarystage;
		this.primaryStage.setTitle("Lorenzo il Magnifico");
		showLobbyView();
	}
	
	public void showLobbyView() {
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Lobby.class.getResource("LobbyView.fxml"));
			lobby = (AnchorPane) loader.load();
			Scene scene = new Scene(lobby);
			primaryStage.setScene(scene);
			primaryStage.show();
			LobbyController controller = loader.getController();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
