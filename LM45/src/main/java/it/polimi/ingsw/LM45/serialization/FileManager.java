package it.polimi.ingsw.LM45.serialization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.model.effects.CardEffect;
import it.polimi.ingsw.LM45.model.effects.Effect;

// Necessari per il main di test
import it.polimi.ingsw.LM45.model.effects.ResourceEffect;
import it.polimi.ingsw.LM45.model.effects.SlotModifierEffect;
import it.polimi.ingsw.LM45.model.cards.Cost;
import it.polimi.ingsw.LM45.model.cards.PeriodType;
import it.polimi.ingsw.LM45.model.effects.ActionEffect;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.model.cards.Territory;
import it.polimi.ingsw.LM45.model.cards.Character;

public class FileManager {

	private static final String BASE_PATH = "Assets/Json";
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting()
			.registerTypeAdapter(Effect.class, new GsonTypeAdapter<Effect>())
			.registerTypeAdapter(Card.class, new GsonTypeAdapter<Card>()).create();

	public static void saveCard(Card card) throws IOException {
		String path = BASE_PATH + "/Cards/" + card.getCardType();
		File directory = new File(path);

		if (!directory.exists()) {
			directory.mkdirs();
		}

		FileWriter writer = new FileWriter(path + "/" + card.getName() + ".json");
		GSON.toJson(card, Card.class, writer);
		writer.close();
	}

	public static Map<CardType, List<Card>> loadCards()
			throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		Map<CardType, List<Card>> deck = new HashMap<CardType, List<Card>>();
		File folder = new File(BASE_PATH + "/Cards/");

		for (File dir : folder.listFiles()) {
			if (dir.isDirectory()) {
				CardType cardType = CardType.valueOf(dir.getName());
				List<Card> cards = new ArrayList<Card>();

				for (File file : dir.listFiles()) {
					if (file.isFile()) {
						Card card = GSON.fromJson(new FileReader(file), Card.class);
						cards.add(card);
					}
				}

				deck.put(cardType, cards);
			}
		}

