package it.polimi.ingsw.LM45.model.effects;

import java.util.Arrays;

import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.model.effects.modifiers.ActionModifier;
import it.polimi.ingsw.LM45.model.effects.modifiers.ResourceAdder;
import it.polimi.ingsw.LM45.model.effects.modifiers.ResourceModifier;

/**
 * This is an effect granting a bonus action or some bonuses when performing certain actions
 * 
 * @author Tommy
 */
public class ActionEffect extends Effect {

	private static final long serialVersionUID = 1L;

	private SlotType slotType;
	private int diceNumber;
	private Resource[] discount;
	private boolean isBonusAction;

	/**
	 * @param slotType
	 *            the slotType of the slot on which this effect can be applied
	 * @param diceNumber
	 *            the value of the dice in the case of a bonus action or the bonus to the value of the dice in case of a bonus on an action
	 * @param discount
	 *            the resource discount granted
	 * @param isBonusAction
	 *            true if this effect grants a bonus action, false if it represents a bonus on some other actions
	 */
	public ActionEffect(SlotType slotType, int diceNumber, Resource[] discount, boolean isBonusAction) {
		this.slotType = slotType;
		this.diceNumber = diceNumber;
		this.discount = discount;
		this.isBonusAction = isBonusAction;
	}

	/**
	 * Instantiate an ActionEffect with no discounts
	 * 
	 * @param slotType the slotType of the slot on which this effect can be applied
	 * @param diceNumber the value of the dice in the case of a bonus action or the bonus to the value of the dice in case of a bonus on an action
	 * @param isBonusAction true if this effect grants a bonus action, false if it represents a bonus on some other actions
	 */
	public ActionEffect(SlotType slotType, int diceNumber, boolean isBonusAction) {
		this(slotType, diceNumber, new Resource[] {}, isBonusAction);
	}

	@Override
	public void resolveEffect(EffectResolutor effectResolutor) {
		effectResolutor.doBonusAction(slotType, diceNumber, discount);
	}

	@Override
	public ActionModifier getActionModifier(SlotType slotType) {
		if (slotType.isCompatible(this.slotType))
			// Make sure the discount is expressed as a negative cost modifier (otherwise it won't be a discount)
			return new ActionModifier(Arrays.stream(discount).map(resource -> resource.getAmount() > 0 ? resource.multiply(-1) : resource)
					.map(ResourceAdder::new).toArray(ResourceModifier[]::new), new ResourceModifier[] {}, diceNumber);
		else
			return ActionModifier.EMPTY();
	}

	@Override
	public String toString() {
		String discountString = "";
		if (discount.length > 0)
			discountString = Arrays.stream(discount).map(Resource::toString).reduce(" and a discount of ", (a, b) -> a + " " + b);
		if (isBonusAction)
			return "You can do an action of type " + slotType.toString() + " with a value of " + diceNumber + discountString;
		else {
			String sign = diceNumber > 0 ? "+" : "-";
			return "Whenever you do an action of type " + slotType.toString() + ", you receive a bonus of " + sign + Math.abs(diceNumber)
					+ " to the value of the familiar " + discountString;
		}
	}

}
