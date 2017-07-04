package it.polimi.ingsw.LM45.test.model.effects;

import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;
import it.polimi.ingsw.LM45.model.effects.ResourceEffect;
import junit.framework.TestCase;
import testUtilities.FakeEffectResolutor;

public class ResourceEffectTest extends TestCase {

	public void testCanResolveEffect() {
		ResourceEffect resourceEffect1 = new ResourceEffect(
				new Resource[] { new Resource(ResourceType.WOOD, 1), new Resource(ResourceType.STONE, 2) });
		ResourceEffect resourceEffect2 = new ResourceEffect(new Resource(ResourceType.STONE, 2),
				new Resource[] { new Resource(ResourceType.COINS, 3), new Resource(ResourceType.MILITARY, 1) });
		ResourceEffect resourceEffect3 = new ResourceEffect(new Resource[] { new Resource(ResourceType.COINS, 1) },
				new Resource[] { new Resource(ResourceType.SERVANTS, 2) });
		EffectResolutor effectResolutor = new FakeEffectResolutor(new Player("Test", PlayerColor.BLUE));
		
		assertTrue(resourceEffect1.canResolveEffect(effectResolutor));
		assertTrue(resourceEffect2.canResolveEffect(effectResolutor));
		assertFalse(resourceEffect3.canResolveEffect(effectResolutor));
		
		resourceEffect1.resolveEffect(effectResolutor);
		assertEquals(effectResolutor.getResourceAmount(ResourceType.WOOD), 1);
		assertEquals(effectResolutor.getResourceAmount(ResourceType.STONE), 2);
		resourceEffect2.resolveEffect(effectResolutor);
		assertEquals(effectResolutor.getResourceAmount(ResourceType.COINS), 3);
		assertEquals(effectResolutor.getResourceAmount(ResourceType.MILITARY), 1);
		resourceEffect3.resolveEffect(effectResolutor);
		assertEquals(effectResolutor.getResourceAmount(ResourceType.COINS), 2);
		assertEquals(effectResolutor.getResourceAmount(ResourceType.SERVANTS), 2);
		resourceEffect3.resolveEffect(effectResolutor);
		assertEquals(effectResolutor.getResourceAmount(ResourceType.COINS), 1);
		assertEquals(effectResolutor.getResourceAmount(ResourceType.SERVANTS), 4);
		resourceEffect3.resolveEffect(effectResolutor);
		assertEquals(effectResolutor.getResourceAmount(ResourceType.COINS), 0);
		assertEquals(effectResolutor.getResourceAmount(ResourceType.SERVANTS), 6);
		assertFalse(resourceEffect3.canResolveEffect(effectResolutor));
	}

}
