package it.polimi.ingsw.LM45.test.model.cards;

import it.polimi.ingsw.LM45.model.cards.Cost;
import it.polimi.ingsw.LM45.model.cards.CostWithPrerequisites;
import it.polimi.ingsw.LM45.model.cards.PeriodType;
import it.polimi.ingsw.LM45.model.cards.Venture;
import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.model.effects.CardEffect;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;
import it.polimi.ingsw.LM45.model.effects.modifiers.ActionModifier;
import junit.framework.TestCase;
import testUtilities.FakeEffectResolutor;

public class VentureTest extends TestCase {

	public void testCanPick() {
		Venture venture1 = new Venture("1", PeriodType.I, new Cost(new Resource[] { new Resource(ResourceType.STONE, 2) }), CardEffect.EMPTY,
				CardEffect.EMPTY, new CostWithPrerequisites(new Resource[] { new Resource(ResourceType.MILITARY, 1) },
						new Resource[] { new Resource(ResourceType.MILITARY, 2) }));
		Venture venture2 = new Venture("2", PeriodType.I, new CostWithPrerequisites(new Resource[] { new Resource(ResourceType.MILITARY, 1) },
				new Resource[] { new Resource(ResourceType.MILITARY, 2) }), CardEffect.EMPTY, CardEffect.EMPTY);
		EffectResolutor effectResolutor = new FakeEffectResolutor(new Player("Test", PlayerColor.BLUE));
		
		assertFalse(venture1.canPick(effectResolutor, ActionModifier.EMPTY()));
		assertFalse(venture2.canPick(effectResolutor, ActionModifier.EMPTY()));
		effectResolutor.addResources(new Resource(ResourceType.STONE, 2));
		assertTrue(venture1.canPick(effectResolutor, ActionModifier.EMPTY()));
		assertFalse(venture2.canPick(effectResolutor, ActionModifier.EMPTY()));
		effectResolutor.addResources(new Resource(ResourceType.MILITARY, 2));
		assertTrue(venture1.canPick(effectResolutor, ActionModifier.EMPTY()));
		assertTrue(venture2.canPick(effectResolutor, ActionModifier.EMPTY()));
	}

	public void testPayCost() {
		Venture venture1 = new Venture("1", PeriodType.I, new Cost(new Resource[] { new Resource(ResourceType.STONE, 2) }), CardEffect.EMPTY,
				CardEffect.EMPTY, new CostWithPrerequisites(new Resource[] { new Resource(ResourceType.MILITARY, 1) },
						new Resource[] { new Resource(ResourceType.MILITARY, 2) }));
		Venture venture2 = new Venture("2", PeriodType.I, new CostWithPrerequisites(new Resource[] { new Resource(ResourceType.MILITARY, 1) },
				new Resource[] { new Resource(ResourceType.MILITARY, 2) }), CardEffect.EMPTY, CardEffect.EMPTY);
		EffectResolutor effectResolutor = new FakeEffectResolutor(new Player("Test", PlayerColor.BLUE));
		
		effectResolutor.addResources(new Resource(ResourceType.STONE, 2));
		venture1.payCost(effectResolutor, ActionModifier.EMPTY());
		assertEquals(effectResolutor.getResourceAmount(ResourceType.STONE), 0);
		
		effectResolutor.addResources(new Resource(ResourceType.MILITARY, 2));
		venture1.payCost(effectResolutor, ActionModifier.EMPTY());
		assertEquals(effectResolutor.getResourceAmount(ResourceType.MILITARY), 1);
		
		effectResolutor.addResources(new Resource(ResourceType.STONE, 2));
		effectResolutor.addResources(new Resource(ResourceType.MILITARY, 2));
		venture1.payCost(effectResolutor, ActionModifier.EMPTY());
		assertTrue(effectResolutor.hasResources(new Resource(ResourceType.STONE, 2)) || effectResolutor.hasResources(new Resource(ResourceType.MILITARY, 2)));
		
		venture2.payCost(effectResolutor, ActionModifier.EMPTY());
		assertTrue(effectResolutor.hasResources(new Resource(ResourceType.STONE, 2)) || effectResolutor.hasResources(new Resource(ResourceType.MILITARY, 1)));
	}

}
