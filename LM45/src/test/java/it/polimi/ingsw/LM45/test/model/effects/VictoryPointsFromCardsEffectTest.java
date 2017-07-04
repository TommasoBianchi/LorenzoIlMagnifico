package it.polimi.ingsw.LM45.test.model.effects;

import it.polimi.ingsw.LM45.model.cards.Building;
import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.model.cards.Cost;
import it.polimi.ingsw.LM45.model.cards.PeriodType;
import it.polimi.ingsw.LM45.model.cards.Venture;
import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.model.effects.CardEffect;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;
import it.polimi.ingsw.LM45.model.effects.ResourceEffect;
import it.polimi.ingsw.LM45.model.effects.VictoryPointsFromCardsEffect;
import junit.framework.TestCase;
import testUtilities.FakeEffectResolutor;

public class VictoryPointsFromCardsEffectTest extends TestCase {

	public void testResolveEffect() {
		VictoryPointsFromCardsEffect victoryPointsFromCardsEffect1 = new VictoryPointsFromCardsEffect(CardType.VENTURE);
		VictoryPointsFromCardsEffect victoryPointsFromCardsEffect2 = new VictoryPointsFromCardsEffect(CardType.BUILDING);
		Player player = new Player("Test", PlayerColor.BLUE);
		EffectResolutor effectResolutor = new FakeEffectResolutor(player);

		Venture venture = new Venture("", PeriodType.I, Cost.EMPTY, CardEffect.EMPTY,
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 5) })));
		effectResolutor.addCard(venture);
		effectResolutor.addCard(venture);
		effectResolutor.addCard(venture);
		player.resolveVentures(effectResolutor);
		assertEquals(effectResolutor.getResourceAmount(ResourceType.VICTORY), 15);
		victoryPointsFromCardsEffect1.resolveEffect(effectResolutor);
		player.resolveVentures(effectResolutor);
		assertEquals(effectResolutor.getResourceAmount(ResourceType.VICTORY), 15);

		Building building = new Building("", PeriodType.I, new Cost(
				new Resource[] { new Resource(ResourceType.STONE, 2), new Resource(ResourceType.WOOD, 2), new Resource(ResourceType.COINS, 2) }),
				CardEffect.EMPTY, CardEffect.EMPTY, 2);
		effectResolutor.addCard(building);
		effectResolutor.addCard(building);
		effectResolutor.addCard(building);
		victoryPointsFromCardsEffect2.resolveEffect(effectResolutor);
		assertEquals(effectResolutor.getResourceAmount(ResourceType.VICTORY), 3);
	}

}
