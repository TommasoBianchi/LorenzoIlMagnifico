package it.polimi.ingsw.LM45.view;

import it.polimi.ingsw.LM45.config.BoardConfiguration;
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
	
	/**
	 * @param playersUsername array of player's usernames
	 * @param playerColors array of player's colors
	 * @param excommunications array of excommunications
	 * @param boardConfiguration the board configuration
	 */
	public void initializeGameBoard(String[] playersUsername, PlayerColor[] playerColors, Excommunication[] excommunications,
			BoardConfiguration boardConfiguration);
	
	/**
	 * @param slotType the type of slot
	 * @param position the position of the slot
	 * @param familiarColor the color of the familiar
	 * @param playerColor the color of the player
	 */
	public void addFamiliar(SlotType slotType, int position, FamiliarColor familiarColor, PlayerColor playerColor);
	
	/**
	 * @param card the card to pick
	 * @param username the username of the player
	 */
	public void pickCard(Card card, String username);
	
	/**
	 * @param cards array of cards
	 * @param slotType the type of slot
	 */
	public void addCardsOnTower(Card[] cards, SlotType slotType);
	
	/**
	 * @param username the username of the player
	 * @param color the color of the familiar
	 * @param value the value of the familiar
	 */
	public void setFamiliar(String username,FamiliarColor color, int value);
	
	/**
	 * @param message the error message
	 */
	public void notifyError(String message);
	
	/**
	 * @param slotType the type of slot to select for the bonus action
	 * @param value the value of the bonus action
	 */
	public void doBonusAction(SlotType slotType, int value);
	
	/**
	 * @param alternatives array of alternatives to choose
	 * @return the index of the choosen alternative
	 */
	public int chooseFrom(String[] alternatives);
	
	/**
	 * @param clientController the client controller
	 */
	public void setClientController(ClientController clientController);
	
	/**
	 * @param resources array of resources
	 * @param username the username of the player
	 */
	public void setResources(Resource[] resources, String username);
	
	public void myTurn();
	
	/**
	 * @param username the username of the player
	 */
	public void playerTurn(String username);
	
	/**
	 * @param leaders array of leaders
	 */
	public void setLeaderCards(LeaderCard[] leaders);
	
	/**
	 * @param username the player username
	 * @param leader the leader to discard
	 */
	public void discardLeaderCard(String username, LeaderCard leader);
	
	/**
	 * @param username the username of the player
	 * @param leader the leader to play
	 */
	public void playLeaderCard(String username, LeaderCard leader);
	
	/**
	 * @param username the username of the player
	 * @param leader the leader to activate
	 */
	public void activateLeaderCard(String username, LeaderCard leader);
	
	/**
	 * @param username the username of the player
	 * @param leader the leader to enable
	 */
	public void enableLeaderCard(String username, LeaderCard leader);
	
	/**
	 * @param username the username of the player
	 * @param personalBonusTile the personal bonus tile
	 */
	public void setPersonalBonusTile(String username, PersonalBonusTile personalBonusTile);
	
	/**
	 * @param playerColor the player color
	 * @param periodType the period of the excommunication
	 */
	public void placeExcommunicationToken(PlayerColor playerColor, PeriodType periodType);
	
	/**
	 * @param playersUsername array of player's usernames
	 * @param playerColors array of player's colors
	 * @param scores array of player's scores
	 */
	public void showFinalScore(String[] playersUsername, PlayerColor[] playerColors, int[] scores);
	
	/**
	 * @param cost number of servants to spend to increase familiar value
	 */
	public void setServantCost(int cost);
	
}
