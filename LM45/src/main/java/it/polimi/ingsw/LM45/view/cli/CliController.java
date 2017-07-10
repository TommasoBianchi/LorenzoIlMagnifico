package it.polimi.ingsw.LM45.view.cli;

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
import it.polimi.ingsw.LM45.view.ViewInterface;
import it.polimi.ingsw.LM45.view.cli.ConsoleWriter.ConsoleColor;

public class CliController implements ViewInterface {
	
	private ClientController clientController;
	private GameBoardCli gameBoard;
	private Object gameBoardInitializationToken = new Object();

	@Override
	public void showLeaderCardChoiceView() {
		ConsoleWriter.println("#     ###  ###  #### #   # #####  ###     # #       #   #   #    ###  #   # # #### #  ###   ###\n" +
							  "#    #   # #  # #    ##  #    #  #   #    # #       ## ##  # #  #   # ##  # # #    # #   # #   #\n" +
							  "#    #   # ###  ###  # # #   #   #   #    # #       # # # ##### #     # # # # ###  # #     #   #\n" +
							  "#    #   # # #  #    #  ##  #    #   #    # #       #   # #   # #  ## #  ## # #    # #   # #   #\n" +
							  "####  ###  #  # #### #   # #####  ###     # ####    #   # #   #  ###  #   # # #    #  ###   ###\n", ConsoleColor.WHITE);
		ConsoleWriter.printCommand("\n\n\n-- LeaderCard Choice Phase --");
	}

	@Override
	public void initializeGameBoard(String[] playersUsername, PlayerColor[] playerColors,
			Excommunication[] excommunications, BoardConfiguration boardConfiguration) {
		synchronized (gameBoardInitializationToken) {
			gameBoard = new GameBoardCli(playersUsername, playerColors, excommunications, boardConfiguration, clientController);			
			gameBoardInitializationToken.notifyAll();
		}
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
		if(gameBoard == null)
			waitGameBoardInitialization();
		gameBoard.addCardsOnTower(cards, slotType);
	}

	@Override
	public void setFamiliar(String username, FamiliarColor familiarColor, int value) {
		if(gameBoard == null)
			waitGameBoardInitialization();
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
		ConsoleWriter.println("");
		for (int i = 0; i < alternatives.length; i++) {
			ConsoleWriter.printChoice((i + 1) + " - " + alternatives[i]);
			ConsoleWriter.printChoice("---------------");
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
		if(gameBoard == null)
			waitGameBoardInitialization();
		gameBoard.setResources(resources, username);

	}

	@Override
	public void myTurn() {
		if(gameBoard == null)
			waitGameBoardInitialization();
		gameBoard.myTurn();

	}

	@Override
	public void playerTurn(String username) {
		if(gameBoard == null)
			waitGameBoardInitialization();
		gameBoard.playerTurn(username);
	}

	@Override
	public void setLeaderCards(LeaderCard[] leaders) {
		if(gameBoard == null)
			waitGameBoardInitialization();
		gameBoard.setLeaderCards(leaders);
	}

	@Override
	public void discardLeaderCard(String username, LeaderCard leader) {
		gameBoard.discardLeaderCard(username, leader);
	}

	@Override
	public void playLeaderCard(String username, LeaderCard leader) {
		gameBoard.playLeaderCard(username, leader);
	}

	@Override
	public void activateLeaderCard(String username, LeaderCard leader) {
		gameBoard.activateLeaderCard(username, leader);
	}

	@Override
	public void enableLeaderCard(String username, LeaderCard leader) {
		gameBoard.enableLeaderCard(username, leader);
	}

	@Override
	public void setPersonalBonusTile(String username, PersonalBonusTile personalBonusTile) {
		if(gameBoard == null)
			waitGameBoardInitialization();
		gameBoard.setPersonalBonusTile(username, personalBonusTile);
	}

	@Override
	public void placeExcommunicationToken(PlayerColor playerColor, PeriodType periodType) {
		gameBoard.placeExcommunicationToken(playerColor, periodType);

	}

	@Override
	public void showFinalScore(String[] playersUsername, PlayerColor[] playerColors, int[] scores) {
		ConsoleWriter.println("");
		ConsoleWriter.printCommand("Final Score");
		for(int i=0; i<playersUsername.length; i++){
			ConsoleWriter.println("");
			ConsoleWriter.printShowInfo(i + ". " + playersUsername[i] + " " + scores[i]);
		}
		ConsoleWriter.println("####### #    # ######    ###### #    # ##### \n"
							+ "   #    #    # #         #      ##   # #    #\n"
							+ "   #    #    # #         #      ###  # #    #\n"
							+ "   #    ###### ####      ####   # ## # #    #\n"
							+ "   #    #    # #         #      #  ### #    #\n"
							+ "   #    #    # #         #      #   ## #    #\n"
							+ "   #    #    # ######    ###### #    # ##### \n",ConsoleColor.WHITE);

	}
	
	private void waitGameBoardInitialization(){
		synchronized (gameBoardInitializationToken) {
			try {
				while(gameBoard == null)
					gameBoardInitializationToken.wait();
			}
			catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				e.printStackTrace();
			}
		}
	}

	@Override
	public void setServantCost(int cost) {
		gameBoard.setServantCost(cost);
	}

}
