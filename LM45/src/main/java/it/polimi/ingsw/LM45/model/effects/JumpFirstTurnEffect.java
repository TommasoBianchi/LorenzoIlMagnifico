package it.polimi.ingsw.LM45.model.effects;

public class JumpFirstTurnEffect extends Effect {

	private static final long serialVersionUID = 1L;

	@Override
	public void resolveEffect(EffectResolutor effectResolutor) {
		effectResolutor.setHasToSkipFirstRound();
	}
	
	@Override
	public String toString() {
		return "Skip the first turn of every round";
	}

}
