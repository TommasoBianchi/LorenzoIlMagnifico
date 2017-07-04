package it.polimi.ingsw.LM45.test.model.cards;

import it.polimi.ingsw.LM45.model.cards.CostWithPrerequisites;
import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;
import it.polimi.ingsw.LM45.model.effects.modifiers.ActionModifier;
import junit.framework.TestCase;
import testUtilities.FakeEffectResolutor;

public class CostWithPrerequisitesTest extends TestCase {

	public void testCanPay() {
		CostWithPrerequisites costWithPrerequisites = new CostWithPrerequisites(new Resource[] { new Resource(ResourceType.MILITARY, 1) },
				new Resource[] { new Resource(ResourceType.MILITARY, 2) });
		EffectResolutor effectResolutor = new FakeEffectResolutor(new Player("Test", PlayerColor.BLUE));

		assertFalse(costWithPrerequisites.canPay(effectResolutor, ActionModifier.EMPTY()));
		effectResolutor.addResources(new Resource(ResourceType.MILITARY, 2));
		assertTrue(costWithPrerequisites.canPay(effectResolutor, ActionModifier.EMPTY()));
		costWithPrerequisites.pay(effectResolutor, ActionModifier.EMPTY());
		assertTrue(effectResolutor.hasResources(new Resource(ResourceType.MILITARY, 1)));
	}

}
