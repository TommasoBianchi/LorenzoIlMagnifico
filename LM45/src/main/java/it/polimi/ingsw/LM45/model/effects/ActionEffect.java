package it.polimi.ingsw.LM45.model.effects;

import java.util.Arrays;

import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.model.effects.modifiers.ActionModifier;
import it.polimi.ingsw.LM45.model.effects.modifiers.ResourceAdder;
import it.polimi.ingsw.LM45.model.effects.modifiers.ResourceModifier;

public class ActionEffect extends Effect {

	private static final long serialVersionUID = 1L;

	private SlotType slotType;
	private int diceNumber;
	private Resource[] discount;
	private boolean isBonusAction;

	public ActionEffect(SlotType slotType, int diceNumber, Resource[] discount, boolean isBonusAction) {
		this.slotType = slotType;
		this.diceNumber = diceNumber;
		this.discount = discount;
		this.isBonusAction = isBonusAction;
	}

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
