package it.polimi.ingsw.LM45.test.model.effects;

import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.model.effects.ChurchSupportBonusEffect;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;
import junit.framework.TestCase;
import testUtilities.FakeEffectResolutor;

public class ChurchSupportBonusEffectTest extends TestCase {

	public void testResolveEffect() {
		ChurchSupportBonusEffect churchSupportBonusEffect = new ChurchSupportBonusEffect(new Resource(ResourceType.MILITARY, 3));
		Player player = new Player("Test", PlayerColor.BLUE);
		EffectResolutor effectResolutor = new FakeEffectResolutor(player);
		
		assertTrue(churchSupportBonusEffect.canResolveEffect(effectResolutor));
		churchSupportBonusEffect.resolveEffect(effectResolutor);
		Resource[] churchSupportBonuses = player.getChurchSupportBonuses();
		assertEquals(churchSupportBonuses.length, 1);
		assertEquals(churchSupportBonuses[0].getAmount(), 3);
		assertEquals(churchSupportBonuses[0].getResourceType(), ResourceType.MILITARY);
	}

}
