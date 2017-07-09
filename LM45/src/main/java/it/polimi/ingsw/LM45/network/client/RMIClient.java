package it.polimi.ingsw.LM45.network.client;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.polimi.ingsw.LM45.config.BoardConfiguration;
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
import it.polimi.ingsw.LM45.network.server.RMIRemoteFactory;
import it.polimi.ingsw.LM45.network.server.RemoteServerInterface;
import it.polimi.ingsw.LM45.network.server.ServerInterface;

public class RMIClient implements RemoteClientInterface, ServerInterface {

	private ClientController clientController;
	private RemoteServerInterface remoteServer;
	private ExecutorService executorService; // This is needed to ensure void RMI methods returns as soon as possible

	public RMIClient(String host, ClientController clientController) throws RemoteException, NotBoundException {
		this.clientController = clientController;
		this.executorService = Executors.newCachedThreadPool();

		Registry registry = LocateRegistry.getRegistry(host);
		remoteServer = ((RMIRemoteFactory) registry.lookup("RMIFactory"))
				.getServerInterface((RemoteClientInterface) UnicastRemoteObject.exportObject(this, 0));
	}

	public void stop() {
		System.out.println("Shutting down RMIClient");
	}

	@Override
	public void login(String username) throws IOException {
		remoteServer.login(username);
	}

	@Override
	public void placeFamiliar(FamiliarColor familiarColor, SlotType slotType, Integer slotID) throws IOException {
		remoteServer.placeFamiliar(familiarColor, slotType, slotID);
	}

	@Override
	public void increaseFamiliarValue(FamiliarColor familiarColor) throws IOException {
		remoteServer.increaseFamiliarValue(familiarColor);
	}

	@Override
	public void playLeaderCard(String leaderCardName) throws IOException {
		remoteServer.playLeaderCard(leaderCardName);
	}

	@Override
	public void activateLeaderCard(String leaderCardName) throws IOException {
		remoteServer.activateLeaderCard(leaderCardName);
	}

	@Override
	public void discardLeaderCard(String leaderCardName) throws IOException {
		remoteServer.discardLeaderCard(leaderCardName);
	}

	@Override
	public void endTurn() throws IOException {
		remoteServer.endTurn();
	}

	@Override
	public void setUsername(String username) throws IOException {
		executorService.submit(() -> clientController.setUsername(username));
	}

	@Override
	public void notifyPlayerTurn(String player) throws IOException {
		executorService.submit(() -> clientController.notifyPlayerTurn(player));
	}

	@Override
	public void throwGameException(GameException gameException) throws IOException {
		executorService.submit(() -> clientController.throwGameException(gameException));
	}

	@Override
	public int chooseFrom(String[] alternatives) throws IOException {
		return clientController.chooseFrom(alternatives);
	}

	@Override
	public void pickCard(Card card, String username) {
		executorService.submit(() -> clientController.pickCard(card, username));
	}

	@Override
	public void addCardsOnTower(Card[] cards, SlotType slotType) {
		executorService.submit(() -> clientController.addCardsOnTower(cards, slotType));
	}

	@Override
	public void addFamiliar(SlotType slotType, int position, FamiliarColor familiarColor, PlayerColor playerColor) {
		executorService.submit(() -> clientController.addFamiliar(slotType, position, familiarColor, playerColor));
	}

	@Override
	public void setServantCost(int cost) throws IOException {
		executorService.submit(() -> clientController.setServantCost(cost));
	}

	@Override
	public void setLeaderCards(LeaderCard[] leaders) {
		executorService.submit(() -> clientController.setLeaderCards(leaders));
	}

	@Override
	public void setFamiliar(String username, FamiliarColor color, int value) {
		executorService.submit(() -> clientController.setFamiliar(username, color, value));
	}

	@Override
	public void doBonusAction(SlotType slotType, int value) {
		executorService.submit(() -> clientController.doBonusAction(slotType, value));
	}

	@Override
	public void setResources(Resource[] resources, String username) {
		executorService.submit(() -> clientController.setResources(resources, username));
	}

	@Override
	public void setPersonalBonusTile(String username, PersonalBonusTile personalBonusTile) throws IOException {
		executorService.submit(() -> clientController.setPersonalBonusTile(username, personalBonusTile));
	}

	@Override
	public void initializeGameBoard(String[] playersUsername, PlayerColor[] playerColors, Excommunication[] excommunications,
			BoardConfiguration boardConfiguration) throws IOException {
		executorService.submit(() -> clientController.initializeGameBoard(playersUsername, playerColors, excommunications, boardConfiguration));
	}

	@Override
	public void placeExcommunicationToken(PlayerColor playerColor, PeriodType periodType) throws IOException {
		executorService.submit(() -> clientController.placeExcommunicationToken(playerColor, periodType));
	}

	@Override
	public void playLeaderCard(String username, LeaderCard leader) throws IOException {
		executorService.submit(() -> clientController.playLeaderCard(username, leader));
	}

	@Override
	public void activateLeaderCard(String username, LeaderCard leader) throws IOException {
		executorService.submit(() -> clientController.activateLeaderCard(username, leader));
	}

	@Override
	public void discardLeaderCard(String username, LeaderCard leader) throws IOException {
		executorService.submit(() -> clientController.discardLeaderCard(username, leader));
	}

	@Override
	public void enableLeaderCard(String username, LeaderCard leader) throws IOException {
		executorService.submit(() -> clientController.enableLeaderCard(username, leader));
	}

	@Override
	public void showFinalScore(String[] playersUsername, PlayerColor[] playerColors, int[] scores) throws IOException {
		executorService.submit(() -> clientController.showFinalScore(playersUsername, playerColors, scores));
	}

}
