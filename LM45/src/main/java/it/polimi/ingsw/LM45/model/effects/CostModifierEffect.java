package it.polimi.ingsw.LM45.model.effects;

import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.model.effects.modifiers.ActionModifier;
import it.polimi.ingsw.LM45.model.effects.modifiers.ResourceAdder;
import it.polimi.ingsw.LM45.model.effects.modifiers.ResourceModifier;
import it.polimi.ingsw.LM45.model.effects.modifiers.ResourceMultiplier;

/**
 * This effect modifies the cost of cards or the cost of entering an occupied tower
 * 
 * @author Tommy
 *
 */
public class CostModifierEffect extends Effect {

	private static final long serialVersionUID = 1L;

	private Resource resource;
	private boolean disableTowerCost;
	private boolean isMultiplier;

	/**
	 * @param disableTowerCost
	 *            true if this effect disables the cost of entering an occupied tower
	 */
	public CostModifierEffect(boolean disableTowerCost) {
		this.disableTowerCost = disableTowerCost;
	}

	/**
	 * Instantiate a CostModifierEffect that modifies the cost of cards
	 * 
	 * @param resource
	 *            the resource to modify the cost
	 * @param isMultiplier
	 *            true if the modification is a multiplication, false if it is only an addition/removal of resources
	 */
	public CostModifierEffect(Resource resource, boolean isMultiplier) {
		this(false);
		this.resource = resource;
		this.isMultiplier = isMultiplier;
	}

	@Override
	public void resolveEffect(EffectResolutor effectResolutor) {
		if (disableTowerCost)
			effectResolutor.setPayIfTowerIsOccupied(false);
	}

	@Override
	public ActionModifier getActionModifier(SlotType slotType) {
		if (disableTowerCost)
			return ActionModifier.EMPTY();
		else {
			ResourceModifier resourceModifier = (isMultiplier) ? new ResourceMultiplier(resource) : new ResourceAdder(resource);
			return new ActionModifier(new ResourceModifier[] { resourceModifier });
		}
	}

	@Override
	public String toString() {
		if (disableTowerCost)
			return "You do not need to spend 3 additional coins to enter an occupied tower";
		else {
			String sign = (resource.getAmount() > 0) ? "+" : "-";
			sign = (isMultiplier) ? "x" : sign;
			return "Whenever you have to pay " + resource.getResourceType() + ", you pay " + sign + Math.abs(resource.getAmount());
		}
	}

}
