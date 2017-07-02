package it.polimi.ingsw.LM45.view.controller;

import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.Excommunication;
import it.polimi.ingsw.LM45.model.cards.LeaderCard;
import it.polimi.ingsw.LM45.model.cards.PeriodType;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.PersonalBonusTile;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.network.client.ClientController;

public interface ViewInterface {

	public void showLeaderCardChoiceView();
	public void initializeGameBoard(String[] playersUsername, PlayerColor[] playerColors, Excommunication[] excommunications);
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
	public void setLeaderCards(LeaderCard[] leaders);
	public void discardLeaderCard(String username, LeaderCard leader);
	public void playLeaderCard(String username, LeaderCard leader);
	public void activateLeaderCard(String username, LeaderCard leader);
	public void enableLeaderCard(String username, LeaderCard leader);
	public void setPersonalBonusTile(String username, PersonalBonusTile personalBonusTile);
	public void placeExcommunicationToken(PlayerColor playerColor, PeriodType periodType);
	public void showFinalScore(String[] playersUsername, PlayerColor[] playerColors, int[] scores);
	
}
