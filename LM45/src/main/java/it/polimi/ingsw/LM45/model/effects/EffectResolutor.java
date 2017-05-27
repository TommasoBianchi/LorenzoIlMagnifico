package it.polimi.ingsw.LM45.model.effects;

import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.model.core.SlotType;

public interface EffectResolutor {
	
	public void addResources(Resource resource);
	public int getResourceAmount(ResourceType resourceType);
	public void addChurchSupportBonus(Resource resource);
	public void addFamiliarBonus(FamiliarColor color, int bonus);
	public void setFamiliarValue(FamiliarColor color, int bonus);
	public void modifyServantCost(int servantBonusCostModifier);
	public void setHasToSkipFirstTurn();
	public void noTerritoryRequisites();
	public void addPermanentEffect(CardEffect permanentEffect);
	public void addCard(Card card);
	 
	public void doBonusAction(SlotType slotType, int diceNumber, Resource[] discount);
	public CardEffect copyEffect();
	
	public <T> T chooseFrom(T[] alternatives);

}
