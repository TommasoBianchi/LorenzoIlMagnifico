package it.polimi.ingsw.LM45.view.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.util.Pair;

public class TowerCli {

	private CardType cardType;
	private TowerSlotCli[] slots;

	public TowerCli(CardType cardType) {
		this.cardType = cardType;
		this.slots = new TowerSlotCli[4];

		for (int i = 0; i < slots.length; i++)
			slots[i] = new TowerSlotCli();
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
		for (int i = 0; i < slots.length; i++)
			if (slots[i].getCard() != null && slots[i].getCard().getName().equals(card.getName())) {
				slots[i].setCard(null);
				break;
			}
	}
	
	public void addFamiliar(int position, FamiliarColor familiarColor, PlayerColor playerColor) {
		slots[position].placeFamiliar(familiarColor, playerColor);
	}
	
	public void clearFamiliars(){
		Arrays.stream(slots).forEach(TowerSlotCli::clearFamiliars);
	}

	private class TowerSlotCli {

		private Card card;
		private List<Pair<FamiliarColor, PlayerColor>> familiars;

		public TowerSlotCli() {
			this.card = null;
			this.familiars = new ArrayList<>();
		}

		public void print() {
			if (card == null)
				ConsoleWriter.println("No card on this slot");
			else
				ConsoleWriter.println(card.toString());

			ConsoleWriter.println("");

			if (familiars.size() > 0)
				ConsoleWriter.println("Familiars: "
						+ familiars.stream().map(pair -> "familiar " + pair._1() + " by player " + pair._2()).reduce("", (a, b) -> a + " " + b));
			else
				ConsoleWriter.println("No familiars on this slot");
		}

		public void setCard(Card card) {
			this.card = card;
		}

		public Card getCard() {
			return this.card;
		}

		public void placeFamiliar(FamiliarColor familiarColor, PlayerColor playerColor) {
			this.familiars.add(new Pair<FamiliarColor, PlayerColor>(familiarColor, playerColor));
		}

		public void clearFamiliars() {
			this.familiars.clear();
		}

	}

}
