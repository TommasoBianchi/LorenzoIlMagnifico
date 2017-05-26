package it.polimi.ingsw.LM45.model.core;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.model.effects.CardEffect;

public class PersonalBoard {
	
	private static final int MAX_CARDS = 6;

	private Map<CardType, List<Card>> cards;
	private Map<ResourceType, Integer> resources;
	private List<CardEffect> permanentEffects;
	private Resource[]  territoryRequisites;
	private List<Familiar> familiars;
	
	public void addResources(Resource resource){
		int newAmount = resources.getOrDefault(resource.getResourceType(), 0) + resource.getAmount();
		resources.put(resource.getResourceType(), newAmount);
	}
	
	public void removeResources(Resource resource){
		int newAmount = resources.getOrDefault(resource.getResourceType(), 0) - resource.getAmount();
		if(newAmount < 0) newAmount = 0;
		resources.put(resource.getResourceType(), newAmount);
	}
	
	public int getResourceAmount(ResourceType resourceType){
		return resources.getOrDefault(resourceType, 0);
	}	
	
	public boolean hasResources(Resource resource){
		return resources.getOrDefault(resource.getResourceType(), 0) >= resource.getAmount();
	}
	
	public boolean canAddCard(Card card){
		boolean canAdd = getResourceAmount(card.getCardType().toResourceType()) < MAX_CARDS;
		if(card.getCardType() == CardType.TERRITORY)
			canAdd = canAdd && hasResources(territoryRequisites[resources.getOrDefault(CardType.TERRITORY, 0)]);
		return canAdd;
	}
	
	public void addCard(Card card){
		if(cards.get(card.getCardType()) == null)
			cards.put(card.getCardType(), new ArrayList<Card>());
		cards.get(card.getCardType()).add(card);
		resources.put(card.getCardType().toResourceType(), resources.getOrDefault(card.getCardType().toResourceType(), 0) + 1);
	}
	
	public void clearTerritoryRequisites() {
		for(Resource resource :territoryRequisites ){
			resource = new Resource ( ResourceType.MILITARY , 0);
		}
	}	

	public void addPermanentEffect(CardEffect permanentEffect){
		permanentEffects.add(permanentEffect);
	}
	
}
