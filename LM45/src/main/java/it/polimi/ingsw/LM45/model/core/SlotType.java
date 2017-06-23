package it.polimi.ingsw.LM45.model.core;

public enum SlotType {
	TERRITORY,
	BUILDING,
	CHARACTER,
	VENTURE,
	ANY_CARD,
	PRODUCTION,
	HARVEST,
	COUNCIL,
	MARKET,
	ANY;
	
	public boolean isCompatible(SlotType other){
		if(this == ANY || other == ANY)
			return true;
		if(this == ANY_CARD && (other == TERRITORY || other == BUILDING || other == CHARACTER || other == VENTURE))
			return true;
		if(other == ANY_CARD && (this == TERRITORY || this == BUILDING || this == CHARACTER || this == VENTURE))
			return true;
		return this == other;
	}
	
}
