package it.polimi.ingsw.LM45.network.client;

import java.io.IOException;

public interface ClientInterface {
	
	public void setUsername(String username) throws IOException;
	public void notifyPlayerTurn(String player) throws IOException;

	/*// FIXME: think of the right methods to have here
	public void updatePersonalBoard();
	public <T> T choose(T[] elements);
	//public void endTurn();
	public void updateBoard();
	public void startGame();*/
	
}
