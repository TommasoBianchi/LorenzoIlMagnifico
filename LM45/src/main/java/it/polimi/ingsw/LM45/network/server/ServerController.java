package it.polimi.ingsw.LM45.network.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import it.polimi.ingsw.LM45.controller.EffectController;
import it.polimi.ingsw.LM45.exceptions.GameException;
import it.polimi.ingsw.LM45.exceptions.IllegalActionException;
import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.model.cards.LeaderCard;
import it.polimi.ingsw.LM45.model.core.Familiar;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.Game;
import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.model.core.Slot;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.model.effects.ActionModifier;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;
import it.polimi.ingsw.LM45.network.client.ClientInterface;
import it.polimi.ingsw.LM45.serialization.FileManager;

import javafx.scene.paint.Color;

// This is designed to manage only one game (consider renaming it to GameController)
public class ServerController {

	private Map<String, ClientInterface> users;
	private Map<String, Player> players;
	private Map<String, EffectResolutor> effectResolutors;
	private List<Color> availableColors;
	private Map<String, LeaderCard> leaderCards;
	private Map<CardType, List<Card>> deck;
	private int maxNumberOfPlayers;
	private long gameStartTimerDelay;
	private Timer gameStartTimer;
	private long turnTimerDelay;
	private Timer turnTimer;
	private Game game;
	private Player currentPlayer;

