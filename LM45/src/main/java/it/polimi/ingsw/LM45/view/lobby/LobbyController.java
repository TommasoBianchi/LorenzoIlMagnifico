package it.polimi.ingsw.LM45.view.lobby;

import java.io.IOException;

import it.polimi.ingsw.LM45.controller.ClientLauncher;
import it.polimi.ingsw.LM45.view.controller.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

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

	public LobbyController(Stage stage) throws IOException {
		this.stage = stage;
		
		FXMLLoader loader = new FXMLLoader();
		loader.setController(this);
		loader.setLocation(Main.class.getResource("../lobby/LobbyView.fxml"));
		AnchorPane lobby = (AnchorPane) loader.load();
		Scene scene = new Scene(lobby);
		stage.setScene(scene);
		//stage.setHeight(Screen.getPrimary().getBounds().getHeight());
		stage.setHeight(720);
		double ratio = lobby.getPrefWidth() / lobby.getPrefHeight();
		stage.setWidth(ratio * Screen.getPrimary().getBounds().getHeight());
		lobby.lookup("#grid").prefWidth(ratio * stage.getHeight());
		lobby.lookup("#grid").maxWidth(ratio * stage.getHeight());
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
