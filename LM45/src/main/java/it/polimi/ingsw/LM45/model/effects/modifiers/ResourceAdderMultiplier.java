package it.polimi.ingsw.LM45.model.effects.modifiers;

import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;

public class ResourceAdderMultiplier extends ResourceModifier {
	
	private ResourceAdder resourceAdder;
	private ResourceMultiplier resourceMultiplier;

	public ResourceAdderMultiplier(ResourceType resourceType, ResourceAdder resourceAdder, ResourceMultiplier resourceMultiplier) {
		super(resourceType);
		this.resourceAdder = resourceAdder;
		this.resourceMultiplier = resourceMultiplier;
	}
	
	@Override
	public Resource modify(Resource resource) {
		if(resourceType == resource.getResourceType())
			return resourceMultiplier.modify(resourceAdder.modify(resource));
		else
			return resource;
	}

	@Override
	public ResourceModifier merge(ResourceModifier other) {
		if(resourceType == other.resourceType)
			return other.merge(this);
		else
			return this;
	}

	@Override
	public ResourceAdderMultiplier merge(ResourceMultiplier other){
		return new ResourceAdderMultiplier(resourceType, resourceAdder, resourceMultiplier.merge(other));
	}

	@Override
	public ResourceAdderMultiplier merge(ResourceAdder other){
		return new ResourceAdderMultiplier(resourceType, resourceAdder.merge(other), resourceMultiplier);
	}

	@Override
	public ResourceAdderMultiplier merge(ResourceAdderMultiplier other){
		return new ResourceAdderMultiplier(resourceType, resourceAdder.merge(other.resourceAdder), resourceMultiplier.merge(other.resourceMultiplier));
	}
	
	@Override
	public String toString(){
		return resourceAdder.toString() + " and then " + resourceMultiplier.toString();
	}

}
