package it.polimi.ingsw.LM45.view.controller;

import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.Excommunication;
import it.polimi.ingsw.LM45.model.cards.LeaderCard;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.network.client.ClientController;

public interface ViewInterface {

	public void showLeaderCardChoiceView();
	public int chooseLeaderCard(String[] leaders);
	public void showGameBoardView(PlayerColor playerColor, String[] playersUsername);
	// playerusername ha tutti gli username...myusername lo prendo dal client controller
	public void addFamiliar(SlotType slotType, int position, FamiliarColor familiarColor, PlayerColor playerColor);
	// do also disableFamiliar() in the GameBoard
	public void pickCard(Card card, String username);
	public void addCardsOnTower(Card[] cards, SlotType slotType);
	//cancel existing cards and sostituisci
	public void setFamiliar(String username,FamiliarColor color, int value);
	public void notifyError(String message);
	public void doBonusAction(SlotType slotType, int value);
	//dopo action disable slots and on doBonusAction renable
	public int chooseFrom(String[] alternatives);
	public void setClientController(ClientController clientController);
	public void setResources(Resource[] resources, String username);
	public void myTurn();
	public void playerTurn(String username);
	public void setExcommunications(Excommunication[] excommunications);
	public void setLeaderCards(String username, LeaderCard[] leaders);
	public void discardLeaderCard(String username, LeaderCard leader);
	public void playLeaderCard(String username, LeaderCard leader);
	public void activateLeaderCard(String username, LeaderCard leader);
	
}
