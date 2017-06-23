package it.polimi.ingsw.LM45.view.gui.finalScore;

import java.io.IOException;

import it.polimi.ingsw.LM45.model.core.PlayerColor;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FinalScoreController {
	
	private Stage stage;
	
	public FinalScoreController(String[] playersUsername, PlayerColor[] playerColors, int[] scores){
		
		stage = new Stage();
		
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(FinalScoreController.class.getResource("FinalScoreView.fxml"));
			loader.setController(this);
			AnchorPane finalScore = (AnchorPane) loader.load();
			Scene scene = new Scene(finalScore);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.getIcons().add(new Image("/Image/Cards/LEADER/LeaderCard Cover.jpg"));
			stage.setTitle("Lorenzo il Magnifico - Final Score");
			showScores(playersUsername, playerColors, scores);
			stage.show();
		} catch (IOException e) { // TODO sistemare
			e.printStackTrace();
		}
	}
	
	private void showScores(String[] playersUsername, PlayerColor[] playerColors, int[] scores) {
		for(int i=0; i<playersUsername.length; i++)
			showPlayerScore(i, playersUsername[i], playerColors[i], scores[i]);
	}
	
	private void showPlayerScore(int position, String name, PlayerColor color, int score) {
		Label positionLabel = (Label) stage.getScene().lookup("#POSITION" + position);
		positionLabel.setOpacity(1);
		ImageView playerColor = (ImageView) stage.getScene().lookup("#IMAGE" + position);
		playerColor.setImage(new Image("/Image/ExcommunicationToken/" + color + ".png"));
		playerColor.setOpacity(1);
		Label nameLabel = (Label) stage.getScene().lookup("#NAME" + position);
		nameLabel.setText(name);
		nameLabel.setOpacity(1);
		Label scoreLabel = (Label) stage.getScene().lookup("#SCORE" + position);
		scoreLabel.setText(Integer.toString(score));
		scoreLabel.setOpacity(1);
	}
}
