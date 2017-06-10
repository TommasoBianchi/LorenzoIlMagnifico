package it.polimi.ingsw.LM45.network.server;

import java.io.IOException;

import it.polimi.ingsw.LM45.exceptions.GameException;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.network.client.ClientInterface;
import it.polimi.ingsw.LM45.network.client.RemoteClientInterface;

public class RMIServer implements RemoteServerInterface, ClientInterface {
	
	private ServerController serverController;
	private RemoteClientInterface remoteClient;
	private String username;
	
	public RMIServer(ServerController serverController, RemoteClientInterface remoteClient) {
		this.serverController = serverController;
		this.remoteClient = remoteClient;
	}

	@Override
	public void login(String username) throws IOException {
		this.username = username;
		serverController.login(username, this);
	}

	@Override
	public void placeFamiliar(FamiliarColor familiarColor, SlotType slotType, Integer slotID) throws IOException {
		serverController.placeFamiliar(username, familiarColor, slotType, slotID);
	}

	@Override
	public void increaseFamiliarValue(FamiliarColor familiarColor) throws IOException {
		serverController.increaseFamiliarValue(username, familiarColor);
	}

	@Override
	public void playLeaderCard(String leaderCardName) throws IOException {
		serverController.playLeaderCard(username, leaderCardName);
	}

	@Override
	public void activateLeaderCard(String leaderCardName) throws IOException {
		serverController.activateLeaderCard(username, leaderCardName);
	}

	@Override
	public void discardLeaderCard(String leaderCardName) throws IOException {
		serverController.discardLeaderCard(username, leaderCardName);
	}

	@Override
	public void endTurn() throws IOException {
		serverController.endPlayerRound(username);
	}

	@Override
	public void setUsername(String username) throws IOException {
		remoteClient.setUsername(username);
	}

	@Override
	public void notifyPlayerTurn(String player) throws IOException {
		remoteClient.notifyPlayerTurn(player);
	}

	@Override
	public void throwGameException(GameException gameException) throws IOException {
		remoteClient.throwGameException(gameException);
	}

	@Override
	public int chooseFrom(String[] alternatives) throws IOException {
		return remoteClient.chooseFrom(alternatives);
	}

}
