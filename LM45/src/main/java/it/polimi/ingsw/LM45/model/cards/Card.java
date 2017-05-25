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
	private boolean effectIsPermanent;
	
	public Card(String name, PeriodType periodType, Cost cost, CardEffect immediateEffect, CardEffect effect, boolean effectIsPermanent){
		this.name = name;
		this.periodType = periodType;
		this.cost = cost;
		this.immediateEffect = immediateEffect;
		this.effect = effect;
		this.effectIsPermanent = effectIsPermanent;
	}
	
	public abstract boolean canPick(Player player);
	
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
