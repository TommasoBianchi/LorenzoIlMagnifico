package it.polimi.ingsw.LM45.model.core;

import java.util.List;
import java.util.Map;

import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.model.effects.CardEffect;

public class PersonalBoard {

	private Map<CardType, List<Card>> cards;
	private Map<ResourceType, Integer> resources;
	private List<CardEffect> permanentEffects;
	private Resource[]  territoryRequisites;
	private List<Familiar> familiars;
	
}
