package it.polimi.ingsw.LM45.model.effects.modifiers;

import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;

public class ResourceMultiplier extends ResourceModifier {
	
	private int factor;
	
	public ResourceMultiplier(ResourceType resourceType, int factor) {
		super(resourceType);
		this.factor = factor;
	}
	
	public ResourceMultiplier(Resource resource) {
		this(resource.getResourceType(), resource.getAmount());
	}

	@Override
	public Resource modify(Resource resource) {
		if(resourceType == resource.getResourceType())
			return resource.multiply(factor);
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
	public ResourceMultiplier merge(ResourceMultiplier other){
		return new ResourceMultiplier(resourceType, factor * other.factor);
	}

	@Override
	public ResourceAdderMultiplier merge(ResourceAdder other){
		return new ResourceAdderMultiplier(resourceType, other, this);
	}

	@Override
	public ResourceAdderMultiplier merge(ResourceAdderMultiplier other){
		return other.merge(this);
	}
	
	@Override
	public String toString(){
		return "x" + factor + " " + resourceType;
	}

}
