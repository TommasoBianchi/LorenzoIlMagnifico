package it.polimi.ingsw.LM45.view.lobby;

import it.polimi.ingsw.LM45.view.controller.InitializeViewController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert.AlertType;

public class LobbyController {
	
	@FXML
	private RadioButton rmi;
	
	@FXML
	private GridPane grid;
	
	@FXML
	private RadioButton socket;
	
	@FXML
	private RadioButton cli;
	
	@FXML
	private RadioButton gui;
	
	@FXML
	private TextField nickname;
	
	private InitializeViewController main;
	
	public LobbyController() {
	}
	
	@FXML
	private void handlePlayButton() {
		
		String playerNickname = nickname.getText();
		
		if(!playerNickname.equals("")){
	    	System.out.println(playerNickname);
	    } else {
	    		Alert alert = new Alert(AlertType.WARNING);
	    		alert.initOwner(main.getPrimaryStage());
	    		alert.setTitle("Attention !");
	    		alert.setHeaderText("No Nickname Inserted");
	    		alert.setContentText("Please Please Insert a Nickname");
	    		
	    		alert.showAndWait();
	   	}
		
		/* Controlla radio toggle tipo
		 * if( rmi.isSelected )
		 * 	avvia RMI;
		 * else
		 * 	avvia socket;
		 * 
		 * lo sesso poi per CLI e GUI
		 * 
		 * metti avvia gioco con Enter
		 */
	}
	
	public void setMain (InitializeViewController main) {
		this.main = main;
	}
}
