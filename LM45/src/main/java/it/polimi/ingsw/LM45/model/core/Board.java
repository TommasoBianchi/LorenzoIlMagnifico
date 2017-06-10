package it.polimi.ingsw.LM45.model.core;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import it.polimi.ingsw.LM45.exceptions.IllegalActionException;
import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.model.cards.Excommunication;
import it.polimi.ingsw.LM45.model.cards.PeriodType;

public class Board {

	private Map<SlotType, Slot[]> slots;
	private Map<SlotType, TowerSlot[]> towerSlots;
	private Map<PeriodType, Excommunication> excommunications;

	/**
	 * Initializes a new Board by instantiating the collections needed to hold slots and excommunications
	 */
	public Board() {
		this.slots = new EnumMap<SlotType, Slot[]>(SlotType.class); // FIXME: maybe this is now not necessary?
		this.towerSlots = new EnumMap<SlotType, TowerSlot[]>(SlotType.class);
		this.excommunications = new EnumMap<PeriodType, Excommunication>(PeriodType.class);

		// TEST
		for (SlotType slotType : new SlotType[] { SlotType.TERRITORY, SlotType.BUILDING, SlotType.CHARACTER, SlotType.VENTURE }) {
			TowerSlot[] tower = new TowerSlot[] { new TowerSlot(new Resource[] {}, 1, slotType, false, false),
					new TowerSlot(new Resource[] {}, 3, slotType, false, false), new TowerSlot(new Resource[] {}, 5, slotType, false, false),
					new TowerSlot(new Resource[] {}, 7, slotType, false, false) };
			for (TowerSlot towerSlot : tower)
				for (TowerSlot neighbourTowerSlot : tower)
					towerSlot.addNeighbouringSlot(neighbourTowerSlot);
			towerSlots.put(slotType, tower);
		}
		slots.put(SlotType.COUNCIL, new Slot[]{ new Slot(1, SlotType.COUNCIL, true, true) });
		// TEST
	}

	/**
	 * @param slotType the slotType we want to retrieve a slot from
	 * @param slotID the ID of the slot we want among all the slots of the given SlotType 
	 * @return the slot of ID slotID among all the slots of the given SlotType
	 * @throws IllegalActionException if there are no slots of the given SlotType or if the do not contain a slot with ID slotID
	 */
	public Slot getSlot(SlotType slotType, int slotID) throws IllegalActionException {
		if (slots.containsKey(slotType)) {
			Slot[] slotsOfThisType = slots.get(slotType);
			if (slotID < slotsOfThisType.length)
				return slotsOfThisType[slotID];
		}
		else if (towerSlots.containsKey(slotType)) {
			Slot[] towerSlotsOfThisType = towerSlots.get(slotType);
			if (slotID < towerSlotsOfThisType.length)
				return towerSlotsOfThisType[slotID];
		}

		throw new IllegalActionException("Board::getSlot -- Slot " + slotID + " of type " + slotType + " does not exist");
	}

	/**
	 * Clears all the slots on this board, i.e. removes cards and familiars present on them
	 */
	public void clearSlots() {
		towerSlots.values().stream().flatMap(slotsOfThisType -> Arrays.stream(slotsOfThisType)).forEach(towerSlot -> towerSlot.clearSlot());

		slots.values().stream().flatMap(slotsOfThisType -> Arrays.stream(slotsOfThisType)).forEach(slot -> slot.clearSlot());
	}

	/**
	 * @param card the card we want to place on this board
	 */
	public void placeCard(Card card) {
		Arrays.stream(towerSlots.get(card.getCardType().toSlotType())).filter(towerSlot -> towerSlot.hasCard()).findFirst()
				.ifPresent(towerSlot -> towerSlot.placeCard(card));
	}

	/**
	 * @return the list of all players that have a familiar on the councilSlot ordered
	 * by the time they have been put on it
	 */
	public List<Player> getCouncilOrder() {
		Slot councilSlot = slots.get(SlotType.COUNCIL)[0];
		Player[] playersInCouncilSlot = councilSlot.getPlayersInSlot();
		List<Player> orderedPlayers = new ArrayList<Player>();
		for (Player player : playersInCouncilSlot)
			// This is not the most efficient way, but it is not so bad considering we never have orderedPlayers.size() > 4
			if (!orderedPlayers.contains(player))
				orderedPlayers.add(player);
		return orderedPlayers;
	}

	/**
	 * @param excommunication the excommunication we want to place on this board
	 */
	public void placeExcommunication(Excommunication excommunication) {
		excommunications.put(excommunication.getPeriodType(), excommunication);
	}
}
