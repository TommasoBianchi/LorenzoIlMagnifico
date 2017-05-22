package it.polimi.ingsw.LM45.model.core;

import java.util.List;
import java.util.Map;
import java.util.Random;

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
	
	private void shuffleDecks(){
		for(CardType cardType : deck.keySet())
			deck.put(cardType, ShuffleHelper.shuffleByPeriod(deck.get(cardType)));
		
		leaderCards = ShuffleHelper.shuffle(leaderCards);
		
		for(PeriodType periodType : excommunicationDeck.keySet())
			excommunicationDeck.put(periodType, ShuffleHelper.shuffle(excommunicationDeck.get(periodType)));
	}
}
