package it.polimi.ingsw.LM45.model.core;

import it.polimi.ingsw.LM45.exceptions.IllegalActionException;
import it.polimi.ingsw.LM45.model.cards.Building;
import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.Cost;
import it.polimi.ingsw.LM45.model.cards.PeriodType;
import it.polimi.ingsw.LM45.model.cards.Territory;
import it.polimi.ingsw.LM45.model.effects.CardEffect;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;
import it.polimi.ingsw.LM45.model.effects.modifiers.ActionModifier;
import it.polimi.ingsw.LM45.model.effects.modifiers.ResourceAdder;
import it.polimi.ingsw.LM45.model.effects.modifiers.ResourceModifier;
import junit.framework.TestCase;
import testUtilities.FakeEffectResolutor;

public class TowerSlotTest extends TestCase {

	public void testCanAddFamiliar() throws IllegalActionException {
		TowerSlot towerSlot = new TowerSlot(new Resource[] {}, 2, SlotType.BUILDING, false, false);
		Card card = new Building("", PeriodType.I, new Cost(new Resource[] { new Resource(ResourceType.WOOD, 2) }), CardEffect.EMPTY,
				CardEffect.EMPTY, 1);
		towerSlot.placeCard(card);

		Player player = new Player("Test", PlayerColor.BLUE);
		EffectResolutor effectResolutor = new FakeEffectResolutor(player);
		player.setFamiliarValue(FamiliarColor.BLACK, 4);

		try {
			towerSlot.canAddFamiliar(player.getFamiliarByColor(FamiliarColor.BLACK), ActionModifier.EMPTY(), effectResolutor);
			fail();
		}
		catch (IllegalActionException e) {
			assertTrue(true);
		}

		player.addResources(new Resource(ResourceType.WOOD, 1));
		assertTrue(towerSlot.canAddFamiliar(player.getFamiliarByColor(FamiliarColor.BLACK),
				new ActionModifier(new ResourceModifier[] { new ResourceAdder(new Resource(ResourceType.WOOD, -1)) }), effectResolutor));
		player.addResources(new Resource(ResourceType.WOOD, 1));
		assertTrue(towerSlot.canAddFamiliar(player.getFamiliarByColor(FamiliarColor.BLACK), ActionModifier.EMPTY(), effectResolutor));
		towerSlot.addFamiliar(player.getFamiliarByColor(FamiliarColor.BLACK), ActionModifier.EMPTY(), effectResolutor);

		TowerSlot towerSlot2 = new TowerSlot(new Resource[] {}, 2, SlotType.BUILDING, false, false);
		towerSlot.addNeighbouringSlot(towerSlot2);
		towerSlot2.addNeighbouringSlot(towerSlot);
		Card card2 = new Building("", PeriodType.I, new Cost(new Resource[] {}), CardEffect.EMPTY,
				CardEffect.EMPTY, 1);
		
		player.setFamiliarValue(FamiliarColor.ORANGE, 5);
		player.setFamiliarValue(FamiliarColor.UNCOLORED, 4);
		try {
			towerSlot2.canAddFamiliar(player.getFamiliarByColor(FamiliarColor.ORANGE), ActionModifier.EMPTY(), effectResolutor);
			fail();
		}
		catch (IllegalActionException e) {
			assertTrue(true);
		}
		towerSlot2.placeCard(card2);
		try {
			towerSlot2.canAddFamiliar(player.getFamiliarByColor(FamiliarColor.UNCOLORED), ActionModifier.EMPTY(), effectResolutor);
			fail();
		}
		catch (IllegalActionException e) {
			assertTrue(true);
		}
		player.setPayIfTowerIsOccupied(false);
		assertTrue(towerSlot2.canAddFamiliar(player.getFamiliarByColor(FamiliarColor.UNCOLORED), ActionModifier.EMPTY(), effectResolutor));
		player.setPayIfTowerIsOccupied(true);
		player.addResources(new Resource(ResourceType.COINS, 3));
		assertTrue(towerSlot2.canAddFamiliar(player.getFamiliarByColor(FamiliarColor.UNCOLORED), ActionModifier.EMPTY(), effectResolutor));
		for(int i = 0; i < 6; i++)
			player.addCard(card2);
		try {
			towerSlot2.canAddFamiliar(player.getFamiliarByColor(FamiliarColor.UNCOLORED), ActionModifier.EMPTY(), effectResolutor);
			fail();
		}
		catch (IllegalActionException e) {
			assertTrue(true);
		}
		
	}

	public void testHasCard() {
		TowerSlot towerSlot = new TowerSlot(new Resource[] {}, 2, SlotType.TERRITORY, false, false);
		assertFalse(towerSlot.hasCard());
		Card card = new Territory("", PeriodType.I, null, null, 1);
		towerSlot.placeCard(card);
		assertTrue(towerSlot.hasCard());
		assertEquals(towerSlot.getCard(), card);
		towerSlot.clearSlot();
		assertFalse(towerSlot.hasCard());
	}

}
