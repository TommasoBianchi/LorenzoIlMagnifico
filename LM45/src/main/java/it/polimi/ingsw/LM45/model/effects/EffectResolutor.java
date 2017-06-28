package it.polimi.ingsw.LM45.model.effects;

import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.model.effects.modifiers.ActionModifier;

public interface EffectResolutor {
	
	public void addResources(Resource resource);
	public int getResourceAmount(ResourceType resourceType);
	public boolean hasResources(Resource resource);
	public void addChurchSupportBonus(Resource resource);
	public void addFamiliarBonus(FamiliarColor color, int bonus);
	public void setFamiliarValue(FamiliarColor color, int bonus);
	public void modifyServantCost(int servantBonusCostModifier);
	public void setHasToSkipFirstRound();
	public void noTerritoryRequisites();
	public void addPermanentEffect(CardEffect permanentEffect);
	public boolean canAddCard(Card card);
	public void addCard(Card card, ActionModifier actionModifier);
	public void harvest(int value);
	public void produce(int value);
	 
	public void doBonusAction(SlotType slotType, int diceNumber, Resource[] discount);
	public CardEffect copyEffect();
	
	public <T> T chooseFrom(T[] alternatives);

}
