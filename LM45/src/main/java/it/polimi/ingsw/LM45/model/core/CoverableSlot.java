package it.polimi.ingsw.LM45.model.core;

import it.polimi.ingsw.LM45.model.effects.ActionModifier;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;

public class CoverableSlot extends Slot {

	private boolean isActive;

	@Override
	public boolean canAddFamiliar(Familiar familiar, ActionModifier actionModifier) {
		return super.canAddFamiliar(familiar, actionModifier) && isActive;
	}

	@Override
	public void addFamiliar(Familiar familiar, ActionModifier actionModifier, EffectResolutor effectResolutor) {
		if(isActive)
			super.addFamiliar(familiar, actionModifier, effectResolutor);
		// FIXME: this may need to throw an exception if isActive == false
	}

}
