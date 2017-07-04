package it.polimi.ingsw.LM45.test.model.effects.modifiers;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.model.effects.modifiers.NilModifier;
import it.polimi.ingsw.LM45.model.effects.modifiers.ResourceAdder;
import it.polimi.ingsw.LM45.model.effects.modifiers.ResourceAdderMultiplier;
import it.polimi.ingsw.LM45.model.effects.modifiers.ResourceModifier;
import it.polimi.ingsw.LM45.model.effects.modifiers.ResourceMultiplier;

public class ResourceModifierTest {

	@Test
	public void testModify() {
		ResourceAdder resourceAdder = new ResourceAdder(new Resource(ResourceType.COINS, 2));
		ResourceMultiplier resourceMultiplier = new ResourceMultiplier(new Resource(ResourceType.COINS, 2));
		ResourceAdderMultiplier resourceAdderMultiplier = new ResourceAdderMultiplier(ResourceType.COINS, resourceAdder, resourceMultiplier);
		NilModifier nilModifier = new NilModifier(ResourceType.COINS);

		assertEquals(resourceAdder.modify(new Resource(ResourceType.WOOD, 1)).getAmount(), 1);
		assertEquals(resourceAdder.modify(new Resource(ResourceType.COINS, 1)).getAmount(), 3);

		assertEquals(resourceMultiplier.modify(new Resource(ResourceType.WOOD, 1)).getAmount(), 1);
		assertEquals(resourceMultiplier.modify(new Resource(ResourceType.COINS, 1)).getAmount(), 2);

		assertEquals(resourceAdderMultiplier.modify(new Resource(ResourceType.WOOD, 1)).getAmount(), 1);
		assertEquals(resourceAdderMultiplier.modify(new Resource(ResourceType.COINS, 1)).getAmount(), 6);
		
		assertEquals(nilModifier.modify(new Resource(ResourceType.WOOD, 1)).getAmount(), 1);
		assertEquals(nilModifier.modify(new Resource(ResourceType.COINS, 1)).getAmount(), 1);
	}

	@Test
	public void testMerge() {
		ResourceModifier resourceAdder1 = new ResourceAdder(new Resource(ResourceType.COINS, 2));
		ResourceModifier resourceAdder2 = new ResourceAdder(new Resource(ResourceType.COINS, 1));
		ResourceModifier resourceAdder3 = new ResourceAdder(new Resource(ResourceType.WOOD, 1));
		ResourceModifier resourceMultiplier1 = new ResourceMultiplier(new Resource(ResourceType.COINS, 3));
		ResourceModifier resourceMultiplier2 = new ResourceMultiplier(new Resource(ResourceType.COINS, 2));
		ResourceModifier resourceMultiplier3 = new ResourceMultiplier(new Resource(ResourceType.WOOD, 2));
		NilModifier nilModifier = new NilModifier(ResourceType.COINS);

		assertEquals(resourceAdder1.merge(resourceAdder2).modify(new Resource(ResourceType.COINS, 1)).getAmount(), 4);
		assertEquals(resourceAdder1.merge(resourceAdder3).modify(new Resource(ResourceType.COINS, 1)).getAmount(), 3);
		assertEquals(resourceMultiplier1.merge(resourceMultiplier2).modify(new Resource(ResourceType.COINS, 1)).getAmount(), 6);
		assertEquals(resourceMultiplier1.merge(resourceMultiplier3).modify(new Resource(ResourceType.COINS, 1)).getAmount(), 3);
		assertEquals(resourceAdder1.merge(resourceMultiplier1).modify(new Resource(ResourceType.COINS, 1)).getAmount(), 9);
		assertEquals(resourceMultiplier1.merge(resourceAdder1).modify(new Resource(ResourceType.COINS, 1)).getAmount(), 9);
		
		assertEquals(nilModifier.merge(resourceAdder1).modify(new Resource(ResourceType.COINS, 1)).getAmount(), 3);
		assertEquals(nilModifier.merge(resourceMultiplier1).modify(new Resource(ResourceType.COINS, 1)).getAmount(), 3);
		assertEquals(nilModifier.merge(resourceAdder1.merge(resourceMultiplier1)).modify(new Resource(ResourceType.COINS, 1)).getAmount(), 9);
		assertEquals(resourceAdder1.merge(nilModifier).modify(new Resource(ResourceType.COINS, 1)).getAmount(), 3);
		assertEquals(resourceMultiplier1.merge(nilModifier).modify(new Resource(ResourceType.COINS, 1)).getAmount(), 3);
		assertEquals(resourceAdder1.merge(resourceMultiplier1).merge(nilModifier).modify(new Resource(ResourceType.COINS, 1)).getAmount(), 9);
		assertEquals(nilModifier.merge(nilModifier).modify(new Resource(ResourceType.COINS, 1)).getAmount(), 1);
		
		assertEquals(resourceAdder1.merge(resourceMultiplier1).merge(nilModifier).merge(resourceAdder2).merge(resourceMultiplier2)
				.modify(new Resource(ResourceType.COINS, 1)).getAmount(), 24);
	}

}
