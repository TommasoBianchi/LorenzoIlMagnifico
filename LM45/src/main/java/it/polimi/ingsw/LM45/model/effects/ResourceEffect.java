package it.polimi.ingsw.LM45.model.effects;

import it.polimi.ingsw.LM45.model.core.Resource;

public class ResourceEffect extends Effect {

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
			effectResolutor.addResources(resource);
		}

		if (resourceToMultiply == null) {
			for (Resource resource : resourcesToGain) {
				effectResolutor.addResources(resource);
			}
		} 
		else {
			int multiplier = effectResolutor.getResourceAmount(resourceToMultiply.getResourceType())
					/ resourceToMultiply.getAmount();
			for (Resource resource : resourcesToGain) {
				effectResolutor.addResources(resource.multiply(multiplier));
			}
		}
	}

}
