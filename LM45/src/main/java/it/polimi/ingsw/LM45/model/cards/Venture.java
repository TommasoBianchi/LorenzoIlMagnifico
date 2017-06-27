package it.polimi.ingsw.LM45.model.cards;

import it.polimi.ingsw.LM45.model.effects.ActionModifier;
import it.polimi.ingsw.LM45.model.effects.CardEffect;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;

public class Venture extends Card {

	private static final long serialVersionUID = 1L;

	private Cost alternativeCost;

	/**
	 * @param name
	 *            name of the Card
	 * @param periodType
	 *            period of the Card : I,II or III
	 * @param cost
	 *            the resources player needs to pay or to have to pick the Card
	 * @param immediateEffects
	 *            the effect that is activated immediately when Card is picked
	 * @param effects
	 *            the effect that is not activated immediately
	 * @param alternativeCost
	 *            alternative resources player can choose to pay instead of the cost's resources
	 */
	public Venture(String name, PeriodType periodType, Cost cost, CardEffect immediateEffects, CardEffect effects, Cost alternativeCost) {
		super(name, periodType, cost, immediateEffects, effects);
		this.alternativeCost = alternativeCost;
		this.cardType = CardType.VENTURE;
	}

	/**
	 * @param name
	 *            name of the Card
	 * @param periodType
	 *            period of the Card : I,II or III
	 * @param cost
	 *            the resources player needs to pay or to have to pick the Card
	 * @param immediateEffects
	 *            the effect that is activated immediately when Card is picked
	 * @param effects
	 *            the effect that is not activated immediately
	 */
	public Venture(String name, PeriodType periodType, Cost cost, CardEffect immediateEffects, CardEffect effects) {
		this(name, periodType, cost, immediateEffects, effects, Cost.EMPTY);
	}

	@Override
	public boolean canPick(EffectResolutor effectResolutor, ActionModifier actionModifier) {
		return super.canPick(effectResolutor, actionModifier)
				|| (!alternativeCost.isEmpty() && alternativeCost.canPay(effectResolutor, actionModifier));
	}

	@Override
	public void payCost(EffectResolutor effectResolutor, ActionModifier actionModifier) {
		if (alternativeCost.isEmpty())
			super.payCost(effectResolutor, actionModifier);
		else {
			Cost chosenCost = cost;
			System.out.println(cost);
			System.out.println(alternativeCost);
			// Make player choose a cost only if he can afford both cost and alternativeCost
			if (cost.canPay(effectResolutor, actionModifier) && alternativeCost.canPay(effectResolutor, actionModifier))
				chosenCost = effectResolutor.chooseFrom(new Cost[] { cost, alternativeCost });
			else if (alternativeCost.canPay(effectResolutor, actionModifier))
				chosenCost = alternativeCost;
			chosenCost.pay(effectResolutor, actionModifier);
		}
	}

	@Override
	public String toString() {
		String result = super.toString();
		if (!alternativeCost.isEmpty())
			result += "\nAlternative cost: " + alternativeCost.toString();
		return result;
	}

}
