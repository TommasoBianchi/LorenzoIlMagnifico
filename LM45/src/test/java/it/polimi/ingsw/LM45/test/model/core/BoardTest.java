package it.polimi.ingsw.LM45.test.model.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import it.polimi.ingsw.LM45.config.BoardConfiguration;
import it.polimi.ingsw.LM45.exceptions.IllegalActionException;
import it.polimi.ingsw.LM45.model.cards.Building;
import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.model.cards.Cost;
import it.polimi.ingsw.LM45.model.cards.Excommunication;
import it.polimi.ingsw.LM45.model.cards.PeriodType;
import it.polimi.ingsw.LM45.model.core.Board;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.model.effects.CardEffect;
import it.polimi.ingsw.LM45.model.effects.modifiers.ActionModifier;
import it.polimi.ingsw.LM45.util.ShuffleHelper;
import junit.framework.TestCase;
import testUtilities.FakeEffectResolutor;
import testUtilities.Helper;

public class BoardTest extends TestCase {

	public void testGetSlot() throws IllegalActionException {
		Board board = new Board(new BoardConfiguration(new HashMap<>(), new Resource[0][0]), 4);

		for (SlotType slotType : new SlotType[] { SlotType.TERRITORY, SlotType.CHARACTER, SlotType.VENTURE, SlotType.BUILDING, SlotType.MARKET }) {
			for (int i = 0; i < 4; i++)
				board.getSlot(slotType, i);
			try {
				board.getSlot(slotType, 4);
				fail();
			}
			catch (IllegalActionException e) {
				assertTrue(true);
			}
		}

		for (SlotType slotType : new SlotType[] { SlotType.HARVEST, SlotType.PRODUCTION }) {
			for (int i = 0; i < 2; i++)
				board.getSlot(slotType, i);
			try {
				board.getSlot(slotType, 2);
				fail();
			}
			catch (IllegalActionException e) {
				assertTrue(true);
			}
		}
		
		try {
			board.getSlot(SlotType.ANY_CARD, 2);
			fail();
		}
		catch (IllegalActionException e) {
			assertTrue(true);
		}
	}

	public void testGetCouncilOrder() throws IllegalActionException {
		Board board = new Board(new BoardConfiguration(new HashMap<>(), new Resource[0][0]), 4);
		Player player1 = new Player("Player1", PlayerColor.BLUE);
		Player player2 = new Player("Player2", PlayerColor.RED);
		Player player3 = new Player("Player3", PlayerColor.YELLOW);
		Player player4 = new Player("Player4", PlayerColor.GREEN);

		List<Player> randomOrderedPlayer = ShuffleHelper.shuffle(Arrays.asList(new Player[] { player1, player2, player3, player4 }));

		for (Player player : randomOrderedPlayer) {
			player.setFamiliarValue(FamiliarColor.BLACK, 1);
			board.getSlot(SlotType.COUNCIL, 0).addFamiliar(player.getFamiliarByColor(FamiliarColor.BLACK), ActionModifier.EMPTY(),
					new FakeEffectResolutor(player));
		}

		List<Player> councilOrderedPlayer = board.getCouncilOrder();
		for (int i = 0; i < 4; i++)
			assertEquals(randomOrderedPlayer.get(i), councilOrderedPlayer.get(i));
	}

	public void testGetPlacedExcommunications() {
		Board board = new Board(new BoardConfiguration(new HashMap<>(), new Resource[0][0]), 4);
		Excommunication excommunication1 = new Excommunication("Ex1", PeriodType.I, CardEffect.EMPTY);
		Excommunication excommunication2 = new Excommunication("Ex2", PeriodType.II, CardEffect.EMPTY);
		Excommunication excommunication3 = new Excommunication("Ex3", PeriodType.III, CardEffect.EMPTY);

		board.placeExcommunication(excommunication1);
		board.placeExcommunication(excommunication2);
		board.placeExcommunication(excommunication3);

		assertEquals(board.getPlacedExcommunications()[0], excommunication1);
		assertEquals(board.getPlacedExcommunications()[1], excommunication2);
		assertEquals(board.getPlacedExcommunications()[2], excommunication3);
	}

	public void testGetChurchSupportResources() {
		Resource[][] churchSupportResources = new Resource[][] {
				new Resource[] { Helper.randomResource(), Helper.randomResource(), Helper.randomResource() },
				new Resource[] { Helper.randomResource(), Helper.randomResource(), Helper.randomResource() },
				new Resource[] { Helper.randomResource(), Helper.randomResource(), Helper.randomResource() } };
		Board board = new Board(new BoardConfiguration(new HashMap<>(), churchSupportResources), 4);
		
		for(int i = 0; i < churchSupportResources.length; i++){
			assertTrue(Helper.sameResources(churchSupportResources[i], board.getChurchSupportResources(i)));
		}
		
		assertEquals(board.getChurchSupportResources(6).length, 0);
	}

	public void testGetCardsOnTower() {
		Board board = new Board(new BoardConfiguration(new HashMap<>(), new Resource[][]{}), 4);

		for(CardType cardType : new CardType[]{ CardType.BUILDING, CardType.CHARACTER, CardType.TERRITORY, CardType.VENTURE })
			assertTrue(Arrays.stream(board.getCardsOnTower(cardType)).allMatch(card -> card == null));
		
		Building building = new Building("", PeriodType.I, Cost.EMPTY, CardEffect.EMPTY, CardEffect.EMPTY, 2);
		board.placeCard(building);
		board.placeCard(building);
		board.placeCard(building);
		board.placeCard(building);
		assertTrue(Arrays.stream(board.getCardsOnTower(CardType.BUILDING)).allMatch(card -> card == building));
		board.clearSlots();
		assertTrue(Arrays.stream(board.getCardsOnTower(CardType.BUILDING)).allMatch(card -> card == null));
	}

}
