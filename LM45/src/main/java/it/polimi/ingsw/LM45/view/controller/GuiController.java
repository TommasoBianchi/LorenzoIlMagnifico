package it.polimi.ingsw.LM45.view.controller;

import java.util.Arrays;

import it.polimi.ingsw.LM45.model.cards.LeaderCard;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.network.client.ClientController;
import it.polimi.ingsw.LM45.view.gameboard.GameBoardController;
import it.polimi.ingsw.LM45.view.leadercard.LeaderCardChoiceController;
import it.polimi.ingsw.LM45.view.lobby.LobbyController;

public class GuiController implements ViewInterface {
	
	LobbyController lobbyController;
	LeaderCardChoiceController leaderChoiceController;
	GameBoardController gameBoardController;
	InitializeViewController initializeController;
	
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
	
	public void setInitializeController(InitializeViewController initializeController){
		this.initializeController = initializeController;
	}
	
	public void showLeaderCardChoiceView (){
		initializeController.showLeaderCardChoice();
	}
	
	public void showGameBoardView(int numPlayers, PlayerColor playerColor, String[] playersUsername) {
				
	}
	
	public int chooseLeaderCard(LeaderCard[] leaders) {
		leaderChoiceController.chooseLeader(Arrays.stream(leaders).map(leaderCard -> leaderCard.getName()).toArray(String[]::new));
		while(choice == -1);
		int x = choice;
		choice = -1;
		return x;
		
		//TODO fix it better
	}

	public void setUsername(String username) {
		// TODO Auto-generated method stub
		
	}

	public void addFamiliar(SlotType slotType, int position, FamiliarColor familiarColor, PlayerColor playerColor) {
		// TODO Auto-generated method stub
		
	}

	public void pickCard(String Name) {
		// TODO Auto-generated method stub
		
	}

	public void numPlayers(int numPlayers) {
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
}