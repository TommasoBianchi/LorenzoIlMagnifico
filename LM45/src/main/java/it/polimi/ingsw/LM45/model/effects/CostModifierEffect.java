package it.polimi.ingsw.LM45.model.effects;

import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.model.effects.modifiers.ActionModifier;
import it.polimi.ingsw.LM45.model.effects.modifiers.ResourceAdder;
import it.polimi.ingsw.LM45.model.effects.modifiers.ResourceModifier;
import it.polimi.ingsw.LM45.model.effects.modifiers.ResourceMultiplier;

public class CostModifierEffect extends Effect {

	private static final long serialVersionUID = 1L;
	
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
		// FIXME: canModifyCardCost and canModifyTowerCost?
		ResourceModifier resourceModifier = (isMultiplier) ? new ResourceMultiplier(resource) : new ResourceAdder(resource);
		return new ActionModifier(new ResourceModifier[]{ resourceModifier });
	}
	
	@Override
	public String toString() {
		String where = (canModifyCardCost && canModifyTowerCost) ? "cards and towers" : (canModifyCardCost) ? "cards" : "towers";
		String sign = (isMultiplier) ? "x" : (resource.getAmount() > 0) ? "+" : "-";
		return "Whenever you have to pay " + resource.getResourceType() + " on " + where + ", you pay " + sign + Math.abs(resource.getAmount());
	}

}
