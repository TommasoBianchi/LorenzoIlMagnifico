package it.polimi.ingsw.LM45.model.effects;

public class NoTerritoryRequisiteEffect extends Effect {

	@Override
	public void resolveEffect(EffectResolutor effectResolutor) {
		effectResolutor.noTerritoryRequisites();
	}
	
	@Override
	public String toString() {
		return "No more military requisites if you have more than 2 Territory Cards\n";
	}

}
