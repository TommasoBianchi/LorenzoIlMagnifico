package it.polimi.ingsw.LM45.model.effects;

import it.polimi.ingsw.LM45.model.core.SlotType;

public abstract class Effect {

	public abstract void resolveEffect(EffectResolutor effectResolutor);
	
	public ActionModifier getActionModifier(SlotType slotType){
		return ActionModifier.EMPTY;
	}

}
