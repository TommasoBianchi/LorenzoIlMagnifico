package it.polimi.ingsw.LM45.model.cards;

import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;
import it.polimi.ingsw.LM45.model.effects.modifiers.ActionModifier;
import it.polimi.ingsw.LM45.model.effects.modifiers.ResourceAdder;
import it.polimi.ingsw.LM45.model.effects.modifiers.ResourceModifier;
import junit.framework.TestCase;
import testUtilities.FakeEffectResolutor;

public class CostTest extends TestCase {

	public void testCost() {
		Cost cost = new Cost(new Resource[] { new Resource(ResourceType.COINS, 5), new Resource(ResourceType.WOOD, 2) });
		EffectResolutor effectResolutor = new FakeEffectResolutor(new Player("Test", PlayerColor.BLUE));

		assertFalse(cost.canPay(effectResolutor, ActionModifier.EMPTY()));
		assertTrue(cost.canPay(effectResolutor, new ActionModifier(new ResourceModifier[] { new ResourceAdder(new Resource(ResourceType.COINS, -5)),
				new ResourceAdder(new Resource(ResourceType.WOOD, -2)) })));
		assertFalse(cost.canPay(effectResolutor, new ActionModifier(new ResourceModifier[] { new ResourceAdder(new Resource(ResourceType.COINS, 3)),
				new ResourceAdder(new Resource(ResourceType.WOOD, -2)) })));
		effectResolutor.addResources(new Resource(ResourceType.COINS, 2));
		assertTrue(cost.canPay(effectResolutor, new ActionModifier(new ResourceModifier[] { new ResourceAdder(new Resource(ResourceType.COINS, -3)),
				new ResourceAdder(new Resource(ResourceType.WOOD, -2)) })));
		effectResolutor.addResources(new Resource(ResourceType.COINS, 3));
		effectResolutor.addResources(new Resource(ResourceType.WOOD, 2));
		assertTrue(cost.canPay(effectResolutor, ActionModifier.EMPTY()));
		cost.pay(effectResolutor, ActionModifier.EMPTY());
		assertFalse(cost.canPay(effectResolutor, ActionModifier.EMPTY()));
		effectResolutor.addResources(new Resource(ResourceType.COINS, 2));
		cost.pay(effectResolutor, new ActionModifier(new ResourceModifier[] { new ResourceAdder(new Resource(ResourceType.COINS, -4)),
				new ResourceAdder(new Resource(ResourceType.WOOD, -2)) }));
		assertTrue(effectResolutor.hasResources(new Resource(ResourceType.COINS, 1)));

	}

	public void testIsEmpty() {
		assertTrue(Cost.EMPTY.isEmpty());
		assertFalse(new Cost(new Resource[] { new Resource(ResourceType.COINS, 5) }).isEmpty());
		assertTrue(new Cost(new Resource[] {}).isEmpty());
	}

}
