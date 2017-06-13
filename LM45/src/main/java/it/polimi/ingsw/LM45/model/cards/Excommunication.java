package it.polimi.ingsw.LM45.model.cards;

import it.polimi.ingsw.LM45.model.effects.CardEffect;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;

public class Excommunication {

	private PeriodType periodType;
	private CardEffect effect;
	
	public PeriodType getPeriodType(){
		return this.periodType;
	}
	
	public void resolveEffect(EffectResolutor effectResolutor){
		if(periodType != PeriodType.III)
			effectResolutor.addPermanentEffect(effect);
		else
			effect.resolveEffects(effectResolutor);
	}
	
}
