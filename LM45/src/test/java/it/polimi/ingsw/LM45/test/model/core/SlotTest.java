package it.polimi.ingsw.LM45.test.model.core;

import it.polimi.ingsw.LM45.exceptions.IllegalActionException;
import it.polimi.ingsw.LM45.model.core.Familiar;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.Slot;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.model.effects.modifiers.ActionModifier;
import junit.framework.TestCase;

public class SlotTest extends TestCase {

	public void testCanAddFamiliar() throws IllegalActionException {
		Slot slot1 = new Slot(new Resource[]{}, 3, SlotType.MARKET, false, false);
		Slot slot2 = new Slot(new Resource[]{}, 3, SlotType.MARKET, false, false);
		slot1.addNeighbouringSlot(slot2);
		slot2.addNeighbouringSlot(slot1);
		
		Player player1 = new Player("Test1", PlayerColor.BLUE);
		Player player2 = new Player("Test2", PlayerColor.RED);
		
		player1.setFamiliarValue(FamiliarColor.BLACK, 1);
		player2.setFamiliarValue(FamiliarColor.BLACK, 3);
		try {
			slot1.canAddFamiliar(player1.getFamiliarByColor(FamiliarColor.BLACK), ActionModifier.EMPTY(), null);
			fail();
		}
		catch (IllegalActionException e) {
			assertTrue(true);
		}
		assertTrue(slot1.canAddFamiliar(player2.getFamiliarByColor(FamiliarColor.BLACK), ActionModifier.EMPTY(), null));
		
		player1.setFamiliarValue(FamiliarColor.ORANGE, 5);
		slot1.addFamiliar(player2.getFamiliarByColor(FamiliarColor.BLACK), ActionModifier.EMPTY(), null);
		try {
			slot1.canAddFamiliar(player1.getFamiliarByColor(FamiliarColor.ORANGE), ActionModifier.EMPTY(), null);
			fail();
		}
		catch (IllegalActionException e) {
			assertTrue(true);
		}
		assertTrue(slot1.canAddFamiliar(player1.getFamiliarByColor(FamiliarColor.ORANGE), new ActionModifier(false, true, true), null));
		
		player2.setFamiliarValue(FamiliarColor.ORANGE, 5);
		try {
			slot2.canAddFamiliar(player2.getFamiliarByColor(FamiliarColor.ORANGE), ActionModifier.EMPTY(), null);
			fail();
		}
		catch (IllegalActionException e) {
			assertTrue(true);
		}
		player2.setFamiliarValue(FamiliarColor.UNCOLORED, 3);
		assertTrue(slot2.canAddFamiliar(player2.getFamiliarByColor(FamiliarColor.UNCOLORED), ActionModifier.EMPTY(), null));
		assertTrue(slot2.canAddFamiliar(player1.getFamiliarByColor(FamiliarColor.ORANGE), ActionModifier.EMPTY(), null));
		
		try {
			slot2.canAddFamiliar(player2.getFamiliarByColor(FamiliarColor.ORANGE), new ActionModifier(-4), null);
			fail();
		}
		catch (IllegalActionException e) {
			assertTrue(true);
		}
		
		slot1.clearSlot();
		assertTrue(slot1.isEmpty());
		assertTrue(slot1.canAddFamiliar(player2.getFamiliarByColor(FamiliarColor.BLACK), ActionModifier.EMPTY(), null));
		slot1.addFamiliar(player2.getFamiliarByColor(FamiliarColor.BLACK), ActionModifier.EMPTY(), null);
		assertEquals(slot1.getPlayersInSlot().length, 1);
		assertEquals(slot1.getPlayersInSlot()[0], player2);
		assertFalse(slot1.isEmpty());
		assertTrue(slot2.isEmpty());
	}

	public void testGetType() {
		for(SlotType slotType : SlotType.values()){
			Slot slot = new Slot(new Resource[]{}, 1, slotType, false, false);
			assertEquals(slot.getType(), slotType);
		}
	}
	
	public void testBonusFamiliar() throws IllegalActionException {
		Slot slot = new Slot(new Resource[]{}, 1, SlotType.MARKET, false, false);
		Player player = new Player("Test", PlayerColor.BLUE);
		Familiar bonusFamiliar = new Familiar(player, FamiliarColor.BONUS);
		bonusFamiliar.setValue(2);
		slot.addFamiliar(bonusFamiliar, ActionModifier.EMPTY(), null);
		assertTrue(slot.isEmpty());
		player.setFamiliarValue(FamiliarColor.BLACK, 3);
		assertTrue(slot.canAddFamiliar(player.getFamiliarByColor(FamiliarColor.BLACK), ActionModifier.EMPTY(), null));
	}

}
