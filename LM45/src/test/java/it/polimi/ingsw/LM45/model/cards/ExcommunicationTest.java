package it.polimi.ingsw.LM45.model.cards;

import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.model.effects.CardEffect;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;
import it.polimi.ingsw.LM45.model.effects.GainModifierEffect;
import it.polimi.ingsw.LM45.model.effects.ResourceEffect;
import junit.framework.TestCase;
import testUtilities.FakeEffectResolutor;

public class ExcommunicationTest extends TestCase {

	public void testGetPeriodType() {
		Excommunication excommunication = new Excommunication("", PeriodType.I, CardEffect.EMPTY);
		assertEquals(excommunication.getPeriodType(), PeriodType.I);
	}

	public void testGetName() {
		Excommunication excommunication = new Excommunication("Name", PeriodType.I, CardEffect.EMPTY);
		assertEquals(excommunication.getName(), "Name");
	}

	public void testResolveEffect() {
		Excommunication excommunication1 = new Excommunication("1", PeriodType.I,
				new CardEffect(new GainModifierEffect(new Resource(ResourceType.MILITARY, -1), false), true));
		Excommunication excommunication2 = new Excommunication("2", PeriodType.III, new CardEffect(
				new ResourceEffect(new Resource(ResourceType.MILITARY, 1), new Resource[] { new Resource(ResourceType.VICTORY, -1) })));
		EffectResolutor effectResolutor = new FakeEffectResolutor(new Player("Test", PlayerColor.BLUE));
		
		excommunication1.resolveEffect(effectResolutor);
		effectResolutor.addResources(new Resource(ResourceType.MILITARY, 3));
		effectResolutor.addResources(new Resource(ResourceType.STONE, 2));
		effectResolutor.addResources(new Resource(ResourceType.MILITARY, 4));
		effectResolutor.addResources(new Resource(ResourceType.COINS, 6));
		assertEquals(effectResolutor.getResourceAmount(ResourceType.MILITARY), 5);
		assertEquals(effectResolutor.getResourceAmount(ResourceType.STONE), 2);
		assertEquals(effectResolutor.getResourceAmount(ResourceType.COINS), 6);
		
		effectResolutor.addResources(new Resource(ResourceType.VICTORY, 25));
		excommunication2.resolveEffect(effectResolutor);
		assertEquals(effectResolutor.getResourceAmount(ResourceType.VICTORY), 20);
	}

}
