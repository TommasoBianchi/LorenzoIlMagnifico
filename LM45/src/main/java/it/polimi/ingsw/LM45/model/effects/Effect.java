package it.polimi.ingsw.LM45.model.effects;

import java.io.Serializable;

import it.polimi.ingsw.LM45.model.core.SlotType;

public abstract class Effect implements Serializable {

	private static final long serialVersionUID = 1L;

	public abstract void resolveEffect(EffectResolutor effectResolutor);
	
	public ActionModifier getActionModifier(SlotType slotType){
		return ActionModifier.EMPTY;
	}

}
