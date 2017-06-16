package it.polimi.ingsw.LM45.view.leadercard;

import java.util.Arrays;

import it.polimi.ingsw.LM45.view.controller.GuiController;
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
	
	private GuiController guiController;
	
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
					Arrays.asList(leadersName).indexOf(leader);
				}
			});
		}
	}
	
	public void setGuiController(GuiController guiController) {
		this.guiController = guiController;
	}
}
