package it.polimi.ingsw.LM45.network.client;

import java.io.IOException;

import it.polimi.ingsw.LM45.exceptions.GameException;
import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.Excommunication;
import it.polimi.ingsw.LM45.model.cards.LeaderCard;
import it.polimi.ingsw.LM45.model.cards.PeriodType;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.PersonalBonusTile;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.SlotType;

public interface ClientInterface {
	
	public void setUsername(String username) throws IOException;
	public void notifyPlayerTurn(String player) throws IOException;
	public void throwGameException(GameException gameException) throws IOException;
	public int chooseFrom(String[] alternatives) throws IOException;
	public void pickCard(SlotType slotType, int position, String username) throws IOException;
	public void addCardsOnTower(Card[] cards, SlotType slotType) throws IOException;
	public void addFamiliar(SlotType slotType, int position, FamiliarColor familiarColor, PlayerColor playerColor) throws IOException;
	public void setLeaderCards(LeaderCard[] leaders) throws IOException;
	public void setFamiliar(String username,FamiliarColor color, int value) throws IOException;
	public void doBonusAction(SlotType slotType, int value) throws IOException;
	public void setResources(Resource[] resources, String username) throws IOException;
	public default void setResources(Resource resource, String username) throws IOException{
		setResources(new Resource[]{ resource }, username);
	}
	public void setPersonalBonusTile(String username, PersonalBonusTile personalBonusTile) throws IOException;
	public void initializeGameBoard(String[] playersUsername, PlayerColor[] playerColors, Excommunication[] excommunications) throws IOException;
	public void placeExcommunicationToken(PlayerColor playerColor, PeriodType periodType) throws IOException;
	
}
