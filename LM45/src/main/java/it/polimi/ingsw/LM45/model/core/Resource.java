package it.polimi.ingsw.LM45.model.core;

import java.io.Serializable;

/**
 * This class represents a certain amount of resources of a given resourceType.
 * It is an immutable class, i.e. its state can never change after initialization in the constructor.
 * 
 * @author Tommy
 */
public class Resource implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private ResourceType resourceType;
	private int amount;

	/**
	 * @param resourceType the type of this resource object
	 * @param amount the amount of resource of the given resourceType this resource object holds
	 */
	public Resource(ResourceType resourceType, int amount) {
		this.resourceType = resourceType;
		this.amount = amount;
	}
	
	/**
	 * @return the type of this resource object
	 */
	public ResourceType getResourceType(){
		return this.resourceType;
	}
	
	/**
	 * @return the amount of resource this resource object holds
	 */
	public int getAmount(){
		return this.amount;
	}
	
	/**
	 * @param value the value to add to the amount of resources hold in this object
	 * @return a new resource object containing the same resourceType and an incremented (or decremented if value < 0) amount
	 */
	public Resource increment(int value){
		return new Resource(resourceType, amount + value);
	}
	
	/**
	 * @param value the value to multiply to the amount of resources hold in this object
	 * @return a new resource object containing the same resourceType and a multiplicated amount
	 */
	public Resource multiply(int value){
		return new Resource(resourceType, amount * value);
	}
	
	/**
	 * Prints the amount of resources hold in this object followed by its resourceType
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return amount + " " + resourceType;
	}

}
