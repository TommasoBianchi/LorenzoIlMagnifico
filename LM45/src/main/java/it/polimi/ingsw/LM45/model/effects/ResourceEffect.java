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
		for (Resource resource : resourcesToPay){
			player.addResources(resource);
		}
		if(resourceToMultiply ==null ){
			for (Resource resource : resourcesToGain){
				player.addResources(resource);
			}
		} else {
			
			int multiplier = player.getResourceAmount(resourceToMultiply.getResourceType())/resourceToMultiply.getAmount();
			for (Resource resource : resourcesToGain){
				player.addResources(resource.multiply(multiplier));
			}
			
		}
	}

}
