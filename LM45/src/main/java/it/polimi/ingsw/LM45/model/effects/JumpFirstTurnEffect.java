package it.polimi.ingsw.LM45.model.effects;

public class JumpFirstTurnEffect extends Effect {

	@Override
	public void resolveEffect(EffectResolutor effectResolutor) {
		effectResolutor.setHasToSkipFirstTurn();
	}

}
