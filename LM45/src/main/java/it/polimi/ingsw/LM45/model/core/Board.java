package it.polimi.ingsw.LM45.model.core;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import it.polimi.ingsw.LM45.config.BoardConfiguration;
import it.polimi.ingsw.LM45.exceptions.IllegalActionException;
import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.model.cards.Excommunication;
import it.polimi.ingsw.LM45.model.cards.PeriodType;

public class Board {

	private Map<SlotType, Slot[]> slots;
	private Map<SlotType, TowerSlot[]> towerSlots;
	private Map<PeriodType, Excommunication> excommunications;
	private Resource[][] churchSupportResources;

	/**
	 * Initializes a new Board by instantiating the collections needed to hold slots and excommunications plus the resources you receive when you support the Church.
	 */
	public Board(BoardConfiguration boardConfiguration) {
		this.slots = new EnumMap<SlotType, Slot[]>(SlotType.class);
		this.towerSlots = new EnumMap<SlotType, TowerSlot[]>(SlotType.class);
		this.excommunications = new EnumMap<PeriodType, Excommunication>(PeriodType.class);
		this.churchSupportResources = boardConfiguration.getChurchSupportResources();

		// Create the four towers
		for (SlotType slotType : new SlotType[] { SlotType.TERRITORY, SlotType.BUILDING, SlotType.CHARACTER, SlotType.VENTURE }) {
			TowerSlot[] tower = new TowerSlot[] { new TowerSlot(boardConfiguration.getSlotBonuses(slotType, 0), 1, slotType, false, false),
					new TowerSlot(boardConfiguration.getSlotBonuses(slotType, 1), 3, slotType, false, false),
					new TowerSlot(boardConfiguration.getSlotBonuses(slotType, 2), 5, slotType, false, false),
					new TowerSlot(boardConfiguration.getSlotBonuses(slotType, 3), 7, slotType, false, false) };
			for (TowerSlot towerSlot : tower) // Setup all the towerSlots in this tower as neighbours of each other
				for (TowerSlot neighbourTowerSlot : tower)
					towerSlot.addNeighbouringSlot(neighbourTowerSlot);
			towerSlots.put(slotType, tower);
		}

		// Create the production/harvest slots
		for (SlotType slotType : new SlotType[] { SlotType.PRODUCTION, SlotType.HARVEST }) {
			Slot smallSlot = new HarvestProductionSlot(boardConfiguration.getSlotBonuses(slotType, 0), 1, slotType, false, false);
			Slot bigSlot = new HarvestProductionSlot(boardConfiguration.getSlotBonuses(slotType, 1), 1, slotType, true, false, -3);
			smallSlot.addNeighbouringSlot(bigSlot);
			bigSlot.addNeighbouringSlot(smallSlot);
			slots.put(slotType, new Slot[] { smallSlot, bigSlot });
		}

		// Create the market slots
		Slot[] marketSlots = new Slot[] { new Slot(boardConfiguration.getSlotBonuses(SlotType.MARKET, 0), 1, SlotType.MARKET, false, false),
				new Slot(boardConfiguration.getSlotBonuses(SlotType.MARKET, 1), 1, SlotType.MARKET, false, false),
				new Slot(boardConfiguration.getSlotBonuses(SlotType.MARKET, 2), 1, SlotType.MARKET, false, false),
				new Slot(boardConfiguration.getSlotBonuses(SlotType.MARKET, 3), 1, SlotType.MARKET, false, false), };
		slots.put(SlotType.MARKET, marketSlots);

		// Create the council slot
		Slot councilSlot = new Slot(boardConfiguration.getSlotBonuses(SlotType.COUNCIL, 0), 1, SlotType.COUNCIL, true, true);
		slots.put(SlotType.COUNCIL, new Slot[] { councilSlot });
	}

	/**
	 * @param slotType
	 *            the slotType we want to retrieve a slot from
	 * @param slotID
	 *            the ID of the slot we want among all the slots of the given SlotType
	 * @return the slot of ID slotID among all the slots of the given SlotType
	 * @throws IllegalActionException
	 *             if there are no slots of the given SlotType or if the do not contain a slot with ID slotID
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
	 * @param card
	 *            the card we want to place on this board
	 */
	public void placeCard(Card card) {
		Arrays.stream(towerSlots.get(card.getCardType().toSlotType())).filter(towerSlot -> towerSlot.hasCard()).findFirst()
				.ifPresent(towerSlot -> towerSlot.placeCard(card));
	}

	/**
	 * @return the list of all players that have a familiar on the councilSlot ordered by the time they have been put on it
	 */
	public List<Player> getCouncilOrder() {
		if (!slots.containsKey(SlotType.COUNCIL) || slots.get(SlotType.COUNCIL).length == 0)
			return new ArrayList<Player>();

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
	 * @param excommunication
	 *            the excommunication we want to place on this board
	 */
	public void placeExcommunication(Excommunication excommunication) {
		excommunications.put(excommunication.getPeriodType(), excommunication);
	}
	
	/**
	 * @return an array containing the excommunications actually on this board
	 */
	public Excommunication[] getPlacedExcommunications(){
		return excommunications.values().stream().toArray(Excommunication[]::new);
	}

	/**
	 * @param faithPoints
	 *            how many faith points a player has
	 * @return the resources he'd gain by supporting the Church
	 */
	public Resource[] getChurchSupportResources(int faithPoints) {
		if (faithPoints < churchSupportResources.length)
			return churchSupportResources[faithPoints].clone();
		else
			return new Resource[] {};
	}

	/**
	 * @param type the cardType of the cards contained in the requested tower
	 * @return the cards present in the requested tower
	 */
	public Card[] getCardsOnTower(CardType type) {
		return Arrays.stream(towerSlots.get(type.toSlotType())).map(towerSlot -> towerSlot.getCard()).toArray(Card[]::new);
	}
}
