package it.polimi.ingsw.LM45.model.cards;

import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.effects.CardEffect;

public class Venture extends Card {
	
	private Cost alternativeCost;

	public Venture(String name, PeriodType periodType, Cost cost, CardEffect immediateEffects,
			CardEffect effects, Cost alternativeCost) {
		super(name, periodType, cost, immediateEffects, effects, false);
		this.alternativeCost = alternativeCost;
		this.cardType = CardType.VENTURE;
	}

	@Override
	public boolean canPick(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

}
