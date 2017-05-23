package it.polimi.ingsw.LM45.model.core;

import java.util.List;

import it.polimi.ingsw.LM45.model.effects.ActionModifier;

public class Slot {

	protected Resource[] immediateBonus;
	protected int minDice;
	protected List<Familiar> familiars;
	protected boolean multipleFamiliars;
	protected boolean multipleFamiliarsOfSamePlayer;
	protected SlotType type;
	
	public void canAddFamiliar (Familiar familiar, ActionModifier actionModifier ){
		
	}
	
	public void addFamiliar(Familiar familiar, ActionModifier actionModifier){
		// TODO: implement
	}
	
	public void clearSlot(){
		for(Familiar familiar : familiars)
			familiar.setIsPlaced(false);
		familiars.clear();
	}
	
	public boolean isOccupied(){
		return familiars.size() > 1 || multipleFamiliars;
	} 
	
}
