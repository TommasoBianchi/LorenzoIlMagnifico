package it.polimi.ingsw.LM45.model.cards;

import it.polimi.ingsw.LM45.model.effects.CardEffect;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;

public class Building extends Card {
	
	private static final long serialVersionUID = 1L;
	
	private int minDiceToProduce;

	/**
	 * @param name name of the Card
	 * @param periodType period of the Card : I,II or III
	 * @param cost the resources player needs to pay or to have to pick the Card
	 * @param immediateEffects the effect that is activated immediately when Card is picked
	 * @param effects the effect that is not activated immediately
	 * @param minDiceToProduce min value to activate effects
	 */
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
	
	@Override
	public String toString(){
		return super.toString() + "\n" + "Min dice to produce: " + minDiceToProduce;
	}

}
