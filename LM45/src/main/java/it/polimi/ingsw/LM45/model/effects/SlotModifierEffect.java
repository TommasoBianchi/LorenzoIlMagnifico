package it.polimi.ingsw.LM45.model.effects;

import it.polimi.ingsw.LM45.model.core.SlotType;

public class SlotModifierEffect extends Effect {

	private static final long serialVersionUID = 1L;

	private SlotType slotType;
	private boolean canPlaceFamiliars;
	private boolean canPlaceMultipleFamiliars;
	private boolean canReceiveResources;

	public SlotModifierEffect(SlotType slotType, boolean canPlaceFamiliars, boolean canPlaceMultipleFamiliars,
			boolean canReceiveResources) {
		this.slotType = slotType;
		this.canPlaceFamiliars = canPlaceFamiliars;
		this.canPlaceMultipleFamiliars = canPlaceMultipleFamiliars;
		this.canReceiveResources = canReceiveResources;
	}

	@Override
	public void resolveEffect(EffectResolutor effectResolutor) {
		// NOTE this may need to do nothing

	}

	@Override
	public ActionModifier getActionModifier(SlotType slotType) {
		if (slotType == this.slotType)
			return new ActionModifier(!canReceiveResources, canPlaceMultipleFamiliars, canPlaceFamiliars);
		else
			return ActionModifier.EMPTY;
	}
	
	@Override
	public String toString() {
		if(!canPlaceFamiliars)
			return "Can't place familiars in " + slotType.toString() + " slots";
		else if(canPlaceMultipleFamiliars)
			return "Can place familiars in occupied action slots";
		else
			return "Can't receive bonus resources from tower slots";
	}

}
