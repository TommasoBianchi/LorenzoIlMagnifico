package it.polimi.ingsw.LM45.test.model.core;

import it.polimi.ingsw.LM45.model.core.SlotType;
import junit.framework.TestCase;

public class SlotTypeTest extends TestCase {

	public void testIsCompatible() {
		for (SlotType slotType : SlotType.values()) {
			assertTrue(slotType.isCompatible(slotType));
			assertTrue(slotType.isCompatible(SlotType.ANY));
			assertTrue(SlotType.ANY.isCompatible(slotType));
		}

		for (SlotType slotType : new SlotType[] { SlotType.TERRITORY, SlotType.BUILDING, SlotType.CHARACTER, SlotType.VENTURE }) {
			assertTrue(slotType.isCompatible(SlotType.ANY_CARD));
			assertTrue(SlotType.ANY_CARD.isCompatible(slotType));
		}

		for (SlotType slotType1 : new SlotType[] { SlotType.TERRITORY, SlotType.BUILDING, SlotType.CHARACTER, SlotType.VENTURE, SlotType.PRODUCTION,
				SlotType.HARVEST, SlotType.COUNCIL, SlotType.MARKET }) {
			for (SlotType slotType2 : new SlotType[] { SlotType.TERRITORY, SlotType.BUILDING, SlotType.CHARACTER, SlotType.VENTURE,
					SlotType.PRODUCTION, SlotType.HARVEST, SlotType.COUNCIL, SlotType.MARKET }) {
				if(slotType1 != slotType2)
					assertFalse(slotType1.isCompatible(slotType2));
			}
		}
	}

}

/*
 * TERRITORY, BUILDING, CHARACTER, VENTURE, ANY_CARD, PRODUCTION, HARVEST, COUNCIL, MARKET, ANY;
 */