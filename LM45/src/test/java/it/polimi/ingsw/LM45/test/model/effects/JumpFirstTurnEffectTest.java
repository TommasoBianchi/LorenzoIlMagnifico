package it.polimi.ingsw.LM45.test.model.effects;

import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;
import it.polimi.ingsw.LM45.model.effects.JumpFirstTurnEffect;
import junit.framework.TestCase;
import testUtilities.FakeEffectResolutor;

public class JumpFirstTurnEffectTest extends TestCase {

	public void testResolveEffect() {
		JumpFirstTurnEffect jumpFirstTurnEffect = new JumpFirstTurnEffect();
		Player player = new Player("Test", PlayerColor.BLUE);
		EffectResolutor effectResolutor = new FakeEffectResolutor(player);
		
		assertFalse(player.getHasToSkipFirstRound());
		jumpFirstTurnEffect.resolveEffect(effectResolutor);
		assertTrue(player.getHasToSkipFirstRound());
	}

}
