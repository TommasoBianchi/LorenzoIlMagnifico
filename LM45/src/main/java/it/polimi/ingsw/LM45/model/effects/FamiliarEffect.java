package it.polimi.ingsw.LM45.model.effects;

import it.polimi.ingsw.LM45.model.core.FamiliarColor;

public class FamiliarEffect extends Effect {
	
	private int bonus;
	private boolean bonusIsToAdd;
	private FamiliarColor[] colors;
	private int servantBonusCostModifier;
	

	@Override
	public void resolveEffect(EffectResolutor effectResolutor) {
		if( bonusIsToAdd ){
			for ( FamiliarColor color:colors)
				effectResolutor.addFamiliarBonus(color, bonus);
		} else {
			for ( FamiliarColor color : colors)
				effectResolutor.setFamiliarValue(color, bonus);
		}
		
		if (servantBonusCostModifier != 1)
			effectResolutor.modifyServantCost(servantBonusCostModifier);
	}
	
}
