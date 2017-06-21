package it.polimi.ingsw.LM45.view.controller;

import java.util.Arrays;

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
import it.polimi.ingsw.LM45.view.gui.gameboard.GameBoardController;
import it.polimi.ingsw.LM45.view.gui.leadercard.LeaderCardChoiceController;
import it.polimi.ingsw.LM45.view.lobby.LobbyController;
import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.StageStyle;

public class GuiController implements ViewInterface {

	// da mettere tutti private quando finiscono i test
	LobbyController lobbyController;
	LeaderCardChoiceController leaderChoiceController;
	GameBoardController gameBoardController;
	ClientController clientController;

	private int choice = -1;
	private Object choiceLockToken = new Object();

	public void setChoice(int value) {
		synchronized (choiceLockToken) {
			choice = value;
			choiceLockToken.notifyAll();
		}
	}

	public void setLobbyController(LobbyController lobbyController) {
		this.lobbyController = lobbyController;
	}

	@Override
	public void showLeaderCardChoiceView() {
		leaderChoiceController = new LeaderCardChoiceController();
		leaderChoiceController.setGuiController(this);
	}

	@Override
	public void initializeGameBoard(String[] playersUsername, PlayerColor[] playerColors, Excommunication[] excommunications) {
		Platform.runLater(() -> {
			gameBoardController = new GameBoardController(playersUsername, playerColors, clientController, excommunications);
			leaderChoiceController.close();
		});
	}

	@Override
	public int chooseFrom(String[] alternatives) {
		if (alternatives.length <= 0)
			return -1;
		else if (alternatives[0].contains("(LeaderCard)"))
			return choose(alternatives, () -> leaderChoiceController
					.chooseLeader(Arrays.stream(alternatives).map(leader -> leader.substring(0, leader.indexOf("(") - 1)).toArray(String[]::new)));
		else {
			return choose(alternatives, () -> {
				Dialog<ButtonType> dialog = new Dialog<>();
				dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
				GridPane root = new GridPane();
				HBox box = new HBox(alternatives.length);
				ToggleGroup group = new ToggleGroup();
				for (int i = 0; i < alternatives.length; i++) {
					RadioButton button = new RadioButton(alternatives[i]);
					button.setToggleGroup(group);
					button.setText(alternatives[i]);
					box.getChildren().add(button);
					if (i == 0)
						group.selectToggle(button);
				}
				root.add(box, 0, 0);
				dialog.getDialogPane().setContent(root);
				dialog.setTitle("Personal Bonus Tiles");
				dialog.initStyle(StageStyle.UNDECORATED);
				dialog.showAndWait();
				setChoice(group.getToggles().indexOf(group.getSelectedToggle()));
			});
		}
	}

	private int choose(String[] alternative, Runnable uiCallback) {
		Platform.runLater(uiCallback);

		synchronized (choiceLockToken) {
			while (choice == -1)
				try {
					choiceLockToken.wait();
				}
				catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					e.printStackTrace();
				}

			int x = choice;
			choice = -1;
			return x;
		}
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
		Platform.runLater(() -> gameBoardController.addCardsOnTower(cards, slotType));
	}

	@Override
	public void setFamiliar(String username, FamiliarColor color, int value) {
		Platform.runLater(() -> gameBoardController.setFamiliarValue(username, color, value));
	}

	@Override
	public void notifyError(String message) {
		Platform.runLater(() -> gameBoardController.writeInDialogBox("ERROR -- " + message));
	}

	@Override
	public void doBonusAction(SlotType slotType, int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setClientController(ClientController clientController) {
		this.clientController = clientController;

	}

	@Override
	public void setResources(Resource[] resources, String username) {
		// TODO Auto-generated method stub

	}

	@Override
	public void myTurn() {
		Platform.runLater(() -> gameBoardController.myTurn());
	}

	@Override
	public void playerTurn(String username) {
		Platform.runLater(() -> {
			gameBoardController.writeInDialogBox("It's " + username + "'s turn!");
			gameBoardController.disableGameBoard();
		});
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
	public void setPersonalBonusTile(String username, PersonalBonusTile personalBonusTile) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void placeExcommunicationToken(PlayerColor playerColor, PeriodType periodType){
		gameBoardController.placeExcommunicationToken(playerColor, periodType);
	}

}