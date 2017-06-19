package it.polimi.ingsw.LM45.view.controller;

import java.io.IOException;
import java.util.Arrays;

import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.Excommunication;
import it.polimi.ingsw.LM45.model.cards.LeaderCard;
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
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.ButtonType;

public class GuiController implements ViewInterface {

	LobbyController lobbyController;
	LeaderCardChoiceController leaderChoiceController;
	GameBoardController gameBoardController;

	Stage stage = new Stage();

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

	public void setLeaderChoiceController(LeaderCardChoiceController leaderChoiceController) {
		this.leaderChoiceController = leaderChoiceController;
	}

	public void setGameBoardController(GameBoardController gameBoardController) {
		this.gameBoardController = gameBoardController;
	}
	
	@Override
	public void showLeaderCardChoiceView() {
		try {
			FXMLLoader loader2 = new FXMLLoader();
			loader2.setLocation(Main.class.getResource("../gui/leadercard/LeaderCardChoiceView.fxml"));
			AnchorPane leaderChoice = (AnchorPane) loader2.load();
			Scene scene = new Scene(leaderChoice);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.getIcons().add(new Image("file:Assets/Image/Cards/LEADER/LeaderCard Cover.jpg"));
			stage.show();
			LeaderCardChoiceController controller = loader2.getController();
			leaderChoiceController = controller;
			controller.setGuiController(this);
		}
		catch (IOException | NullPointerException e) { // TODO sistemare
			e.printStackTrace();
		}
	}
	
	@Override
	public void showGameBoard(PlayerColor playerColor, String[] playersUsername) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("../gui/gameboard/GameBoardView.fxml"));
			AnchorPane gameBoard = (AnchorPane) loader.load();
			Scene scene = new Scene(gameBoard);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.getIcons().add(new Image("file:Assets/Image/Cards/LEADER/LeaderCard Cover.jpg"));
			stage.show();
			GameBoardController controllerGameBoard = loader.getController();
			this.setGameBoardController(controllerGameBoard);
			controllerGameBoard.setScene(scene);
			controllerGameBoard.coverSlots(playersUsername.length);
			controllerGameBoard.setFamiliars(playerColor, new int[] { 1, 2, 3, 4 });
			controllerGameBoard.setUsernames(playersUsername);
			controllerGameBoard.setMyUsername("JOH");
			controllerGameBoard.setServantCost(1);
			stage.getScene().setOnMouseClicked(null);
		}
		catch (IOException | NullPointerException e) { // TODO sistemare
			e.printStackTrace();
		}
	}

	public void addFamiliar(SlotType slotType, int position, FamiliarColor familiarColor, PlayerColor playerColor) {
		// TODO Auto-generated method stub

	}

	public void pickCard(String cardName, String username) {
		// TODO Auto-generated method stub

	}

	public void addCardsOnTower(String[] names, SlotType slotType) {
		// TODO Auto-generated method stub

	}

	public void doBonusAction(SlotType slotType, int value) {
		// TODO Auto-generated method stub

	}

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
					if(i == 0)
						group.selectToggle(button);
				}
				root.add(box, 0, 0);
				dialog.getDialogPane().setContent(root);
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			int x = choice;
			choice = -1;
			return x;
		}
	}

	public void setClientController(ClientController clientController) {
		// TODO Auto-generated method stub

	}

	public void setResources(Resource[] resources, String username) {
		// TODO Auto-generated method stub

	}

	public void setFamiliarValue(FamiliarColor familiarColor, int value, String username) {
		// TODO Auto-generated method stub

	}

	public void myTurn() {

	}

	public void playerTurn(String username) {

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
	public void setExcommunications(Excommunication[] excommunications) {
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
	public void setPersonalBonusTile(String username, PersonalBonusTile personalBonusTile) {
		// TODO Auto-generated method stub

	}
}