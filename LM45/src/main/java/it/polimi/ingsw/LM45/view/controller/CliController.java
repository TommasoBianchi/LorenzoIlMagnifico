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
import it.polimi.ingsw.LM45.view.cli.ConsoleReader;
import it.polimi.ingsw.LM45.view.cli.ConsoleWriter;
import it.polimi.ingsw.LM45.view.cli.ConsoleWriter.ConsoleColor;

public class CliController implements ViewInterface {

	@Override
	public void showLeaderCardChoiceView() {
		ConsoleWriter.println("Welcome to Lorenzo il Magnifico !\n", ConsoleColor.RED, ConsoleColor.WHITE);
		ConsoleWriter.println("-- LeaderCard Choice Phase\n", ConsoleColor.BLUE, ConsoleColor.WHITE);
	}

	@Override
	public void initializeGameBoard(String[] playersUsername, PlayerColor[] playerColors,
			Excommunication[] excommunications) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addFamiliar(SlotType slotType, int position, FamiliarColor familiarColor, PlayerColor playerColor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pickCard(Card card, String username) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addCardsOnTower(Card[] cards, SlotType slotType) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFamiliar(String username, FamiliarColor color, int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyError(String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doBonusAction(SlotType slotType, int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public int chooseFrom(String[] alternatives) {
		ConsoleWriter.println("Choose between: \n", ConsoleColor.CYAN);
		for (int i = 0; i < alternatives.length; i++) {
			ConsoleWriter.println((i + 1) + " - " + alternatives[i]);
		}
		
		try {
			int choice = ConsoleReader.readInt();
			while(choice <= 0 || choice > alternatives.length){
				ConsoleWriter.println("Invalid choice", ConsoleColor.RED);
				choice = ConsoleReader.readInt();			
			}
			ConsoleWriter.println("Valid choice", ConsoleColor.GREEN);
			return choice - 1;
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return -1;
		}
	}

	@Override
	public void setClientController(ClientController clientController) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setResources(Resource[] resources, String username) {
		// TODO Auto-generated method stub

	}

	@Override
	public void myTurn() {
		// TODO Auto-generated method stub

	}

	@Override
	public void playerTurn(String username) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLeaderCards(LeaderCard[] leaders) {
		// TODO Auto-generated method stub

	}

	@Override
	public void discardLeaderCard(String username, LeaderCard leader) {
		// TODO Auto-generated method stub

	}

	@Override
	public void playLeaderCard(String username, LeaderCard leader) {
		// TODO Auto-generated method stub

	}

	@Override
	public void activateLeaderCard(String username, LeaderCard leader) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enableLeaderCard(String username, LeaderCard leader) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPersonalBonusTile(String username, PersonalBonusTile personalBonusTile) {
		// TODO Auto-generated method stub

	}

	@Override
	public void placeExcommunicationToken(PlayerColor playerColor, PeriodType periodType) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showFinalScore(String[] playersUsername, PlayerColor[] playerColors, int[] scores) {
		// TODO Auto-generated method stub

	}

}
