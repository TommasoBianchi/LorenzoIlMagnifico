package it.polimi.ingsw.LM45.model.cards;

import java.io.Serializable;

import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.effects.ActionModifier;
import it.polimi.ingsw.LM45.model.effects.CardEffect;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;

public abstract class Card implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	protected String name;
	private PeriodType periodType;
	protected CardType cardType;
	protected Cost cost;
	protected CardEffect immediateEffect;
	protected CardEffect effect;
	
	/**
	 * @param name name of the Card
	 * @param periodType period of the Card : I,II or III
	 * @param cost the resources player needs to pay ot to have to pick the Card
	 * @param immediateEffect the effect that is activated immediately when Card is picked
	 * @param effect the effect that is not activated immediately
	 */
	public Card(String name, PeriodType periodType, Cost cost, CardEffect immediateEffect, CardEffect effect){
		this.name = name;
		this.periodType = periodType;
		this.cost = cost;
		this.immediateEffect = immediateEffect;
		this.effect = effect;
	}
	
	/**
	 * @param player Player that want to pick the Card
	 * @param actionModifier the actionModifier for the action the player is trying to do
	 * @return True, if player has requisites and resources to pick the card
	 */
	public boolean canPick(Player player, ActionModifier actionModifier){
		return cost.canPay(player, actionModifier);
	}
	
	/**
	 * @param effectResolutor the effectResolutor of player that wants to pay the cost's resources
	 * @param actionModifier the actionModifier for the action the player is trying to do
	 */
	public void payCost(EffectResolutor effectResolutor, ActionModifier actionModifier){
		cost.pay(effectResolutor, actionModifier);
	}
	
	/**
	 * @return the Card name
	 */
	public String getName(){
		return this.name;
	}

	/**
	 * @return the Card Period : I, II or III
	 */
	public PeriodType getPeriodType() {
		return this.periodType;
	}
	
	/**
	 * @return the cardType of the Card : BUILDING, TERRITORY, CHARACTER or VENTURE
	 */
	public CardType getCardType(){
		return this.cardType;
	}
	
	/**
	 * Resolves ImmediateEffect and controls if effect is a permanentEffect
	 * If it's permanent it calls addPermanentEffect(effect), otherwise it does nothing
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
	 * @param diceValue min value of the action to resolve the effect
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
	
	@Override
	public String toString(){
		return name + " (" + cardType + " - " + periodType + ")\n" +
				"Cost: " + cost.toString() + "\n" +
				"Immediate Effect: " + immediateEffect.toString() + "\n" +
				"Effect: " + effect.toString();
	}
}
