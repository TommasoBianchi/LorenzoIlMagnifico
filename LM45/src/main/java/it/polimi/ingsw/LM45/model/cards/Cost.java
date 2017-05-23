package it.polimi.ingsw.LM45.model.cards;

import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.effects.CardEffect;
import it.polimi.ingsw.LM45.model.effects.Effect;

public class Cost {
	
	public static final Cost EMPTY = new Cost(new Resource[]{});
	
	protected Resource[] cost;

	public Cost(Resource[] cost) {
		this.cost = cost;
	}
	
	public boolean canPlay(Player player){
		
	}
}
