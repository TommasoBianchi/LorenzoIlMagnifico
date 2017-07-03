package it.polimi.ingsw.LM45.model.core;

import java.util.Arrays;
import java.util.stream.Stream;

import it.polimi.ingsw.LM45.exceptions.IllegalActionException;
import it.polimi.ingsw.LM45.model.cards.Building;
import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.Cost;
import it.polimi.ingsw.LM45.model.cards.LeaderCard;
import it.polimi.ingsw.LM45.model.cards.PeriodType;
import it.polimi.ingsw.LM45.model.cards.Territory;
import it.polimi.ingsw.LM45.model.cards.Venture;
import it.polimi.ingsw.LM45.model.effects.CardEffect;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;
import it.polimi.ingsw.LM45.model.effects.FamiliarEffect;
import it.polimi.ingsw.LM45.model.effects.ResourceEffect;
import junit.framework.TestCase;
import testUtilities.FakeEffectResolutor;
import testUtilities.Helper;

public class PlayerTest extends TestCase {

	public void testAddCard() {
		Player player = new Player("Test", PlayerColor.BLUE);
		Card card = new Territory("", PeriodType.I, CardEffect.EMPTY, CardEffect.EMPTY, 2);

		assertTrue(player.canAddCard(card));
		player.addCard(card);
		assertTrue(player.canAddCard(card));
		player.addCard(card);
		assertFalse(player.canAddCard(card));

		player.addResources(new Resource(ResourceType.MILITARY, 5));
		assertTrue(player.canAddCard(card));
		player.addCard(card);
	}

