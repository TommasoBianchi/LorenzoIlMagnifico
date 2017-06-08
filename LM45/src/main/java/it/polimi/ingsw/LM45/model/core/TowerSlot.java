package it.polimi.ingsw.LM45.model.core;

import java.util.List;

import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.effects.ActionModifier;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;

public class TowerSlot extends Slot {

	public TowerSlot(Resource[] immediateBonus, int minDice, SlotType type, boolean multipleFamiliars,
			boolean multipleFamiliarsOfSamePlayer, List<Slot> neighbouringSlots) {
		super(immediateBonus, minDice, type, multipleFamiliars, multipleFamiliarsOfSamePlayer, neighbouringSlots);
		this.card = null;
		this.hasCard = false;
	}

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
	public void addFamiliar(Familiar familiar, ActionModifier actionModifier, EffectResolutor effectResolutor){
		super.addFamiliar(familiar, actionModifier, effectResolutor);
		effectResolutor.addCard(card);
		this.hasCard = false;
	}
	
}
