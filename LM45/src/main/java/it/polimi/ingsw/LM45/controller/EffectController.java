package it.polimi.ingsw.LM45.controller;

import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.model.effects.CardEffect;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;

public class EffectController implements EffectResolutor {
	
	private Player player;
	
	public void addResources(Resource resource){
		
	}
	
	public int getResourceAmount(ResourceType resourceType){
		return player.getResourceAmount(resourceType);
	}
	
	public void addChurchSupportBonus(Resource resource){
		player.addChurchSupportBonus(resource);
	}
	
	public void addFamiliarBonus(FamiliarColor color, int bonus){
		player.addFamiliarBonus(color, bonus);
	}
	
	public void setFamiliarValue(FamiliarColor color, int bonus){
		player.setFamiliarValue(color, bonus);
	}
	
	public void modifyServantCost(int servantBonusCostModifier){
		player.modifyServantCost(servantBonusCostModifier);
	}
	
	public void setHasToSkipFirstTurn(){
		player.setHasToSkipFirstTurn();
	}
	
	public void noTerritoryRequisites(){
		player.noTerritoryRequisites();
	}
	
	public void addPermanentEffect(CardEffect permanentEffect){
		player.addPermanentEffect(permanentEffect);
	}
	
	public void addCard(Card card){
		player.addCard(card);
	}
	 
	public void doBonusAction(SlotType slotType, int diceNumber, Resource[] discount){
		
	}
	
	public CardEffect copyEffect(){
		
	}
	
	public <T> T chooseFrom(T[] alternatives){
		
	}

}
