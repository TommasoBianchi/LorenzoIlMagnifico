package it.polimi.ingsw.LM45.model.core;

import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.effects.ActionModifier;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;

public class TowerSlot extends Slot {

	public TowerSlot(Resource[] immediateBonus, int minDice, SlotType type, boolean multipleFamiliars,
			boolean multipleFamiliarsOfSamePlayer) {
		super(immediateBonus, minDice, type, multipleFamiliars, multipleFamiliarsOfSamePlayer);
		this.card = null;
		this.hasCard = false;
	}

	private Card card;
	private boolean hasCard;

	public void placeCard(Card card) {
		hasCard = true;
		this.card = card;
	}

	public boolean hasCard() {
		return this.hasCard;
	}

	@Override
	public boolean canAddFamiliar(Familiar familiar, ActionModifier actionModifier) {
		return super.canAddFamiliar(familiar, actionModifier) && hasCard && card.canPick(familiar.getPlayer());
	}

	@Override
	public void addFamiliar(Familiar familiar, ActionModifier actionModifier, EffectResolutor effectResolutor) {
		super.addFamiliar(familiar, actionModifier, effectResolutor);
		effectResolutor.addCard(card);
		this.hasCard = false;
	}
	
	@Override
	public void clearSlot(){
		super.clearSlot();
		hasCard = false;
		card = null;
	}

}
