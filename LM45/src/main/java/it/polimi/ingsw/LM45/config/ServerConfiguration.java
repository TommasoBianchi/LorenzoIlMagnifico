package it.polimi.ingsw.LM45.config;

public class ServerConfiguration implements Configuration {

	/**
	 * The number of players you need to reach to automatically start game (so also the maximum number of
	 * players allowed to play in the same game).
	 */
	private int maxPlayersAmount = 2;
	
	/**
	 * The time (in milliseconds) before the game starts after at least 2 players have logged in.
	 */
	private int gameStartTimerDelay = 25 * 1000;
	
	/**
	 * The time (in milliseconds) each player has to complete his turn.
	 */
	private int turnTimerDelay = 1 * 1000;
	
	/**
	 * The port on which the server listens for clients to connect.
	 */
	private int serverSocketPort = 7000;
	
	/**
	 * @param maxPlayersAmount The number of players you need to reach to automatically start game (so also the maximum number of
	 * players allowed to play in the same game).
	 * @param gameStartTimerDelay The time (in milliseconds) before the game starts after at least 2 players have logged in.
	 * @param turnTimerDelay The time (in milliseconds) each player has to complete his turn.
	 * @param serverSocketPort The port on which the server listens for clients to connect.
	 */
	public ServerConfiguration(int maxPlayersAmount, int gameStartTimerDelay, int turnTimerDelay, int serverSocketPort){
		this.maxPlayersAmount = maxPlayersAmount;
		this.gameStartTimerDelay = gameStartTimerDelay;
		this.turnTimerDelay = turnTimerDelay;
		this.serverSocketPort = serverSocketPort;
	}

	/**
	 * @return The number of players you need to reach to automatically start game (so also the maximum number of
	 * players allowed to play in the same game).
	 */
	public int getMaxPlayersAmount() {
		return maxPlayersAmount;
	}

	/**
	 * @return gameStartTimerDelay The time (in milliseconds) before the game starts after at least 2 players have logged in.
	 */
	public int getGameStartTimerDelay() {
		return gameStartTimerDelay;
	}
	
	/**
	 * @return The time (in milliseconds) each player has to complete his turn.
	 */
	public int getTurnTimerDelay(){
		return turnTimerDelay;
	}
	
	/**
	 * @return The port on which the server listens for clients to connect.
	 */
	public int getServerSocketPort(){
		return serverSocketPort;
	}
	
}
