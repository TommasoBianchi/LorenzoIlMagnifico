package it.polimi.ingsw.LM45.model.effects;

import java.util.Arrays;

import it.polimi.ingsw.LM45.model.core.FamiliarColor;

public class FamiliarEffect extends Effect {
	
	private int bonus;
	private boolean bonusIsToAdd;
	private FamiliarColor[] colors;
	private int servantBonusCostModifier;
	
	public FamiliarEffect(int bonus, boolean bonusIsToAdd, FamiliarColor[] colors, int servantBonusCostModifier){
		this.bonus = bonus;
		this.bonusIsToAdd = bonusIsToAdd;
		this.colors = colors;
		this.servantBonusCostModifier = servantBonusCostModifier;
	}
	

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
	
	@Override
	public String toString() {
		if(servantBonusCostModifier != 1)
			return "Spend " + servantBonusCostModifier + " to increase familiars value of 1 point";
		else if(bonusIsToAdd)
			return "+" + bonus + " bonus for familiars " +
					Arrays.stream(colors).map(FamiliarColor -> FamiliarColor.toString()).reduce((a,b) -> a + " " + b).orElse("");
		else
			return "Set value " + bonus + " for familiars " +
			Arrays.stream(colors).map(FamiliarColor -> FamiliarColor.toString()).reduce((a,b) -> a + " " + b).orElse("");
	}
	
}
