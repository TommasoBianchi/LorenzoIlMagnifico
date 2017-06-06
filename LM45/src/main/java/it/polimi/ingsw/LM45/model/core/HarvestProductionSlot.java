package it.polimi.ingsw.LM45.model.core;

import it.polimi.ingsw.LM45.model.effects.ActionModifier;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;

public class HarvestProductionSlot extends Slot {
	
	public int diceModifier;
	
	@Override
	public void addFamiliar(Familiar familiar, ActionModifier actionModifier, EffectResolutor effectResolutor){
		super.addFamiliar(familiar, actionModifier, effectResolutor);
		if(type == SlotType.HARVEST)
			effectResolutor.harvest(familiar.getValue() + actionModifier.getDiceBonus() + diceModifier);
		else if(type == SlotType.PRODUCTION)
			effectResolutor.produce(familiar.getValue() + actionModifier.getDiceBonus() + diceModifier);
	}

}
