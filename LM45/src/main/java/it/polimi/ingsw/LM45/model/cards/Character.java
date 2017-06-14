package it.polimi.ingsw.LM45.model.cards;

import it.polimi.ingsw.LM45.model.effects.CardEffect;

public class Character extends Card {

	/**
	 * @param name name of the Card
	 * @param periodType period of the Card : I,II or III
	 * @param cost the resources player needs to pay or to have to pick the Card
	 * @param immediateEffects the effect that is activated immediately when Card is picked
	 * @param effects the effect that is not activated immediately
	 */
	public Character(String name, PeriodType periodType, Cost cost, CardEffect immediateEffects,
			CardEffect effects) {
		super(name, periodType, cost, immediateEffects, effects);
		this.cardType = CardType.CHARACTER;
	}
	
}
