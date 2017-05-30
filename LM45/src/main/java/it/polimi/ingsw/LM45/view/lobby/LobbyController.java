package it.polimi.ingsw.LM45.view.lobby;

import it.polimi.ingsw.LM45.view.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;

public class LobbyController {
	
	@FXML
	private RadioButton rmi;
	
	@FXML
	private RadioButton socket;
	
	@FXML
	private RadioButton cli;
	
	@FXML
	private RadioButton gui;
	
	@FXML
	private TextField nickname;
	
	private Main main;
	
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
		 */
	}
	
	public void setMain (Main main) {
		this.main = main;
	}
}
