package it.polimi.ingsw.LM45.network.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
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
import it.polimi.ingsw.LM45.model.cards.Excommunication;
import it.polimi.ingsw.LM45.model.cards.LeaderCard;
import it.polimi.ingsw.LM45.model.cards.PeriodType;
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
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;
import it.polimi.ingsw.LM45.model.effects.modifiers.ActionModifier;
import it.polimi.ingsw.LM45.network.client.ClientInterface;
import it.polimi.ingsw.LM45.serialization.FileManager;
import it.polimi.ingsw.LM45.util.CheckedFunction1;
import it.polimi.ingsw.LM45.util.CheckedFunction2;
import it.polimi.ingsw.LM45.util.Pair;
import it.polimi.ingsw.LM45.util.ShuffleHelper;

// This is designed to manage only one game (consider renaming it to GameController)
public class ServerController {

	private int gameID;
	private Map<String, ClientInterface> users;
	private Map<String, Player> players;
	private Map<String, EffectResolutor> effectResolutors;
	private List<PlayerColor> availableColors;
	private BoardConfiguration boardConfiguration;
	private PersonalBonusTilesConfiguration personalBonusTilesConfiguration;
	private Map<String, LeaderCard> leaderCards;
	private Map<CardType, List<Card>> deck;
	private Map<PeriodType, List<Excommunication>> excommunications;
	private int maxNumberOfPlayers;
	private long gameStartTimerDelay;
	private Timer gameStartTimer;
	private long turnTimerDelay;
	private Timer turnTimer;
	private Game game;
	private Player currentPlayer;
	private boolean currentPlayerAlreadyPlacedFamiliar;
	private SlotType bonusActionSlotType;
	private Queue<CheckedFunction2<String, ClientInterface, IOException>> clientNotificationQueue = new LinkedList<>();

