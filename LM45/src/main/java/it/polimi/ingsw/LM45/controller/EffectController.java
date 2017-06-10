package it.polimi.ingsw.LM45.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.model.effects.CardEffect;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;
import it.polimi.ingsw.LM45.network.server.ServerController;

public class EffectController implements EffectResolutor {
	
	private Player player;
	private ServerController serverController;
	
	public EffectController(Player player, ServerController serverController){
		this.player = player;
		this.serverController = serverController;
	}
	
	public void addResources(Resource resource){
		if(resource.getResourceType() == ResourceType.COUNCIL_PRIVILEGES){
			Resource[][] resourcesToChooseFrom = new Resource[][] { 
				new Resource[]{ new Resource(ResourceType.WOOD, 1), new Resource(ResourceType.STONE, 1) },
				new Resource[]{ new Resource(ResourceType.SERVANTS, 2) },
				new Resource[]{ new Resource(ResourceType.COINS, 2) },
				new Resource[]{ new Resource(ResourceType.MILITARY, 2) },
				new Resource[]{ new Resource(ResourceType.FAITH, 1) }
			};
			
			for(int i = 0; i < resource.getAmount(); i++){
				Resource[] choosenResources = chooseFrom(resourcesToChooseFrom);
				Arrays.stream(choosenResources).forEach(res -> player.addResources(res));
				resourcesToChooseFrom = Arrays.stream(resourcesToChooseFrom).filter(resources -> resources != choosenResources).toArray(Resource[][]::new);
			}
		}
		else {
			player.addResources(resource);
		}
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
		// TODO: implement
	}
	
	public void harvest(int value){
		player.harvest(this, value);
	}
	
	public void produce(int value){
		player.produce(this, value);
	}
	
	public CardEffect copyEffect(){
		// TODO: implement and remove exception
		throw new UnsupportedOperationException();
	}
	
	public <T> T chooseFrom(T[] alternatives){
		// TODO: implement and remove exception
		throw new UnsupportedOperationException();		
	}

}
