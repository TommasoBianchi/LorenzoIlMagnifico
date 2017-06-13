package it.polimi.ingsw.LM45.config;

import it.polimi.ingsw.LM45.model.core.PersonalBonusTile;

public class PersonalBonusTilesConfiguration implements Configuration {

	private PersonalBonusTile[] personalBonusTiles;
	
	public PersonalBonusTilesConfiguration(PersonalBonusTile[] personalBonusTiles){
		this.personalBonusTiles = personalBonusTiles.clone();
	}
	
	public PersonalBonusTile[] getPersonalBonusTiles(){
		return personalBonusTiles.clone();
	}
	
}