	public ServerController(int gameID, int maxNumberOfPlayers, long gameStartTimerDelay, long turnTimerDelay)
			throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		this.gameID = gameID;
		this.users = new HashMap<>();
		this.players = new HashMap<>();
		this.effectResolutors = new HashMap<>();
		this.availableColors = new ArrayList<>(Arrays.asList(PlayerColor.values()));
		this.boardConfiguration = FileManager.loadConfiguration(BoardConfiguration.class);
		this.personalBonusTilesConfiguration = FileManager.loadConfiguration(PersonalBonusTilesConfiguration.class);
		this.leaderCards = FileManager.loadLeaderCards().stream().collect(Collectors.toMap(LeaderCard::getName, leaderCard -> leaderCard));
		this.deck = FileManager.loadCards();
		this.excommunications = FileManager.loadExcommunications();
		this.maxNumberOfPlayers = maxNumberOfPlayers;
		this.gameStartTimerDelay = gameStartTimerDelay;
		this.gameStartTimer = null;
		this.turnTimerDelay = turnTimerDelay;
	}

	public void login(String username, ClientInterface clientInterface) {
		if (players.containsKey(username) && !users.containsKey(username)) {
			// The player is reconnecting for some reason
			logInfo("Player " + username + " has been reconnected to this game");
			users.put(username, clientInterface);
			setPlayerUsername(username, clientInterface);
			// If the game has already started, notify the reconnecting player about its state
			if (game != null) {
				initializeBoardForReconnectingPlayer(username, clientInterface);
				return;
			}
		}
		else {
			while (users.containsKey(username) || players.containsKey(username)) {
				username += Integer.toString(new Random().nextInt(10));
			}
			setPlayerUsername(username, clientInterface);

			users.put(username, clientInterface);
			PlayerColor randomColor = availableColors.remove(new Random().nextInt(availableColors.size()));
			Player player = new Player(username, randomColor);
			players.put(username, player);
			effectResolutors.put(username, new EffectController(player, this));
			logInfo(username + " logged in. Currently in this game: " + users.keySet().stream().reduce("", (a, b) -> a + b + " "));
		}

		if (users.size() == maxNumberOfPlayers) {
			// Do not start the game immediately but with a short delay,
			// to make this method end and return to the caller before starting
			setGameStartTimer(3000);
		}
		else if (users.size() > 1) {
			setGameStartTimer(gameStartTimerDelay);
		}
	}

	public void removeUser(String username) {
		logInfo("Disconnecting " + username);
		users.remove(username);
		ServerControllerFactory.addDisconnectedUser(username, this);

		// NOTE: what happens when all the players disconnect?
		// We may want to stop and cancel this game?
		// Maybe we prefer to stop it waiting for a player to log in again?

		// If only one player is remaining stop the gameStartTimer (if present and only before the game starts)
		if (game == null && users.size() == 1 && gameStartTimer != null) {
			logInfo("Only one player remaining. Resetting timer");
			gameStartTimer.cancel();
			gameStartTimer = null;
		}
	}

	public void placeFamiliar(String player, FamiliarColor familiarColor, SlotType slotType, Integer slotID) {
		if (playerCanDoActions(player)) {
			try {
				if (familiarColor != FamiliarColor.BONUS && currentPlayerAlreadyPlacedFamiliar)
					throw new IllegalActionException("You have already placed a familiar this turn!");
				else if (familiarColor == FamiliarColor.BONUS && !bonusActionSlotType.isCompatible(slotType))
					throw new IllegalActionException("You cannot place this bonus familiar on a " + slotType + " slot!");

				Slot slot = game.getSlot(slotType, slotID);
				Familiar familiar = players.get(player).getFamiliarByColor(familiarColor);
				EffectResolutor effectResolutor = effectResolutors.get(player);
				ActionModifier actionModifier = players.get(player).getActionModifier(slotType, effectResolutor); // NOTE: this may be incorrect
				if (slot.canAddFamiliar(familiar, actionModifier, effectResolutor)) {
					slot.addFamiliar(familiar, actionModifier, effectResolutor);
					notifyPlayers(clientInterface -> clientInterface.addFamiliar(slotType, slotID, familiarColor, players.get(player).getColor()));
					if (player.equals(currentPlayer.getUsername()))
						currentPlayerAlreadyPlacedFamiliar = familiarColor != FamiliarColor.BONUS;
					logInfo(player + " successfully placed the familiar " + familiarColor + " in slot " + slotType + " " + slotID);

					if (familiarColor == FamiliarColor.BONUS) {
						players.get(player).removeBonusFamiliar();
						bonusActionSlotType = null;
					}
				}
				else {
					logInfo(player + " failed to place familiar " + familiarColor + " in slot " + slotID + " of type " + slotType);
				}
			}
			catch (IllegalActionException e) {
				manageGameExceptions(player, e);
			}
		}
	}

	public void increaseFamiliarValue(String player, FamiliarColor familiarColor) {
		if (playerCanDoActions(player)) {
			try {
				players.get(player).increaseFamiliarValue(familiarColor);
				notifyPlayers(
						clientInterface -> clientInterface.setFamiliar(player, familiarColor, players.get(player).getFamiliarValue(familiarColor)));
				notifyPlayers(clientInterface -> clientInterface
						.setResources(new Resource(ResourceType.SERVANTS, players.get(player).getResourceAmount(ResourceType.SERVANTS)), player));
				logInfo(player + " increased value of familiar " + familiarColor);
			}
			catch (IllegalActionException e) {
				manageGameExceptions(player, e);
				logInfo(player + " faled increasing value of familiar " + familiarColor);
			}
		}
	}

	public void playLeaderCard(String player, String leaderCardName) {
		if (playerCanDoActions(player) && leaderCards.containsKey(leaderCardName)) {
			try {
				LeaderCard leaderCard = leaderCards.get(leaderCardName);
				players.get(player).playLeaderCard(leaderCard, effectResolutors.get(player));
				notifyPlayers(clientInterface -> clientInterface.playLeaderCard(player, leaderCard));
				logInfo(player + " played leader card " + leaderCardName);
			}
			catch (IllegalActionException e) {
				manageGameExceptions(player, e);
			}
		}
	}

	public void activateLeaderCard(String player, String leaderCardName) {
		if (playerCanDoActions(player) && leaderCards.containsKey(leaderCardName)) {
			try {
				LeaderCard leaderCard = leaderCards.get(leaderCardName);
				players.get(player).activateLeaderCard(leaderCards.get(leaderCardName), effectResolutors.get(player));
				notifyPlayers(clientInterface -> clientInterface.activateLeaderCard(player, leaderCard));
				logInfo(player + " activated leader card " + leaderCardName);
			}
			catch (IllegalActionException e) {
				manageGameExceptions(player, e);
			}
		}
	}

	public void discardLeaderCard(String player, String leaderCardName) {
		if (playerCanDoActions(player) && leaderCards.containsKey(leaderCardName)) {
			try {
				LeaderCard leaderCard = leaderCards.get(leaderCardName);
				players.get(player).discardLeaderCard(leaderCard);
				notifyPlayers(clientInterface -> clientInterface.discardLeaderCard(player, leaderCard));
				effectResolutors.get(player).addResources(new Resource(ResourceType.COUNCIL_PRIVILEGES, 1));
				logInfo(player + " discarded leader card " + leaderCardName);
			}
			catch (IllegalActionException e) {
				manageGameExceptions(player, e);
			}
		}
	}

	public void doBonusAction(String player, SlotType slotType, int value, Resource[] discount) {
		try {
			players.get(player).addBonusFamiliar(value, discount);
			bonusActionSlotType = slotType;
			users.get(player).doBonusAction(slotType, value);
		}
		catch (IOException e) {
			manageIOException(player, e);
		}
	}

	public void endPlayerRound(String player) {
		if (currentPlayer.getUsername().equals(player)) {
			logInfo(player + " ended his turn");
			turnTimer.cancel();

			// Reset servants bonus on each familiar
			Arrays.stream(players.get(player).getFamiliars()).forEach(familiar -> {
				familiar.clearServantsBonus();
				notifyPlayers(clientInterface -> clientInterface.setFamiliar(player, familiar.getFamiliarColor(), familiar.getValue()));
			});

			nextPlayerRound();
		}
	}

	public boolean isMyTurn(Player player) {
		return currentPlayer == player;
	}

	public LeaderCard[] getAllPlayedLeaderCards() {
		return players.values().stream().flatMap(player -> Arrays.stream(player.getPlayedLeaderCards())).toArray(LeaderCard[]::new);
	}

	private void setPlayerUsername(String username, ClientInterface clientInterface) {
		try {
			clientInterface.setUsername(username);
		}
		catch (IOException e) {
			System.err.println("ServerController::setPlayerUsername -- user " + username + " is now unreachable. Disconnecting");
			e.printStackTrace();
			manageIOException(username, e);
		}
	}

	private void nextPlayerRound() {
		if (game.hasNextPlayer()) {
			currentPlayer = game.getNextPlayer();
			currentPlayerAlreadyPlacedFamiliar = false;
			logInfo("Next Round! It's " + currentPlayer.getUsername() + "'s time to play!");

			// Make disconnected players skip their turns
			if (!users.containsKey(currentPlayer.getUsername())) {
				endPlayerRound(currentPlayer.getUsername());
				return;
			}

			notifyPlayers(clientInterface -> clientInterface.notifyPlayerTurn(currentPlayer.getUsername()));
			turnTimer = new Timer();
			turnTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					// TODO: reset every pending choice, bonus familiar, ...
					endPlayerRound(currentPlayer.getUsername());
				}
			}, turnTimerDelay);
		}
		else {
			// The turn has finished, so proceed with the next one and do church support phase if necessary
			if (game.getCurrentTurn() % 2 == 0) {
				logInfo("Church support phase!");
				churchSupportPhase();
			}

			if (game.getCurrentTurn() == 6) {
				logInfo("Game ended!");
				endGame();
			}
			else {
				nextGameTurn();
			}
		}
	}

	private void startGame() {
		if (gameStartTimer != null)
			gameStartTimer.cancel();
		logInfo("Game is starting!");
		game = new Game(new ArrayList<Player>(players.values()), boardConfiguration, deck, new ArrayList<LeaderCard>(leaderCards.values()),
				excommunications);
		game.start();

		Map<String, PersonalBonusTile> chosenPersonalBonusTiles = choosePersonalBonusTiles();
		Map<String, LeaderCard[]> chosenLeaderCards = chooseLeaderCards();

		// Initialize the gameboards client-side
		String[] playersUsername = players.values().stream().map(Player::getUsername).toArray(String[]::new);
		PlayerColor[] playerColors = players.values().stream().map(Player::getColor).toArray(PlayerColor[]::new);
		notifyPlayers(clientInterface -> clientInterface.initializeGameBoard(playersUsername, playerColors, game.getPlacedExcommunications()));

		// Notify of chosen personalBonusTiles and leaderCards only after gameboards have been initialized
		chosenPersonalBonusTiles.forEach(
				(username, personalBonusTile) -> notifyPlayers(clientInterface -> clientInterface.setPersonalBonusTile(username, personalBonusTile)));
		notifyPlayers((username, clientInterface) -> clientInterface.setLeaderCards(chosenLeaderCards.get(username)));

		giveBaseResources();

		nextGameTurn();
	}

	private void endGame() {
		// Add final victory points to each player
		players.values().forEach(player -> {
			// For territories
			int[] territoriesVictoryPoints = new int[] { 0, 0, 0, 1, 4, 10, 20 };
			player.addResources(new Resource(ResourceType.VICTORY, territoriesVictoryPoints[player.getResourceAmount(ResourceType.TERRITORY)]));

			// For characters
			int[] charactersVictoryPoints = new int[] { 1, 3, 6, 10, 15, 21 };
			player.addResources(new Resource(ResourceType.VICTORY, charactersVictoryPoints[player.getResourceAmount(ResourceType.CHARACTER)]));

			// For ventures
			player.resolveVentures(effectResolutors.get(player.getUsername()));

			// For resources owned
			int totalResources = Arrays
					.stream(new ResourceType[] { ResourceType.STONE, ResourceType.WOOD, ResourceType.COINS, ResourceType.SERVANTS })
					.mapToInt(resourceType -> player.getResourceAmount(resourceType)).sum();
			player.addResources(new Resource(ResourceType.VICTORY, totalResources));
		});

		// Give the two players with most military points some victory points (5 for the first, 2 for the second)
		// If we have more than one player with the highest amount of military points, all of them get 5 victory points
		// and none of them gains 2.
		// If we have more than one player with the second highest amount of military points, all of them get 2 victory points
		Player[] militaryOrderedPlayers = players.values().stream()
				.sorted((player1, player2) -> player2.getResourceAmount(ResourceType.MILITARY) - player1.getResourceAmount(ResourceType.MILITARY))
				.toArray(Player[]::new);
		if (militaryOrderedPlayers[0].getResourceAmount(ResourceType.MILITARY) == militaryOrderedPlayers[1]
				.getResourceAmount(ResourceType.MILITARY)) {
			Arrays.stream(militaryOrderedPlayers).filter(
					player -> player.getResourceAmount(ResourceType.MILITARY) == militaryOrderedPlayers[0].getResourceAmount(ResourceType.MILITARY))
					.forEach(player -> player.addResources(new Resource(ResourceType.VICTORY, 5)));
		}
		else {
			militaryOrderedPlayers[0].addResources(new Resource(ResourceType.VICTORY, 5));
			Arrays.stream(militaryOrderedPlayers).filter(
					player -> player.getResourceAmount(ResourceType.MILITARY) == militaryOrderedPlayers[1].getResourceAmount(ResourceType.MILITARY))
					.forEach(player -> player.addResources(new Resource(ResourceType.VICTORY, 2)));
		}

		// Notify client about the final ordering
		Player[] victoryOrderedPlayers = players.values().stream()
				.sorted((player1, player2) -> player2.getResourceAmount(ResourceType.VICTORY) - player1.getResourceAmount(ResourceType.VICTORY))
				.toArray(Player[]::new);
		String[] playersUsername = Arrays.stream(victoryOrderedPlayers).map(Player::getUsername).toArray(String[]::new);
		PlayerColor[] playerColors = Arrays.stream(victoryOrderedPlayers).map(Player::getColor).toArray(PlayerColor[]::new);
		int[] scores = Arrays.stream(victoryOrderedPlayers).mapToInt(player -> player.getResourceAmount(ResourceType.VICTORY)).toArray();
		notifyPlayers(clientInterface -> clientInterface.showFinalScore(playersUsername, playerColors, scores));
	}

	private void nextGameTurn() {
		logInfo("Starting a new turn!");
		game.startTurn();

		// Notify players about the new cards on the towers
		for (CardType cardType : new CardType[] { CardType.TERRITORY, CardType.BUILDING, CardType.CHARACTER, CardType.VENTURE })
			notifyPlayers(clientInterface -> clientInterface.addCardsOnTower(game.getCardsOnTower(cardType), cardType.toSlotType()));

		// Notify players about the value of their familiars (which includes the new value of the dices rolled by the game)
		notifyPlayers((username, clientInterface) -> {
			Familiar[] familiars = players.get(username).getFamiliars();
			for (Familiar familiar : familiars)
				clientInterface.setFamiliar(username, familiar.getFamiliarColor(), familiar.getValue());
		});

		// Notify players about leaderCards that can now be activated again
		players.forEach((username, player) -> {
			LeaderCard[] playedLeaderCards = player.getPlayedLeaderCards();
			Arrays.stream(playedLeaderCards).filter(leaderCard -> leaderCard.canBeEnabled()).forEach(leaderCard -> {
				leaderCard.enable();
				notifyPlayers(clientInterface -> clientInterface.enableLeaderCard(username, leaderCard));
			});
		});

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

	private void setGameStartTimer(long delay) {
		if (gameStartTimer == null) {
			gameStartTimer = new Timer();
		}

		gameStartTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				logInfo("Timer ended! Game is about to start!");
				startGame();
			}
		}, delay);
	}

	private void manageIOException(String user, IOException e) {
		System.err.println("ServerController::manageIOException -- disconnecting user");
		e.printStackTrace();
		logInfo("Disconnecting" + user);
		removeUser(user);
	}

	private void manageGameExceptions(String player, GameException gameException) {
		try {
			users.get(player).throwGameException(gameException);
			logInfo(player + " raised a gameException saying " + gameException.getMessage());
			gameException.printStackTrace();
		}
		catch (IOException e) {
			manageIOException(player, e);
		}
	}

	private boolean playerCanDoActions(String player) {
		boolean canDo = players.containsKey(player) && users.containsKey(player) && game != null && currentPlayer != null
				&& currentPlayer.getUsername().equals(player);

		if (!canDo && users.containsKey(player)) {
			GameException gameException = new GameException();

			if (game == null || currentPlayer == null)
				gameException = new IllegalActionException("Game is not started yet");
			else if (!currentPlayer.getUsername().equals(player))
				gameException = new IllegalActionException("It is not your turn");

			manageGameExceptions(player, gameException);
		}

		return canDo;
	}

	private Map<String, LeaderCard[]> chooseLeaderCards() {
		Map<String, LeaderCard[]> chosenLeaderCards = new HashMap<>();

		// Shuffle the leaderCards and take 4 of them for each of the players
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
			chosenLeaderCards.put(playerUsername, new LeaderCard[4]);
			i++;
		}

		// Make players choose their leaderCards
		for (i = 0; i < 3; i++) {
			ExecutorService executorService = Executors.newFixedThreadPool(4);
			int currentIndex = i;
			// Give each (connected) player the possibility to choose a leaderCard (in parallel)
			users.keySet().forEach(playerUsername -> executorService.submit(() -> {
				int index = chooseFrom(playerUsername,
						leaderCardsToChoose.get(playerUsername).stream().map(LeaderCard::toString).toArray(String[]::new));
				LeaderCard chosenLeaderCard = leaderCardsToChoose.get(playerUsername).get(index);
				logInfo(playerUsername + " has chosen leaderCard " + chosenLeaderCard.getName());
				players.get(playerUsername).addLeaderCard(chosenLeaderCard);
				chosenLeaderCards.get(playerUsername)[currentIndex] = chosenLeaderCard;
				leaderCardsToChoose.get(playerUsername).remove(chosenLeaderCard);
			}));

			// Wait their decision only for a fixed amount of time
			executorService.shutdown();
			try {
				executorService.awaitTermination(turnTimerDelay, TimeUnit.MILLISECONDS);
			}
			catch (InterruptedException e) {
				System.err.println(
						"ServerController::chooseLeaderCards() -- an InterruptedException occurred while awaiting executorService to terminate."
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
					logInfo("I have chosen for " + playerUsername + " leaderCard " + chosenLeaderCard.getName());
					players.get(playerUsername).addLeaderCard(chosenLeaderCard);
					leaderCardsToChoose.get(playerUsername).remove(chosenLeaderCard);
					chosenLeaderCards.get(playerUsername)[currentIndex] = chosenLeaderCard;
				}
			});

			// Swap chosable leaderCards between players
			String[] playersUsernames = players.keySet().stream().toArray(String[]::new);
			List<LeaderCard> firstList = leaderCardsToChoose.get(playersUsernames[0]);
			for (int j = 1; j < playersUsernames.length; j++) {
				leaderCardsToChoose.put(playersUsernames[j - 1], leaderCardsToChoose.get(playersUsernames[j]));
			}
			leaderCardsToChoose.put(playersUsernames[playersUsernames.length - 1], firstList);
		}

		players.forEach((username, player) -> {
			LeaderCard leaderCard = leaderCardsToChoose.get(username).get(0);
			player.addLeaderCard(leaderCard);
			chosenLeaderCards.get(username)[3] = leaderCard;
		});

		return chosenLeaderCards;
	}

	private Map<String, PersonalBonusTile> choosePersonalBonusTiles() {
		Map<String, PersonalBonusTile> chosenPersonalBonusTiles = new HashMap<>();
		List<PersonalBonusTile> personalBonusTiles = new ArrayList<>(Arrays.asList(personalBonusTilesConfiguration.getPersonalBonusTiles()));
		Player[] orderedPlayers = game.getOrderedPlayers();

		for (Player player : orderedPlayers) {
			int chosenIndex = 0;
			if (personalBonusTiles.size() > 1 && users.containsKey(player.getUsername())) {
				chosenIndex = chooseFrom(player.getUsername(), personalBonusTiles.stream().map(PersonalBonusTile::toString).toArray(String[]::new));
			}

			PersonalBonusTile chosenPersonalBonusTile = personalBonusTiles.remove(chosenIndex);
			logInfo(player.getUsername() + " has chosen " + chosenPersonalBonusTile);
			player.setPersonalBonusTile(chosenPersonalBonusTile);
			chosenPersonalBonusTiles.put(player.getUsername(), chosenPersonalBonusTile);
		}

		return chosenPersonalBonusTiles;
	}

	private void giveBaseResources() {
		Player[] orderedPlayers = game.getOrderedPlayers();

		Arrays.stream(orderedPlayers).forEach(player -> {
			player.addResources(new Resource(ResourceType.WOOD, 2));
			player.addResources(new Resource(ResourceType.STONE, 2));
			player.addResources(new Resource(ResourceType.SERVANTS, 3));
		});

		orderedPlayers[0].addResources(new Resource(ResourceType.COINS, 5));
		notifyPlayers(clientInterface -> clientInterface.setResources(orderedPlayers[0].getAllResources(), orderedPlayers[0].getUsername()));
		orderedPlayers[1].addResources(new Resource(ResourceType.COINS, 6));
		notifyPlayers(clientInterface -> clientInterface.setResources(orderedPlayers[1].getAllResources(), orderedPlayers[1].getUsername()));
		if (orderedPlayers.length > 2) {
			orderedPlayers[2].addResources(new Resource(ResourceType.COINS, 7));
			notifyPlayers(clientInterface -> clientInterface.setResources(orderedPlayers[2].getAllResources(), orderedPlayers[2].getUsername()));
		}
		if (orderedPlayers.length > 3) {
			orderedPlayers[3].addResources(new Resource(ResourceType.COINS, 8));
			notifyPlayers(clientInterface -> clientInterface.setResources(orderedPlayers[3].getAllResources(), orderedPlayers[3].getUsername()));
		}
	}

	private void churchSupportPhase() {
		players.forEach((username, player) -> {
			int faithPointsRequired = PeriodType.I.ordinal() + 3;
			boolean playerCanSupportChurch = player.hasResources(new Resource(ResourceType.FAITH, faithPointsRequired));
			boolean playerWantsToSupportChurch = true;

			if (users.containsKey(username) && playerCanSupportChurch) {
				playerWantsToSupportChurch = chooseFrom(username, new String[] { "Support Church", "Do not support Church" }) == 0;
			}

			if (playerCanSupportChurch && playerWantsToSupportChurch) {
				// Support Church

				Set<ResourceType> changedResourcesTypes = new HashSet<>();

				// Remove all Faith points
				int faithPoints = player.getResourceAmount(ResourceType.FAITH);
				player.addResources(new Resource(ResourceType.FAITH, -faithPoints));
				changedResourcesTypes.add(ResourceType.FAITH);

				// Give player the resources he's gained by supporting the Church
				Arrays.stream(game.getChurchSupportResources(faithPoints)).forEach(resource -> {
					player.addResources(resource);
					changedResourcesTypes.add(resource.getResourceType());
				});
				Arrays.stream(player.getChurchSupportBonuses()).forEach(resource -> {
					player.addResources(resource);
					changedResourcesTypes.add(resource.getResourceType());
				});

				// Notify all players only of the resources that have changed
				Resource[] changedResources = changedResourcesTypes.stream()
						.map(resourceType -> new Resource(resourceType, player.getResourceAmount(resourceType))).toArray(Resource[]::new);
				notifyPlayers(clientInterface -> clientInterface.setResources(changedResources, username));

				logInfo(username + " has supported the Church!");
			}
			else {
				// Do not support Church

				Excommunication excommunication = game.getPlacedExcommunications()[game.getCurrentTurn() / 2 - 1]; // 2 -> 0, 4 -> 1, 6 -> 2
				// NOTE: not sure if this is going to work
				excommunication.resolveEffect(effectResolutors.get(username));
				notifyPlayers(clientInterface -> clientInterface.placeExcommunicationToken(player.getColor(), excommunication.getPeriodType()));

				logInfo(username + " has not supported the Church!");
			}
		});
	}

	private void initializeBoardForReconnectingPlayer(String playerUsername, ClientInterface clientInterface) {
		clientNotificationQueue.forEach(f -> {
			try {
				f.apply(playerUsername, clientInterface);
			}
			catch (IOException e) {
				manageIOException(playerUsername, e);
			}
		});
	}

	private void logInfo(String message) {
		System.out.println("> Game " + gameID + ": " + message);
	}

	/**
	 * Notify every connected client about something
	 * 
	 * @param c
	 *            the function we want to call on every connected player (providing access to only the clientInterface)
	 */
	public void notifyPlayers(CheckedFunction1<ClientInterface, IOException> c) {
		notifyPlayers((username, clientInterface) -> c.apply(clientInterface));
	}

	/**
	 * Notify every connected client about something
	 * 
	 * @param c
	 *            the function we want to call on every connected player (providing access to both the username and the clientInterface)
	 */
	private void notifyPlayers(CheckedFunction2<String, ClientInterface, IOException> c) {
		clientNotificationQueue.add(c);
		List<Pair<String, IOException>> raisedIOException = new ArrayList<>();

		users.entrySet().stream().parallel().forEach(entry -> {
			try {
				c.apply(entry.getKey(), entry.getValue());
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
