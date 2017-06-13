package it.polimi.ingsw.LM45.view.controller;

import it.polimi.ingsw.LM45.view.gameboard.GameBoardController;
import it.polimi.ingsw.LM45.view.leadercard.LeaderCardChoiceController;
import it.polimi.ingsw.LM45.view.lobby.LobbyController;

public class GuiController {
	
	LobbyController lobbyController;
	LeaderCardChoiceController leaderChoiceController;
	GameBoardController gameBoardController;
	InitializeViewController initializeController;
	
	public void niente(){
		
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

}
