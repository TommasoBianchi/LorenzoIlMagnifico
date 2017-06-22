package it.polimi.ingsw.LM45.model.core;

import it.polimi.ingsw.LM45.exceptions.IllegalActionException;
import it.polimi.ingsw.LM45.model.effects.ActionModifier;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;

public class CoverableSlot extends Slot {

	public CoverableSlot(Resource[] immediateBonus, int minDice, SlotType type, boolean multipleFamiliars,
			boolean multipleFamiliarsOfSamePlayer, boolean isActive) {
		super(immediateBonus, minDice, type, multipleFamiliars, multipleFamiliarsOfSamePlayer);
		this.isActive = isActive;
	}

	private boolean isActive;

	@Override
	public boolean canAddFamiliar(Familiar familiar, ActionModifier actionModifier) throws IllegalActionException {
		if(!isActive)
			throw new IllegalActionException("Cannot place a familiar " + familiar.getFamiliarColor() + " because this slot is not active");
		
		return super.canAddFamiliar(familiar, actionModifier) && isActive;
	}

	@Override
	public void addFamiliar(Familiar familiar, ActionModifier actionModifier, EffectResolutor effectResolutor) {
		if(isActive)
			super.addFamiliar(familiar, actionModifier, effectResolutor);
	}

}
