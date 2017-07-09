package it.polimi.ingsw.LM45.view.gui;

import java.util.Arrays;

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
import it.polimi.ingsw.LM45.view.gui.finalScore.FinalScoreController;
import it.polimi.ingsw.LM45.view.gui.gameboard.GameBoardController;
import it.polimi.ingsw.LM45.view.gui.leadercard.LeaderCardChoiceController;
import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GuiController implements ViewInterface {

	// TODO da mettere tutti private quando finiscono i test
	LeaderCardChoiceController leaderChoiceController;
	GameBoardController gameBoardController;
	ClientController clientController;
	FinalScoreController finalController;
	
	private Stage currentStage;

	private int choice = -1;
	private Object choiceLockToken = new Object();
	private Dialog<ButtonType> choiceDialog;
	private boolean leaderChoicePhase = true;

	public void setChoice(int value) {
		synchronized (choiceLockToken) {
			choice = value;
			choiceLockToken.notifyAll();
		}
	}

	@Override
	public void showLeaderCardChoiceView() {
		leaderChoiceController = new LeaderCardChoiceController();
		currentStage = leaderChoiceController.getStage();
		leaderChoiceController.setGuiController(this);
	}

	@Override
	public void initializeGameBoard(String[] playersUsername, PlayerColor[] playerColors, Excommunication[] excommunications,
			BoardConfiguration boardConfiguration) {
		Platform.runLater(() -> {
			gameBoardController = new GameBoardController(playersUsername, playerColors, clientController, excommunications);
			currentStage = gameBoardController.getStage();
			leaderChoicePhase = false;
			leaderChoiceController.close();
		});
	}

	@Override
	public int chooseFrom(String[] alternatives) {
		if (alternatives.length <= 0)
			return -1;
		else if (leaderChoicePhase && alternatives[0].contains("(LeaderCard)"))
			return choose(() -> leaderChoiceController
					.chooseLeader(Arrays.stream(alternatives).map(leader -> leader.substring(0, leader.indexOf('(') - 1)).toArray(String[]::new)));
		else {
			return choose(() -> {
				if (choiceDialog != null && choiceDialog.isShowing())
					choiceDialog.close();

				choiceDialog = new Dialog<>();
				if(currentStage != null)
					choiceDialog.initOwner(currentStage);
				choiceDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
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
				choiceDialog.getDialogPane().setContent(root);
				choiceDialog.setTitle("Choose between this elements");
				choiceDialog.initStyle(StageStyle.UNDECORATED);
				System.err.println("Choice prompted");
				choiceDialog.showAndWait();
				System.err.println("Choice done");
				setChoice(group.getToggles().indexOf(group.getSelectedToggle()));
			});
		}
	}

	private int choose(Runnable uiCallback) {
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
		Platform.runLater(() -> gameBoardController.addFamiliar(slotType, position, familiarColor, playerColor));
	}

	@Override
	public void pickCard(Card card, String username) {
		Platform.runLater(() -> gameBoardController.pickCard(card, username));
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
		Platform.runLater(() -> gameBoardController.doBonusAction(slotType, value));
	}

	@Override
	public void setClientController(ClientController clientController) {
		this.clientController = clientController;

	}

	@Override
	public void setResources(Resource[] resources, String username) {
		Platform.runLater(() -> gameBoardController.setResources(resources, username));
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

			// My turn has ended, so if a choice was prompted to me close its dialog
			if (choiceDialog != null && choiceDialog.isShowing()) {
				choiceDialog.close();
			}
		});
	}

	@Override
	public void setLeaderCards(LeaderCard[] leaders) {
		Platform.runLater(() -> gameBoardController.setLeaderCards(leaders));
	}

	@Override
	public void discardLeaderCard(String username, LeaderCard leader) {
		Platform.runLater(() -> gameBoardController.discardLeaderCard(username, leader));
	}

	@Override
	public void playLeaderCard(String username, LeaderCard leader) {
		Platform.runLater(() -> gameBoardController.playLeaderCard(username, leader));
	}

	@Override
	public void activateLeaderCard(String username, LeaderCard leader) {
		Platform.runLater(() -> gameBoardController.activateLeaderCard(username, leader));
	}

	@Override
	public void enableLeaderCard(String username, LeaderCard leader) {
		Platform.runLater(() -> gameBoardController.enableLeaderCard(username, leader));
	}

	@Override
	public void setPersonalBonusTile(String username, PersonalBonusTile personalBonusTile) {
		Platform.runLater(() -> gameBoardController.setPersonalBonusTile(username, personalBonusTile));
	}

	@Override
	public void placeExcommunicationToken(PlayerColor playerColor, PeriodType periodType) {
		Platform.runLater(() -> gameBoardController.placeExcommunicationToken(playerColor, periodType));
	}

	@Override
	public void showFinalScore(String[] playersUsername, PlayerColor[] playerColors, int[] scores) {
		Platform.runLater(() -> {
			finalController = new FinalScoreController(playersUsername, playerColors, scores);
			gameBoardController.close();
		});
	}

	@Override
	public void setServantCost(int cost) {
		Platform.runLater(() -> gameBoardController.setServantCost(cost));		
	}

}