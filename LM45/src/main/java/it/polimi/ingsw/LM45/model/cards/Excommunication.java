package it.polimi.ingsw.LM45.model.cards;

import it.polimi.ingsw.LM45.model.effects.CardEffect;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;

public class Excommunication {

	private String name;
	private PeriodType periodType;
	private CardEffect effect;
	
	public Excommunication(String name, PeriodType periodType, CardEffect effect){
		this.name = name;
		this.periodType = periodType;
		this.effect = effect;
	}
	
	/**
	 * @return the period: I, II or III
	 */
	public PeriodType getPeriodType(){
		return this.periodType;
	}
	
	/**
	 * @return excommunication name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * @param effectResolutor interface with all methods that model can use to call the EffectController
	 */
	public void resolveEffect(EffectResolutor effectResolutor){
		if(periodType != PeriodType.III)
			effectResolutor.addPermanentEffect(effect);
		else
			effect.resolveEffects(effectResolutor);
	}
	
}
