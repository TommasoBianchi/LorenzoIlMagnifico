package it.polimi.ingsw.LM45.model.cards;

import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.effects.CardEffect;

public class Building extends Card {
	
	private int minDiceToProduce;

	public Building(String name, PeriodType periodType, Cost cost, CardEffect immediateEffects,
			CardEffect effects, int minDiceToProduce) {
		super(name, periodType, cost, immediateEffects, effects, false);
		this.minDiceToProduce = minDiceToProduce;
		this.cardType = CardType.BUILDING;
	}

	@Override
	public boolean canPick(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

}
