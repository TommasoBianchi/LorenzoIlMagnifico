package it.polimi.ingsw.LM45.test.model.effects;

import it.polimi.ingsw.LM45.model.cards.PeriodType;
import it.polimi.ingsw.LM45.model.cards.Territory;
import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.effects.CardEffect;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;
import it.polimi.ingsw.LM45.model.effects.NoTerritoryRequisiteEffect;
import junit.framework.TestCase;
import testUtilities.FakeEffectResolutor;

public class NoTerritoryRequisiteEffectTest extends TestCase {

	public void testResolveEffect() {
		NoTerritoryRequisiteEffect noTerritoryRequisiteEffect = new NoTerritoryRequisiteEffect();
		EffectResolutor effectResolutor = new FakeEffectResolutor(new Player("Test", PlayerColor.BLUE));
		Territory territory = new Territory("", PeriodType.I, CardEffect.EMPTY, CardEffect.EMPTY, 2);
		
		effectResolutor.addCard(territory);
		effectResolutor.addCard(territory);
		assertFalse(effectResolutor.canAddCard(territory));
		noTerritoryRequisiteEffect.resolveEffect(effectResolutor);
		assertTrue(effectResolutor.canAddCard(territory));
		effectResolutor.addCard(territory);
		assertTrue(effectResolutor.canAddCard(territory));
		effectResolutor.addCard(territory);
		assertTrue(effectResolutor.canAddCard(territory));
		effectResolutor.addCard(territory);
		assertTrue(effectResolutor.canAddCard(territory));
		effectResolutor.addCard(territory);
		assertFalse(effectResolutor.canAddCard(territory));
	}

}
