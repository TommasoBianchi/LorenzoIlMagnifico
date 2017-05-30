package it.polimi.ingsw.LM45.view;

import java.io.IOException;

import it.polimi.ingsw.LM45.view.lobby.LobbyController;
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
		showLobbyView();
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
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
