package it.polimi.ingsw.LM45.test.model.effects;

import java.util.Map;

import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.model.effects.ActionEffect;
import it.polimi.ingsw.LM45.model.effects.CardEffect;
import it.polimi.ingsw.LM45.model.effects.Effect;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;
import it.polimi.ingsw.LM45.model.effects.ResourceEffect;
import it.polimi.ingsw.LM45.model.effects.modifiers.ResourceModifier;
import junit.framework.TestCase;
import testUtilities.FakeEffectResolutor;

public class CardEffectTest extends TestCase {

	public void testResolveEffects() {
		CardEffect cardEffect1 = new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.COINS, 4) }));
		CardEffect cardEffect2 = new CardEffect(new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.STONE, 4) }),
				new ResourceEffect(new Resource[] { new Resource(ResourceType.WOOD, 4) }),
				new ResourceEffect(new Resource[] { new Resource(ResourceType.SERVANTS, 4) }) }, true);
		EffectResolutor effectResolutor = new FakeEffectResolutor(new Player("Test", PlayerColor.BLUE));

		cardEffect1.resolveEffects(effectResolutor);
		assertTrue(effectResolutor.hasResources(new Resource(ResourceType.COINS, 4)));

		cardEffect2.resolveEffects(effectResolutor);
		assertTrue(
				effectResolutor.hasResources(new Resource(ResourceType.WOOD, 4)) || effectResolutor.hasResources(new Resource(ResourceType.STONE, 4))
						|| effectResolutor.hasResources(new Resource(ResourceType.SERVANTS, 4)));
	}

	public void testGetActionModifier() {
		CardEffect cardEffect1 = new CardEffect(new ActionEffect(SlotType.BUILDING, 2, false), true);
		CardEffect cardEffect2 = new CardEffect(
				new Effect[] { new ActionEffect(SlotType.TERRITORY, 2, new Resource[] { new Resource(ResourceType.STONE, 1) }, false),
						new ActionEffect(SlotType.TERRITORY, 2, new Resource[] { new Resource(ResourceType.WOOD, 1) }, false) },
				true, true);
		EffectResolutor effectResolutor = new FakeEffectResolutor(new Player("Test", PlayerColor.BLUE));

		effectResolutor.addPermanentEffect(cardEffect1);
		assertEquals(cardEffect1.getActionModifier(SlotType.BUILDING, effectResolutor).getDiceBonus(), 2);
		assertTrue(cardEffect1.getActionModifier(SlotType.MARKET, effectResolutor).isEmpty());

		effectResolutor.addPermanentEffect(cardEffect2);
		assertEquals(cardEffect2.getActionModifier(SlotType.TERRITORY, effectResolutor).getDiceBonus(), 2);
		Map<ResourceType, ResourceModifier> costModifiers = cardEffect2.getActionModifier(SlotType.TERRITORY, effectResolutor).getCostModifiers();
		assertTrue(costModifiers.containsKey(ResourceType.WOOD) || costModifiers.containsKey(ResourceType.STONE));
		assertTrue(cardEffect2.getActionModifier(SlotType.MARKET, effectResolutor).isEmpty());

	}

}
