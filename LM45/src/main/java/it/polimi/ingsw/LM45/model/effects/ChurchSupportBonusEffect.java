package it.polimi.ingsw.LM45.model.effects;

import it.polimi.ingsw.LM45.model.core.Resource;

public class ChurchSupportBonusEffect extends Effect {

	private static final long serialVersionUID = 1L;
	
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
		return "Whenever you support the Church you gain an additional " + resource.toString();
	}

}
