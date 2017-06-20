package it.polimi.ingsw.LM45.model.effects;

import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.SlotType;

public class GainModifierEffect extends Effect {

	private static final long serialVersionUID = 1L;
	
	private Resource resource;
	private boolean canModifyCardGain;
	private boolean canModifyTowerGain;
	private boolean isMultiplier;
	
	public GainModifierEffect(Resource resource, boolean canModifyCardGain, boolean canModifyTowerGain, boolean isMultiplier){
		this.resource = resource;
		this.canModifyCardGain = canModifyCardGain;
		this.canModifyTowerGain = canModifyTowerGain;
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
		String where = (canModifyCardGain && canModifyTowerGain) ? "cards and towers" : (canModifyCardGain) ? "cards" : "towers";
		String sign = (isMultiplier) ? "x" : (resource.getAmount() > 0) ? "+" : "-";
		return "Whenever you have to gain " + resource.getResourceType() + " on " + where + ", you gain " + sign + Math.abs(resource.getAmount());
	}

}
