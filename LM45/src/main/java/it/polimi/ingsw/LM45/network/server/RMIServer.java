package it.polimi.ingsw.LM45.network.server;

import java.io.IOException;

import it.polimi.ingsw.LM45.exceptions.GameException;
import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.model.cards.Excommunication;
import it.polimi.ingsw.LM45.model.cards.LeaderCard;
import it.polimi.ingsw.LM45.model.cards.PeriodType;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.PersonalBonusTile;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.network.client.ClientInterface;
import it.polimi.ingsw.LM45.network.client.RemoteClientInterface;

public class RMIServer implements RemoteServerInterface, ClientInterface {

	private ServerController serverController;
	private RemoteClientInterface remoteClient;
	private String username;

	public RMIServer(RemoteClientInterface remoteClient) {
		this.remoteClient = remoteClient;
	}

	@Override
	public void login(String username) throws IOException {
		this.serverController = ServerControllerFactory.getServerControllerInstance(username);
		serverController.login(username, this);
	}

	@Override
	public void placeFamiliar(FamiliarColor familiarColor, SlotType slotType, Integer slotID) throws IOException {
		if (serverController != null) {
			serverController.placeFamiliar(username, familiarColor, slotType, slotID);
		}
	}

	@Override
	public void increaseFamiliarValue(FamiliarColor familiarColor) throws IOException {
		if (serverController != null) {
			serverController.increaseFamiliarValue(username, familiarColor);
		}
	}

	@Override
	public void playLeaderCard(String leaderCardName) throws IOException {
		if (serverController != null) {
			serverController.playLeaderCard(username, leaderCardName);
		}
	}

	@Override
	public void activateLeaderCard(String leaderCardName) throws IOException {
		if (serverController != null) {
			serverController.activateLeaderCard(username, leaderCardName);
		}
	}

	@Override
	public void discardLeaderCard(String leaderCardName) throws IOException {
		if (serverController != null) {
			serverController.discardLeaderCard(username, leaderCardName);
		}
	}

	@Override
	public void endTurn() throws IOException {
		if (serverController != null) {
			serverController.endPlayerRound(username);
		}
	}

	@Override
	public void setUsername(String username) throws IOException {
		this.username = username;
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

	@Override
	public void pickCard(Card card, String username) throws IOException {
		remoteClient.pickCard(card, username);		
	}

	@Override
	public void addCardsOnTower(Card[] cards, SlotType slotType) throws IOException {
		remoteClient.addCardsOnTower(cards, slotType);		
	}

	@Override
	public void addFamiliar(SlotType slotType, int position, FamiliarColor familiarColor, PlayerColor playerColor) throws IOException {
		remoteClient.addFamiliar(slotType, position, familiarColor, playerColor);
	}

	@Override
	public void setLeaderCards(LeaderCard[] leaders) throws IOException {
		remoteClient.setLeaderCards(leaders);
	}

	@Override
	public void setFamiliar(String username, FamiliarColor color, int value) throws IOException {
		remoteClient.setFamiliar(username, color, value);
	}

	@Override
	public void doBonusAction(SlotType slotType, int value) throws IOException {
		remoteClient.doBonusAction(slotType, value);
	}

	@Override
	public void setResources(Resource[] resources, String username) throws IOException {
		remoteClient.setResources(resources, username);
	}

	@Override
	public void setPersonalBonusTile(String username, PersonalBonusTile personalBonusTile) throws IOException {
		remoteClient.setPersonalBonusTile(username, personalBonusTile);
	}

	@Override
	public void initializeGameBoard(String[] playersUsername, PlayerColor[] playerColors, Excommunication[] excommunications) throws IOException {
		remoteClient.initializeGameBoard(playersUsername, playerColors, excommunications);
	}

	@Override
	public void placeExcommunicationToken(PlayerColor playerColor, PeriodType periodType) throws IOException {
		remoteClient.placeExcommunicationToken(playerColor, periodType);
	}

}
