package it.polimi.ingsw.LM45.model.core;

import java.util.List;
import java.util.Map;
import java.util.Random;

import it.polimi.ingsw.LM45.config.BoardConfiguration;
import it.polimi.ingsw.LM45.exceptions.IllegalActionException;
import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.model.cards.Excommunication;
import it.polimi.ingsw.LM45.model.cards.LeaderCard;
import it.polimi.ingsw.LM45.model.cards.PeriodType;
import it.polimi.ingsw.LM45.util.ShuffleHelper;

public class Game {

	private List<Player> players;
	private Board board;
	private Map<CardType, List<Card>> deck;
	private List<LeaderCard> leaderCards;
	private Map<PeriodType, List<Excommunication>> excommunicationDeck;
	private int currentPlayerIndex;
	private int currentRound; // A game is made of 3 periods, 6 turns, 24 rounds
	private int currentTurn;
	private Player currentPlayer;

	/**
	 * @param players the List of Players that partecipate in this game
	 * @param deck the Cards already loaded by the FileManager
	 * @param leaderCards the LeaderCards already loaded by the FileManager
	 * @param excommunicationDeck the Excommunications already loaded by the FileManager
	 */
	public Game(List<Player> players, BoardConfiguration boardConfiguration, Map<CardType, List<Card>> deck, List<LeaderCard> leaderCards,
			Map<PeriodType, List<Excommunication>> excommunicationDeck) {
		this.players = players;
		this.board = new Board(boardConfiguration);
		this.deck = deck;
		this.leaderCards = leaderCards;
		this.excommunicationDeck = excommunicationDeck;
		this.currentPlayerIndex = 0;
		this.currentRound = 0;
		this.currentTurn = 0;
	}

	/**
	 * Initialize the status of this Game performing all the operations that are
	 * we need to do at the beginning of game, in particular:
	 * - suffle the decks (cards, leaderCards and excommunications)
	 * - place on the Board three excommunications (one per period)
	 * - shuffle the players to randomize first turn's order
	 */
	public void start() {
		shuffleDecks();

		for (PeriodType periodType : excommunicationDeck.keySet())
			board.placeExcommunication(excommunicationDeck.get(periodType).get(0)); // Pick the first because they have been already shuffled

		players = ShuffleHelper.shuffle(players); // Randomize first turn order
	}

	/**
	 * @return a boolean that is true if and only if there is still someone that needs to play his turn
	 */
	public boolean hasNextPlayer() {
		// Return true if we are in one of the 4 rounds or we are in the 5th (used by the player that have skipped the first) and there are still
		// players that have skipped the 1st
		return currentRound < 4
				|| (currentRound == 4 && players.stream().skip(currentPlayerIndex).anyMatch(player -> player.getHasToSkipFirstRound()));
	}

	/**
	 * @return the next Player that needs to play his turn
	 */
	public Player getNextPlayer() {
		if (currentRound == 0) {
			while (currentPlayerIndex < players.size() && players.get(currentPlayerIndex).getHasToSkipFirstRound() == true)
				currentPlayerIndex++;
		}
		else if (currentRound == 4) {
			while (currentPlayerIndex < players.size() && players.get(currentPlayerIndex).getHasToSkipFirstRound() == false)
				currentPlayerIndex++;
		}

		currentPlayer = players.get(currentPlayerIndex);

		currentPlayerIndex++;
		if (currentPlayerIndex >= players.size()) {
			currentPlayerIndex = 0;
			currentRound++;
		}

		return currentPlayer;
	}

	/**
	 * Starts a round by resetting bookkeeping indices, clearing slots, picking new cards for the towerSlots, taking the 
	 * new turn's order from the councilSlot and rolling the dices for the familiars' values.
	 */
	public void startRound() {
		// Reset turn counters
		currentPlayerIndex = 0;
		currentRound = 0;
		currentTurn++;

		// Get player order from the council slot
		List<Player> orderedPlayers = board.getCouncilOrder();
		for (Player player : players)
			if (!orderedPlayers.contains(player)) // Add players that did not put a familiar in the council slot
				orderedPlayers.add(player);
		players = orderedPlayers;
		
		board.clearSlots();
		deck.keySet().stream().forEach(cardType -> {
			for (int i = 0; i < 4; i++)
				board.placeCard(deck.get(cardType).remove(0));
		});
		
		// Roll dices for familiars' values
		Random random = new Random();
		for (FamiliarColor familiarColor : FamiliarColor.values()) {
			int diceValue = random.nextInt(6) + 1; // A random number between 1 (inclusive) and 6 (inclusive)
			players.stream().forEach(player -> player.setFamiliarValue(familiarColor, diceValue));
		}
	}

	/**
	 * @return the current turn (from 1 to 6)
	 */
	public int getCurrentTurn() {
		return currentTurn;
	}

	/**
	 * @param slotType the slotType we want to retrieve a slot from
	 * @param slotID the ID of the slot we want among all the slots of the given SlotType 
	 * @return the slot of ID slotID among all the slots of the given SlotType
	 * @throws IllegalActionException if there are no slots of the given SlotType or if the do not contain a slot with ID slotID
	 */
	public Slot getSlot(SlotType slotType, int slotID) throws IllegalActionException {
		return board.getSlot(slotType, slotID);
	}

	private void shuffleDecks() {
		for (CardType cardType : deck.keySet())
			deck.put(cardType, ShuffleHelper.shuffleByPeriod(deck.get(cardType)));

		leaderCards = ShuffleHelper.shuffle(leaderCards);

		for (PeriodType periodType : excommunicationDeck.keySet())
			excommunicationDeck.put(periodType, ShuffleHelper.shuffle(excommunicationDeck.get(periodType)));
	}
	
	/**
	 * @return an array containing the players ordered by the turn order
	 */
	public Player[] getOrderedPlayers(){
		return players.stream().toArray(Player[]::new);
	}

}
