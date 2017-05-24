package it.polimi.ingsw.LM45.model.effects;

import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.SlotType;

public class SlotModifierEffect extends Effect {
	
	private SlotType slotType;
	private boolean canPlaceFamiliars;
	private boolean canPlaceMultipleFamiliars;
	private boolean canReceiveResources;
	
	public SlotModifierEffect(SlotType slotType, boolean canPlaceFamiliars, boolean canPlaceMultipleFamiliars, boolean canReceiveResources){
		this.slotType = slotType;
		this.canPlaceFamiliars = canPlaceFamiliars;
		this.canPlaceMultipleFamiliars = canPlaceMultipleFamiliars;
		this.canReceiveResources = canReceiveResources;
	}

	@Override
	public void ResolveEffect(Player player) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public ActionModifier getActionModifier(SlotType slotType){
		if(slotType == this.slotType)
			return new ActionModifier(!canReceiveResources, canPlaceMultipleFamiliars, canPlaceFamiliars);
		else
			return ActionModifier.EMPTY;
	}

}
