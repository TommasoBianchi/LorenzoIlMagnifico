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
	
	public Game(List<Player> players, Map<CardType, List<Card>> deck, List<LeaderCard> leaderCards, Map<PeriodType, List<Excommunication>> excommunicationDeck){
		this.players = players;
		this.board = new Board();
		this.deck = deck;
		this.leaderCards = leaderCards;
		this.excommunicationDeck = excommunicationDeck;
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
	}
	
	private void shuffleDecks(){
		for(CardType cardType : deck.keySet())
			deck.put(cardType, ShuffleHelper.shuffleByPeriod(deck.get(cardType)));
		
		leaderCards = ShuffleHelper.shuffle(leaderCards);
		
		for(PeriodType periodType : excommunicationDeck.keySet())
			excommunicationDeck.put(periodType, ShuffleHelper.shuffle(excommunicationDeck.get(periodType)));
	}
}
