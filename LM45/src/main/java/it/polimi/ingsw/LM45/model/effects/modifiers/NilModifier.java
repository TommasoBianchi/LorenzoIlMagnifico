package it.polimi.ingsw.LM45.model.effects.modifiers;

import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;

public class NilModifier extends ResourceModifier {

	public NilModifier(ResourceType resourceType) {
		super(resourceType);
	}

	@Override
	public Resource modify(Resource resource) {
		return resource;
	}

	@Override
	public ResourceModifier merge(ResourceModifier other) {
		return other;
	}

	@Override
	public ResourceModifier merge(ResourceAdder other) {
		return other;
	}

	@Override
	public ResourceModifier merge(ResourceMultiplier other) {
		return other;
	}

	@Override
	public ResourceModifier merge(ResourceAdderMultiplier other) {
		return other;
	}
	
	@Override
	public String toString(){
		return "";
	}

}
