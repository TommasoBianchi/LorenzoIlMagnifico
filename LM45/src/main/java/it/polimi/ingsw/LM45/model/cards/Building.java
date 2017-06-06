package it.polimi.ingsw.LM45.model.cards;

import it.polimi.ingsw.LM45.model.effects.CardEffect;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;

public class Building extends Card {
	
	private int minDiceToProduce;

	public Building(String name, PeriodType periodType, Cost cost, CardEffect immediateEffects,
			CardEffect effects, int minDiceToProduce) {
		super(name, periodType, cost, immediateEffects, effects);
		this.minDiceToProduce = minDiceToProduce;
		this.cardType = CardType.BUILDING;
	}
	
	@Override
	public void resolveEffect(EffectResolutor effectResolutor, int diceValue){
		if(diceValue >= minDiceToProduce)
			effect.resolveEffects(effectResolutor);
	}

}
