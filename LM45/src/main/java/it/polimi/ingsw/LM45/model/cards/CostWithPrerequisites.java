package it.polimi.ingsw.LM45.model.cards;

import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.Resource;

public class CostWithPrerequisites extends Cost {

	private Resource[] prerequisites;
	
	public CostWithPrerequisites(Resource[] cost, Resource[] prerequisites){
		super(cost);
		this.prerequisites = prerequisites;
	}
	
	@Override
	public boolean canPlay(Player player){
		
	}
}
