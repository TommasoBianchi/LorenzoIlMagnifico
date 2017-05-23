package it.polimi.ingsw.LM45.model.core;

import java.util.Map;

import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.Excommunication;
import it.polimi.ingsw.LM45.model.cards.PeriodType;

public class Board {

	private Map<SlotType, Slot[]> slots;
	private Map<PeriodType, Excommunication> excommunications;
	
	public void placeCard(Card card){
		// TODO: implement
	}
	
	public void placeExcommunication(Excommunication excommunication){
		excommunications.put(excommunication.getPeriodType(), excommunication);
	}
}
