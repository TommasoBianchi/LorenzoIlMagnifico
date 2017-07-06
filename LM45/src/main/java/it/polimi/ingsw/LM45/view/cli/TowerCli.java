package it.polimi.ingsw.LM45.view.cli;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import it.polimi.ingsw.LM45.config.BoardConfiguration;
import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;

public class TowerCli {

	private CardType cardType;
	private TowerSlotCli[] slots;

	public TowerCli(CardType cardType, BoardConfiguration boardConfiguration) {
		this.cardType = cardType;
		this.slots = new TowerSlotCli[4];

		for (int i = 0; i < slots.length; i++)
			slots[i] = new TowerSlotCli(cardType, i, boardConfiguration.getSlotBonuses(cardType.toSlotType(), i));
	}

	public void print() {
		ConsoleWriter.println("");
		ConsoleWriter.println("--------------------");
		for (int i = slots.length - 1; i >= 0; i--) {
			slots[i].print();
			ConsoleWriter.println("--------------------");
		}
		ConsoleWriter.println("");
	}

	public void addCards(Card[] cards) {
		for (int i = 0; i < slots.length; i++)
			slots[i].setCard(cards[i]);
	}

	public void removeCard(Card card) {
		System.err.println("Removing card " + card.getName());
		for (int i = 0; i < slots.length; i++)
			if (slots[i].getCard() != null && slots[i].getCard().getName().equals(card.getName())) {
				slots[i].setCard(null);
				System.err.println("Removed");
				break;
			}
	}

	public void addFamiliar(int position, FamiliarColor familiarColor, PlayerColor playerColor) {
		slots[position].placeFamiliar(familiarColor, playerColor);
	}

	public void clearFamiliars() {
		Arrays.stream(slots).forEach(TowerSlotCli::clearFamiliars);
	}
	
	public List<SlotCli> getNonEmptySlots(){
		return Arrays.stream(slots).filter(towerSlotCli -> towerSlotCli.getCard() != null).collect(Collectors.toList());
	}

	private class TowerSlotCli extends SlotCli {

		private Card card;

		public TowerSlotCli(CardType cardType, int slotID, Resource[] immediateResources) {
			super(cardType.toSlotType(), slotID, immediateResources);
			this.card = null;
		}

		@Override
		public void print() {
			super.print();
			
			ConsoleWriter.println("");
			
			if (card == null)
				ConsoleWriter.println("No card on this slot");
			else
				ConsoleWriter.println("Card: " + card.toString());
		}

		public void setCard(Card card) {
			this.card = card;
		}

		public Card getCard() {
			return this.card;
		}

	}

}
