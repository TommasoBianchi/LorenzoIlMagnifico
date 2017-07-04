package it.polimi.ingsw.LM45.model.cards;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;
import it.polimi.ingsw.LM45.model.effects.modifiers.ActionModifier;
import it.polimi.ingsw.LM45.model.effects.modifiers.NilModifier;
import it.polimi.ingsw.LM45.model.effects.modifiers.ResourceModifier;

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
	 * @return the resources this cost is made of
	 */
	public Resource[] getResources(){
		return this.costResources.clone();
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
		Map<ResourceType, ResourceModifier> costModifiers = actionModifier.getCostModifiers();
		Set<ResourceType> modifiedResources = new HashSet<>();
		Stream<Resource> resourcesToPay = Arrays.stream(costResources).map(resource -> {
			Resource modifiedResource = costModifiers.getOrDefault(resource.getResourceType(), new NilModifier(resource.getResourceType())).modify(resource);
			modifiedResources.add(resource.getResourceType());
			return modifiedResource;
		});

		// Make sure you pay also resources in the costModifier that were not present in the original cost
		// (i.e. if this was an empty cost and the costModifier contains +3 COINS, than you have to pay them)
		resourcesToPay = Stream.concat(resourcesToPay, costModifiers.entrySet().stream()
				.filter(entry -> !modifiedResources.contains(entry.getKey())).map(entry -> entry.getValue().modify(new Resource(entry.getKey(), 0))));
		return resourcesToPay.filter(resource -> resource.getAmount() > 0);
	}

	@Override
	public String toString() {
		return Arrays.stream(costResources).map(Resource::toString).reduce((a, b) -> a + " " + b).orElse("");
	}
}
