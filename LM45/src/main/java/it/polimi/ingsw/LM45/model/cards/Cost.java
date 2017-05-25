package it.polimi.ingsw.LM45.model.cards;

import java.util.Arrays;

import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.effects.CardEffect;
import it.polimi.ingsw.LM45.model.effects.Effect;

public class Cost {

	public static final Cost EMPTY = new Cost(new Resource[] {});

	protected Resource[] cost;

	public Cost(Resource[] cost) {
		this.cost = cost;
	}

	public boolean canPay(Player player) {
		// Make sure to check if player has a positive amount of resources
		return Arrays.stream(cost).map(resource -> resource.getAmount() > 0 ? resource : resource.multiply(-1))
				.allMatch(resource -> player.hasResources(resource));
	}

	public void pay(Player player) {
		// Make sure to add a negative amount of resources (to pay the cost)
		Arrays.stream(cost).map(resource -> resource.getAmount() > 0 ? resource.multiply(-1) : resource)
				.forEach(resource -> player.addResources(resource));
	}
}
