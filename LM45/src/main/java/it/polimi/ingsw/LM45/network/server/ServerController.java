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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import it.polimi.ingsw.LM45.config.BoardConfiguration;
import it.polimi.ingsw.LM45.config.PersonalBonusTilesConfiguration;
import it.polimi.ingsw.LM45.controller.EffectController;
import it.polimi.ingsw.LM45.exceptions.GameException;
import it.polimi.ingsw.LM45.exceptions.IllegalActionException;
import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.model.cards.LeaderCard;
import it.polimi.ingsw.LM45.model.core.Familiar;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.Game;
import it.polimi.ingsw.LM45.model.core.PersonalBonusTile;
import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.model.core.Slot;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.model.effects.ActionModifier;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;
import it.polimi.ingsw.LM45.network.client.ClientInterface;
import it.polimi.ingsw.LM45.serialization.FileManager;
import it.polimi.ingsw.LM45.util.CheckedFunction;
import it.polimi.ingsw.LM45.util.Pair;
import it.polimi.ingsw.LM45.util.ShuffleHelper;

// This is designed to manage only one game (consider renaming it to GameController)
public class ServerController {

	private Map<String, ClientInterface> users;
	private Map<String, Player> players;
	private Map<String, EffectResolutor> effectResolutors;
	private List<PlayerColor> availableColors;
	private BoardConfiguration boardConfiguration;
	private PersonalBonusTilesConfiguration personalBonusTilesConfiguration;
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
		this.availableColors = new ArrayList<PlayerColor>(Arrays.asList(PlayerColor.values()));
		this.boardConfiguration = FileManager.loadConfiguration(BoardConfiguration.class);
		this.personalBonusTilesConfiguration = FileManager.loadConfiguration(PersonalBonusTilesConfiguration.class);
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
			System.out.println("Player " + username + " has been reconnected to this game");
			users.put(username, clientInterface);
			setPlayerUsername(username, clientInterface);
			return;
		}

		while (users.containsKey(username) || players.containsKey(username)) {
			username += new Random().nextInt(10);
		}
		setPlayerUsername(username, clientInterface);

		System.out.println(username + " logged in");
		users.put(username, clientInterface);
		PlayerColor randomColor = availableColors.remove(new Random().nextInt(availableColors.size()));
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
		System.out.println("Disconnecting " + username);
		users.remove(username);
		ServerControllerFactory.addDisconnectedUser(username, this);
		// NOTE: what happens when all the players disconnect?
		// We may want to stop and cancel this game?
		// Maybe we prefer to stop it waiting for a player to log in again?
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
				effectResolutors.get(player).addResources(new Resource(ResourceType.COUNCIL_PRIVILEGES, 1));
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
			nextPlayerRound();
		}
	}

	private void setPlayerUsername(String username, ClientInterface clientInterface) {
		try {
			clientInterface.setUsername(username);
		}
		catch (IOException e) {
			// TODO: check this code here and think about it. What has to happen
			// if a client contacts me to ask for
			// login but then I cannot call him back?
			e.printStackTrace();
			// manageIOException(player, e);
		}
	}

	private void nextPlayerRound() {
		if (game.hasNextPlayer()) {
			currentPlayer = game.getNextPlayer();

			// Make disconnected players skip their rounds
			if (!users.containsKey(currentPlayer.getUsername())) {
				endPlayerRound(currentPlayer.getUsername());
				return;
			}

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
		game = new Game(new ArrayList<Player>(players.values()), boardConfiguration, deck, new ArrayList<LeaderCard>(leaderCards.values()),
				new HashMap<>()/* load the excommunication deck */);
		game.start();

		giveBaseResources();
		// choosePersonalBonusTiles();
		chooseLeaderCards();

		// TODO: send players the starting state of the board

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
		System.err.println("IOexception!");
		removeUser(user);
		// e.printStackTrace();
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

	private void chooseLeaderCards() {
		// Make players choose their leaderCards
		List<LeaderCard> shuffledLeaderCards = ShuffleHelper.shuffle(leaderCards.values());
		Map<String, List<LeaderCard>> leaderCardsToChoose = new HashMap<>();
		int i = 0;
		for (String playerUsername : players.keySet()) {
			List<LeaderCard> leaderCardsToChooseByThisPlayer = new ArrayList<>();
			leaderCardsToChooseByThisPlayer.add(shuffledLeaderCards.get(4 * i + 0));
			leaderCardsToChooseByThisPlayer.add(shuffledLeaderCards.get(4 * i + 1));
			leaderCardsToChooseByThisPlayer.add(shuffledLeaderCards.get(4 * i + 2));
			leaderCardsToChooseByThisPlayer.add(shuffledLeaderCards.get(4 * i + 3));
			leaderCardsToChoose.put(playerUsername, leaderCardsToChooseByThisPlayer);
			i++;
		}

		for (i = 0; i < 3; i++) {
			ExecutorService executorService = Executors.newFixedThreadPool(4);
			// Give each (connected) player the possibility to choose a leaderCard (in parallel)
			users.keySet().forEach(playerUsername -> {
				executorService.submit(() -> {
					int index = chooseFrom(playerUsername,
							leaderCardsToChoose.get(playerUsername).stream().map(leaderCard -> leaderCard.toString()).toArray(String[]::new));
					LeaderCard chosenLeaderCard = leaderCardsToChoose.get(playerUsername).get(index);
					System.out.println(playerUsername + " has chosen " + chosenLeaderCard.getName());
					players.get(playerUsername).addLeaderCard(chosenLeaderCard);
					leaderCardsToChoose.get(playerUsername).remove(chosenLeaderCard);
				});
			});

			// Wait their decision only for a fixed amount of time
			executorService.shutdown();
			try {
				executorService.awaitTermination(turnTimerDelay, TimeUnit.MILLISECONDS);
			}
			catch (InterruptedException e) {
				// TODO: think better about how should we manage this exception
				System.err.println("ServerController::chooseLeaderCards() -- " +
						"An InterruptedException occurred while awaiting executorService to terminate."
						+ "Forcing executorService shutdown and propagating interrupt.");
				e.printStackTrace();
				executorService.shutdownNow();
				Thread.currentThread().interrupt();
			}

			// Choose for the player that have not chose before the timeout
			int remainingLeaderCardsAtThisIteration = 4 - 1 - i;
			players.keySet().forEach(playerUsername -> {
				if (leaderCardsToChoose.get(playerUsername).size() > remainingLeaderCardsAtThisIteration) {
					LeaderCard chosenLeaderCard = leaderCardsToChoose.get(playerUsername).get(0);
					System.out.println("I have chosen for " + playerUsername + " " + chosenLeaderCard.getName());
					players.get(playerUsername).addLeaderCard(chosenLeaderCard);
					leaderCardsToChoose.get(playerUsername).remove(chosenLeaderCard);
				}
			});

			/*
			 * // Make every player choose a leaderCard and remove it from the chosable ones players.keySet().forEach(playerUsername -> { System.out.println(playerUsername + " has to choose"); int
			 * index = (users.containsKey(playerUsername)) ? chooseFrom(playerUsername, leaderCardsToChoose.get(playerUsername).stream().map(leaderCard ->
			 * leaderCard.toString()).toArray(String[]::new)) : 0; System.out.println(playerUsername + " has chosen"); LeaderCard chosenLeaderCard = leaderCardsToChoose.get(playerUsername).get(index);
			 * players.get(playerUsername).addLeaderCard(chosenLeaderCard); leaderCardsToChoose.get(playerUsername).remove(chosenLeaderCard); });
			 */

			// Swap chosable leaderCards between players
			String[] playersUsernames = players.keySet().stream().toArray(String[]::new);
			List<LeaderCard> firstList = leaderCardsToChoose.get(playersUsernames[0]);
			for (int j = 1; j < playersUsernames.length; j++) {
				leaderCardsToChoose.put(playersUsernames[j - 1], leaderCardsToChoose.get(playersUsernames[j]));
			}
			leaderCardsToChoose.put(playersUsernames[playersUsernames.length - 1], firstList);
		}

		players.entrySet().forEach(entry -> entry.getValue().addLeaderCard(leaderCardsToChoose.get(entry.getKey()).get(0)));
	}

	private void choosePersonalBonusTiles() {
		List<PersonalBonusTile> personalBonusTiles = new ArrayList<>(Arrays.asList(personalBonusTilesConfiguration.getPersonalBonusTiles()));
		Player[] orderedPlayers = game.getOrderedPlayers();

		for (Player player : orderedPlayers) {
			int chosenIndex = 0;
			if (personalBonusTiles.size() > 1 && users.containsKey(player.getUsername())) {
				chosenIndex = chooseFrom(player.getUsername(),
						personalBonusTiles.stream().map(personalBonusTile -> personalBonusTile.toString()).toArray(String[]::new));
			}

			PersonalBonusTile chosenPersonalBonusTile = personalBonusTiles.remove(chosenIndex);
			player.setPersonalBonusTile(chosenPersonalBonusTile);
		}
	}

	private void giveBaseResources() {
		Player[] orderedPlayers = game.getOrderedPlayers();

		Arrays.stream(orderedPlayers).forEach(player -> {
			player.addResources(new Resource(ResourceType.WOOD, 2));
			player.addResources(new Resource(ResourceType.STONE, 2));
			player.addResources(new Resource(ResourceType.SERVANTS, 3));
		});

		orderedPlayers[0].addResources(new Resource(ResourceType.COINS, 5));
		orderedPlayers[1].addResources(new Resource(ResourceType.COINS, 6));
		if (orderedPlayers.length > 2)
			orderedPlayers[2].addResources(new Resource(ResourceType.COINS, 7));
		if (orderedPlayers.length > 3)
			orderedPlayers[3].addResources(new Resource(ResourceType.COINS, 8));
	}

	/**
	 * Notify every connected client about something
	 * 
	 * @param c
	 *            the ClientInterface's function we want to call on every connected player
	 */
	private void notifyPlayers(CheckedFunction<ClientInterface, IOException> c) {
		List<Pair<String, IOException>> raisedIOException = new ArrayList<>();

		users.entrySet().stream().parallel().forEach(entry -> {
			System.out.println("Notify " + entry.getKey() + " of something");
			try {
				c.apply(entry.getValue());
			}
			catch (IOException e) {
				// Save the raised IOException to manage them later because they can cause
				// a player to be disconnected and so the underlying users map may be modified
				raisedIOException.add(new Pair<String, IOException>(entry.getKey(), e));
			}
		});

		raisedIOException.stream().forEach(pair -> manageIOException(pair._1(), pair._2()));
	}

}
