package it.polimi.ingsw.LM45.view.controller;

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
import it.polimi.ingsw.LM45.view.cli.ConsoleReader;
import it.polimi.ingsw.LM45.view.cli.ConsoleWriter;
import it.polimi.ingsw.LM45.view.cli.ConsoleWriter.ConsoleColor;
import it.polimi.ingsw.LM45.view.cli.GameBoardCli;

public class CliController implements ViewInterface {
	
	private ClientController clientController;
	private GameBoardCli gameBoard;

	@Override
	public void showLeaderCardChoiceView() {
		ConsoleWriter.println("#     # ###### #       ####   #####  #     # ######    #######  #####     #       #####  ######  ###### #    # ######  #####     ### #         #     #    #     ####   #    # ### ###### ###  ####   #####\n"+ 
				              "#  #  # #      #      #    # #     # ##   ## #            #    #     #    #      #     # #     # #      ##   #     ## #     #     #  #         ##   ##   # #   #    #  ##   #  #  #       #  #    # #     #\n"+
				              "#  #  # #      #      #      #     # ### ### #            #    #     #    #      #     # #     # #      ###  #    ##  #     #     #  #         ### ###  #   #  #       ###  #  #  #       #  #      #     #\n"+
				              "#  #  # ####   #      #      #     # # ### # ####         #    #     #    #      #     # ######  ####   # ## #   ##   #     #     #  #         # ### # #     # #   ### # ## #  #  ####    #  #      #     #\n"+
				              "#  #  # #      #      #      #     # #     # #            #    #     #    #      #     # #   #   #      #  ###  ##    #     #     #  #         #     # ####### #    #  #  ###  #  #       #  #      #     #\n"+
				              "#  #  # #      #      #    # #     # #     # #            #    #     #    #      #     # #    #  #      #   ## ##     #     #     #  #         #     # #     # #    #  #   ##  #  #       #  #    # #     #\n"+
				              " ## ##  ###### ######  ####   #####  #     # ######       #     #####     ######  #####  #     # ###### #    # ######  #####     ### ######    #     # #     #  ####   #    # ### #      ###  ####   ##### ", ConsoleColor.WHITE);
		ConsoleWriter.printCommand("\n\n\n-- LeaderCard Choice Phase --");
	}

	@Override
	public void initializeGameBoard(String[] playersUsername, PlayerColor[] playerColors,
			Excommunication[] excommunications, BoardConfiguration boardConfiguration) {
		gameBoard = new GameBoardCli(playersUsername, playerColors, excommunications, boardConfiguration, clientController);
	}

	@Override
	public void addFamiliar(SlotType slotType, int position, FamiliarColor familiarColor, PlayerColor playerColor) {
		gameBoard.addFamiliar(slotType, position, familiarColor, playerColor);
	}

	@Override
	public void pickCard(Card card, String username) {
		gameBoard.pickCard(card, username);
	}

	@Override
	public void addCardsOnTower(Card[] cards, SlotType slotType) {
		gameBoard.addCardsOnTower(cards, slotType);
	}

	@Override
	public void setFamiliar(String username, FamiliarColor familiarColor, int value) {
		gameBoard.setFamiliarValue(username, familiarColor, value);
	}

	@Override
	public void notifyError(String message) {
		ConsoleWriter.println("");
		ConsoleWriter.printError(message);
		gameBoard.showMain();
	}

	@Override
	public void doBonusAction(SlotType slotType, int value) {
		gameBoard.doBonusAction(slotType, value);
	}

	@Override
	public int chooseFrom(String[] alternatives) {
		ConsoleWriter.println("");
		ConsoleWriter.printChoice("Choose between : ");
		for (int i = 0; i < alternatives.length; i++) {
			ConsoleWriter.printChoice((i + 1) + " - " + alternatives[i]);
		}
		
		try {
			int choice = ConsoleReader.readInt();
			while(choice <= 0 || choice > alternatives.length){
				ConsoleWriter.printError("Invalid choice !");
				choice = ConsoleReader.readInt();			
			}
			ConsoleWriter.printValidInput("Valid choice !");
			return choice - 1;
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return -1;
		}
	}

	@Override
	public void setClientController(ClientController clientController) {
		this.clientController = clientController;
	}

	@Override
	public void setResources(Resource[] resources, String username) {
		gameBoard.setResources(resources, username);

	}

	@Override
	public void myTurn() {
		gameBoard.myTurn();

	}

	@Override
	public void playerTurn(String username) {
		gameBoard.playerTurn(username);
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
		gameBoard.placeExcommunicationToken(playerColor, periodType);

	}

	@Override
	public void showFinalScore(String[] playersUsername, PlayerColor[] playerColors, int[] scores) {
		// TODO Auto-generated method stub

	}

}
