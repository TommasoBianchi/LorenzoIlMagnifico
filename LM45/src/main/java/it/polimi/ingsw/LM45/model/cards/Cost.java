package it.polimi.ingsw.LM45.model.cards;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.model.effects.ActionModifier;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;

public class Cost implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final Cost EMPTY = new Cost(new Resource[] {});

	protected Resource[] costResources;

	/**
	 * @param costResources
	 *            the resources that compose this cost
	 */
	public Cost(Resource[] costResources) {
		this.costResources = costResources;
	}

	/**
	 * @param effectResolutor
	 *            the effectResolutor of the player whom resources you want to check
	 * @param actionModifier
	 *            the actionModifier for the action the player is trying to do
	 * @return true if the player can pay the cost modified (either increased or decreased) by the action modifier
	 */
	public boolean canPay(EffectResolutor effectResolutor, ActionModifier actionModifier) {
		return getResourcesToPay(actionModifier).allMatch(resource -> effectResolutor.hasResources(resource));
	}

	/**
	 * @param effectResolutor
	 *            the effectResolutor of the player which has to pay this cost
	 * @param actionModifier
	 *            the actionModifier for the action the player is trying to do
	 */
	public void pay(EffectResolutor effectResolutor, ActionModifier actionModifier) {
		getResourcesToPay(actionModifier).map(resource -> resource.getAmount() > 0 ? resource.multiply(-1) : resource) // Make sure to subtract resources
		 .forEach(resource -> effectResolutor.addResources(resource));
	}

	/**
	 * @return whether or not this is an empty cost
	 */
	public boolean isEmpty() {
		return costResources == null || costResources.length == 0;
	}

	private Stream<Resource> getResourcesToPay(ActionModifier actionModifier) {
		Map<ResourceType, Integer> costModifiers = actionModifier.getCostModifiers();
		Set<ResourceType> incrementedResources = new HashSet<>();
		Stream<Resource> resourcesToPay = Arrays.stream(costResources).map(resource -> {
			Resource incrementedResource = resource.increment(costModifiers.getOrDefault(resource.getResourceType(), 0));
			System.out.println(incrementedResource);
			incrementedResources.add(resource.getResourceType());
			return incrementedResource;
		});

		// Make sure you pay also resources in the costModifier that were not present in the original cost
		// (i.e. if this was an empty cost and the costModifier contains +3 COINS, than you have to pay them)
		resourcesToPay = Stream.concat(resourcesToPay, costModifiers.entrySet().stream()
				.filter(entry -> !incrementedResources.contains(entry.getKey())).map(entry -> new Resource(entry.getKey(), entry.getValue())));
		return resourcesToPay.filter(resource -> resource.getAmount() > 0);
	}

	@Override
	public String toString() {
		return Arrays.stream(costResources).map(Resource::toString).reduce((a, b) -> a + " " + b).orElse("");
	}
}
