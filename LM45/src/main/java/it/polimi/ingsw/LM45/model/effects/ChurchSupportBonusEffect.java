package it.polimi.ingsw.LM45.model.effects;

import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.Resource;

public class ChurchSupportBonusEffect extends Effect {
	
	private Resource resource;

	@Override
	public void ResolveEffect(Player player) {
		player.addChurchSupportBonus(resource);
	}

}
