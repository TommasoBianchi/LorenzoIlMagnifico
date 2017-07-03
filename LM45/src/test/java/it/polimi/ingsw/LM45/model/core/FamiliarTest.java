package it.polimi.ingsw.LM45.model.core;

import java.util.Random;

import it.polimi.ingsw.LM45.exceptions.IllegalActionException;
import junit.framework.TestCase;

public class FamiliarTest extends TestCase {

	private Player player = new Player("Test", PlayerColor.BLUE);
	private Random random = new Random();

	public void testGetValue() {
		Familiar familiar = new Familiar(player, FamiliarColor.BLACK);

		for (int i = 0; i < 10; i++) {
			int randomNumber = random.nextInt(6) + 1;
			familiar.setValue(randomNumber);
			assertEquals(familiar.getValue(), randomNumber);
		}
	}

	public void testSetValueIntBoolean() {
		Familiar familiar = new Familiar(player, FamiliarColor.BLACK);
		int staticValue = random.nextInt(6) + 1;
		familiar.setValue(staticValue, true);

		for (int i = 0; i < 10; i++) {
			int randomNumber = random.nextInt(6) + 1;
			familiar.setValue(randomNumber);
			assertEquals(familiar.getValue(), staticValue);
		}
	}

	public void testAddBonus() {
		Familiar familiar = new Familiar(player, FamiliarColor.BLACK);
		int value = random.nextInt(6) + 1;
		familiar.setValue(value);
		
		for (int i = 0; i < 10; i++) {
			int randomNumber = random.nextInt(6) + 1;
			familiar.addBonus(randomNumber);
			assertEquals(familiar.getValue(), value + randomNumber);
			value += randomNumber;
		}
	}

	public void testAddServantsBonus() throws IllegalActionException {
		Familiar familiar = new Familiar(player, FamiliarColor.BLACK);
		int value = random.nextInt(6) + 1;
		familiar.setValue(value);
		
		player.addResources(new Resource(ResourceType.SERVANTS, 4));
		familiar.addServantsBonus();
		familiar.addServantsBonus();
		familiar.addServantsBonus();
		familiar.addServantsBonus();
		
		try {
			familiar.addServantsBonus();
			assertTrue(false);
		}
		catch (IllegalActionException e) {
			assertTrue(true);
		}
		
		assertEquals(familiar.getValue(), value + 4);
		familiar.clearServantsBonus();
		assertEquals(familiar.getValue(), value);
		
		player.addResources(new Resource(ResourceType.SERVANTS, 4));
		familiar.setServantBonusCost(2);
		familiar.addServantsBonus();
		familiar.addServantsBonus();
		
		try {
			familiar.addServantsBonus();
			assertTrue(false);
		}
		catch (IllegalActionException e) {
			assertTrue(true);
		}
		
		assertEquals(familiar.getValue(), value + 2);
		familiar.clearServantsBonus();
		assertEquals(familiar.getValue(), value);
	}
	
	public void testGetPlayer(){
		Familiar familiar = new Familiar(player, FamiliarColor.BLACK);
		assertEquals(familiar.getPlayer(), player);
	}
	
	public void testGetFamiliarColor(){
		for(FamiliarColor familiarColor : FamiliarColor.values()){
			Familiar familiar = new Familiar(player, familiarColor);
			assertEquals(familiar.getFamiliarColor(), familiarColor);			
		}
	}
	
	public void testIsPlaced(){
		Familiar familiar = new Familiar(player, FamiliarColor.BLACK);
		assertFalse(familiar.getIsPlaced());
		familiar.setIsPlaced(true);
		assertTrue(familiar.getIsPlaced());
		familiar.setIsPlaced(false);
		assertFalse(familiar.getIsPlaced());
	}

}
