package it.polimi.ingsw.LM45.test.model.effects;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.model.effects.CostModifierEffect;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;
import testUtilities.FakeEffectResolutor;

public class CostModifierEffectTest {

	@Test
	public void testResolveEffect() {
		CostModifierEffect costModifierEffect = new CostModifierEffect(true);
		Player player = new Player("Test", PlayerColor.BLUE);
		EffectResolutor effectResolutor = new FakeEffectResolutor(player);
		
		costModifierEffect.resolveEffect(effectResolutor);
		assertFalse(player.getPayIfTowerIsOccupied());
		assertTrue(costModifierEffect.getActionModifier(SlotType.ANY).isEmpty());
	}

	@Test
	public void testGetActionModifier() {
		CostModifierEffect costModifierEffect1 = new CostModifierEffect(new Resource(ResourceType.COINS, 2), false);
		CostModifierEffect costModifierEffect2 = new CostModifierEffect(new Resource(ResourceType.COINS, 2), true);
		
		Resource baseCost = new Resource(ResourceType.COINS, 3);
		assertEquals(costModifierEffect1.getActionModifier(SlotType.ANY).getCostModifiers().get(ResourceType.COINS).modify(baseCost).getAmount(), 5);
		assertEquals(costModifierEffect2.getActionModifier(SlotType.ANY).getCostModifiers().get(ResourceType.COINS).modify(baseCost).getAmount(), 6);
	}

}
