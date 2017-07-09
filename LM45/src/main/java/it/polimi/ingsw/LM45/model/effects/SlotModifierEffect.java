package it.polimi.ingsw.LM45.model.effects;

import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.model.effects.modifiers.ActionModifier;

/**
 * This effect modifies how a player can place familiars on a slot
 * 
 * @author Tommy
 *
 */
public class SlotModifierEffect extends Effect {

	private static final long serialVersionUID = 1L;

	private SlotType slotType;
	private boolean canPlaceFamiliars;
	private boolean canPlaceMultipleFamiliars;
	private boolean canReceiveResources;

	/**
	 * @param slotType
	 *            the slotType of the slots affected by this effect
	 * @param canPlaceFamiliars
	 *            true if the player can still place familiars on the affected slot
	 * @param canPlaceMultipleFamiliars
	 *            true if the player can place familiars on the affected slot even if there is already one
	 * @param canReceiveResources
	 *            true if the player can receive resources when placing familiars on the affected slot from the slot itself
	 */
	public SlotModifierEffect(SlotType slotType, boolean canPlaceFamiliars, boolean canPlaceMultipleFamiliars, boolean canReceiveResources) {
		this.slotType = slotType;
		this.canPlaceFamiliars = canPlaceFamiliars;
		this.canPlaceMultipleFamiliars = canPlaceMultipleFamiliars;
		this.canReceiveResources = canReceiveResources;
	}

	@Override
	public void resolveEffect(EffectResolutor effectResolutor) {
		// This does not need to do nothing because this effect will alway be a permanent one
	}

	@Override
	public ActionModifier getActionModifier(SlotType slotType) {
		if (slotType.isCompatible(this.slotType))
			return new ActionModifier(!canReceiveResources, canPlaceMultipleFamiliars, canPlaceFamiliars);
		else
			return ActionModifier.EMPTY();
	}

	@Override
	public String toString() {
		if (!canPlaceFamiliars)
			return "Can't place familiars in " + slotType.toString() + " slots";
		else if (canPlaceMultipleFamiliars)
			return "Can place familiars in occupied action slots";
		else
			return "Can't receive bonus resources from tower slots";
	}

}
