package it.polimi.ingsw.LM45.model.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.LM45.model.effects.ActionModifier;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;

public class Slot {

	protected Resource[] immediateBonus;
	protected int minDice;
	protected List<Familiar> familiars;
	protected boolean multipleFamiliars;
	protected boolean multipleFamiliarsOfSamePlayer;
	protected SlotType type;
	protected List<Slot> neighbouringSlots;
	
	/**
	 * @param immediateBonus the resource immediately gained when a familiar is placed in this slot
	 * @param minDice the minimum value of a familiar in order to be place in this slot
	 * @param type the {@link SlotType} of this slot
	 * @param multipleFamiliars true if multiple players can put their familiars in this slot
	 * @param multipleFamiliarsOfSamePlayer true if even a single player can put multiple of his familiars in this slot
	 */
	public Slot(Resource[] immediateBonus, int minDice, SlotType type, boolean multipleFamiliars, boolean multipleFamiliarsOfSamePlayer){
		this.immediateBonus = immediateBonus;
		this.minDice = minDice;
		this.familiars = new ArrayList<Familiar>();
		this.multipleFamiliars = multipleFamiliars;
		this.multipleFamiliarsOfSamePlayer = multipleFamiliarsOfSamePlayer;
		this.type = type;
		this.neighbouringSlots = new ArrayList<>();
	}

	/**
	 * @param minDice the minimum value of a familiar in order to be place in this slot
	 * @param type the {@link SlotType} of this slot
	 * @param multipleFamiliars true if multiple players can put their familiars in this slot
	 * @param multipleFamiliarsOfSamePlayer true if even a single player can put multiple of his familiars in this slot
	 */
	public Slot(int minDice, SlotType type, boolean multipleFamiliars, boolean multipleFamiliarsOfSamePlayer){
		this(new Resource[]{}, minDice, type, multipleFamiliars, multipleFamiliarsOfSamePlayer);
	}
	
	/**
	 * This function is used to form groups of slots in which a player can place only one of his familiars
	 * @param slot a slot that is part of the same group as this one (i.e. it is a neighbour)
	 */
	public void addNeighbouringSlot(Slot slot){
		neighbouringSlots.add(slot);
	}

	/**
	 * @param familiar the familiar to check for this slot
	 * @param actionModifier the actionModifier collected by doing this action in this moment
	 * @return true if it is legal to place the familiar in this slots with regards to the given actionModifier
	 */
	public boolean canAddFamiliar(Familiar familiar, ActionModifier actionModifier) {
		return !isOccupied(familiar) || actionModifier.getCanPlaceMultipleFamiliars()
				&& isFamiliarValueOK(familiar, actionModifier);
	}

	/**
	 * @param familiar the familiar to add in this slot
	 * @param actionModifier the actionModifier collected by doing this action in this moment
	 * @param effectResolutor the effectResolutor needed to add the immediateBonus to the player (subclass may use
	 * this to provide more interaction, even client-side)
	 */
	public void addFamiliar(Familiar familiar, ActionModifier actionModifier, EffectResolutor effectResolutor) {
		// FIXME: think about gain modifiers that multiply instead of increment
		// Think also about the slots that decrement the familiar value
		// Think also about production and harvest
		Map<ResourceType, Integer> gainModifiers = actionModifier.getGainModifiers();
		if(!actionModifier.getBlockImmediateResources())
			Arrays.stream(immediateBonus)
					.map(resource -> resource.increment(gainModifiers.getOrDefault(resource.getResourceType(), 0)))
					.forEach(resource -> effectResolutor.addResources(resource));
		familiars.add(familiar);
		familiar.setIsPlaced(true);
	}

	/**
	 * Remove every familiar standing on this slot and prepares it for the next turn
	 */
	public void clearSlot() {
		for (Familiar familiar : familiars)
			familiar.setIsPlaced(false);
		familiars.clear();
	}
	
	/**
	 * @return all the players that currently have a familiar standing on this slot
	 */
	public Player[] getPlayersInSlot(){
		return familiars.stream().map(familiar -> familiar.getPlayer()).toArray(Player[]::new);
	}
	
	private boolean isOccupied(Familiar familiar) {
		boolean hasFamiliarInNeighbouringSlots = false;
		for (Slot neighbouringSlot : neighbouringSlots) {
			for (Familiar f : neighbouringSlot.familiars)
				if (f.getFamiliarColor() != FamiliarColor.UNCOLORED && f.getPlayer() == familiar.getPlayer()) {
					hasFamiliarInNeighbouringSlots = true;
					break;
				}
		}

		return (familiars.size() > 1 || multipleFamiliars)
				&& (!multipleFamiliarsOfSamePlayer || hasFamiliarInNeighbouringSlots);
	}

	private boolean isFamiliarValueOK(Familiar familiar, ActionModifier actionModifier) {
		return familiar.getValue() + actionModifier.getDiceBonus() >= minDice;
	}

}
