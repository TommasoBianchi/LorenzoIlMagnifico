package it.polimi.ingsw.LM45.model.effects;

import java.util.function.Function;

/**
 * This effect copies another effect and then resolves himself as the copied one
 * 
 * @author Tommy
 *
 */
public class CopyEffect extends Effect {

	private static final long serialVersionUID = 1L;

	private CardEffect copiedEffect;

	@Override
	public void resolveEffect(EffectResolutor effectResolutor) {
		if (copiedEffect == null) {
			copiedEffect = effectResolutor.copyEffect();
			copiedEffect.resolveEffects(effectResolutor);
		}
		else if (!copiedEffect.getEffectsArePermanent()) {
			copiedEffect.resolveEffects(effectResolutor);
		}
	}
	
	@Override
	public Function<Boolean, Boolean> changeIsPermanent() {
		if(copiedEffect == null)
			return super.changeIsPermanent();
		else 
			return x -> copiedEffect.getEffectsArePermanent();
	}

	@Override
	public String toString() {
		if (copiedEffect != null)
			return copiedEffect.toString();
		else
			return "Copy the effect of an active leaderCard";
	}

}
