package it.polimi.ingsw.LM45.model.core;

import it.polimi.ingsw.LM45.exceptions.IllegalActionException;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;
import it.polimi.ingsw.LM45.model.effects.modifiers.ActionModifier;
import junit.framework.TestCase;
import testUtilities.FakeEffectResolutor;

public class HarvestProductionSlotTest extends TestCase {

	public void testAddFamiliar() throws IllegalActionException {
		HarvestProductionSlot harvestSlot = new HarvestProductionSlot(new Resource[]{}, 2, SlotType.HARVEST, false, false);
		HarvestProductionSlot productionSlot = new HarvestProductionSlot(new Resource[]{}, 2, SlotType.PRODUCTION, false, false);
		Player player = new Player("Test", PlayerColor.BLUE);
		player.setPersonalBonusTile(new PersonalBonusTile(new Resource[]{}, new Resource[]{}));
		EffectResolutor effectResolutor = new FakeEffectResolutor(player);
		
		try {
			harvestSlot.canAddFamiliar(player.getFamiliarByColor(FamiliarColor.BLACK), ActionModifier.EMPTY(), null);
			fail();
		}
		catch (IllegalActionException e) {
			assertTrue(true);
		}
		try {
			productionSlot.canAddFamiliar(player.getFamiliarByColor(FamiliarColor.BLACK), ActionModifier.EMPTY(), null);
			fail();
		}
		catch (IllegalActionException e) {
			assertTrue(true);
		}
		
		player.setFamiliarValue(FamiliarColor.BLACK, 3);
		player.setFamiliarValue(FamiliarColor.ORANGE, 3);
		harvestSlot.addFamiliar(player.getFamiliarByColor(FamiliarColor.BLACK), ActionModifier.EMPTY(), effectResolutor);
		try {
			productionSlot.canAddFamiliar(player.getFamiliarByColor(FamiliarColor.BLACK), ActionModifier.EMPTY(), null);
			fail();
		}
		catch (IllegalActionException e) {
			assertTrue(true);
		}
		productionSlot.addFamiliar(player.getFamiliarByColor(FamiliarColor.ORANGE), ActionModifier.EMPTY(), effectResolutor);
	}

}
