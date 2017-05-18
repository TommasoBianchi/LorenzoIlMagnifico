package it.polimi.ingsw.LM45.model.core;

public class Resource {
	
	private ResourceType resourceType;
	private int amount;

	public Resource(ResourceType resourceType, int amount) {
		this.resourceType = resourceType;
		this.amount = amount;
	}

}
