package it.polimi.ingsw.LM45.model.core;

import it.polimi.ingsw.LM45.exceptions.IllegalActionException;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;
import it.polimi.ingsw.LM45.model.effects.modifiers.ActionModifier;

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
	public boolean canAddFamiliar(Familiar familiar, ActionModifier actionModifier, EffectResolutor effectResolutor) throws IllegalActionException {
		// A player can always do a bonus action of HARVEST/PRODUCE, so do not check anything in this slot if that's the case
		if(familiar.getFamiliarColor() == FamiliarColor.BONUS)
			return true;
		else
			return super.canAddFamiliar(familiar, actionModifier, effectResolutor);
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
