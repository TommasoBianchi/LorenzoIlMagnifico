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
		
		if(playerNickname.equals("")){
	    		Alert alert = new Alert(AlertType.WARNING);
	    		alert.initOwner(main.getPrimaryStage());
	    		alert.setTitle("Attention !");
	    		alert.setHeaderText("No Nickname Inserted");
	    		alert.setContentText("Please Please Insert a Nickname");
	    		
	    		alert.showAndWait();
	   	} else if(serverIp.getText().equals("")){
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.initOwner(main.getPrimaryStage());
    		alert.setTitle("Attention !");
    		alert.setHeaderText("No Server IP Inserted");
    		alert.setContentText("Please Please Insert a Server IP");
    		
    		alert.showAndWait();
		} else if(serverPort.getText().equals("")){
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.initOwner(main.getPrimaryStage());
    		alert.setTitle("Attention !");
    		alert.setHeaderText("No Server Port Inserted");
    		alert.setContentText("Please Please Insert a Server Port");
    		
    		alert.showAndWait();
		}
		
		ClientLauncher.launch(playerNickname, serverIp.getText(), Integer.parseInt(serverPort.getText()), rmi.isSelected(), gui.isSelected());
	}
	
	public void serverError(String error){
		Alert alert = new Alert(AlertType.WARNING);
		alert.initOwner(main.getPrimaryStage());
		alert.setTitle("Attention !");
		alert.setHeaderText("Server Error");
		alert.setContentText(error);
	}
	
	public void setMain (InitializeViewController main) {
		this.main = main;
	}
}
