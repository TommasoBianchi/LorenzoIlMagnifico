package it.polimi.ingsw.LM45.model.effects;

public class CopyEffect extends Effect {

	private static final long serialVersionUID = 1L;

	private CardEffect copiedEffect;
	
	@Override
	public void resolveEffect(EffectResolutor effectResolutor) {
		if(copiedEffect == null)
			copiedEffect = effectResolutor.copyEffect();
		copiedEffect.resolveEffects(effectResolutor);
	}
	
	@Override
	public String toString() {
		if(copiedEffect != null)
			return copiedEffect.toString();
		else
			return "Copy the effect of an active leaderCard";
	}

}
