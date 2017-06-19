package it.polimi.ingsw.LM45.view.controller;

import java.io.IOException;
import java.util.Arrays;

import com.sun.glass.ui.Screen;

import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.Excommunication;
import it.polimi.ingsw.LM45.model.cards.LeaderCard;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.network.client.ClientController;
import it.polimi.ingsw.LM45.view.gui.gameboard.GameBoardController;
import it.polimi.ingsw.LM45.view.gui.leadercard.LeaderCardChoiceController;
import it.polimi.ingsw.LM45.view.lobby.LobbyController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class GuiController implements ViewInterface {
	
	LobbyController lobbyController;
	LeaderCardChoiceController leaderChoiceController;
	GameBoardController gameBoardController;
	
	Stage stage = new Stage();
	
	int choice = -1;
	
	public void setChoice(int value){
		this.choice = value;
	}
	
	public void setLobbyController(LobbyController lobbyController){
		this.lobbyController = lobbyController;
	}
	
	public void setLeaderChoiceController(LeaderCardChoiceController leaderChoiceController){
		this.leaderChoiceController = leaderChoiceController;
	}
	
	public void setGameBoardController(GameBoardController gameBoardController){
		this.gameBoardController = gameBoardController;
	}
	
	public void showLeaderCardChoice() {
		try {
			FXMLLoader loader2 = new FXMLLoader();
			loader2.setLocation(Main.class.getResource("../leadercard/LeaderCardChoiceView.fxml"));
			AnchorPane leaderChoice = (AnchorPane) loader2.load();
			Scene scene = new Scene(leaderChoice);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.setWidth(Screen.getMainScreen().getVisibleWidth());
			stage.setHeight(Screen.getMainScreen().getVisibleHeight());
			stage.show();
			LeaderCardChoiceController controller = loader2.getController();
			leaderChoiceController = controller;
			controller.setGuiController(this);
		} catch (IOException | NullPointerException e) { //TODO sistemare
			e.printStackTrace();
		}
	}

	public void showGameBoard(PlayerColor playerColor, String[] playersUsername) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("../gameboard/GameBoardView.fxml"));
			AnchorPane gameBoard = (AnchorPane) loader.load();
			Scene scene = new Scene(gameBoard);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.setWidth(Screen.getMainScreen().getVisibleWidth());
			stage.setHeight(Screen.getMainScreen().getVisibleHeight());
			stage.show();
			GameBoardController controllerGameBoard = loader.getController();
			//guiController.setGameBoardController(controllerGameBoard);
			controllerGameBoard.setScene(scene);
			controllerGameBoard.coverSlots(playersUsername.length);
			controllerGameBoard.setFamiliars(playerColor, new int[]{1,2,3,4});
			controllerGameBoard.setUsernames(playersUsername);
			controllerGameBoard.setMyUsername("JOH");
			controllerGameBoard.setServantCost(1);
			stage.getScene().setOnMouseClicked(null);
		} catch (IOException | NullPointerException e) { // TODO sistemare
			e.printStackTrace();
		}
	}
	
	public void showGameBoardView(PlayerColor[] playerColor, String[] playersUsername) {
		// playerusername ha tutti gli username...myusername lo prendo dal client controller
	}
	
	public int chooseLeaderCard(String[] leaders) {
		leaderChoiceController.chooseLeader(Arrays.stream(leaders).map(leader -> leader.substring(0, leader.indexOf("(")-1)).toArray(String[]::new));
		while(choice == -1);
		int x = choice;
		choice = -1;
		return x;
		
	}

	public void addFamiliar(SlotType slotType, int position, FamiliarColor familiarColor, PlayerColor playerColor) {
		// TODO Auto-generated method stub
		
	}

	public void pickCard(String cardName, String username) {
		// TODO Auto-generated method stub
		
	}

	public void addCardsOnTower(String[] names, SlotType slotType) {
		// TODO Auto-generated method stub
		
	}

	public void doBonusAction(SlotType slotType, int value) {
		// TODO Auto-generated method stub
		
	}

	public int chooseFrom(String[] alternatives) {
		
		return 0;
	}

	public void setClientController(ClientController clientController) {
		// TODO Auto-generated method stub
		
	}

	public void setResources(Resource[] resources, String username) {
		// TODO Auto-generated method stub
		
	}

	public void setFamiliarValue(FamiliarColor familiarColor, int value, String username) {
		// TODO Auto-generated method stub
		
	}
	
	public void myTurn(){
		
	}
	
	public void playerTurn(String username){
		
	}

	@Override
	public void showGameBoardView(PlayerColor playerColor, String[] playersUsername) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pickCard(Card card, String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addCardsOnTower(Card[] cards, SlotType slotType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFamiliar(String username, FamiliarColor color, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyError(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setExcommunications(Excommunication[] excommunications) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLeaderCards(String username, LeaderCard[] leaders) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void discardLeaderCard(String username, LeaderCard leader) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playLeaderCard(String username, LeaderCard leader) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void activateLeaderCard(String username, LeaderCard leader) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showLeaderCardChoiceView() {
		// TODO Auto-generated method stub
		
	}
}