package it.polimi.ingsw.LM45.model.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.LM45.exceptions.IllegalActionException;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;
import it.polimi.ingsw.LM45.model.effects.modifiers.ActionModifier;
import it.polimi.ingsw.LM45.model.effects.modifiers.NilModifier;
import it.polimi.ingsw.LM45.model.effects.modifiers.ResourceModifier;

public class Slot {

	protected Resource[] immediateBonus;
	protected int minDice;
	protected List<Familiar> familiars;
	protected boolean multipleFamiliars;
	protected boolean multipleFamiliarsOfSamePlayer;
	protected SlotType type;
	protected List<Slot> neighbouringSlots;

	/**
	 * @param immediateBonus
	 *            the resource immediately gained when a familiar is placed in this slot
	 * @param minDice
	 *            the minimum value of a familiar in order to be place in this slot
	 * @param type
	 *            the {@link SlotType} of this slot
	 * @param multipleFamiliars
	 *            true if multiple players can put their familiars in this slot
	 * @param multipleFamiliarsOfSamePlayer
	 *            true if even a single player can put multiple of his familiars in this slot
	 */
	public Slot(Resource[] immediateBonus, int minDice, SlotType type, boolean multipleFamiliars, boolean multipleFamiliarsOfSamePlayer) {
		this.immediateBonus = immediateBonus;
		this.minDice = minDice;
		this.familiars = new ArrayList<>();
		this.multipleFamiliars = multipleFamiliars;
		this.multipleFamiliarsOfSamePlayer = multipleFamiliarsOfSamePlayer;
		this.type = type;
		this.neighbouringSlots = new ArrayList<>();
	}

	/**
	 * This function is used to form groups of slots in which a player can place only one of his familiars
	 * 
	 * @param slot
	 *            a slot that is part of the same group as this one (i.e. it is a neighbour)
	 */
	public void addNeighbouringSlot(Slot slot) {
		neighbouringSlots.add(slot);
	}

	/**
	 * @param familiar
	 *            the familiar to check for this slot
	 * @param actionModifier
	 *            the actionModifier collected by doing this action in this moment
	 * @param effectResolutor
	 *            the effect resolutor for the player is trying to add a familiar
	 * @return true if it is legal to place the familiar in this slots with regards to the given actionModifier
	 * @throws IllegalActionException
	 *             if the familiar cannot be placed in this slot
	 */
	public boolean canAddFamiliar(Familiar familiar, ActionModifier actionModifier, EffectResolutor effectResolutor) throws IllegalActionException {
		boolean isFree = !isOccupied(familiar) || actionModifier.getCanPlaceMultipleFamiliars();
		boolean valueOK = isFamiliarValueOK(familiar, actionModifier);

		if (!isFree)
			throw new IllegalActionException(
					"Cannot place a familiar " + familiar.getFamiliarColor() + " because this slot (or a neighbouring one) is already occupied");
		else if (!valueOK)
			throw new IllegalActionException("Cannot place a familiar " + familiar.getFamiliarColor() + " because its value is not sufficient");

		return true;
	}

	/**
	 * @param familiar
	 *            the familiar to add in this slot
	 * @param actionModifier
	 *            the actionModifier collected by doing this action in this moment
	 * @param effectResolutor
	 *            the effectResolutor needed to add the immediateBonus to the player (subclass may use this to provide more interaction, even client-side)
	 */
	public void addFamiliar(Familiar familiar, ActionModifier actionModifier, EffectResolutor effectResolutor) {
		Map<ResourceType, ResourceModifier> gainModifiers = actionModifier.getGainModifiers();
		if (!actionModifier.getBlockImmediateResources())
			Arrays.stream(immediateBonus)
					.map(resource -> gainModifiers.getOrDefault(resource.getResourceType(), new NilModifier(resource.getResourceType()))
					.modify(resource)).forEach(resource -> effectResolutor.addResources(resource));
		if (familiar.getFamiliarColor() != FamiliarColor.BONUS)
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
	public Player[] getPlayersInSlot() {
		return familiars.stream().map(Familiar::getPlayer).toArray(Player[]::new);
	}

	/**
	 * @return the slotType of this slot
	 */
	public SlotType getType() {
		return this.type;
	}

	/**
	 * @return true if there are no familiars in this slot
	 */
	public boolean isEmpty() {
		return this.familiars.isEmpty();
	}

	private boolean isOccupied(Familiar familiar) {
		boolean hasFamiliarInNeighbouringSlots = false;
		if (familiar.getFamiliarColor() != FamiliarColor.UNCOLORED) {
			for (Slot neighbouringSlot : neighbouringSlots) {
				for (Familiar f : neighbouringSlot.familiars)
					if (f.getFamiliarColor() != FamiliarColor.UNCOLORED && f.getPlayer() == familiar.getPlayer()) {
						hasFamiliarInNeighbouringSlots = true;
						break;
					}
			}
		}

		return (familiars.size() > 0 && !multipleFamiliars) || (!multipleFamiliarsOfSamePlayer && hasFamiliarInNeighbouringSlots);
	}

	private boolean isFamiliarValueOK(Familiar familiar, ActionModifier actionModifier) {
		return familiar.getValue() + actionModifier.getDiceBonus() >= minDice;
	}

}
