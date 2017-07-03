package it.polimi.ingsw.LM45.model.core;

import it.polimi.ingsw.LM45.exceptions.IllegalActionException;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;
import it.polimi.ingsw.LM45.model.effects.modifiers.ActionModifier;

public class CoverableSlot extends Slot {

	private Slot baseSlot;
	private boolean isActive;
	
	public CoverableSlot(Slot baseSlot, boolean isActive) {
		super(baseSlot.immediateBonus, baseSlot.minDice, baseSlot.type, baseSlot.multipleFamiliars, baseSlot.multipleFamiliarsOfSamePlayer);
		this.baseSlot = baseSlot;
		this.isActive = isActive;
	}

	@Override
	public boolean canAddFamiliar(Familiar familiar, ActionModifier actionModifier, EffectResolutor effectResolutor) throws IllegalActionException {
		if(!isActive)
			throw new IllegalActionException("Cannot place a familiar " + familiar.getFamiliarColor() + " because this slot is not active");
		else
			return baseSlot.canAddFamiliar(familiar, actionModifier, effectResolutor);
	}

	@Override
	public void addFamiliar(Familiar familiar, ActionModifier actionModifier, EffectResolutor effectResolutor) {
		if(isActive)
			baseSlot.addFamiliar(familiar, actionModifier, effectResolutor);
	}

}
