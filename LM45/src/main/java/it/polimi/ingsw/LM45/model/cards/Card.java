package it.polimi.ingsw.LM45.model.cards;

import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.effects.ActionModifier;
import it.polimi.ingsw.LM45.model.effects.CardEffect;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;

public abstract class Card {

	protected String name;
	private PeriodType periodType;
	protected CardType cardType;
	protected Cost cost;
	protected CardEffect immediateEffect;
	protected CardEffect effect;
	
	public Card(String name, PeriodType periodType, Cost cost, CardEffect immediateEffect, CardEffect effect){
		this.name = name;
		this.periodType = periodType;
		this.cost = cost;
		this.immediateEffect = immediateEffect;
		this.effect = effect;
	}
	
	/**
	 * @param player Player that want to pick the Card
	 * @param actionModifier Collection of effect's attributes of other Cards or Malus that player owns
	 * that influence this Card attributes
	 * @return True, if player has requisites and resources to pick the card
	 */
	public boolean canPick(Player player, ActionModifier actionModifier){
		return cost.canPay(player, actionModifier);
	}
	
	/**
	 * @param player player that what to pay the cost's resources
	 * @param actionModifier Collection of effect's attributes of other Cards or Malus that player owns
	 * that influence this Card attributes
	 */
	public void payCost(Player player, ActionModifier actionModifier){
		cost.pay(player, actionModifier);
	}
	
	/**
	 * @return the Card name.
	 */
	public String getName(){
		return this.name;
	}

	/**
	 * @return the Card Period : I, II or III.
	 */
	public PeriodType getPeriodType() {
		return this.periodType;
	}
	
	/**
	 * @return the cardType of the Card : BUILDING, TERRITORY, CHARACTER or VENTURE.
	 */
	public CardType getCardType(){
		return this.cardType;
	}
	
	/**
	 * Resolves ImmediateEffect and controls if effect is a permanentEffect.
	 * If it's permanent it calls addPermanentEffect(effect), otherwise it means
	 * it's a BUILDING or TERRITORY card with a resourceEffect that needs to be activated
	 * only by a production or harvest action.
	 * 
	 * @param effectResolutor interface with all methods that model can use to call the EffectController
	 */
	public void resolveImmediateEffect(EffectResolutor effectResolutor){
		immediateEffect.resolveEffects(effectResolutor);
		if(effect.getEffectsArePermanent())
			effectResolutor.addPermanentEffect(effect);
	}
	
	/**
	 * @param effectResolutor interface with all methods that model can use to call the EffectController
	 * @param diceValue value of harvest and produce action
	 */
	public void resolveEffect(EffectResolutor effectResolutor, int diceValue){
		effect.resolveEffects(effectResolutor);
	}
	
	/**
	 * @param effectResolutor interface with all methods that model can use to call the EffectController
	 */
	public void resolveEffect(EffectResolutor effectResolutor){
		resolveEffect(effectResolutor, 0);
	}
}
