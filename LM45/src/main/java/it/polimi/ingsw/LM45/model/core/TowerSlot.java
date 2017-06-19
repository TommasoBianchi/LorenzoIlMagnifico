package it.polimi.ingsw.LM45.model.core;

import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.effects.ActionModifier;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;

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
	public boolean canAddFamiliar(Familiar familiar, ActionModifier actionModifier) {
		if(hasToPayTower(familiar.getPlayer()))
			actionModifier.merge(new ActionModifier(new Resource[]{ new Resource(ResourceType.COINS, 3) }));
		return super.canAddFamiliar(familiar, actionModifier) && hasCard && card.canPick(familiar.getPlayer(), actionModifier);
	}

	@Override
	public void addFamiliar(Familiar familiar, ActionModifier actionModifier, EffectResolutor effectResolutor) {
		super.addFamiliar(familiar, actionModifier, effectResolutor);
		if(hasToPayTower(familiar.getPlayer()))
			actionModifier.merge(new ActionModifier(new Resource[]{ new Resource(ResourceType.COINS, 3) }));
		effectResolutor.addCard(card, actionModifier);
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
