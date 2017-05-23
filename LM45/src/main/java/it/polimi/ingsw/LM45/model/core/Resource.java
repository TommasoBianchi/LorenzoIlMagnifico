package it.polimi.ingsw.LM45.model.core;

public class Resource {
	
	private ResourceType resourceType;
	private int amount;

	public Resource(ResourceType resourceType, int amount) {
		this.resourceType = resourceType;
		this.amount = amount;
	}
	
	public ResourceType getResourceType(){
		return this.resourceType;
	}
	
	public int getAmount(){
		return this.amount;
	}
	
	public Resource increment(int value){
		return new Resource(resourceType, amount + value);
	}
	
	public Resource multiply(int value){
		return new Resource(resourceType, amount * value);
	}

}
