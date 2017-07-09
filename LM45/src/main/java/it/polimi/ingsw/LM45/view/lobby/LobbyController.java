package it.polimi.ingsw.LM45.view.lobby;

import java.io.IOException;

import it.polimi.ingsw.LM45.controller.ClientLauncher;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * The controller of "LobbyView.fxml" where there is displayed
 * the game Lobby and where players have to insert their usernames,
 * the server Port, the server IP and have to choose between cli and gui interface
 * and between socket and RMI
 * 
 * It gets all these informations and starts the game based on them
 * 
 * @author Kostandin
 *
 */
public class LobbyController {

	@FXML
	private RadioButton rmi;

	@FXML
	private GridPane grid;

	@FXML
	private RadioButton gui;

	@FXML
	private TextField nickname;

	@FXML
	private TextField serverIp;

	@FXML
	private TextField serverPort;

	private Stage stage;

	/**
	 * @param stage the stage where to put the scene
	 * @throws IOException exception on loading fxml file
	 */
	public LobbyController(Stage stage) throws IOException {
		this.stage = stage;
		
		FXMLLoader loader = new FXMLLoader();
		loader.setController(this);
		loader.setLocation(LobbyController.class.getResource("LobbyView.fxml"));
		AnchorPane lobby = (AnchorPane) loader.load();
		Scene scene = new Scene(lobby);
		stage.setTitle("Lorenzo il Magnifico");
		stage.getIcons().add(new Image("/Image/Cards/LEADER/LeaderCard Cover.jpg"));
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}

	@FXML
	private void handlePlayButton() {

		String playerNickname = nickname.getText();

		Alert alert = new Alert(AlertType.WARNING);
		alert.initOwner(stage);
		alert.setTitle("Attention !");

		int portNumber = 0;
		try {
			portNumber = Integer.parseInt(serverPort.getText());
		}
		catch (NumberFormatException e1) {
			alert.setHeaderText("No Valid Server Port Inserted");
			alert.setContentText("Please Insert a Valid Server Port");
			alert.showAndWait();
			return;
		}

		if (playerNickname.equals("")) {
			alert.setHeaderText("No Nickname Inserted");
			alert.setContentText("Please Insert a Nickname");
			alert.showAndWait();
		}
		else if (serverIp.getText().equals("")) {
			alert.setHeaderText("No Server IP Inserted");
			alert.setContentText("Please Insert a Server IP");
			alert.showAndWait();
		}
		else {
			try {
				ClientLauncher.launch(playerNickname, serverIp.getText(), portNumber, rmi.isSelected(), gui.isSelected());
				stage.close();
			}
			catch (IOException e) {
				alert.setHeaderText("Unable to connect");
				alert.setContentText("Couldn't find server or RMI registry");
				alert.showAndWait();
			}
		}
	}

}
