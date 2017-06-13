package it.polimi.ingsw.LM45.model.cards;

import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.effects.ActionModifier;
import it.polimi.ingsw.LM45.model.effects.CardEffect;

public class Venture extends Card {
	
	private Cost alternativeCost;

	public Venture(String name, PeriodType periodType, Cost cost, CardEffect immediateEffects,
			CardEffect effects, Cost alternativeCost) {
		super(name, periodType, cost, immediateEffects, effects);
		this.alternativeCost = alternativeCost;
		this.cardType = CardType.VENTURE;
	}
	
	public Venture(String name, PeriodType periodType, Cost cost, CardEffect immediateEffects,
			CardEffect effects) {
		this(name, periodType, cost, immediateEffects, effects, Cost.EMPTY);
	}

	@Override
	public boolean canPick(Player player, ActionModifier actionModifier) {
		return super.canPick(player, actionModifier) && alternativeCost.canPay(player, actionModifier);
	}

}
