package it.polimi.ingsw.LM45.model.cards;

import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.effects.CardEffect;

public class LeaderCard {
	
	private String name;
	private CardEffect effect;
	private Resource[] requisites;
	
	public LeaderCard(String name, CardEffect effect, Resource[] requisites){
		this.name = name;
		this.effect = effect;
		this.requisites = requisites;
	}
	
	public String getName(){
		return this.name;
	}
	
}
