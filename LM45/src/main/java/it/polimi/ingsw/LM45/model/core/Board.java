package it.polimi.ingsw.LM45.model.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.model.cards.Excommunication;
import it.polimi.ingsw.LM45.model.cards.PeriodType;

public class Board {

	private Map<SlotType, Slot[]> slots;
	private Map<CardType, TowerSlot[]> towerSlots;
	private Map<PeriodType, Excommunication> excommunications;
	
	public Board(){
		this.slots = new HashMap<SlotType, Slot[]>(); // FIXME: maybe this is now not necessary?
		this.towerSlots = new HashMap<CardType, TowerSlot[]>();
		this.excommunications = new HashMap<PeriodType, Excommunication>();
	}

	public void clearTowers() {
		towerSlots.values().stream().flatMap(towerSlots -> Arrays.stream(towerSlots))
				.forEach(towerSlot -> towerSlot.clearSlot());
	}

	public void placeCard(Card card) {
		Arrays.stream(towerSlots.get(card.getCardType())).filter(towerSlot -> towerSlot.hasCard()).findFirst()
				.ifPresent(towerSlot -> towerSlot.placeCard(card));
	}

	public void placeExcommunication(Excommunication excommunication) {
		excommunications.put(excommunication.getPeriodType(), excommunication);
	}
}
