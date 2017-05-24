package it.polimi.ingsw.LM45.model.cards;

import java.util.Arrays;

import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.effects.CardEffect;
import it.polimi.ingsw.LM45.model.effects.Effect;

public class Cost {
	
	public static final Cost EMPTY = new Cost(new Resource[]{});
	
	protected Resource[] cost;

	public Cost(Resource[] cost) {
		// Make sure every cost is saved as a negative amount of resources
		this.cost = Arrays.stream(cost).map(res -> res.getAmount() < 0 ? res : res.multiply(-1)).toArray(Resource[]::new);
	}
	
	public boolean canPay(Player player){
		return Arrays.stream(cost).allMatch(resource -> player.hasResources(resource));
	}
	
	public void pay(Player player){
		Arrays.stream(cost).forEach(resource -> player.addResources(resource));
	}
}
