package it.polimi.ingsw.LM45.model.core;

import java.util.Arrays;

import it.polimi.ingsw.LM45.model.effects.EffectResolutor;

public class PersonalBonusTile {
	
	private static final int MIN_DICE = 1;

	private Resource[] productionBonuses;
	private Resource[] harvestBonuses;
	
	/**
	 * @param productionBonuses the base bonuses for production actions
	 * @param harvestBonuses the base bonuses for harvest actions
	 */
	public PersonalBonusTile(Resource[] productionBonuses, Resource[] harvestBonuses){
		this.productionBonuses = productionBonuses;
		this.harvestBonuses = harvestBonuses;
	}	
	
	/**
	 * @param effectResolutor the effectResolutor we need in order to resolve the production effects
	 * @param diceAmount the value of the production action
	 */
	public void harvest (EffectResolutor effectResolutor, int diceAmount){
		if(diceAmount >= MIN_DICE)
			Arrays.stream(harvestBonuses).forEach(resource -> effectResolutor.addResources(resource));
	}
	
	/**
	 * @param effectResolutor the effectResolutor we need in order to resolve the production effects
	 * @param diceAmount the value of the production action
	 */
	public void produce (EffectResolutor effectResolutor, int diceAmount){
		if(diceAmount >= MIN_DICE)
			Arrays.stream(productionBonuses).forEach(resource -> effectResolutor.addResources(resource));
	}
}
