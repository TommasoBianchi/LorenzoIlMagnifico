package it.polimi.ingsw.LM45.model.cards;

import java.util.Arrays;

import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.Resource;

public class CostWithPrerequisites extends Cost {

	private Resource[] prerequisites;
	
	public CostWithPrerequisites(Resource[] cost, Resource[] prerequisites){
		super(cost);
		this.prerequisites = prerequisites;
	}
	
	@Override
	public boolean canPay(Player player) {
		// Make sure to check if player has a positive amount of resources
		return super.canPay(player) && Arrays.stream(prerequisites).map(resource -> resource.getAmount() > 0 ? resource : resource.multiply(-1))
				.allMatch(resource -> player.hasResources(resource));
	}
}
