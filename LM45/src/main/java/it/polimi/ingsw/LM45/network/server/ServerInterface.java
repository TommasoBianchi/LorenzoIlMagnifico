package it.polimi.ingsw.LM45.network.server;

import java.io.IOException;

import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.SlotType;

public interface ServerInterface {

	public void login(String username) throws IOException;
	public void placeFamiliar(FamiliarColor familiarColor, SlotType slotType, Integer slotID) throws IOException;
	public void increaseFamiliarValue(FamiliarColor familiarColor) throws IOException;
	public void playLeaderCard(String leaderCardName) throws IOException;
	public void activateLeaderCard(String leaderCardName) throws IOException;
	public void discardLeaderCard(String leaderCardName) throws IOException;
	public void endTurn() throws IOException;
	
}
