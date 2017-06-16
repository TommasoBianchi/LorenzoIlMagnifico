package it.polimi.ingsw.LM45.model.effects;

import it.polimi.ingsw.LM45.model.core.Resource;

public class ChurchSupportBonusEffect extends Effect {
	
	private Resource resource;
	
	public ChurchSupportBonusEffect(Resource resource){
		this.resource = resource;
	}

	@Override
	public void resolveEffect(EffectResolutor effectResolutor) {
		effectResolutor.addChurchSupportBonus(resource);
	}
	
	@Override
	public String toString() {
		return "Church Support Bonus : " + resource.toString();
	}

}
