package it.polimi.ingsw.LM45.model.effects.modifiers;

import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;

public class ResourceAdder extends ResourceModifier {
	
	private int increment;
	
	public ResourceAdder(ResourceType resourceType, int increment) {
		super(resourceType);
		this.increment = increment;
	}
	
	public ResourceAdder(Resource resource) {
		this(resource.getResourceType(), resource.getAmount());
	}

	@Override
	public Resource modify(Resource resource) {
		if(resourceType == resource.getResourceType())
			return resource.increment(increment);
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
	public ResourceAdder merge(ResourceAdder other){
		return new ResourceAdder(resourceType, increment + other.increment);
	}

	@Override
	public ResourceAdderMultiplier merge(ResourceMultiplier other){
		return new ResourceAdderMultiplier(resourceType, this, other);
	}

	@Override
	public ResourceAdderMultiplier merge(ResourceAdderMultiplier other){
		return other.merge(this);
	}
	
	@Override
	public String toString(){
		String sign = increment > 0 ? "+" : "-";
		return sign + Math.abs(increment) + " " + resourceType;
	}

}
