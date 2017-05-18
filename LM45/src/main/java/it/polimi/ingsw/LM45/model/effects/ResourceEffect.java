package it.polimi.ingsw.LM45.model.effects;

import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.SlotType;

public class ResourceEffect extends Effect {
	
	private Resource[] resourcesToPay;
	private Resource resourceToMultiply;
	private Resource[] resourcesToGain;
	
	public ResourceEffect(Resource[] resourcesToPay, Resource resourceToMultiply, Resource[] resourcesToGain) {
		this.resourcesToPay = resourcesToPay;
		this.resourceToMultiply = resourceToMultiply;
		this.resourcesToGain = resourcesToGain;
	}

	@Override
	public void ResolveEffect(Player player) {
		// TODO Auto-generated method stub

	}

}
