package it.polimi.ingsw.LM45.model.effects;

import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.SlotType;

public class FamiliarEffect extends Effect {
	
	private int bonus;
	private boolean bonusIsToAdd;
	private FamiliarColor[] colors ;
	private int servantBonusCostModifier;
	

	@Override
	public void ResolveEffect(Player player) {
		if( bonusIsToAdd ){
			for ( FamiliarColor color:colors)
				player.addFamiliarBonus(color, bonus);
		} else {
			for ( FamiliarColor color : colors)
				player.setFamiliarValue(color, bonus);
		}
		
		if (servantBonusCostModifier != 1)
			player.modifyServantCost(servantBonusCostModifier);
	}
}
