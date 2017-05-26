package it.polimi.ingsw.LM45.model.core;

import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.effects.ActionModifier;

public class TowerSlot extends Slot {

	private Card card;
	private boolean hasCard;
	
	public void placeCard(Card card){
		hasCard = true;
		this.card = card;
	}
	
	public void removeCard(){
		hasCard = false;
	}
	
	public boolean hasCard(){
		return this.hasCard;
	}
	
	@Override
	public boolean canAddFamiliar(Familiar familiar, ActionModifier actionModifier){
		return super.canAddFamiliar(familiar, actionModifier) && hasCard && card.canPick(familiar.getPlayer());
	}
	
	@Override
	public void addFamiliar(Familiar familiar, ActionModifier actionModifier){
		super.addFamiliar(familiar, actionModifier);
		// TODO: implement
	}
	
}
