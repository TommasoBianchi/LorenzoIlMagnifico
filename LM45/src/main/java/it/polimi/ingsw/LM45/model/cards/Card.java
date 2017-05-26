package it.polimi.ingsw.LM45.model.cards;

import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.effects.CardEffect;

public abstract class Card {

	protected String name;
	private PeriodType periodType;
	protected CardType cardType;
	protected Cost cost;
	protected CardEffect immediateEffect;
	protected CardEffect effect;
	
	public Card(String name, PeriodType periodType, Cost cost, CardEffect immediateEffect, CardEffect effect){
		this.name = name;
		this.periodType = periodType;
		this.cost = cost;
		this.immediateEffect = immediateEffect;
		this.effect = effect;
	}
	
	public boolean canPick(Player player){
		return cost.canPay(player) && player.canAddCard(this);
	}
	
	public String getName(){
		return this.name;
	}

	public PeriodType getPeriodType() {
		return this.periodType;
	}
	
	public CardType getCardType(){
		return this.cardType;
	}
	
}
