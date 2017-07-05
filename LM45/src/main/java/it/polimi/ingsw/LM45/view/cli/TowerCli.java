package it.polimi.ingsw.LM45.view.cli;

import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.CardType;

public class TowerCli {

	private CardType cardType;
	private Card[] cards;
	
	public TowerCli(CardType cardType) {
		this.cardType = cardType;
		this.cards = new Card[4];
	}
	
	public void print(){
		ConsoleWriter.println("--------------------");
		for(int i = 3; i >= 0; i--){
			ConsoleWriter.println("--------------------");
			if(cards[i] == null)
				ConsoleWriter.println("\nEmpty slot\n");
			else
				ConsoleWriter.println(cards[i].toString());
		}
	}
	
	public void addCards(Card[] cards){
		this.cards = cards.clone();
	}
	
	public void removeCard(Card card){
		for(int i = 0; i < 4; i++)
			if(cards[i].getName().equals(card.getName())){
				cards[i] = null;
				break;
			}
	}
	
}
