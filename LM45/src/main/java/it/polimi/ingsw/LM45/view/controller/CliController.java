package it.polimi.ingsw.LM45.view.controller;

import it.polimi.ingsw.LM45.model.cards.LeaderCard;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.network.client.ClientController;

public class CliController implements ViewInterface {

	@Override
	public void showLeaderCardChoiceView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int chooseLeaderCard(LeaderCard[] leaders) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void showGameBoardView(int numPlayers, PlayerColor playerColor, String[] playersUsername) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUsername(String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addFamiliar(SlotType slotType, int position, FamiliarColor familiarColor, PlayerColor playerColor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pickCard(String Name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addCardsOnTower(String[] names, SlotType slotType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doBonusAction(SlotType slotType, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int chooseFrom(String[] alternatives) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setClientController(ClientController clientController) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setResources(Resource[] resources, String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFamiliarValue(FamiliarColor familiarColor, int value, String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void myTurn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerTurn(String username) {
		// TODO Auto-generated method stub
		
	}

}
