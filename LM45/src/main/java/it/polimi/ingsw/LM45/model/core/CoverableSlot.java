package it.polimi.ingsw.LM45.model.core;

import java.util.List;

import it.polimi.ingsw.LM45.model.effects.ActionModifier;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;

public class CoverableSlot extends Slot {

	public CoverableSlot(Resource[] immediateBonus, int minDice, SlotType type, boolean multipleFamiliars,
			boolean multipleFamiliarsOfSamePlayer, List<Slot> neighbouringSlots, boolean isActive) {
		super(immediateBonus, minDice, type, multipleFamiliars, multipleFamiliarsOfSamePlayer, neighbouringSlots);
		this.isActive = isActive;
	}

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
