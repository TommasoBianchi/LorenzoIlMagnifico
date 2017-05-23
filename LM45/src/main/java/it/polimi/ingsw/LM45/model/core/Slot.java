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
	protected List<Slot> neighbouringSlots;
	
	public boolean canAddFamiliar (Familiar familiar, ActionModifier actionModifier){
		return !isOccupied(familiar) && isFamiliarValueOK(familiar, actionModifier);
	}
	
	private boolean isOccupied(Familiar familiar){
		boolean hasFamiliarInNeighbouringSlots = false;
		for(Slot neighbouringSlot : neighbouringSlots){
			for(Familiar f : neighbouringSlot.familiars)
				if(f.getFamiliarColor() != FamiliarColor.UNCOLORED && f.getPlayer() == familiar.getPlayer()){
					hasFamiliarInNeighbouringSlots = true;
					break;
				}
		}
		
		return (familiars.size() > 1 || multipleFamiliars) && (!multipleFamiliarsOfSamePlayer || hasFamiliarInNeighbouringSlots);
	}
	
	private boolean isFamiliarValueOK(Familiar familiar, ActionModifier actionModifier){
		return familiar.getValue() + actionModifier.getDiceBonus() >= minDice;
	}
	
	public void addFamiliar(Familiar familiar, ActionModifier actionModifier){
		// TODO: implement
	}
	
	public void clearSlot(){
		for(Familiar familiar : familiars)
			familiar.setIsPlaced(false);
		familiars.clear();
	} 
	
}
