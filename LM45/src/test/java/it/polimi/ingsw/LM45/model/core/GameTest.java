package it.polimi.ingsw.LM45.model.core;

import java.io.FileNotFoundException;
import java.util.Arrays;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import it.polimi.ingsw.LM45.config.BoardConfiguration;
import it.polimi.ingsw.LM45.exceptions.IllegalActionException;
import it.polimi.ingsw.LM45.serialization.FileManager;
import junit.framework.TestCase;

public class GameTest extends TestCase {

	public void testGame() throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		Player[] players = new Player[] { new Player("Test1", PlayerColor.BLUE), new Player("Test2", PlayerColor.RED) };
		Game game = new Game(Arrays.asList(players), 
				FileManager.loadConfiguration(BoardConfiguration.class), 
				FileManager.loadCards(),
				FileManager.loadLeaderCards(), 
				FileManager.loadExcommunications());
		
		testGetSlot(game);
		
		game.start();
		game.startTurn();
		Player firstPlayer = game.getNextPlayer();
		
		for(int i = 0; i < 7; i++){
			assertTrue(game.hasNextPlayer());
			game.getNextPlayer();
		}		
		assertFalse(game.hasNextPlayer());
		
		game.startTurn();
		assertEquals(game.getNextPlayer(), firstPlayer);
		assertEquals(game.getCurrentTurn(), 2);
		
		for(int i = 0; i < 7; i++){
			assertTrue(game.hasNextPlayer());
			game.getNextPlayer();
		}		
		assertFalse(game.hasNextPlayer());
		
		if(firstPlayer == players[0])
			players[0].setHasToSkipFirstRound();
		else
			players[1].setHasToSkipFirstRound();
		game.startTurn();
		assertNotSame(game.getNextPlayer(), firstPlayer);
		
		for(int i = 0; i < 7; i++){
			assertTrue(game.hasNextPlayer());
			game.getNextPlayer();
		}		
		assertFalse(game.hasNextPlayer());
	}

	private void testGetSlot(Game game) {
		try {
			game.getSlot(SlotType.CHARACTER, 1);
			assertTrue(true);
		}
		catch (IllegalActionException e) {
			fail();
		}
		try {
			game.getSlot(SlotType.CHARACTER, 10);
			fail();
		}
		catch (IllegalActionException e) {
			assertTrue(true);
		}
	}

}
