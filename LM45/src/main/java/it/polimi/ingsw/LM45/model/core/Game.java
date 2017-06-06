package it.polimi.ingsw.LM45.model.core;


import java.util.List;
import java.util.Map;

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
	
	public Game(List<Player> players, Map<CardType, List<Card>> deck, List<LeaderCard> leaderCards, Map<PeriodType, List<Excommunication>> excommunicationDeck){
		this.players = players;
		this.board = new Board();
		this.deck = deck;
		this.leaderCards = leaderCards;
		this.excommunicationDeck = excommunicationDeck;
		this.currentPlayerIndex = 0;
		this.currentRound = 0;
		this.currentTurn = 0;
	}
	
	public void start(){
		shuffleDecks();
		
		for(PeriodType periodType : excommunicationDeck.keySet())
			board.placeExcommunication(excommunicationDeck.get(periodType).get(0)); // Pick the first because they have been already shuffled
	}
	
	public void startTurn(){
		board.clearTowers();
		deck.keySet().stream().forEach(cardType -> {
			for(int i = 0; i < 4; i++)
				board.placeCard(deck.get(cardType).remove(0));
		});
		players = ShuffleHelper.shuffle(players); // Randomize first turn order
	}
	
	public boolean hasNextPlayer(){
		// Return true if we are in one of the 4 rounds or we are in the 5th (used by the player that have skipped the first) and there are still
		// players that have skipped the 1st
		return currentRound < 4 || (currentRound == 4 && players.stream().skip(currentPlayerIndex).anyMatch(player -> player.getHasToSkipFirstTurn()));
	}
	
	public Player getNextPlayer(){
		if(currentRound == 0){
			while(currentPlayerIndex < players.size() && players.get(currentPlayerIndex).getHasToSkipFirstTurn() == true)
				currentPlayerIndex++;
		}
		else if (currentRound == 4){
			while(currentPlayerIndex < players.size() && players.get(currentPlayerIndex).getHasToSkipFirstTurn() == false)
				currentPlayerIndex++;
		}
		
		Player currentPlayer = players.get(currentPlayerIndex);
		
		currentPlayerIndex++;		
		if(currentPlayerIndex >= players.size()){
			currentPlayerIndex = 0;
			currentRound++;
		}
		
		return currentPlayer;
	}
	
	private void shuffleDecks(){
		for(CardType cardType : deck.keySet())
			deck.put(cardType, ShuffleHelper.shuffleByPeriod(deck.get(cardType)));
		
		leaderCards = ShuffleHelper.shuffle(leaderCards);
		
		for(PeriodType periodType : excommunicationDeck.keySet())
			excommunicationDeck.put(periodType, ShuffleHelper.shuffle(excommunicationDeck.get(periodType)));
	}
}
