package it.polimi.ingsw.LM45.model.effects;

/**
 * This effect makes the player resolving it having to not satisfy anymore the requisites for the TERRITORY cards
 * 
 * @author Tommy
 *
 */
public class NoTerritoryRequisiteEffect extends Effect {

	private static final long serialVersionUID = 1L;

	@Override
	public void resolveEffect(EffectResolutor effectResolutor) {
		effectResolutor.noTerritoryRequisites();
	}
	
	@Override
	public String toString() {
		return "No more military requisites for Territories";
	}

}
