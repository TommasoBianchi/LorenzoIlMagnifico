package it.polimi.ingsw.LM45.model.effects;

public class CopyEffect extends Effect {

	private CardEffect copiedEffect;
	
	@Override
	public void resolveEffect(EffectResolutor effectResolutor) {
		if(copiedEffect != null)
			copiedEffect = effectResolutor.copyEffect();
		copiedEffect.resolveEffects(effectResolutor);
	}

}
