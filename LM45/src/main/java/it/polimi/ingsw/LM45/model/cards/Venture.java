package it.polimi.ingsw.LM45.model.cards;

import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.effects.ActionModifier;
import it.polimi.ingsw.LM45.model.effects.CardEffect;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;

public class Venture extends Card {
	
	private static final long serialVersionUID = 1L;
	
	private Cost alternativeCost;

	/**
	 * @param name name of the Card
	 * @param periodType period of the Card : I,II or III
	 * @param cost the resources player needs to pay or to have to pick the Card
	 * @param immediateEffects the effect that is activated immediately when Card is picked
	 * @param effects the effect that is not activated immediately
	 * @param alternativeCost alternative resources player can choose to pay
	 * instead of the cost's resources
	 */
	public Venture(String name, PeriodType periodType, Cost cost, CardEffect immediateEffects,
			CardEffect effects, Cost alternativeCost) {
		super(name, periodType, cost, immediateEffects, effects);
		this.alternativeCost = alternativeCost;
		this.cardType = CardType.VENTURE;
	}
	
	/**
	 * @param name name of the Card
	 * @param periodType period of the Card : I,II or III
	 * @param cost the resources player needs to pay or to have to pick the Card
	 * @param immediateEffects the effect that is activated immediately when Card is picked
	 * @param effects the effect that is not activated immediately
	 */
	public Venture(String name, PeriodType periodType, Cost cost, CardEffect immediateEffects,
			CardEffect effects) {
		this(name, periodType, cost, immediateEffects, effects, Cost.EMPTY);
	}

	@Override
	public boolean canPick(Player player, ActionModifier actionModifier) {
		return super.canPick(player, actionModifier) || alternativeCost.canPay(player, actionModifier);
	}

	@Override
	public void payCost(EffectResolutor effectResolutor, ActionModifier actionModifier){
		if(alternativeCost.isEmpty())
			super.payCost(effectResolutor, actionModifier);
		else {
			Cost chosenCost = effectResolutor.chooseFrom(new Cost[]{ cost, alternativeCost });
			chosenCost.pay(effectResolutor, actionModifier);
		}
	}
	
	@Override
	public String toString(){
		String result = super.toString();
		if(!alternativeCost.isEmpty())
			result += "\nAlternative cost: " + alternativeCost.toString();
		return result;
	}

}
