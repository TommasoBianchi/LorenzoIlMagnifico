package it.polimi.ingsw.LM45.model.core;

import it.polimi.ingsw.LM45.exceptions.IllegalActionException;
import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;
import it.polimi.ingsw.LM45.model.effects.modifiers.ActionModifier;
import it.polimi.ingsw.LM45.model.effects.modifiers.ResourceAdder;
import it.polimi.ingsw.LM45.model.effects.modifiers.ResourceModifier;

public class TowerSlot extends Slot {

	private Card card;
	private boolean hasCard;

	public TowerSlot(Resource[] immediateBonus, int minDice, SlotType type, boolean multipleFamiliars,
			boolean multipleFamiliarsOfSamePlayer) {
		super(immediateBonus, minDice, type, multipleFamiliars, multipleFamiliarsOfSamePlayer);
		this.card = null;
		this.hasCard = false;
	}

	public void placeCard(Card card) {
		hasCard = true;
		this.card = card;
	}

	public boolean hasCard() {
		return this.hasCard;
	}
	
	public Card getCard(){
		return card;
	}

	@Override
	public boolean canAddFamiliar(Familiar familiar, ActionModifier actionModifier, EffectResolutor effectResolutor) throws IllegalActionException {
		if(!hasCard)
			throw new IllegalActionException("Cannot place a familiar " + familiar.getFamiliarColor() + " because this slot's card has already been taken");
		
		if(hasToPayTower(familiar.getPlayer()))
			actionModifier.merge(new ActionModifier(new ResourceModifier[]{ new ResourceAdder(ResourceType.COINS, 3) }));
		
		boolean canAffordCard = card.canPick(effectResolutor, actionModifier);
		boolean playerCanPickCard = effectResolutor.canAddCard(card);
		
		if(!canAffordCard)
			throw new IllegalActionException("Cannot place a familiar " + familiar.getFamiliarColor() + " because you cannot afford the card on this slot");
		else if(!playerCanPickCard)
			throw new IllegalActionException("Cannot place a familiar " + familiar.getFamiliarColor() + " because you cannot pick " + card.getCardType() + "s");
		
		return super.canAddFamiliar(familiar, actionModifier, effectResolutor) && hasCard && canAffordCard && playerCanPickCard;
	}

	@Override
	public void addFamiliar(Familiar familiar, ActionModifier actionModifier, EffectResolutor effectResolutor) {
		super.addFamiliar(familiar, actionModifier, effectResolutor);
		card.payCost(effectResolutor, actionModifier);
		card.resolveImmediateEffect(effectResolutor);
		effectResolutor.addCard(card);
		this.hasCard = false;
	}
	
	@Override
	public void clearSlot(){
		super.clearSlot();
		hasCard = false;
		card = null;
	}
	
	private boolean hasToPayTower(Player player){
		long nonEmptyNeighbours = neighbouringSlots.stream().filter(slot -> !slot.isEmpty()).count();
		return nonEmptyNeighbours > 0 && player.getPayIfTowerIsOccupied();
	}

}
