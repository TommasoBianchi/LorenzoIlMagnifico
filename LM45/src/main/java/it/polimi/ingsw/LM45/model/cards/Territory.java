package it.polimi.ingsw.LM45.model.cards;

import it.polimi.ingsw.LM45.model.effects.CardEffect;

public class Territory extends Card {

	private int minDiceToGather;
	
	public Territory(String name, PeriodType periodType, CardEffect immediateEffects,
			CardEffect effects, int minDiceToGather) {
		super(name, periodType, Cost.EMPTY, immediateEffects, effects);
		this.minDiceToGather = minDiceToGather;
		this.cardType = CardType.TERRITORY;
	}
	
}
