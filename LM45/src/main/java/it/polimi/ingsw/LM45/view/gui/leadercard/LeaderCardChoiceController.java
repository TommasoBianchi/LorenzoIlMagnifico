package it.polimi.ingsw.LM45.view.gui.leadercard;

import java.io.IOException;
import java.util.Arrays;

import it.polimi.ingsw.LM45.view.gui.GuiController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

/**
 * The controller of "LeaderCardChoiceView.fxml" where there are displayed
 * the leaderCards to choose before the real game starts
 * 
 * @author Kostandin
 *
 */
public class LeaderCardChoiceController {

	@FXML
	private FlowPane leaders;

	private GuiController guiController;
	private Stage stage;
	
	public LeaderCardChoiceController() {
		this.stage = new Stage();
		
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(LeaderCardChoiceController.class.getResource("LeaderCardChoiceView.fxml"));
			loader.setController(this);
			AnchorPane leaderChoice = (AnchorPane) loader.load();
			Scene scene = new Scene(leaderChoice);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.getIcons().add(new Image("/Image/Cards/LEADER/LeaderCard Cover.jpg"));
			stage.setTitle("Lorenzo il Magnifico");
			stage.show();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param leadersName an array of leaders to choose from
	 */
	public void chooseLeader(String[] leadersName) {
		String path = "/Image/Cards/LEADER/";

		System.out.println("LeaderCardChoiceController::chooseLeader -- " + leadersName.length + " leaders");

		leaders.getChildren().clear();

		for (String leader : leadersName) {
			System.out.println(leader);
			leaders.setHgap(20);
			leaders.setAlignment(Pos.CENTER);
			ImageView leaderView = new ImageView(new Image(path + leader + ".jpg"));
			leaderView.setPreserveRatio(true);
			leaderView.setFitHeight(leaders.getHeight());
			leaders.getChildren().add(leaderView);
			leaderView.setCursor(Cursor.HAND);
			leaderView.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					System.out.println(leader + " chosen");
					leaders.getChildren().clear();
					guiController.setChoice(Arrays.asList(leadersName).indexOf(leader));
				}
			});
		}
	}
	
	public Stage getStage(){
		return this.stage;
	}

	/**
	 * @param guiController the main controller of the GUI
	 */
	public void setGuiController(GuiController guiController) {
		this.guiController = guiController;
	}
	
	public void close(){
		stage.close();
	}
}