		return deck;
	}

	// Main di test
	public static void main(String[] args) {
		Card[] cards = new Card[48];

		// Territories
		// I Period
		cards[0] = new Territory("Avamposto Commerciale", PeriodType.I, new Cost(new Resource[] {}), CardEffect.EMPTY,
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.COINS, 1) })),
				1);
		cards[1] = new Territory("Bosco", PeriodType.I, new Cost(new Resource[] {}),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.WOOD, 1) })),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.WOOD, 1) })),
				2);
		cards[2] = new Territory("Borgo", PeriodType.I, new Cost(new Resource[] {}), CardEffect.EMPTY,
				new CardEffect(new ResourceEffect(new Resource[] {}, null, new Resource[] {
						new Resource(ResourceType.COINS, 1), new Resource(ResourceType.SERVANTS, 1) })),
				3);
		cards[3] = new Territory("Cava di Ghiaia", PeriodType.I, new Cost(new Resource[] {}),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.STONE, 2) })),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.STONE, 2) })),
				4);
		cards[4] = new Territory("Foresta", PeriodType.I, new Cost(new Resource[] {}),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.WOOD, 1) })),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.WOOD, 3) })),
				5);
		cards[5] = new Territory("Monastero", PeriodType.I, new Cost(new Resource[] {}),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.MILITARY, 2),
								new Resource(ResourceType.SERVANTS, 1) })),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.FAITH, 1), new Resource(ResourceType.STONE, 1) })),
				6);
		cards[6] = new Territory("Rocca", PeriodType.I, new Cost(new Resource[] {}), CardEffect.EMPTY,
				new CardEffect(new ResourceEffect(new Resource[] {}, null, new Resource[] {
						new Resource(ResourceType.MILITARY, 2), new Resource(ResourceType.STONE, 1) })),
				5);
		cards[7] = new Territory("Città", PeriodType.I, new Cost(new Resource[] {}),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.COINS, 3) })),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.COUNCIL_PRIVILEGES, 1) })),
				6);
		// II Period
		cards[8] = new Territory("Miniera d'Oro", PeriodType.II, new Cost(new Resource[] {}),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.COINS, 1) })),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.COINS, 2) })),
				1);
		cards[9] = new Territory("Villaggio Montano", PeriodType.II, new Cost(new Resource[] {}),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.SERVANTS, 1) })),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.MILITARY, 1), new Resource(ResourceType.WOOD, 2) })),
				3);
		cards[10] = new Territory("Villaggio Minerario", PeriodType.II, new Cost(new Resource[] {}),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.SERVANTS, 2),
								new Resource(ResourceType.STONE, 1) })),
				new CardEffect(new ResourceEffect(new Resource[] {}, null, new Resource[] {
						new Resource(ResourceType.SERVANTS, 1), new Resource(ResourceType.STONE, 2) })),
				4);
		cards[11] = new Territory("Cava di Pietra", PeriodType.II, new Cost(new Resource[] {}),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.WOOD, 1) })),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.STONE, 3) })),
				3);
		cards[12] = new Territory("Possedimento", PeriodType.II, new Cost(new Resource[] {}),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.SERVANTS, 2), new Resource(ResourceType.WOOD, 1) })),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.COINS, 1), new Resource(ResourceType.WOOD, 2) })),
				4);
		cards[13] = new Territory("Eremo", PeriodType.II, new Cost(new Resource[] {}),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.FAITH, 1) })),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.FAITH, 1) })),
				2);
		cards[14] = new Territory("Maniero", PeriodType.II, new Cost(new Resource[] {}), CardEffect.EMPTY,
				new CardEffect(new ResourceEffect(new Resource[] {}, null, new Resource[] {
						new Resource(ResourceType.MILITARY, 2), new Resource(ResourceType.SERVANTS, 2) })),
				5);
		cards[15] = new Territory("Ducato", PeriodType.II, new Cost(new Resource[] {}),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.COINS, 4) })),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.COINS, 1), new Resource(ResourceType.STONE, 1),
								new Resource(ResourceType.WOOD, 2) })),
				6);
		// III Period
		cards[16] = new Territory("Città Mercantile", PeriodType.III, new Cost(new Resource[] {}),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.COINS, 1),
								new Resource(ResourceType.SERVANTS, 1) })),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.COINS, 3) })),
				1);
		cards[17] = new Territory("Tenuta", PeriodType.III, new Cost(new Resource[] {}),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.VICTORY, 1), new Resource(ResourceType.WOOD, 1) })),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.VICTORY, 2), new Resource(ResourceType.WOOD, 2) })),
				3);
		cards[18] = new Territory("Colonia", PeriodType.III, new Cost(new Resource[] {}),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.MILITARY, 2) })),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.VICTORY, 4), new Resource(ResourceType.WOOD, 1) })),
				5);
		cards[19] = new Territory("Cava di Marmo", PeriodType.III, new Cost(new Resource[] {}),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.VICTORY, 3) })),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.VICTORY, 1), new Resource(ResourceType.STONE, 2) })),
				2);
		cards[20] = new Territory("Provincia", PeriodType.III, new Cost(new Resource[] {}),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.COUNCIL_PRIVILEGES, 1),
								new Resource(ResourceType.STONE, 1) })),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.VICTORY, 4), new Resource(ResourceType.STONE, 1) })),
				6);
		cards[21] = new Territory("Santuario", PeriodType.III, new Cost(new Resource[] {}),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.FAITH, 1) })),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.COINS, 1), new Resource(ResourceType.FAITH, 1) })),
				1);
		cards[22] = new Territory("Castello", PeriodType.III, new Cost(new Resource[] {}),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.VICTORY, 2), new Resource(ResourceType.COINS, 2) })),
				new CardEffect(new ResourceEffect(new Resource[] {}, null, new Resource[] {
						new Resource(ResourceType.MILITARY, 3), new Resource(ResourceType.SERVANTS, 1) })),
				4);
		cards[23] = new Territory("Città Fortificata", PeriodType.III, new Cost(new Resource[] {}),
				new CardEffect(new ResourceEffect(new Resource[] {}, null,
						new Resource[] { new Resource(ResourceType.MILITARY, 2),
								new Resource(ResourceType.SERVANTS, 1) })),
				new CardEffect(new ResourceEffect(new Resource[] {}, null, new Resource[] {
						new Resource(ResourceType.MILITARY, 1), new Resource(ResourceType.SERVANTS, 2) })),
				2);

		// Characters
		// I Period
		cards[24] = new Character("Predicatore", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 2) }),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 4) })),
				new CardEffect(new SlotModifierEffect(SlotType.ANY_CARD, true, false, false)));
		cards[25] = new Character("Costruttore", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 4) }), CardEffect.EMPTY,
				new CardEffect(new ActionEffect(SlotType.BUILDING, 2,
						new Resource[] { /* TODO: alternative discount */ })));
		cards[26] = new Character("Dama", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 4) }), CardEffect.EMPTY,
				new CardEffect(new ActionEffect(SlotType.CHARACTER, 2,
						new Resource[] { new Resource(ResourceType.COINS, 1) })));
		cards[27] = new Character("Cavaliere", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 2) }),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.COUNCIL_PRIVILEGES, 1) })),
				new CardEffect(new ActionEffect(SlotType.VENTURE, 2, new Resource[] {})));
		cards[28] = new Character("Condottiero", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 2) }),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.MILITARY, 3) })),
				new CardEffect(new ActionEffect(SlotType.TERRITORY, 2, new Resource[] {})));
		cards[29] = new Character("Artigiano", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 3) }), CardEffect.EMPTY,
				new CardEffect(new ActionEffect(SlotType.PRODUCTION, 2, new Resource[] {})));
		cards[30] = new Character("Contadino", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 3) }), CardEffect.EMPTY,
				new CardEffect(new ActionEffect(SlotType.HARVEST, 2, new Resource[] {})));
		cards[31] = new Character("Badessa", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 3) }),
				new CardEffect(new Effect[] { new ActionEffect(SlotType.ANY_CARD, 4, new Resource[] {}),
						new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 1) }) }, false),
				CardEffect.EMPTY);
		// II Period
		cards[32] = new Character("Mecenate", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 3) }),
				new CardEffect(new ActionEffect(SlotType.CHARACTER, 6,
						new Resource[] { new Resource(ResourceType.COINS, 2) })),
				CardEffect.EMPTY);
		cards[33] = new Character("Architetto", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 4) }),
				new CardEffect(new ActionEffect(SlotType.BUILDING, 6,
						new Resource[] { new Resource(ResourceType.WOOD, 1), new Resource(ResourceType.STONE, 1) })),
				CardEffect.EMPTY);
		cards[34] = new Character("Eroe", PeriodType.II,
				new Cost(
						new Resource[] { new Resource(ResourceType.COINS, 4) }),
				new CardEffect(new Effect[] { new ActionEffect(SlotType.VENTURE, 6, new Resource[] {}),
						new ResourceEffect(new Resource[] { new Resource(ResourceType.COUNCIL_PRIVILEGES, 1) }) },
						false),
				CardEffect.EMPTY);
		cards[35] = new Character("Capitano", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 4) }),
				new CardEffect(new Effect[] { new ActionEffect(SlotType.TERRITORY, 6, new Resource[] {}),
						new ResourceEffect(new Resource[] { new Resource(ResourceType.MILITARY, 2) }) }, false),
				CardEffect.EMPTY);
		cards[36] = new Character("Studioso", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 4) }), CardEffect.EMPTY,
				new CardEffect(new ActionEffect(SlotType.PRODUCTION, 3, new Resource[] {})));
		cards[37] = new Character("Messo Reale", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 5) }),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.COUNCIL_PRIVILEGES, 3) })),
				CardEffect.EMPTY);
		cards[38] = new Character("Messo Papale", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 5) }),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 3) })),
				CardEffect.EMPTY);
		cards[39] = new Character("Fattore", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 4) }), CardEffect.EMPTY,
				new CardEffect(new ActionEffect(SlotType.HARVEST, 3, new Resource[] {})));
		// III Period
		cards[40] = new Character("Ambasciatore", PeriodType.III,
				new Cost(
						new Resource[] { new Resource(ResourceType.COINS, 6) }),
				new CardEffect(new Effect[] { new ActionEffect(SlotType.ANY_CARD, 7, new Resource[] {}),
						new ResourceEffect(new Resource[] { new Resource(ResourceType.COUNCIL_PRIVILEGES, 1) }) },
						false),
				CardEffect.EMPTY);
		cards[41] = new Character("Generale", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 5) }),
				new CardEffect(new ResourceEffect(new Resource(ResourceType.MILITARY, 2),
						new Resource[] { new Resource(ResourceType.VICTORY, 1) })),
				CardEffect.EMPTY);
		cards[42] = new Character("Cortigiana", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 7) }),
				new CardEffect(new ResourceEffect(new Resource(ResourceType.CHARACTER, 1),
						new Resource[] { new Resource(ResourceType.VICTORY, 2) })),
				CardEffect.EMPTY);
		cards[43] = new Character("Araldo", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 6) }),
				new CardEffect(new ResourceEffect(new Resource(ResourceType.VENTURE, 1),
						new Resource[] { new Resource(ResourceType.VICTORY, 2) })),
				CardEffect.EMPTY);
		cards[44] = new Character("Nobile", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 6) }),
				new CardEffect(new ResourceEffect(new Resource(ResourceType.TERRITORY, 1),
						new Resource[] { new Resource(ResourceType.VICTORY, 2) })),
				CardEffect.EMPTY);
		cards[45] = new Character("Governatore", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 6) }),
				new CardEffect(new ResourceEffect(new Resource(ResourceType.BUILDING, 1),
						new Resource[] { new Resource(ResourceType.VICTORY, 2) })),
				CardEffect.EMPTY);
		cards[46] = new Character("Cardinale", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 4) }),
				new CardEffect(new Effect[] { new ActionEffect(SlotType.HARVEST, 4, new Resource[] {}),
						new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 2) }) }, false),
				CardEffect.EMPTY);
		cards[47] = new Character("Vescovo", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 5) }),
				new CardEffect(new Effect[] { new ActionEffect(SlotType.PRODUCTION, 4, new Resource[] {}),
						new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 1) }) }, false),
				CardEffect.EMPTY);

		try {
			for (Card card : cards)
				saveCard(card);

			Map<CardType, List<Card>> deck = loadCards();
			for (CardType cardType : new CardType[] { CardType.TERRITORY, CardType.CHARACTER }) {
				System.out.println("");
				System.out.println(cardType + ": ");
				System.out.println("");
				for (Card card : deck.get(cardType))
					System.out.println(card.getName() + " - Period " + card.getPeriodType());

				System.out.println("------------------");
				List<Card> shuffledCards = it.polimi.ingsw.LM45.util.ShuffleHelper
						.shuffle(deck.get(cardType));
				for (Card card : shuffledCards)
					System.out.println(card.getName() + " - Period " + card.getPeriodType());

				System.out.println("------------------");
				List<Card> shuffledByPeriodCards = it.polimi.ingsw.LM45.util.ShuffleHelper
						.shuffleByPeriod(deck.get(cardType));
				for (Card card : shuffledByPeriodCards)
					System.out.println(card.getName() + " - Period " + card.getPeriodType());
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
