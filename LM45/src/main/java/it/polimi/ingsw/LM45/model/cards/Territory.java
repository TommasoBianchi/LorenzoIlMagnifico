package it.polimi.ingsw.LM45.model.cards;

import it.polimi.ingsw.LM45.model.effects.CardEffect;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;

public class Territory extends Card {

	private int minDiceToHarvest;
	
	/**
	 * @param name name of the Card
	 * @param periodType period of the Card : I,II or III
	 * @param immediateEffects the effect that is activated immediately when Card is picked
	 * @param effects the effect that is not activated immediately
	 * @param minDiceToHarvest min value to activate effects
	 */
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
