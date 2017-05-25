package it.polimi.ingsw.LM45.model.cards;

import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.effects.CardEffect;

public class Character extends Card {

	public Character(String name, PeriodType periodType, Cost cost, CardEffect immediateEffects,
			CardEffect effects) {
		super(name, periodType, cost, immediateEffects, effects, true);
		this.cardType = CardType.CHARACTER;
	}

	@Override
	public boolean canPick(Player player) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
