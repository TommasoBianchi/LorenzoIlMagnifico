package it.polimi.ingsw.LM45.model.core;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import it.polimi.ingsw.LM45.exceptions.IllegalActionException;
import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.Excommunication;
import it.polimi.ingsw.LM45.model.cards.PeriodType;

public class Board {

	private Map<SlotType, Slot[]> slots;
	private Map<SlotType, TowerSlot[]> towerSlots;
	private Map<PeriodType, Excommunication> excommunications;
	
	public Board(){
		this.slots = new HashMap<SlotType, Slot[]>(); // FIXME: maybe this is now not necessary?
		this.towerSlots = new HashMap<SlotType, TowerSlot[]>();
		this.excommunications = new HashMap<PeriodType, Excommunication>();
	}
	
	public Slot getSlot(SlotType slotType, int slotID) throws IllegalActionException {
		if(slots.containsKey(slotType)){
			Slot[] slotsOfThisType = slots.get(slotType);
			if(slotID < slotsOfThisType.length)
				return slotsOfThisType[slotID];
		}
		else if (towerSlots.containsKey(slotType)){
			Slot[] towerSlotsOfThisType = towerSlots.get(slotType);
			if(slotID < towerSlotsOfThisType.length)
				return towerSlotsOfThisType[slotID];
		}
		
		throw new IllegalActionException("Board::getSlot -- Slot " + slotID + " of type " + slotType + " does not exist");
	}

	public void clearTowers() {
		towerSlots.values().stream().flatMap(towerSlots -> Arrays.stream(towerSlots))
				.forEach(towerSlot -> towerSlot.clearSlot());
	}

	public void placeCard(Card card) {
		Arrays.stream(towerSlots.get(card.getCardType().toSlotType())).filter(towerSlot -> towerSlot.hasCard()).findFirst()
				.ifPresent(towerSlot -> towerSlot.placeCard(card));
	}
	
	public List<Player> getCouncilOrder(){
		Slot councilSlot = slots.get(SlotType.COUNCIL)[0];
		Player[] playersInCouncilSlot = councilSlot.getPlayersInSlot();
		List<Player> orderedPlayers = new ArrayList<Player>();
		for(Player player : playersInCouncilSlot)
			// This is not the most efficient way, but it is okay considering we never have orderedPlayers.size() > 4
			if(!orderedPlayers.contains(player)) 
				orderedPlayers.add(player);
		return orderedPlayers;
	}

	public void placeExcommunication(Excommunication excommunication) {
		excommunications.put(excommunication.getPeriodType(), excommunication);
	}
}
