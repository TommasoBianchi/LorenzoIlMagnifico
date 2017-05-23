package it.polimi.ingsw.LM45.model.core;

public class PersonalBonusTile {
	
	private static final int MIN_DICE = 1;

	private Resource[] productionBonuses;
	private Resource[] harvestBonuses;
	
	public Resource[] produce (int diceAmount){
		if(diceAmount >= MIN_DICE)
			return productionBonuses;
		else
			return new Resource[]{};
	}	

	public Resource[] harvest (int diceAmount){
		if(diceAmount >= MIN_DICE)
			return harvestBonuses;
		else
			return new Resource[]{};
	}
}
