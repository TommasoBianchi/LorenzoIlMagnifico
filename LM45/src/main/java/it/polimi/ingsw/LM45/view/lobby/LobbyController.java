package it.polimi.ingsw.LM45.view.lobby;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

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
	
	public LobbyController() {
	}
	
	@FXML
	private void handlePlayButton() {
		
		String playernickname = nickname.getText();
		
		if ( )
	}
	
}
