package it.polimi.ingsw.LM45.model.effects;

import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.SlotType;

public class CostModifierEffect extends Effect {
	
	private Resource resource;
	private boolean canModifyCardCost;
	private boolean canModifyTowerCost;
	private boolean isMultiplier;

	@Override
	public void resolveEffect(EffectResolutor effectResolutor) {
		// TODO this may need to do nothing

	}
	
	@Override
	public ActionModifier getActionModifier(SlotType slotType) {
		
	}

}
