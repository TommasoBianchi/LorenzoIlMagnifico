package it.polimi.ingsw.LM45.model.cards;

import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.effects.CardEffect;

public class Territory extends Card {

	private int minDiceToGather;
	
	public Territory(String name, PeriodType periodType, Cost cost, CardEffect immediateEffects,
			CardEffect effects, int minDiceToGather) {
		super(name, periodType, cost, immediateEffects, effects);
		this.minDiceToGather = minDiceToGather;
		this.cardType = CardType.TERRITORY;
	}

	@Override
	public void canPick(Player player) {
		// TODO Auto-generated method stub

	}

}
