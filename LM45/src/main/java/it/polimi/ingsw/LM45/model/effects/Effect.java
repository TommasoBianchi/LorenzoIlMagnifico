package it.polimi.ingsw.LM45.model.effects;

import java.io.Serializable;
import java.util.function.Function;

import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.model.effects.modifiers.ActionModifier;

public abstract class Effect implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public boolean canResolveEffect(EffectResolutor effectResolutor){
		return true;
	}

	public abstract void resolveEffect(EffectResolutor effectResolutor);
	
	public ActionModifier getActionModifier(SlotType slotType){
		return ActionModifier.EMPTY();
	}
	
	public Function<Boolean, Boolean> changeIsPermanent(){
		return x -> x;
	}

}
