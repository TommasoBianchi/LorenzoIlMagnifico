package it.polimi.ingsw.LM45.model.core;

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

	public boolean canAddFamiliar(Familiar familiar, ActionModifier actionModifier) {
		return !isOccupied(familiar) || actionModifier.getCanPlaceMultipleFamiliars()
				&& isFamiliarValueOK(familiar, actionModifier);
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

	public void addFamiliar(Familiar familiar, ActionModifier actionModifier, EffectResolutor effectResolutor) {
		// FIXME: think about gain modifiers that multiply instead of increment
		// Think also about the slots that decrement the familiar value
		// Think also about production and harvest
		Map<ResourceType, Integer> gainModifiers = actionModifier.getGainModifiers();
		Arrays.stream(immediateBonus)
				.map(resource -> resource.increment(gainModifiers.getOrDefault(resource.getResourceType(), 0)))
				.forEach(resource -> effectResolutor.addResources(resource));
		familiars.add(familiar);
		familiar.setIsPlaced(true);
	}

	public void clearSlot() {
		for (Familiar familiar : familiars)
			familiar.setIsPlaced(false);
		familiars.clear();
	}

}
