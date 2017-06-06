package it.polimi.ingsw.LM45.model.cards;

import it.polimi.ingsw.LM45.model.effects.CardEffect;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;

public class Territory extends Card {

	private int minDiceToHarvest;
	
	public Territory(String name, PeriodType periodType, CardEffect immediateEffects,
			CardEffect effects, int minDiceToHarvest) {
		super(name, periodType, Cost.EMPTY, immediateEffects, effects);
		this.minDiceToHarvest = minDiceToHarvest;
		this.cardType = CardType.TERRITORY;
	}
	
	@Override
	public void resolveEffect(EffectResolutor effectResolutor, int diceValue){
		if(diceValue >= minDiceToHarvest)
			effect.resolveEffects(effectResolutor);
	}
	
}
