package it.polimi.ingsw.LM45.model.effects;

import it.polimi.ingsw.LM45.model.core.Player;

public class NoTerritoryRequisiteEffect extends Effect {

	@Override
	public void ResolveEffect(Player player) {
		player.noTerritoryRequisites();
	}

}
