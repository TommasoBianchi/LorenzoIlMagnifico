package it.polimi.ingsw.LM45.view.lobby;

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
		if(playerNickname.equals("")){
	    		alert.setHeaderText("No Nickname Inserted");
	    		alert.setContentText("Please Please Insert a Nickname");
	   	} else if(serverIp.getText().equals("")){
    		alert.setHeaderText("No Server IP Inserted");
    		alert.setContentText("Please Please Insert a Server IP");
		} else if(serverPort.getText().equals("")){
    		alert.setHeaderText("No Server Port Inserted");
    		alert.setContentText("Please Please Insert a Server Port");
		}
		alert.showAndWait();
		
		//TODO try catch of the method launch if the server doesn't exists
		
		ClientLauncher.launch(playerNickname, serverIp.getText(), Integer.parseInt(serverPort.getText()), rmi.isSelected(), gui.isSelected());
		
	}
	
		public void setMain (InitializeViewController main) {
		this.main = main;
	}
}
