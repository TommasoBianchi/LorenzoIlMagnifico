package it.polimi.ingsw.LM45.model.effects.modifiers;

import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;

public abstract class ResourceModifier {
	
	protected ResourceType resourceType;
	
	public ResourceModifier(ResourceType resourceType){
		this.resourceType = resourceType;
	}
	
	public ResourceType getResourceType(){
		return this.resourceType;
	}

	public abstract Resource modify(Resource resource);
	public abstract ResourceModifier merge(ResourceModifier other);
	
}
