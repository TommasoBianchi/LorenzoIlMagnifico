package it.polimi.ingsw.LM45.network.client;

import java.io.IOException;

import it.polimi.ingsw.LM45.exceptions.GameException;

public interface ClientInterface {
	
	public void setUsername(String username) throws IOException;
	public void notifyPlayerTurn(String player) throws IOException;
	public void throwGameException(GameException gameException) throws IOException;
	public int chooseFrom(String[] alternatives) throws IOException;
	
}
