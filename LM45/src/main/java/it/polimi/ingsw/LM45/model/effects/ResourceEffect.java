package it.polimi.ingsw.LM45.model.effects;

import java.util.Arrays;

import it.polimi.ingsw.LM45.model.core.Resource;

public class ResourceEffect extends Effect {

	private static final long serialVersionUID = 1L;

	private Resource[] resourcesToPay;
	private Resource resourceToMultiply;
	private Resource[] resourcesToGain;

	public ResourceEffect(Resource[] resourcesToPay, Resource resourceToMultiply, Resource[] resourcesToGain) {
		this.resourcesToPay = resourcesToPay;
		this.resourceToMultiply = resourceToMultiply;
		this.resourcesToGain = resourcesToGain;
	}

	public ResourceEffect(Resource[] resourcesToGain) {
		this(new Resource[] {}, null, resourcesToGain);
	}

	public ResourceEffect(Resource[] resourcesToPay, Resource[] resourcesToGain) {
		this(resourcesToPay, null, resourcesToGain);
	}

	public ResourceEffect(Resource resourcesToMultiply, Resource[] resourcesToGain) {
		this(new Resource[] {}, resourcesToMultiply, resourcesToGain);
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
