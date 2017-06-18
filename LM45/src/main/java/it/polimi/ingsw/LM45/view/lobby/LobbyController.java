package it.polimi.ingsw.LM45.view.lobby;

import java.io.IOException;

import it.polimi.ingsw.LM45.controller.ClientLauncher;
import it.polimi.ingsw.LM45.view.controller.InitializeViewController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

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
	
	private InitializeViewController main;
	
	@FXML
	private void handlePlayButton() {
		
		String playerNickname = nickname.getText();
		
		Alert alert = new Alert(AlertType.WARNING);
		alert.initOwner(main.getPrimaryStage());
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
		
		if(playerNickname.equals("")){
	    		alert.setHeaderText("No Nickname Inserted");
	    		alert.setContentText("Please Insert a Nickname");
	    		alert.showAndWait();
	   	} else if(serverIp.getText().equals("")){
    		alert.setHeaderText("No Server IP Inserted");
    		alert.setContentText("Please Insert a Server IP");
    		alert.showAndWait();
	   	}
		else {
			try {
				ClientLauncher.launch(playerNickname, serverIp.getText(), portNumber, rmi.isSelected(), gui.isSelected());
			}
			catch (IOException e) {
	    		alert.setHeaderText("Unable to connect");
	    		alert.setContentText("Couldn't find server or RMI registry");
	    		alert.showAndWait();
			}			
		}
	}
	
		public void setMain (InitializeViewController main) {
		this.main = main;
	}
}
