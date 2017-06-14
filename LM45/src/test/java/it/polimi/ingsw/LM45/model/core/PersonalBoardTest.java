package it.polimi.ingsw.LM45.model.core;

import java.util.Random;

import it.polimi.ingsw.LM45.model.cards.PeriodType;
import it.polimi.ingsw.LM45.model.cards.Territory;
import junit.framework.TestCase;

public class PersonalBoardTest extends TestCase {
	
	private Random random = new Random();

	public void testGetResourceAmount() {
		PersonalBoard personalBoard = new PersonalBoard();
		
		for(ResourceType resourceType : ResourceType.values())
			assertEquals(personalBoard.getResourceAmount(resourceType), 0);
		
		for(ResourceType resourceType : ResourceType.values()){
			int amountToAdd = random.nextInt(10);
			personalBoard.addResources(new Resource(resourceType, amountToAdd));
			assertEquals(personalBoard.getResourceAmount(resourceType), amountToAdd);
		}
		
		for(ResourceType resourceType : ResourceType.values()){
			int oldAmount = personalBoard.getResourceAmount(resourceType);
			int amountToRemove = random.nextInt(10);
			personalBoard.removeResources(new Resource(resourceType, amountToRemove));
			assertEquals(personalBoard.getResourceAmount(resourceType), (oldAmount - amountToRemove) > 0 ? (oldAmount - amountToRemove) : 0);
		}
		
		for(ResourceType resourceType : ResourceType.values()){
			int oldAmount = personalBoard.getResourceAmount(resourceType);
			int amountToAdd = random.nextInt(10);
			personalBoard.addResources(new Resource(resourceType, amountToAdd));
			assertEquals(personalBoard.getResourceAmount(resourceType), oldAmount + amountToAdd);
		}
		
		for(ResourceType resourceType : ResourceType.values()){
			assertTrue(personalBoard.hasResources(new Resource(resourceType, personalBoard.getResourceAmount(resourceType))));
		}
	}

	public void testCanAddCard() {
		assertTrue(true); // TODO
	}

	public void testAddCard() {
		assertTrue(true); // TODO
	}

	public void testClearTerritoryRequisites() {
		PersonalBoard personalBoard = new PersonalBoard();
		
		personalBoard.clearTerritoryRequisites();
		
		for(int i = 0; i < 6; i++){
			Territory territory = new Territory("", PeriodType.I, null, null, 0);
			assertTrue(personalBoard.canAddCard(territory));
			personalBoard.addCard(territory);
		}
	}

	public void testAddPermanentEffect() {
		assertTrue(true); // TODO
	}

}
