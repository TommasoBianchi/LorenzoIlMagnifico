package it.polimi.ingsw.LM45.model.effects;

public class JumpFirstTurnEffect extends Effect {

	@Override
	public void resolveEffect(EffectResolutor effectResolutor) {
		effectResolutor.setHasToSkipFirstRound();
	}
	
	@Override
	public String toString() {
		return "Jump First Turn\n";
	}

}
