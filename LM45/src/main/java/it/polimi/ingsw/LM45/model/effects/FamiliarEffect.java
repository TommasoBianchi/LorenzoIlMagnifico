package it.polimi.ingsw.LM45.model.effects;

import java.util.Arrays;

import it.polimi.ingsw.LM45.model.core.FamiliarColor;

/**
 * This is an effect affecting the value of the familiars or the cost to increase by one their value
 * 
 * @author Tommy
 *
 */
public class FamiliarEffect extends Effect {

	private static final long serialVersionUID = 1L;
	
	private int bonus;
	private boolean bonusIsToAdd;
	private FamiliarColor[] colors;
	private int servantBonusCostModifier;
	
	/**
	 * @param bonus the value of the bonus
	 * @param bonusIsToAdd true if the bonus has to be added to the familiar's value, false if it directly sets the familiar's value
	 * @param colors an array containing all the familiarColors affected by this effect
	 * @param servantBonusCostModifier the new cost to increase by one the familiars' value
	 */
	public FamiliarEffect(int bonus, boolean bonusIsToAdd, FamiliarColor[] colors, int servantBonusCostModifier){
		this.bonus = bonus;
		this.bonusIsToAdd = bonusIsToAdd;
		this.colors = colors;
		this.servantBonusCostModifier = servantBonusCostModifier;
	}
	

	@Override
	public void resolveEffect(EffectResolutor effectResolutor) {
		if (servantBonusCostModifier != 1)
			effectResolutor.modifyServantCost(servantBonusCostModifier);
		else if( bonusIsToAdd ){
			for ( FamiliarColor color:colors)
				effectResolutor.addFamiliarBonus(color, bonus);
		} else {
			for ( FamiliarColor color : colors)
				effectResolutor.setFamiliarValue(color, bonus);
		}
	}
	
	@Override
	public String toString() {
		if(servantBonusCostModifier != 1)
			return "Spend " + servantBonusCostModifier + " servants to increase familiars value of 1 point";
		else if(bonusIsToAdd)
			return "+" + bonus + " bonus for familiars " +
					Arrays.stream(colors).map(FamiliarColor::toString).reduce((a,b) -> a + " " + b).orElse("");
		else
			return "Set value " + bonus + " for familiars " +
			Arrays.stream(colors).map(FamiliarColor::toString).reduce((a,b) -> a + " " + b).orElse("");
	}
	
}
