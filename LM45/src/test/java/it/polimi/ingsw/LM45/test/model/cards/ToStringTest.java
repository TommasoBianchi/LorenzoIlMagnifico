package it.polimi.ingsw.LM45.test.model.cards;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.List;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.Excommunication;
import it.polimi.ingsw.LM45.serialization.FileManager;
import junit.framework.TestCase;

public class ToStringTest extends TestCase {

	public void testToString() throws JsonSyntaxException, JsonIOException, FileNotFoundException{
		FileManager.loadCards().values().stream().flatMap(List::stream).forEach(card -> {
			assertTrue(classImplementToString(card));
			assertTrue(classImplementToString(card.getCardCost()));
			try {
				Field immediateEffectField = Card.class.getDeclaredField("immediateEffect");
				Field effectField = Card.class.getDeclaredField("effect");
				immediateEffectField.setAccessible(true);
				effectField.setAccessible(true);
				assertTrue(classImplementToString(immediateEffectField.get(card)));
				assertTrue(classImplementToString(effectField.get(card)));
			}
			catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
				fail();
			}
		});
		
		FileManager.loadLeaderCards().stream().forEach(leaderCard -> {
			assertTrue(classImplementToString(leaderCard));
			assertTrue(classImplementToString(leaderCard.getEffect()));
		});
		
		FileManager.loadExcommunications().values().stream().flatMap(List::stream).forEach(excommunication -> {
			assertTrue(classImplementToString(excommunication));
			try {
				Field effectField = Excommunication.class.getDeclaredField("effect");
				effectField.setAccessible(true);
				assertTrue(classImplementToString(effectField.get(excommunication)));
			}
			catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
				fail();
			}
		});
	}
	
	private <T> boolean classImplementToString(T element){
		String defaultToString = element.getClass().getName() + "@" + 
	            Integer.toHexString(System.identityHashCode(element));
		return defaultToString.equals(element.toString()) == false;
	}
	
}
