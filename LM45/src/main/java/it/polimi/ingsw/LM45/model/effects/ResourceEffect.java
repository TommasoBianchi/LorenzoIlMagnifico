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
	
	public ResourceEffect(Resource[] resourcesToGain){
		this(new Resource[]{}, null, resourcesToGain);
	}
	
	public ResourceEffect(Resource[] resourcesToPay, Resource[] resourcesToGain){
		this(resourcesToPay, null, resourcesToGain);
	}	
	
	public ResourceEffect(Resource resourcesToMultiply, Resource[] resourcesToGain){
		this(new Resource[]{}, resourcesToMultiply, resourcesToGain);
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
