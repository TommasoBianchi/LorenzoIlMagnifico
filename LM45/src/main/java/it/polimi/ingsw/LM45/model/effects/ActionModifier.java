package it.polimi.ingsw.LM45.model.effects;

import java.util.EnumMap;
import java.util.Map;

import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;

public class ActionModifier {

	private Map<ResourceType, Integer> costModifiers = new EnumMap<>(ResourceType.class);
	private Map<ResourceType, Integer> gainModifiers = new EnumMap<>(ResourceType.class);
	private int diceBonus;
	private boolean blockImmediateResources;
	private boolean canPlaceMultipleFamiliars;
	private boolean canPlaceFamiliars;
	
	public static final ActionModifier EMPTY(){
		return new ActionModifier(0);
	}

	public ActionModifier(Resource[] costModifiers, Resource[] gainModifiers, int diceBonus,
			boolean blockImmediateResources, boolean canPlaceMultipleFamiliars, boolean canPlaceFamiliars) {
		for (Resource resource : costModifiers)
			this.costModifiers.put(resource.getResourceType(),
					this.costModifiers.getOrDefault(resource.getResourceType(), 0) + resource.getAmount());

		for (Resource resource : gainModifiers)
			this.gainModifiers.put(resource.getResourceType(),
					this.gainModifiers.getOrDefault(resource.getResourceType(), 0) + resource.getAmount());

		this.diceBonus = diceBonus;
		this.blockImmediateResources = blockImmediateResources;
		this.canPlaceMultipleFamiliars = canPlaceMultipleFamiliars;
		this.canPlaceFamiliars = canPlaceFamiliars;
	}

	public ActionModifier(Resource[] costModifiers, Resource[] gainModifiers, int diceBonus) {
		this(costModifiers, gainModifiers, diceBonus, false, false, true);
	}
	
	public ActionModifier(Resource[] costModifiers) {
		this(costModifiers, new Resource[]{}, 0);
	}

	public ActionModifier(boolean blockImmediateResources, boolean canPlaceMultipleFamiliars,
			boolean canPlaceFamiliars) {
		this(new Resource[] {}, new Resource[] {}, 0, blockImmediateResources, canPlaceMultipleFamiliars,
				canPlaceFamiliars);
	}

	public ActionModifier(int diceBonus) {
		this(new Resource[] {}, new Resource[] {}, diceBonus);
	}

	public Map<ResourceType, Integer> getCostModifiers() {
		return this.costModifiers;
	}

	public Map<ResourceType, Integer> getGainModifiers() {
		return this.gainModifiers;
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
			this.costModifiers.put(resourceType,
					this.costModifiers.getOrDefault(resourceType, 0) + other.costModifiers.get(resourceType));

		for (ResourceType resourceType : other.gainModifiers.keySet())
			this.gainModifiers.put(resourceType,
					this.gainModifiers.getOrDefault(resourceType, 0) + other.gainModifiers.get(resourceType));

		this.diceBonus += other.diceBonus;

		this.blockImmediateResources = this.blockImmediateResources || other.blockImmediateResources;
		this.canPlaceMultipleFamiliars = this.canPlaceMultipleFamiliars || other.canPlaceMultipleFamiliars;
		this.canPlaceFamiliars = this.canPlaceFamiliars && other.canPlaceFamiliars;

		return this;
	}

	public boolean isEmpty() {
		return costModifiers.isEmpty() && gainModifiers.isEmpty() && diceBonus == 0 && !blockImmediateResources
				&& !canPlaceMultipleFamiliars && canPlaceFamiliars;
	}

}
