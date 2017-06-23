package it.polimi.ingsw.LM45.model.cards;

import java.util.Arrays;

import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.effects.ActionModifier;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;

public class CostWithPrerequisites extends Cost {
	
	private static final long serialVersionUID = 1L;

	private Resource[] prerequisites;

	/**
	 * @param cost
	 *            the resources player needs to pay to pick the Card
	 * @param prerequisites
	 *            the resources player needs to have to pick the card
	 */
	public CostWithPrerequisites(Resource[] cost, Resource[] prerequisites) {
		super(cost);
		this.prerequisites = prerequisites;
	}

	@Override
	public boolean canPay(EffectResolutor effectResolutor, ActionModifier actionModifier) {
		// Make sure to check if player has a positive amount of resources
		return super.canPay(effectResolutor, actionModifier) && Arrays.stream(prerequisites)
				.map(resource -> resource.getAmount() > 0 ? resource : resource.multiply(-1)).allMatch(resource -> effectResolutor.hasResources(resource));
	}

	@Override
	public String toString() {
		String prerequisitesString = "(to pay this cost need the following requisites: "
				+ Arrays.stream(prerequisites).map(Resource::toString).reduce((a, b) -> a + " " + b).orElse("") + ")";
		return super.toString() + ((prerequisites.length > 0) ? prerequisitesString : "");
	}
}
