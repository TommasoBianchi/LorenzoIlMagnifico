package it.polimi.ingsw.LM45.model.effects;

import it.polimi.ingsw.LM45.model.core.Resource;

public class ChurchSupportBonusEffect extends Effect {
	
	private Resource resource;

	@Override
	public void resolveEffect(EffectResolutor effectResolutor) {
		effectResolutor.addChurchSupportBonus(resource);
	}

}
