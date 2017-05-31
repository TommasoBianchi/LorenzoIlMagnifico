package it.polimi.ingsw.LM45.network.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.SlotType;

public interface RemoteServerInterface extends Remote {

	public void login(String username) throws RemoteException;
	public void placeFamiliar(FamiliarColor familiarColor, Integer slotID) throws RemoteException;
	public void increaseFamiliarValue(FamiliarColor familiarColor) throws RemoteException;
	public void playLeaderCard(String leaderCardName) throws RemoteException;
	public void activateLeaderCard(String leaderCardName) throws RemoteException;
	public void discardLeaderCard(String leaderCardName) throws RemoteException;
	public void endTurn() throws RemoteException;
	
}