	public ServerController(int maxNumberOfPlayers, long gameStartTimerDelay, long turnTimerDelay)
			throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		this.users = new HashMap<String, ClientInterface>();
		this.players = new HashMap<String, Player>();
		this.effectResolutors = new HashMap<String, EffectResolutor>();
		this.availableColors = new ArrayList<Color>();
		this.availableColors.add(Color.BLUE);
		this.availableColors.add(Color.RED);
		this.availableColors.add(Color.GREEN);
		this.availableColors.add(Color.YELLOW);
		this.leaderCards = FileManager.loadLeaderCards().stream()
				.collect(Collectors.toMap(leaderCard -> leaderCard.getName(), leaderCard -> leaderCard));
		this.deck = FileManager.loadCards();
		this.maxNumberOfPlayers = maxNumberOfPlayers;
		this.gameStartTimerDelay = gameStartTimerDelay;
		this.gameStartTimer = new Timer();
		this.turnTimerDelay = turnTimerDelay;

	}

	public void login(String username, ClientInterface clientInterface) {
		if (players.containsKey(username) && !users.containsKey(username)) {
			// The player is reconnecting for some reason
			users.put(username, clientInterface);
			return;
		}

		while (users.containsKey(username) || players.containsKey(username)) {
			username += new Random().nextInt(10);
		}
		try {
			clientInterface.setUsername(username);
		}
		catch (IOException e) {
			// TODO: check this code here and think about it. What has to happen
			// if a client contacts me to ask for
			// login but then I can not call him back?
			e.printStackTrace();
			return;
			// manageIOException(player, e);
		}

		System.out.println(username + " logged in");
		users.put(username, clientInterface);
		Color randomColor = availableColors.remove(new Random().nextInt(availableColors.size()));
		Player player = new Player(username, randomColor);
		players.put(username, player);
		effectResolutors.put(username, new EffectController(player, this));
		System.out.println("Currently in the game: " + players.keySet().stream().reduce("", (a, b) -> a + b + " "));

		if (players.size() == maxNumberOfPlayers) {
			startGame();
		}
		else if (players.size() > 1) {
			setGameStartTimer();
		}
	}

	public void removeUser(String username) {
		users.remove(username);
	}

	public void placeFamiliar(String player, FamiliarColor familiarColor, SlotType slotType, Integer slotID) {
		if (playerCanDoActions(player)) {
			System.out.println(player + " tried to place familiar " + familiarColor + " in slot " + slotID + " of type " + slotType);
			try {
				Slot slot = game.getSlot(slotType, slotID);
				Familiar familiar = players.get(player).getFamiliarByColor(familiarColor);
				ActionModifier actionModifier = ActionModifier.EMPTY; // FIXME: grab the right ActionModifier
				if (slot.canAddFamiliar(familiar, actionModifier)) {
					slot.addFamiliar(familiar, actionModifier, effectResolutors.get(player));
					System.out.println(player + " successfully placed the familiar");
				}
				else
					throw new IllegalActionException(
							"Cannot add a familiar of color " + familiarColor + " in slot " + slotID + " of type " + slotType);
			}
			catch (IllegalActionException e) {
				manageGameExceptions(player, e);
			}
		}
	}

	public void increaseFamiliarValue(String player, FamiliarColor familiarColor) {
		if (playerCanDoActions(player)) {
			System.out.println(player + " increased value of familiar " + familiarColor);
			players.get(player).increaseFamiliarValue(familiarColor);
		}
	}

	public void playLeaderCard(String player, String leaderCardName) {
		if (playerCanDoActions(player) && leaderCards.containsKey(leaderCardName)) {
			System.out.println(player + " played leader card " + leaderCardName);
			try {
				players.get(player).playLeaderCard(leaderCards.get(leaderCardName));
			}
			catch (IllegalActionException e) {
				manageGameExceptions(player, e);
			}
		}
	}

	public void activateLeaderCard(String player, String leaderCardName) {
		if (playerCanDoActions(player) && leaderCards.containsKey(leaderCardName)) {
			System.out.println(player + " activated leader card " + leaderCardName);
			try {
				players.get(player).activateLeaderCard(leaderCards.get(leaderCardName), effectResolutors.get(player));
			}
			catch (IllegalActionException e) {
				manageGameExceptions(player, e);
			}
		}
	}

	public void discardLeaderCard(String player, String leaderCardName) {
		if (playerCanDoActions(player) && leaderCards.containsKey(leaderCardName)) {
			try {
				players.get(player).discardLeaderCard(leaderCards.get(leaderCardName));
				System.out.println(player + " discarded leader card " + leaderCardName);
				effectResolutors.get(player).addResources(new Resource(ResourceType.COUNCIL_PRIVILEGES, 2));
			}
			catch (IllegalActionException e) {
				manageGameExceptions(player, e);
			}
		}
	}

	public void endPlayerRound(String player) {
		if (currentPlayer.getUsername() == player) {
			System.out.println(player + " ended his turn");
			turnTimer.cancel();
			// TODO: implement
			nextPlayerRound();
		}
	}

	private void nextPlayerRound() {
		if (game.hasNextPlayer()) {
			currentPlayer = game.getNextPlayer();
			notifyPlayers(clientInterface -> clientInterface.notifyPlayerTurn(currentPlayer.getUsername()));
			turnTimer = new Timer();
			turnTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					endPlayerRound(currentPlayer.getUsername());
				}
			}, turnTimerDelay);
		}
		else {
			// The turn has finished, so proceed with the next one and do church support phase if necessary
			if (game.getCurrentTurn() % 2 == 0) {
				// TODO: Do church support phase
				System.out.println("Church support phase!");
			}

			if (game.getCurrentTurn() == 6) {
				// TODO: end the game!
				System.out.println("Game ended!");
			}
			else {
				System.out.println("Next turn!");
				game.startTurn();
				nextPlayerRound();
			}
		}
	}

	private void startGame() {
		gameStartTimer.cancel();
		System.out.println("Game is starting!");
		game = new Game(new ArrayList<Player>(players.values()), deck, new ArrayList<LeaderCard>(leaderCards.values()),
				new HashMap<>()/* load the excommunication deck */);
		game.start();
		// TODO: notify players

		// TODO: make players choose their leaderCards
		// TODO: make players choose their personalBonusTile

		game.startTurn();

		// Make first player start his turn
		nextPlayerRound();
	}

	public int chooseFrom(String player, String[] alternatives) {
		int index = 0;
		try {
			index = users.get(player).chooseFrom(alternatives);
		}
		catch (IOException e) {
			manageIOException(player, e);
		}
		return index;
	}

	private void setGameStartTimer() {
		gameStartTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				System.out.println("Timer ended! Game is about to start!");
				startGame();
			}
		}, gameStartTimerDelay);
	}

	private void manageIOException(String user, IOException e) {
		// TODO: implement (maybe just disconnect the user)
		e.printStackTrace();
	}

	private void manageGameExceptions(String player, GameException gameException) {
		try {
			users.get(player).throwGameException(gameException);
		}
		catch (IOException e) {
			manageIOException(player, e);
		}
	}

	private boolean playerCanDoActions(String player) {
		boolean canDo = players.containsKey(player) && users.containsKey(player) && game != null && currentPlayer != null
				&& currentPlayer.getUsername() == player;

		if (!canDo && users.containsKey(player)) {
			GameException gameException = new GameException();

			if (game == null || currentPlayer == null)
				gameException = new IllegalActionException("Game is not started yet");
			else if (currentPlayer.getUsername() != player)
				gameException = new IllegalActionException("It is not your turn");

			manageGameExceptions(player, gameException);
		}

		return canDo;
	}

	/**
	 * @author Tommy
	 *
	 * @param <T>
	 *            The element this function has to operate on
	 * @param <E>
	 *            The exception this function may throw
	 */
	@FunctionalInterface
	interface CheckedFunction<T, E extends Throwable> {
		void apply(T t) throws E;
	}

	/**
	 * @param c
	 *            The function ClientInterface's function we want to call on every connected player
	 */
	private void notifyPlayers(CheckedFunction<ClientInterface, IOException> c) {
		users.entrySet().stream().forEach(entry -> {
			try {
				c.apply(entry.getValue());
			}
			catch (IOException e) {
				manageIOException(entry.getKey(), e);
			}
		});
	}

}
