package it.polimi.ingsw.LM45.view.controller;

import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.network.client.ClientController;

public interface ViewInterface {

	public void showLeaderCardChoiceView();
	public void showGameBoardView();
	public void setUsername(String username);
	public void addFamiliar(SlotType slotType, int position, FamiliarColor familiarColor, PlayerColor playerColor);
	// do also disableFamiliar() in the GameBoard
	public void pickCard(String Name);
	public void numPlayers(int numPlayers);
	public void addCardsOnTower(String[] names, SlotType slotType);
	public void doBonusAction(SlotType slotType, int value);
	public int chooseFrom(String[] alternatives);
	public void setClientController(ClientController clientController);
	public void setResources(Resource[] resources, String username);
	public void setFamiliarValue(FamiliarColor familiarColor, int value, String username);
	public void myTurn();
	public void playerTurn(String username);
	
}