	public void testLeaderCards() {
		Player player = new Player("Test", PlayerColor.BLUE);
		EffectResolutor effectResolutor = new FakeEffectResolutor(player);
		LeaderCard leaderCard1 = new LeaderCard("1", CardEffect.EMPTY, new Resource[] {});
		LeaderCard leaderCard2 = new LeaderCard("2", new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.COINS, 1) })),
				new Resource[] { new Resource(ResourceType.COINS, 18) });
		LeaderCard leaderCard3 = new LeaderCard("3",
				new CardEffect(new FamiliarEffect(2, false, new FamiliarColor[] { FamiliarColor.BLACK }, 1), true), new Resource[] {});

		player.addLeaderCard(leaderCard1);
		player.addLeaderCard(leaderCard2);
		player.addLeaderCard(leaderCard3);

		assertFalse(Arrays.stream(player.getPlayedLeaderCards()).anyMatch(leaderCard -> leaderCard == leaderCard1));
		assertFalse(Arrays.stream(player.getPlayedLeaderCards()).anyMatch(leaderCard -> leaderCard == leaderCard2));
		assertFalse(Arrays.stream(player.getPlayedLeaderCards()).anyMatch(leaderCard -> leaderCard == leaderCard3));

		try {
			player.playLeaderCard(leaderCard1, effectResolutor);
		}
		catch (IllegalActionException e) {
			fail();
		}
		try {
			player.discardLeaderCard(leaderCard1);
			fail();
		}
		catch (IllegalActionException e) {
		}

		try {
			player.playLeaderCard(leaderCard2, effectResolutor);
			fail();
		}
		catch (IllegalActionException e) {
		}
		player.addResources(new Resource(ResourceType.COINS, 18));
		try {
			player.playLeaderCard(leaderCard2, effectResolutor);
		}
		catch (IllegalActionException e) {
			fail();
		}
		try {
			player.activateLeaderCard(leaderCard2, effectResolutor);
		}
		catch (IllegalActionException e) {
			fail();
		}
		assertEquals(player.getResourceAmount(ResourceType.COINS), 19);
		try {
			player.activateLeaderCard(leaderCard2, effectResolutor);
			fail();
		}
		catch (IllegalActionException e) {
		}

		try {
			player.activateLeaderCard(leaderCard3, effectResolutor);
			fail();
		}
		catch (IllegalActionException e) {
		}
		try {
			player.playLeaderCard(leaderCard3, effectResolutor);
		}
		catch (IllegalActionException e) {
			fail();
		}
		assertEquals(player.getFamiliarValue(FamiliarColor.BLACK), 2);

		assertTrue(Arrays.stream(player.getPlayedLeaderCards()).anyMatch(leaderCard -> leaderCard == leaderCard1));
		assertTrue(Arrays.stream(player.getPlayedLeaderCards()).anyMatch(leaderCard -> leaderCard == leaderCard2));
		assertTrue(Arrays.stream(player.getPlayedLeaderCards()).anyMatch(leaderCard -> leaderCard == leaderCard3));
	}

	public void testGetResourceAmount() {
		Player player = new Player("Test", PlayerColor.BLUE);
		Resource randomResource = Helper.randomResource();
		player.addResources(randomResource);
		assertEquals(player.getResourceAmount(randomResource.getResourceType()), randomResource.getAmount());
		assertTrue(player.hasResources(randomResource));
	}

	public void testGetAllResources() {
		Player player = new Player("Test", PlayerColor.BLUE);
		Resource[] randomResources = new Resource[] { Helper.randomResource(), Helper.randomResource(), Helper.randomResource() };
		Arrays.stream(randomResources).forEach(resource -> player.addResources(resource));
		Helper.sameResources(randomResources, player.getAllResources());
	}

	public void testFamiliars() throws IllegalActionException {
		Player player = new Player("Test", PlayerColor.BLUE);

		assertEquals(player.getFamiliars().length, 4);

		try {
			player.getFamiliarByColor(FamiliarColor.BLACK);
			player.getFamiliarByColor(FamiliarColor.WHITE);
			player.getFamiliarByColor(FamiliarColor.ORANGE);
			player.getFamiliarByColor(FamiliarColor.UNCOLORED);
		}
		catch (IllegalActionException e) {
			fail();
		}
		try {
			player.getFamiliarByColor(FamiliarColor.BONUS);
			fail();
		}
		catch (IllegalActionException e) {
		}

		player.setFamiliarValue(FamiliarColor.BLACK, 1);
		try {
			player.increaseFamiliarValue(FamiliarColor.BLACK);
			fail();
		}
		catch (IllegalActionException e) {
		}
		player.addResources(new Resource(ResourceType.SERVANTS, 2));
		player.increaseFamiliarValue(FamiliarColor.BLACK);
		player.modifyServantCost(2);
		try {
			player.increaseFamiliarValue(FamiliarColor.BLACK);
			fail();
		}
		catch (IllegalActionException e) {
		}
		player.addFamiliarBonus(FamiliarColor.BLACK, 2);
		assertEquals(player.getFamiliarValue(FamiliarColor.BLACK), 4);
	}

	public void testNoTerritoryRequisites() {
		Player player = new Player("Test", PlayerColor.BLUE);
		player.noTerritoryRequisites();
		Territory territory = new Territory("", PeriodType.I, CardEffect.EMPTY, CardEffect.EMPTY, 2);
		for (int i = 0; i < 6; i++) {
			assertTrue(player.canAddCard(territory));
			player.addCard(territory);
		}
		assertFalse(player.canAddCard(territory));
	}

	public void testGetChurchSupportBonuses() {
		Player player = new Player("Test", PlayerColor.BLUE);
		Resource resource = Helper.randomResource();
		player.addChurchSupportBonus(resource);
		assertTrue(Helper.sameResources(new Resource[] { resource }, player.getChurchSupportBonuses()));
	}

	public void testGetUsername() {
		Player player = new Player("Test", PlayerColor.BLUE);
		assertEquals(player.getUsername(), "Test");
	}

	public void testGetColor() {
		Player player = new Player("Test", PlayerColor.BLUE);
		assertEquals(player.getColor(), PlayerColor.BLUE);
	}

	public void testHasToSkipFirstRound() {
		Player player = new Player("Test", PlayerColor.BLUE);
		assertFalse(player.getHasToSkipFirstRound());
		player.setHasToSkipFirstRound();
		assertTrue(player.getHasToSkipFirstRound());
	}

	public void testPayIfTowerIsOccupied() {
		Player player = new Player("Test", PlayerColor.BLUE);
		assertTrue(player.getPayIfTowerIsOccupied());
		player.setPayIfTowerIsOccupied(false);
		assertFalse(player.getPayIfTowerIsOccupied());
	}

	public void testHarvest() {
		Player player = new Player("Test", PlayerColor.BLUE);
		PersonalBonusTile personalBonusTile = new PersonalBonusTile(new Resource[] {},
				new Resource[] { Helper.randomResource(), Helper.randomResource(), Helper.randomResource() });
		player.setPersonalBonusTile(personalBonusTile);
		Resource[][] territoryResources = new Resource[][] {
				new Resource[] { Helper.randomResource(), Helper.randomResource(), Helper.randomResource() },
				new Resource[] { Helper.randomResource(), Helper.randomResource(), Helper.randomResource() },
				new Resource[] { Helper.randomResource(), Helper.randomResource(), Helper.randomResource() } };
		Territory[] territories = new Territory[] {
				new Territory("1", PeriodType.I, CardEffect.EMPTY, new CardEffect(new ResourceEffect(territoryResources[0])), 1),
				new Territory("2", PeriodType.I, CardEffect.EMPTY, new CardEffect(new ResourceEffect(territoryResources[1])), 3),
				new Territory("3", PeriodType.I, CardEffect.EMPTY, new CardEffect(new ResourceEffect(territoryResources[2])), 5) };
		player.noTerritoryRequisites();
		player.addCard(territories[0]);
		player.addCard(territories[1]);
		player.addCard(territories[2]);

		player.harvest(new FakeEffectResolutor(player), 3);
		Resource[] harvestedResources = Stream.concat(
				Stream.concat(Arrays.stream(personalBonusTile.getHarvestBonuses()),
						Arrays.stream(new Resource[] { new Resource(ResourceType.TERRITORY, 3) })),
				Stream.concat(Arrays.stream(territoryResources[0]), Arrays.stream(territoryResources[1]))).toArray(Resource[]::new);

		assertTrue(Helper.sameResources(harvestedResources, player.getAllResources()));
	}

	public void testProduce() {
		Player player = new Player("Test", PlayerColor.BLUE);
		PersonalBonusTile personalBonusTile = new PersonalBonusTile(
				new Resource[] { Helper.randomResource(), Helper.randomResource(), Helper.randomResource() }, new Resource[] {});
		player.setPersonalBonusTile(personalBonusTile);
		Resource[][] buildingResources = new Resource[][] {
				new Resource[] { Helper.randomResource(), Helper.randomResource(), Helper.randomResource() },
				new Resource[] { Helper.randomResource(), Helper.randomResource(), Helper.randomResource() },
				new Resource[] { Helper.randomResource(), Helper.randomResource(), Helper.randomResource() } };
		Building[] buildings = new Building[] {
				new Building("1", PeriodType.I, Cost.EMPTY, CardEffect.EMPTY, new CardEffect(new ResourceEffect(buildingResources[0])), 1),
				new Building("2", PeriodType.I, Cost.EMPTY, CardEffect.EMPTY, new CardEffect(new ResourceEffect(buildingResources[1])), 3),
				new Building("3", PeriodType.I, Cost.EMPTY, CardEffect.EMPTY, new CardEffect(new ResourceEffect(buildingResources[2])), 5) };
		player.addCard(buildings[0]);
		player.addCard(buildings[1]);
		player.addCard(buildings[2]);

		player.produce(new FakeEffectResolutor(player), 3);
		Resource[] producedResources = Stream.concat(
				Stream.concat(Arrays.stream(personalBonusTile.getProductionBonuses()),
						Arrays.stream(new Resource[] { new Resource(ResourceType.BUILDING, 3) })),
				Stream.concat(Arrays.stream(buildingResources[0]), Arrays.stream(buildingResources[1]))).toArray(Resource[]::new);

		assertTrue(Helper.sameResources(producedResources, player.getAllResources()));
	}

	public void testResolveVentures() {
		Player player = new Player("Test", PlayerColor.BLUE);
		int[] victoryBonuses = new int[] { 3, 8, 5, 4, 9 };
		Venture[] ventures = new Venture[] {
				new Venture("1", PeriodType.I, Cost.EMPTY, CardEffect.EMPTY,
						new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, victoryBonuses[0]) }))),
				new Venture("2", PeriodType.I, Cost.EMPTY, CardEffect.EMPTY,
						new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, victoryBonuses[1]) }))),
				new Venture("3", PeriodType.I, Cost.EMPTY, CardEffect.EMPTY,
						new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, victoryBonuses[2]) }))),
				new Venture("4", PeriodType.I, Cost.EMPTY, CardEffect.EMPTY,
						new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, victoryBonuses[3]) }))),
				new Venture("5", PeriodType.I, Cost.EMPTY, CardEffect.EMPTY,
						new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, victoryBonuses[4]) }))) };

		for (Venture venture : ventures)
			player.addCard(venture);

		player.resolveVentures(new FakeEffectResolutor(player));

		assertEquals(player.getResourceAmount(ResourceType.VICTORY), Arrays.stream(victoryBonuses).sum());
		assertEquals(player.getResourceAmount(ResourceType.VENTURE), ventures.length);
		assertEquals(player.getAllResources().length, 2);
	}

	public void testBonusFamiliar() {
		Player player = new Player("Test", PlayerColor.BLUE);

		try {
			player.getFamiliarByColor(FamiliarColor.BONUS);
			fail();
		}
		catch (IllegalActionException e) {
		}
		player.addBonusFamiliar(4, new Resource[] {});
		try {
			player.getFamiliarByColor(FamiliarColor.BONUS);
		}
		catch (IllegalActionException e) {
			fail();
		}
		player.removeBonusFamiliar();
		try {
			player.getFamiliarByColor(FamiliarColor.BONUS);
			fail();
		}
		catch (IllegalActionException e) {
		}
	}

	public void testGetActionModifier() {
		Player player = new Player("Test", PlayerColor.BLUE);
		EffectResolutor effectResolutor = new FakeEffectResolutor(player);

		assertTrue(player.getActionModifier(SlotType.TERRITORY, effectResolutor).isEmpty());

		Resource randomDiscount = Helper.randomResource();
		player.addBonusFamiliar(4, new Resource[] { randomDiscount });
		assertFalse(player.getActionModifier(SlotType.TERRITORY, effectResolutor).isEmpty());
		assertEquals(player.getActionModifier(SlotType.TERRITORY, effectResolutor).getCostModifiers().get(randomDiscount.getResourceType())
				.modify(randomDiscount).getAmount(), 0);
	}

}
