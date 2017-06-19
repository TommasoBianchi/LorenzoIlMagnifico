package it.polimi.ingsw.LM45.model.effects;

import java.util.Arrays;

import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.SlotType;

public class ActionEffect extends Effect {

	private SlotType slotType;
	private int diceNumber;
	private Resource[] discount;

	public ActionEffect(SlotType slotType, int diceNumber, Resource[] discount) {
		this.slotType = slotType;
		this.diceNumber = diceNumber;
		this.discount = discount;
	}

	public ActionEffect(SlotType slotType, int diceNumber) {
		this(slotType, diceNumber, new Resource[] {});
	}

	@Override
	public void resolveEffect(EffectResolutor effectResolutor) {
		effectResolutor.doBonusAction(slotType, diceNumber, discount);
	}

	@Override
	public ActionModifier getActionModifier(SlotType slotType) {
		if (slotType == this.slotType)
			// Make sure the discount is expressed as a negative cost modifier (otherwise it won't be a discount)
			return new ActionModifier(
					Arrays.stream(discount).map(resource -> resource.getAmount() > 0 ? resource.multiply(-1) : resource).toArray(Resource[]::new),
					new Resource[] {}, diceNumber);
		else
			return ActionModifier.EMPTY;
	}

	@Override
	public String toString() {
		String discountString = "";
		if (discount.length > 0)
			discountString = Arrays.stream(discount).map(resource -> resource.toString()).reduce(" and a discount of ", (a, b) -> a + " " + b);
		return "You can do an action of type " + slotType.toString() + " with a value of " + diceNumber + discountString;
	}

}
