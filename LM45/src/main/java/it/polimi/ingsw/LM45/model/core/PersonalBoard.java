package it.polimi.ingsw.LM45.model.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.model.cards.Territory;
import it.polimi.ingsw.LM45.model.effects.CardEffect;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;

public class PersonalBoard {

	private static final int MAX_CARDS = 6;

	private Map<CardType, List<Card>> cards;
	private Map<ResourceType, Integer> resources;
	private List<CardEffect> permanentEffects;
	private Resource[] territoryRequisites;

	/**
	 * Initializes a new Board by instantiating the collections needed to hold resources, cards and permanentEffects.
	 * In addition initializes the requisites for acquiring territory cards
	 */
	public PersonalBoard() {
		this.cards = new EnumMap<>(CardType.class);
		this.resources = new EnumMap<>(ResourceType.class);
		this.permanentEffects = new ArrayList<>();
		this.territoryRequisites = new Resource[] { new Resource(ResourceType.MILITARY, 0),
				new Resource(ResourceType.MILITARY, 0), new Resource(ResourceType.MILITARY, 3),
				new Resource(ResourceType.MILITARY, 7), new Resource(ResourceType.MILITARY, 12),
				new Resource(ResourceType.MILITARY, 18) };
	}

	/**
	 * @param resource the resource to add (it has to contain a positive amount)
	 */
	public void addResources(Resource resource) {
		int newAmount = resources.getOrDefault(resource.getResourceType(), 0) + resource.getAmount();
		resources.put(resource.getResourceType(), newAmount);
	}

	/**
	 * @param resource the resource to remove (it has to contain a negative amount)
	 */
	public void removeResources(Resource resource) {
		int newAmount = resources.getOrDefault(resource.getResourceType(), 0) - resource.getAmount();
		if (newAmount < 0)
			newAmount = 0;
		resources.put(resource.getResourceType(), newAmount);
	}

	/**
	 * @param resourceType the type of resource to count
	 * @return the amount of resources of the given ResourceType
	 */
	public int getResourceAmount(ResourceType resourceType) {
		return resources.getOrDefault(resourceType, 0);
	}
	
	/**
	 * @return an array containing all the resources this personalBoard contains
	 */
	public Resource[] getAllResources(){
		return resources.entrySet().stream().map(entry -> new Resource(entry.getKey(), entry.getValue())).toArray(Resource[]::new);
	}

	/**
	 * @param resource the type of resource to check
	 * @return true if the amount of resource of the same type of the parameter resource
	 * is at least the same as the amount of the parameter resource
	 */
	public boolean hasResources(Resource resource) {
		return resources.getOrDefault(resource.getResourceType(), 0) >= resource.getAmount();
	}

	/**
	 * @param card the card to check
	 * @return true if we can add that card to this personalBoard (including check about territoryRequisites)
	 */
	public boolean canAddCard(Card card) {
		boolean canAdd = getResourceAmount(card.getCardType().toResourceType()) < MAX_CARDS;
		if (card.getCardType() == CardType.TERRITORY)
			canAdd = canAdd && hasResources(territoryRequisites[resources.getOrDefault(CardType.TERRITORY, 0)]);
		return canAdd;
	}

	/**
	 * @param card the card we want to add to this personalBoard
	 */
	public void addCard(Card card) {
		if (cards.get(card.getCardType()) == null)
			cards.put(card.getCardType(), new ArrayList<Card>());
		cards.get(card.getCardType()).add(card);
		resources.put(card.getCardType().toResourceType(),
				resources.getOrDefault(card.getCardType().toResourceType(), 0) + 1);
	}

	/**
	 * Clears the requisites for the territory cards by setting them all to 0 military points
	 */
	public void clearTerritoryRequisites() {
		territoryRequisites = Arrays.stream(territoryRequisites).map(resource -> new Resource(ResourceType.MILITARY, 0)).toArray(Resource[]::new);
	}

	/**
	 * @param permanentEffect the CardEffect to add as a permanent effects
	 */
	public void addPermanentEffect(CardEffect permanentEffect) {
		permanentEffects.add(permanentEffect);
	}
	
	/**
	 * @param effectResolutor the effectResolutor we need in order to resolve the harvest effects
	 * @param value the value of the harvest action
	 */
	public void harvest(EffectResolutor effectResolutor, int value){
		if(cards.get(CardType.TERRITORY) != null)
			for(Card card : cards.get(CardType.TERRITORY)){
				card.resolveEffect(effectResolutor, value);
			}
	}

	/**
	 * @param effectResolutor the effectResolutor we need in order to resolve the production effects
	 * @param value the value of the production action
	 */
	public void produce(EffectResolutor effectResolutor, int value){
		if(cards.get(CardType.BUILDING) != null)
			for(Card card : cards.get(CardType.BUILDING)){
				card.resolveEffect(effectResolutor, value);
			}
	}
}
