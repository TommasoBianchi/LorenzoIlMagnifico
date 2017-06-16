package it.polimi.ingsw.LM45.model.effects;

import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.SlotType;

public class CostModifierEffect extends Effect {
	
	private Resource resource;
	private boolean canModifyCardCost;
	private boolean canModifyTowerCost;
	private boolean isMultiplier;
	
	public CostModifierEffect(Resource resource, boolean canModifyCardCost, boolean canModifyTowerCost, boolean isMultiplier){
		this.resource = resource;
		this.canModifyCardCost = canModifyCardCost;
		this.canModifyTowerCost = canModifyTowerCost;
		this.isMultiplier = isMultiplier;
	}

	@Override
	public void resolveEffect(EffectResolutor effectResolutor) {
		// NOTE this may need to do nothing

	}
	
	@Override
	public ActionModifier getActionModifier(SlotType slotType) {
		// FIXME: need a smart way to implement this!
		return ActionModifier.EMPTY;
	}
	
	@Override
	public String toString() {
		//TODO
	}

}
