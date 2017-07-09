package it.polimi.ingsw.LM45.model.effects;

import java.util.Arrays;

import it.polimi.ingsw.LM45.model.core.Resource;

/**
 * This effect gives the player resolving it some resources
 * 
 * @author Tommy
 *
 */
public class ResourceEffect extends Effect {

	private static final long serialVersionUID = 1L;

	private Resource[] resourcesToPay;
	private Resource resourceToMultiply;
	private Resource[] resourcesToGain;

	/**
	 * @param resourcesToPay an array of resources to pay in order to resolve this effect
	 * @param resourceToMultiply the resource to count to multiply resourcesToGain
	 * @param resourcesToGain an array of resources gained when resolving this effect
	 */
	public ResourceEffect(Resource[] resourcesToPay, Resource resourceToMultiply, Resource[] resourcesToGain) {
		this.resourcesToPay = resourcesToPay;
		this.resourceToMultiply = resourceToMultiply;
		this.resourcesToGain = resourcesToGain;
	}

	/**
	 * Instantiate a resourceEffect that simply gives some resources when resolved
	 * 
	 * @param resourcesToGain an array of resources gained when resolving this effect
	 */
	public ResourceEffect(Resource[] resourcesToGain) {
		this(new Resource[] {}, null, resourcesToGain);
	}

	/**
	 * Instantiate a resourceEffect that gives some resources if some other are payed
	 * 
	 * @param resourcesToPay an array of resources to pay in order to resolve this effect
	 * @param resourcesToGain an array of resources gained when resolving this effect
	 */
	public ResourceEffect(Resource[] resourcesToPay, Resource[] resourcesToGain) {
		this(resourcesToPay, null, resourcesToGain);
	}

	/**
	 * Instantiate a resourceEffect that gives some resources multiplied by the amount of
	 * resourceToMultiply owned (i.e. if resourceToMultiply is 3 COINS and a player has 8 COINS,
	 * the resourceToGain are multiplied by 2)
	 * 
	 * @param resourcesToMultiply the resource to count to multiply resourcesToGain
	 * @param resourcesToGain an array of resources gained when resolving this effect
	 */
	public ResourceEffect(Resource resourcesToMultiply, Resource[] resourcesToGain) {
		this(new Resource[] {}, resourcesToMultiply, resourcesToGain);
	}

	@Override
	public boolean canResolveEffect(EffectResolutor effectResolutor) {
		for (Resource resource : resourcesToPay) {
			if(!effectResolutor.hasResources(resource))
				return false;
		}
		
		return super.canResolveEffect(effectResolutor);
	}

	@Override
	public void resolveEffect(EffectResolutor effectResolutor) {
		for (Resource resource : resourcesToPay) {
			effectResolutor.addResources(resource.multiply(-1)); // Pay a negative amount of resources
		}

		if (resourceToMultiply == null) {
			for (Resource resource : resourcesToGain) {
				effectResolutor.addResources(resource);
			}
		}
		else {
			int multiplier = effectResolutor.getResourceAmount(resourceToMultiply.getResourceType()) / resourceToMultiply.getAmount();
			for (Resource resource : resourcesToGain) {
				effectResolutor.addResources(resource.multiply(multiplier));
			}
		}
	}

	@Override
	public String toString() {
		String toPay = "";
		if (resourcesToPay.length > 0)
			toPay = Arrays.stream(resourcesToPay).map(Resource::toString).reduce("Pay ", (a, b) -> a + " " + b);
		String toGain = "";
		if (resourcesToGain.length > 0)
			toGain = Arrays.stream(resourcesToGain).map(Resource::toString).reduce("Gain ", (a, b) -> a + " " + b);

		if (resourceToMultiply != null)
			return toPay + " " + toGain + " X " + resourceToMultiply.toString();
		else
			return toPay + " " + toGain;
	}

}
