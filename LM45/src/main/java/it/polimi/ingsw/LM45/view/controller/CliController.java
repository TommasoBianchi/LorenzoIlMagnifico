package it.polimi.ingsw.LM45.view.controller;

import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.Excommunication;
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
	public int chooseLeaderCard(String[] leaders) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void showGameBoardView(PlayerColor playerColor, String[] playersUsername) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addFamiliar(SlotType slotType, int position, FamiliarColor familiarColor, PlayerColor playerColor) {
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
	public void myTurn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerTurn(String username) {
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
	
	/* 4 personalboard new class to do on cli with all personalBoard components
	 * Board
	 * 
	 */


}
