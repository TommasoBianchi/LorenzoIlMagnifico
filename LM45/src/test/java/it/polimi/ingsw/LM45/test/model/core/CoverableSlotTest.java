package it.polimi.ingsw.LM45.test.model.core;

import it.polimi.ingsw.LM45.exceptions.IllegalActionException;
import it.polimi.ingsw.LM45.model.core.CoverableSlot;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.Slot;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.model.effects.modifiers.ActionModifier;
import junit.framework.TestCase;

public class CoverableSlotTest extends TestCase {

	public void testCanAddFamiliar() throws IllegalActionException {
		CoverableSlot coverableSlot = new CoverableSlot(new Slot(new Resource[]{}, 2, SlotType.MARKET, false, false), false);
		CoverableSlot coverableSlot2 = new CoverableSlot(new Slot(new Resource[]{}, 2, SlotType.MARKET, false, false), true);
		Player player = new Player("Test", PlayerColor.BLUE);
		
		player.setFamiliarValue(FamiliarColor.BLACK, 4);
		try {
			coverableSlot.canAddFamiliar(player.getFamiliarByColor(FamiliarColor.BLACK), ActionModifier.EMPTY(), null);
			fail();
		}
		catch (IllegalActionException e) {
			assertTrue(true);
		}
		coverableSlot.addFamiliar(player.getFamiliarByColor(FamiliarColor.BLACK), ActionModifier.EMPTY(), null);
		assertTrue(coverableSlot2.canAddFamiliar(player.getFamiliarByColor(FamiliarColor.BLACK), ActionModifier.EMPTY(), null));
		coverableSlot2.addFamiliar(player.getFamiliarByColor(FamiliarColor.BLACK), ActionModifier.EMPTY(), null);
	}

}
