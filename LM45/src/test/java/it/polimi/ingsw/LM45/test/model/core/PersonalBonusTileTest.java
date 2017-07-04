package it.polimi.ingsw.LM45.test.model.core;

import java.util.Random;

import it.polimi.ingsw.LM45.model.core.PersonalBonusTile;
import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;
import junit.framework.TestCase;
import testUtilities.FakeEffectResolutor;
import testUtilities.Helper;

public class PersonalBonusTileTest extends TestCase {

	Random random = new Random();

	public void testPersonalBonusTile() {
		Resource[] harvestBonuses = new Resource[] { Helper.randomResource(), Helper.randomResource(), Helper.randomResource(),
				Helper.randomResource(), Helper.randomResource() };
		Resource[] productionBonuses = new Resource[] { Helper.randomResource(), Helper.randomResource(), Helper.randomResource(),
				Helper.randomResource(), Helper.randomResource() };

		PersonalBonusTile personalBonusTile = new PersonalBonusTile(productionBonuses, harvestBonuses);

		assertTrue(Helper.sameResources(harvestBonuses, personalBonusTile.getHarvestBonuses()));
		assertTrue(Helper.sameResources(productionBonuses, personalBonusTile.getProductionBonuses()));

		Player player = new Player("Test", PlayerColor.BLUE);
		EffectResolutor effectResolutor = new FakeEffectResolutor(player);

		personalBonusTile.harvest(effectResolutor, 0);
		assertTrue(player.getAllResources().length == 0);
		personalBonusTile.produce(effectResolutor, 0);
		assertTrue(player.getAllResources().length == 0);

		personalBonusTile.harvest(effectResolutor, 1);
		for (Resource resource : harvestBonuses)
			assertTrue(player.hasResources(resource));
		personalBonusTile.produce(effectResolutor, 1);
		for (Resource resource : productionBonuses)
			assertTrue(player.hasResources(resource));
	}

}
