package it.polimi.ingsw.LM45.model.effects;

import it.polimi.ingsw.LM45.model.core.SlotType;

public class CopyEffect extends Effect {

	private Effect copiedEffect;
	
	@Override
	public void resolveEffect(EffectResolutor effectResolutor) {
		if(copiedEffect != null)
			copiedEffect = effectResolutor.copyEffect();
		copiedEffect.resolveEffect(effectResolutor);
	}
	
	@Override
	public ActionModifier getActionModifier(SlotType slotType) {
		if(copiedEffect != null)
			return copiedEffect.getActionModifier(slotType);
		else
			// this sould never happen 
			return ActionModifier.EMPTY;
	}

}
