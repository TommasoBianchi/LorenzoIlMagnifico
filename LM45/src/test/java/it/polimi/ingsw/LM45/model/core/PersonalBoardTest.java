package it.polimi.ingsw.LM45.model.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import it.polimi.ingsw.LM45.model.cards.Building;
import it.polimi.ingsw.LM45.model.cards.Character;
import it.polimi.ingsw.LM45.model.cards.Cost;
import it.polimi.ingsw.LM45.model.cards.PeriodType;
import it.polimi.ingsw.LM45.model.cards.Territory;
import it.polimi.ingsw.LM45.model.cards.Venture;
import junit.framework.TestCase;
import testUtilities.Helper;

public class PersonalBoardTest extends TestCase {

	private Random random = new Random();

	public void testGetResourceAmount() {
		PersonalBoard personalBoard = new PersonalBoard();

		for (ResourceType resourceType : ResourceType.values())
			assertEquals(personalBoard.getResourceAmount(resourceType), 0);

		for (ResourceType resourceType : ResourceType.values()) {
			int amountToAdd = random.nextInt(10);
			personalBoard.addResources(new Resource(resourceType, amountToAdd));
			assertEquals(personalBoard.getResourceAmount(resourceType), amountToAdd);
		}

		for (ResourceType resourceType : ResourceType.values()) {
			int oldAmount = personalBoard.getResourceAmount(resourceType);
			int amountToRemove = random.nextInt(10);
			personalBoard.removeResources(new Resource(resourceType, amountToRemove));
			assertEquals(personalBoard.getResourceAmount(resourceType), (oldAmount - amountToRemove) > 0 ? (oldAmount - amountToRemove) : 0);
		}

		for (ResourceType resourceType : ResourceType.values()) {
			int oldAmount = personalBoard.getResourceAmount(resourceType);
			int amountToAdd = random.nextInt(10);
			personalBoard.addResources(new Resource(resourceType, amountToAdd));
			assertEquals(personalBoard.getResourceAmount(resourceType), oldAmount + amountToAdd);
		}

		for (ResourceType resourceType : ResourceType.values()) {
			assertTrue(personalBoard.hasResources(new Resource(resourceType, personalBoard.getResourceAmount(resourceType))));
		}
	}

	public void testCanAddCard() {
		PersonalBoard personalBoard = new PersonalBoard();

		Territory territory = new Territory("", PeriodType.I, null, null, 0);
		Building building = new Building("", PeriodType.I, Cost.EMPTY, null, null, 0);
		Character character = new Character("", PeriodType.I, Cost.EMPTY, null, null);
		Venture venture = new Venture("", PeriodType.I, Cost.EMPTY, null, null);

		for (int i = 0; i < 6; i++) {
			assertTrue(personalBoard.canAddCard(building));
			personalBoard.addCard(building);
			assertTrue(personalBoard.canAddCard(character));
			personalBoard.addCard(character);
			assertTrue(personalBoard.canAddCard(venture));
			personalBoard.addCard(venture);
		}
		
		assertFalse(personalBoard.canAddCard(building));
		assertFalse(personalBoard.canAddCard(character));
		assertFalse(personalBoard.canAddCard(venture));

		for (int i = 0; i < 2; i++) {
			assertTrue(personalBoard.canAddCard(territory));
			personalBoard.addCard(territory);
		}
		assertFalse(personalBoard.canAddCard(territory));
		personalBoard.addResources(new Resource(ResourceType.MILITARY, 20));
		for (int i = 0; i < 4; i++) {
			assertTrue(personalBoard.canAddCard(territory));
			personalBoard.addCard(territory);
		}

		assertFalse(personalBoard.canAddCard(territory));
	}

	public void testClearTerritoryRequisites() {
		PersonalBoard personalBoard = new PersonalBoard();

		personalBoard.clearTerritoryRequisites();

		for (int i = 0; i < 6; i++) {
			Territory territory = new Territory("", PeriodType.I, null, null, 0);
			assertTrue(personalBoard.canAddCard(territory));
			personalBoard.addCard(territory);
		}
	}

	public void testGetAllResources() {
		PersonalBoard personalBoard = new PersonalBoard();
		List<Resource> resources = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			ResourceType[] resourceTypes = ResourceType.values();
			Resource resource = new Resource(resourceTypes[random.nextInt(resourceTypes.length)], random.nextInt(10));
			resources.add(resource);
			personalBoard.addResources(resource);
		}
		
		Helper.sameResources(resources.stream().toArray(Resource[]::new), personalBoard.getAllResources());
	}

}
