package it.polimi.ingsw.LM45.model.cards;

import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.model.core.SlotType;

public enum CardType {
	TERRITORY(SlotType.TERRITORY, ResourceType.TERRITORY),
	BUILDING(SlotType.BUILDING, ResourceType.BUILDING),
	CHARACTER(SlotType.CHARACTER, ResourceType.CHARACTER),
	VENTURE(SlotType.VENTURE, ResourceType.VENTURE),
	ANY(SlotType.ANY_CARD, ResourceType.ANY_CARD);
	
	private SlotType slotType;
	private ResourceType resourceType;
	
	/**
	 * @param slotType the type of the Slot : BUILDING, TERRITORY, CHARACTER, VENTURE or ANY_CARD.
	 * @param resourceType the type of the Resource : BUILDING, TERRITORY, CHARACTER, VENTURE or ANY_CARD.
	 */
	private CardType(SlotType slotType, ResourceType resourceType){
		this.slotType = slotType;
		this.resourceType = resourceType;
	}
	
	/**
	 * @return the equivalent slotType.
	 */
	public SlotType toSlotType(){
		return this.slotType;
	}
	
	/**
	 * @return the equivalent resourceType.
	 */
	public ResourceType toResourceType(){
		return this.resourceType;
	}
}
