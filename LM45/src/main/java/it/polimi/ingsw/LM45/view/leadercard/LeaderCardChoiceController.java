package it.polimi.ingsw.LM45.view.leadercard;

import java.util.Arrays;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

public class LeaderCardChoiceController {
	
	@FXML
	private FlowPane leaders;
	
	private static int choice = 10;
	private static boolean choiceMade = false;
	
	public void chooseLeader(String[] leadersName){
		
		String path = "file:Assets/Image/Cards/LEADER/";
		
		leaders.getChildren().clear();
		
		for(String leader : leadersName){
			leaders.setHgap(20);
			leaders.setAlignment(Pos.CENTER);
			ImageView leaderView = new ImageView(new Image(path + leader + ".jpg"));
			leaderView.setPreserveRatio(true);
			leaderView.setFitHeight(leaders.getHeight());
			leaders.getChildren().add(leaderView);
			leaderView.setCursor(Cursor.HAND);			
			leaderView.setOnMouseClicked(new EventHandler<MouseEvent>() {
				
				@Override
				public void handle(MouseEvent event){
					System.out.println(leader);
					leaders.getChildren().clear();
					LeaderCardChoiceController.choice = Arrays.asList(leadersName).indexOf(leader);
					choiceMade = true;
				}
			});
		}
		while(!choiceMade);
		int x = choice;
		choice = 10;
		System.out.println(x);
	}
}
