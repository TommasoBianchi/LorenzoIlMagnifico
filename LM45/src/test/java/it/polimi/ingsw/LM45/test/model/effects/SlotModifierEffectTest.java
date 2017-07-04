package it.polimi.ingsw.LM45.test.model.effects;

import it.polimi.ingsw.LM45.exceptions.IllegalActionException;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.Slot;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;
import it.polimi.ingsw.LM45.model.effects.SlotModifierEffect;
import junit.framework.TestCase;
import testUtilities.FakeEffectResolutor;

public class SlotModifierEffectTest extends TestCase {

	public void testGetActionModifier() {
		SlotModifierEffect slotModifierEffect = new SlotModifierEffect(SlotType.MARKET, false, false, true);
		Slot slot = new Slot(new Resource[]{}, 1, SlotType.MARKET, false, false);
		Player player = new Player("Test", PlayerColor.BLUE);
		EffectResolutor effectResolutor = new FakeEffectResolutor(player);
		
		assertTrue(slotModifierEffect.getActionModifier(SlotType.BUILDING).isEmpty());
		assertFalse(slotModifierEffect.getActionModifier(SlotType.MARKET).isEmpty());
		player.setFamiliarValue(FamiliarColor.BLACK, 2);
		try {
			slot.canAddFamiliar(player.getFamiliarByColor(FamiliarColor.BLACK), slotModifierEffect.getActionModifier(SlotType.MARKET), effectResolutor);
			fail();
		}
		catch (IllegalActionException e) {
		}
	}

}
