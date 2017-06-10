package it.polimi.ingsw.LM45.model.core;

import it.polimi.ingsw.LM45.model.effects.ActionModifier;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;

public class HarvestProductionSlot extends Slot {

	private int diceModifier;
	
	public HarvestProductionSlot(Resource[] immediateBonus, int minDice, SlotType type, boolean multipleFamiliars,
			boolean multipleFamiliarsOfSamePlayer, int diceModifier) {
		super(immediateBonus, minDice, type, multipleFamiliars, multipleFamiliarsOfSamePlayer);
		this.diceModifier = diceModifier;
	}
	
	public HarvestProductionSlot(Resource[] immediateBonus, int minDice, SlotType type, boolean multipleFamiliars,
			boolean multipleFamiliarsOfSamePlayer) {
		this(immediateBonus, minDice, type, multipleFamiliars, multipleFamiliarsOfSamePlayer, 0);
	}
	
	@Override
	public void addFamiliar(Familiar familiar, ActionModifier actionModifier, EffectResolutor effectResolutor){
		super.addFamiliar(familiar, actionModifier, effectResolutor);
		if(type == SlotType.HARVEST)
			effectResolutor.harvest(familiar.getValue() + actionModifier.getDiceBonus() + diceModifier);
		else if(type == SlotType.PRODUCTION)
			effectResolutor.produce(familiar.getValue() + actionModifier.getDiceBonus() + diceModifier);
	}

}
