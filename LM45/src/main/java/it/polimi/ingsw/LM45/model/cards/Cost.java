package it.polimi.ingsw.LM45.model.cards;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.model.effects.ActionModifier;

public class Cost implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static final Cost EMPTY = new Cost(new Resource[] {});

	protected Resource[] costResources;

	/**
	 * @param costResources the resources that compose this cost
	 */
	public Cost(Resource[] costResources) {
		this.costResources = costResources;
	}

	/**
	 * @param player the player whom resources you want to check
	 * @param actionModifier the actionModifier for the action the player is trying to do
	 * @return true if the player can pay the cost modified (either increased or decreased) by the action modifier
	 */
	public boolean canPay(Player player, ActionModifier actionModifier) {
		Map<ResourceType, Integer> costModifiers = actionModifier.getCostModifiers();
		Stream<Resource> resourcesToPay = Arrays.stream(costResources)
				.map(resource -> resource.increment(costModifiers.getOrDefault(resource.getResourceType(), 0)));
		return resourcesToPay.allMatch(resource -> player.hasResources(resource));
	}

	/**
	 * @param player the player which has to pay this cost
	 * @param actionModifier the actionModifier for the action the player is trying to do
	 */
	public void pay(Player player, ActionModifier actionModifier) {
		Map<ResourceType, Integer> costModifiers = actionModifier.getCostModifiers();
		Stream<Resource> resourcesToPay = Arrays.stream(costResources)
				.map(resource -> resource.getAmount() > 0 ? resource.multiply(-1) : resource) // To make sure to subtract resources
				.map(resource -> resource.increment(costModifiers.getOrDefault(resource.getResourceType(), 0)));
		resourcesToPay.forEach(resource -> player.addResources(resource));
	}
	
	@Override
	public String toString(){
		return Arrays.stream(costResources).map(resource -> resource.toString()).reduce((a, b) -> a + " " + b).orElse("");
	}
}
