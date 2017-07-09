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

/**
 * The controller of "FinalScoreView.fxml" where there is displayed the final rankinng
 * with players positions, colors, usernames and scores
 * 
 * @author Kostandin
 *
 */
public class FinalScoreController {
	
	private Stage stage;
	
	/**
	 * @param playersUsername an array of players usernames
	 * @param playerColors an array of players colors
	 * @param scores an array of players scores
	 */
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param playersUsername an array of players usernames
	 * @param playerColors an array of players colors
	 * @param scores an array of players scores
	 */
	private void showScores(String[] playersUsername, PlayerColor[] playerColors, int[] scores) {
		for(int i=0; i<playersUsername.length; i++)
			showPlayerScore(i, playersUsername[i], playerColors[i], scores[i]);
	}
	
	/**
	 * @param position ranking of the player
	 * @param name the name of the player
	 * @param color the color of the player
	 * @param score the final score of the player
	 */
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
