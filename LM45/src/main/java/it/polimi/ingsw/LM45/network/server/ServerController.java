package it.polimi.ingsw.LM45.network.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.model.cards.LeaderCard;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.Game;
import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.network.client.ClientInterface;
import it.polimi.ingsw.LM45.serialization.FileManager;
import javafx.scene.paint.Color;

// This is designed to manage only one game (consider renaming it to GameController)
public class ServerController {

	private Map<String, ClientInterface> users;
	private Map<String, Player> players;
	private List<Color> availableColors;
	private Map<String, LeaderCard> leaderCards;
	private Map<CardType, List<Card>> deck;
	private int maxNumberOfPlayers;
	private long gameStartTimerDelay;
	private Timer gameStartTimer;
	private Game game;

	public ServerController(int maxNumberOfPlayers, long gameStartTimerDelay)
			throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		this.users = new HashMap<String, ClientInterface>();
		this.players = new HashMap<String, Player>();
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
	}

	public void login(String username, ClientInterface clientInterface) {
		while (users.containsKey(username) || players.containsKey(username)) {
			username += new Random().nextInt(10);
		}
		try {
			clientInterface.setUsername(username);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(username + " logged in");
		users.put(username, clientInterface);
		Color randomColor = availableColors.remove(new Random().nextInt(availableColors.size()));
		players.put(username, new Player(username, randomColor));
		System.out.println("Currently in the game: " + players.keySet().stream().reduce("", (a, b) -> a + b + " "));
		
		if(players.size() == maxNumberOfPlayers){
			startGame();
		}
		else if(players.size() > 1){
			setGameStartTimer();
		}
	}

	public void removeUser(String username) {
		users.remove(username);
	}

	public void placeFamiliar(String player, FamiliarColor familiarColor, Integer slotID) {
		// TODO: implement
		System.out.println(player + " placed familiar " + familiarColor + " in slot " + slotID);
	}

	public void increaseFamiliarValue(String player, FamiliarColor familiarColor) {
		if (players.containsKey(player)) {
			System.out.println(player + " increased value of familiar " + familiarColor);
			players.get(player).increaseFamiliarValue(familiarColor);
		}
	}

	public void playLeaderCard(String player, String leaderCardName) {
		if (players.containsKey(player) && leaderCards.containsKey(leaderCardName)) {
			System.out.println(player + " played leader card " + leaderCardName);
			players.get(player).playLeaderCard(leaderCards.get(leaderCardName));
		}
	}

	public void activateLeaderCard(String player, String leaderCardName) {
		if (players.containsKey(player) && leaderCards.containsKey(leaderCardName)) {
			System.out.println(player + " activated leader card " + leaderCardName);
			// TODO: implement
		}
	}

	public void discardLeaderCard(String player, String leaderCardName) {
		if (players.containsKey(player) && leaderCards.containsKey(leaderCardName)) {
			System.out.println(player + " discarded leader card " + leaderCardName);
			players.get(player).discardLeaderCard(leaderCards.get(leaderCardName));
		}
	}

	public void endTurn(String player) {
		// TODO: implement
		System.out.println(player + " ended his turn");
	}
	
	public void startGame(){
		gameStartTimer.cancel();
		System.out.println("Game is starting!");
		game = new Game(new ArrayList<Player>(players.values()), deck, new ArrayList<LeaderCard>(leaderCards.values()), new HashMap<>()/*load the excommunication deck*/);
		game.start();
		// TODO: notify players
		// TODO: make first player start his turn
		
		// TEST!!
		while(game.hasNextPlayer()){
			Player nextPlayer = game.getNextPlayer();
			System.out.println(nextPlayer.getUsername());
			users.values().stream().forEach(clientInterface -> {
				try {
					clientInterface.notifyPlayerTurn(nextPlayer.getUsername());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		}
		// TEST!!
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

}
