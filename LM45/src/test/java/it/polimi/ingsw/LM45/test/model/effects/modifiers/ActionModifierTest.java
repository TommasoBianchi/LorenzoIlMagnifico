package it.polimi.ingsw.LM45.test.model.effects.modifiers;

import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.model.effects.modifiers.ActionModifier;
import it.polimi.ingsw.LM45.model.effects.modifiers.ResourceAdder;
import it.polimi.ingsw.LM45.model.effects.modifiers.ResourceModifier;
import it.polimi.ingsw.LM45.model.effects.modifiers.ResourceMultiplier;
import junit.framework.TestCase;
import testUtilities.Helper;

public class ActionModifierTest extends TestCase {

	public void testMerge() {
		ActionModifier actionModifier1 = new ActionModifier(new ResourceModifier[] { new ResourceAdder(new Resource(ResourceType.COINS, 2)) },
				new ResourceModifier[] { new ResourceMultiplier(new Resource(ResourceType.MILITARY, 2)) }, 2, false, true, true);
		ActionModifier actionModifier2 = new ActionModifier(new ResourceModifier[] { new ResourceMultiplier(new Resource(ResourceType.WOOD, 2)) },
				new ResourceModifier[] { new ResourceAdder(new Resource(ResourceType.MILITARY, 2)) }, -1, true, false, false);
		
		assertTrue(ActionModifier.EMPTY().isEmpty());
		assertFalse(actionModifier1.isEmpty());
		assertFalse(actionModifier2.isEmpty());
		
		ActionModifier merge1 = actionModifier1.merge(ActionModifier.EMPTY());
		assertFalse(merge1.isEmpty());
		assertEquals(merge1.getCostModifiers().size(), 1);
		assertEquals(merge1.getCostModifiers().get(ResourceType.COINS).modify(new Resource(ResourceType.COINS, 1)).getAmount(), 3);
		assertEquals(merge1.getGainModifiers().size(), 1);
		assertEquals(merge1.getGainModifiers().get(ResourceType.MILITARY).modify(new Resource(ResourceType.MILITARY, 1)).getAmount(), 2);
		assertEquals(merge1.getDiceBonus(), 2);
		assertFalse(merge1.getBlockImmediateResources());
		assertTrue(merge1.getCanPlaceFamiliars());
		assertTrue(merge1.getCanPlaceMultipleFamiliars());
		
		ActionModifier merge2 = actionModifier1.merge(actionModifier2);
		assertFalse(merge2.isEmpty());
		assertEquals(merge2.getCostModifiers().size(), 2);
		assertEquals(merge2.getCostModifiers().get(ResourceType.COINS).modify(new Resource(ResourceType.COINS, 1)).getAmount(), 3);
		assertEquals(merge2.getCostModifiers().get(ResourceType.WOOD).modify(new Resource(ResourceType.WOOD, 1)).getAmount(), 2);
		assertEquals(merge2.getGainModifiers().size(), 1);
		assertEquals(merge2.getGainModifiers().get(ResourceType.MILITARY).modify(new Resource(ResourceType.MILITARY, 1)).getAmount(), 6);
		assertEquals(merge2.getDiceBonus(), 1);
		assertTrue(merge2.getBlockImmediateResources());
		assertFalse(merge2.getCanPlaceFamiliars());
		assertTrue(merge2.getCanPlaceMultipleFamiliars());
	}

	public void testToString() {
		ActionModifier actionModifier1 = new ActionModifier(new ResourceModifier[] { new ResourceAdder(new Resource(ResourceType.COINS, 2)) },
				new ResourceModifier[] { new ResourceMultiplier(new Resource(ResourceType.MILITARY, 2)) }, 2, false, true, true);
		ActionModifier actionModifier2 = new ActionModifier(new ResourceModifier[] { new ResourceMultiplier(new Resource(ResourceType.WOOD, 2)) },
				new ResourceModifier[] { new ResourceAdder(new Resource(ResourceType.MILITARY, 2)) }, -1, true, false, false);
		
		Helper.classImplementToString(actionModifier1);
		Helper.classImplementToString(actionModifier2);
		Helper.classImplementToString(ActionModifier.EMPTY());
	}

}
