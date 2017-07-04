package it.polimi.ingsw.LM45.test.model.effects;

import java.util.Arrays;

import it.polimi.ingsw.LM45.exceptions.IllegalActionException;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;
import it.polimi.ingsw.LM45.model.effects.FamiliarEffect;
import junit.framework.TestCase;
import testUtilities.FakeEffectResolutor;

public class FamiliarEffectTest extends TestCase {

	public void testResolveEffect() throws IllegalActionException {
		FamiliarEffect familiarEffect1 = new FamiliarEffect(2, true, FamiliarColor.values(), 1);
		FamiliarEffect familiarEffect2 = new FamiliarEffect(5, false, new FamiliarColor[]{ FamiliarColor.BLACK }, 1);
		FamiliarEffect familiarEffect3 = new FamiliarEffect(0, true, FamiliarColor.values(), 2);
		Player player = new Player("Test", PlayerColor.BLUE);
		EffectResolutor effectResolutor = new FakeEffectResolutor(player);
		
		Arrays.stream(player.getFamiliars()).forEach(familiar -> familiar.setValue(2));
		familiarEffect1.resolveEffect(effectResolutor);
		assertTrue(Arrays.stream(player.getFamiliars()).allMatch(familiar -> familiar.getValue() == 4));
		
		familiarEffect2.resolveEffect(effectResolutor);
		assertEquals(player.getFamiliarByColor(FamiliarColor.BLACK).getValue(), 7);
		
		effectResolutor.addResources(new Resource(ResourceType.SERVANTS, 2));
		player.increaseFamiliarValue(FamiliarColor.BLACK);
		familiarEffect3.resolveEffect(effectResolutor);
		try {
			player.increaseFamiliarValue(FamiliarColor.BLACK);
			fail();
		}
		catch (IllegalActionException e) {
		}
		assertEquals(player.getFamiliarByColor(FamiliarColor.BLACK).getValue(), 8);
		
		assertTrue(familiarEffect1.getActionModifier(SlotType.ANY).isEmpty());
		assertTrue(familiarEffect2.getActionModifier(SlotType.ANY).isEmpty());
		assertTrue(familiarEffect3.getActionModifier(SlotType.ANY).isEmpty());
	}

}
