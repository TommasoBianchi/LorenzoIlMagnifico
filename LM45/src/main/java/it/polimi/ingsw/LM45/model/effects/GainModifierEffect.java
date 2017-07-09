package it.polimi.ingsw.LM45.model.effects;

import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.model.effects.modifiers.ActionModifier;
import it.polimi.ingsw.LM45.model.effects.modifiers.ResourceAdder;
import it.polimi.ingsw.LM45.model.effects.modifiers.ResourceModifier;
import it.polimi.ingsw.LM45.model.effects.modifiers.ResourceMultiplier;

/**
 * This effect modifies the amount of resources gained each time a player has to gain some
 * 
 * @author Tommy
 *
 */
public class GainModifierEffect extends Effect {

	private static final long serialVersionUID = 1L;

	private Resource resource;
	private boolean isMultiplier;

	/**
	 * @param resource
	 *            the resource to modify the gain
	 * @param isMultiplier
	 *            true if the resource gained are multiplied by the amount specified, false if the amount specified is added/subtracted
	 */
	public GainModifierEffect(Resource resource, boolean isMultiplier) {
		this.resource = resource;
		this.isMultiplier = isMultiplier;
	}

	@Override
	public void resolveEffect(EffectResolutor effectResolutor) {
		// This does not need to do nothing because this effect will alway be a permanent one
	}

	@Override
	public ActionModifier getActionModifier(SlotType slotType) {
		ResourceModifier resourceModifier = (isMultiplier) ? new ResourceMultiplier(resource) : new ResourceAdder(resource);
		return new ActionModifier(new ResourceModifier[] {}, new ResourceModifier[] { resourceModifier }, 0);
	}

	@Override
	public String toString() {
		String sign = (resource.getAmount() > 0) ? "+" : "-";
		sign = (isMultiplier) ? "x" : sign;
		return "Whenever you have to gain " + resource.getResourceType() + ", you gain " + sign + Math.abs(resource.getAmount());
	}

}
