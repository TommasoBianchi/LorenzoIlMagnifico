package it.polimi.ingsw.LM45.model.effects.modifiers;

import java.util.EnumMap;
import java.util.Map;

import it.polimi.ingsw.LM45.model.core.ResourceType;

public class ActionModifier {

	private Map<ResourceType, ResourceModifier> costModifiers = new EnumMap<>(ResourceType.class);
	private Map<ResourceType, ResourceModifier> gainModifiers = new EnumMap<>(ResourceType.class);
	private int diceBonus;
	private boolean blockImmediateResources;
	private boolean canPlaceMultipleFamiliars;
	private boolean canPlaceFamiliars;

	public static final ActionModifier EMPTY() {
		return new ActionModifier(0);
	}

	public ActionModifier(ResourceModifier[] costModifiers, ResourceModifier[] gainModifiers, int diceBonus, boolean blockImmediateResources,
			boolean canPlaceMultipleFamiliars, boolean canPlaceFamiliars) {
		for (ResourceModifier resourceModifier : costModifiers)
			this.costModifiers.put(resourceModifier.getResourceType(), this.costModifiers
					.getOrDefault(resourceModifier.getResourceType(), new NilModifier(resourceModifier.getResourceType())).merge(resourceModifier));

		for (ResourceModifier resourceModifier : gainModifiers)
			this.gainModifiers.put(resourceModifier.getResourceType(), this.gainModifiers
					.getOrDefault(resourceModifier.getResourceType(), new NilModifier(resourceModifier.getResourceType())).merge(resourceModifier));

		this.diceBonus = diceBonus;
		this.blockImmediateResources = blockImmediateResources;
		this.canPlaceMultipleFamiliars = canPlaceMultipleFamiliars;
		this.canPlaceFamiliars = canPlaceFamiliars;
	}

	public ActionModifier(ResourceModifier[] costModifiers, ResourceModifier[] gainModifiers, int diceBonus) {
		this(costModifiers, gainModifiers, diceBonus, false, false, true);
	}

	public ActionModifier(ResourceModifier[] costModifiers) {
		this(costModifiers, new ResourceModifier[] {}, 0);
	}

	public ActionModifier(boolean blockImmediateResources, boolean canPlaceMultipleFamiliars, boolean canPlaceFamiliars) {
		this(new ResourceModifier[] {}, new ResourceModifier[] {}, 0, blockImmediateResources, canPlaceMultipleFamiliars, canPlaceFamiliars);
	}

	public ActionModifier(int diceBonus) {
		this(new ResourceModifier[] {}, new ResourceModifier[] {}, diceBonus);
	}

	public Map<ResourceType, ResourceModifier> getCostModifiers() {
		return new EnumMap<>(this.costModifiers);
	}

	public Map<ResourceType, ResourceModifier> getGainModifiers() {
		return new EnumMap<>(this.gainModifiers);
	}

	public int getDiceBonus() {
		return this.diceBonus;
	}

	public boolean getBlockImmediateResources() {
		return this.blockImmediateResources;
	}

	public boolean getCanPlaceMultipleFamiliars() {
		return this.canPlaceMultipleFamiliars;
	}

	public boolean getCanPlaceFamiliars() {
		return this.canPlaceFamiliars;
	}

	public ActionModifier merge(ActionModifier other) {
		for (ResourceType resourceType : other.costModifiers.keySet())
			this.costModifiers.put(resourceType, this.costModifiers.getOrDefault(resourceType, new NilModifier(resourceType)).merge(other.costModifiers.get(resourceType)));

		for (ResourceType resourceType : other.gainModifiers.keySet())
			this.gainModifiers.put(resourceType, this.gainModifiers.getOrDefault(resourceType, new NilModifier(resourceType)).merge(other.gainModifiers.get(resourceType)));

		this.diceBonus += other.diceBonus;

		this.blockImmediateResources = this.blockImmediateResources || other.blockImmediateResources;
		this.canPlaceMultipleFamiliars = this.canPlaceMultipleFamiliars || other.canPlaceMultipleFamiliars;
		this.canPlaceFamiliars = this.canPlaceFamiliars && other.canPlaceFamiliars;

		return this;
	}

	public boolean isEmpty() {
		return costModifiers.isEmpty() && gainModifiers.isEmpty() && diceBonus == 0 && !blockImmediateResources && !canPlaceMultipleFamiliars
				&& canPlaceFamiliars;
	}

	@Override
	public String toString() {
		String costModifierString = costModifiers.values().stream().map(ResourceModifier::toString)
				.reduce((a, b) -> a + " " + b).orElse("");
		String gainModifierString = gainModifiers.values().stream().map(ResourceModifier::toString)
				.reduce((a, b) -> a + " " + b).orElse("");

		String result = "";
		if (!costModifierString.equals(""))
			result += "Cost modifiers: " + costModifierString + "\n";
		if (!gainModifierString.equals(""))
			result += "Gain modifiers: " + gainModifierString + "\n";
		if (diceBonus != 0)
			result += "Dice bonus: " + diceBonus + "\n";
		if (blockImmediateResources)
			result += "You can not receive immediate resources\n";
		if (canPlaceMultipleFamiliars)
			result += "You can place multiple familiars\n";
		if (!canPlaceFamiliars)
			result += "You can not place familiars\n";

		return result;
	}

}
