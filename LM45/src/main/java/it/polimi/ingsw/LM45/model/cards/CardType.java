package it.polimi.ingsw.LM45.model.cards;

import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.model.core.SlotType;

public enum CardType {
	TERRITORY(SlotType.TERRITORY, ResourceType.TERRITORY),
	BUILDING(SlotType.BUILDING, ResourceType.BUILDING),
	CHARACTER(SlotType.CHARACTER, ResourceType.CHARACTER),
	VENTURE(SlotType.VENTURE, ResourceType.VENTURE),
	ANY(SlotType.ANY_CARD, null);
	
	private SlotType slotType;
	private ResourceType resourceType;
	
	private CardType(SlotType slotType, ResourceType resourceType){
		this.slotType = slotType;
		this.resourceType = resourceType;
	}
	
	public SlotType toSlotType(){
		return this.slotType;
	}
	
	public ResourceType toResourceType(){
		return this.resourceType;
	}
}
