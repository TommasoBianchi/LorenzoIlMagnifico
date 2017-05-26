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

import it.polimi.ingsw.LM45.model.cards.Building;
import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.model.effects.CardEffect;
import it.polimi.ingsw.LM45.model.effects.Effect;

// Necessari per il main di test
import it.polimi.ingsw.LM45.model.effects.ResourceEffect;
import it.polimi.ingsw.LM45.model.effects.SlotModifierEffect;
import it.polimi.ingsw.LM45.model.cards.Cost;
import it.polimi.ingsw.LM45.model.cards.CostWithPrerequisites;
import it.polimi.ingsw.LM45.model.cards.PeriodType;
import it.polimi.ingsw.LM45.model.effects.ActionEffect;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.model.cards.Territory;
import it.polimi.ingsw.LM45.model.cards.Venture;
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
		Card[] cards = new Card[96];

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
				new CardEffect(new SlotModifierEffect(SlotType.ANY_CARD, true, false, false), true));
		cards[25] = new Character("Costruttore", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 4) }), CardEffect.EMPTY,
				new CardEffect(new Effect[] {
						new ActionEffect(SlotType.BUILDING, 2, new Resource[] { new Resource(ResourceType.WOOD, 1) }),
						new ActionEffect(SlotType.BUILDING, 2, new Resource[] { new Resource(ResourceType.STONE, 1) }) },
						true, true));
		cards[26] = new Character("Dama", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 4) }), CardEffect.EMPTY,
				new CardEffect(new ActionEffect(SlotType.CHARACTER, 2,
						new Resource[] { new Resource(ResourceType.COINS, 1) }), true));
		cards[27] = new Character("Cavaliere", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 2) }),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.COUNCIL_PRIVILEGES, 1) })),
				new CardEffect(new ActionEffect(SlotType.VENTURE, 2, new Resource[] {}), true));
		cards[28] = new Character("Condottiero", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 2) }),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.MILITARY, 3) })),
				new CardEffect(new ActionEffect(SlotType.TERRITORY, 2, new Resource[] {}), true));
		cards[29] = new Character("Artigiano", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 3) }), CardEffect.EMPTY,
				new CardEffect(new ActionEffect(SlotType.PRODUCTION, 2, new Resource[] {}), true));
		cards[30] = new Character("Contadino", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 3) }), CardEffect.EMPTY,
				new CardEffect(new ActionEffect(SlotType.HARVEST, 2, new Resource[] {}), true));
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
				new CardEffect(new ActionEffect(SlotType.PRODUCTION, 3, new Resource[] {}), true));
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
				new CardEffect(new ActionEffect(SlotType.HARVEST, 3, new Resource[] {}), true));
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

		// Ventures
		// I Period
		cards[48] = new Venture("Campagna Militare", PeriodType.I,
				new CostWithPrerequisites(new Resource[] { new Resource(ResourceType.MILITARY, 2) },
						new Resource[] { new Resource(ResourceType.MILITARY, 3) }),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.COINS, 3) }) },
						false),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 5) }) },
						false),
				Cost.EMPTY);
		cards[49] = new Venture("Ingaggiare Reclute", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 4) }),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.MILITARY, 5) }) },
						false),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 4) }) },
						false),
				Cost.EMPTY);
		cards[50] = new Venture("Innalzare una Statua", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 2), new Resource(ResourceType.STONE, 2) }),
				new CardEffect(new Effect[] {
						new ResourceEffect(new Resource[] { new Resource(ResourceType.COUNCIL_PRIVILEGES, 2) }) },
						false),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 4) }) },
						false),
				Cost.EMPTY);
		cards[51] = new Venture("Combattere le Eresie", PeriodType.I,
				new CostWithPrerequisites(new Resource[] { new Resource(ResourceType.MILITARY, 3) },
						new Resource[] { new Resource(ResourceType.MILITARY, 5) }),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 2) }) },
						false),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 5) }) },
						false),
				Cost.EMPTY);
		cards[52] = new Venture("Sostegno al Vescovo", PeriodType.I,
				new CostWithPrerequisites(new Resource[] { new Resource(ResourceType.MILITARY, 2) },
						new Resource[] { new Resource(ResourceType.MILITARY, 4) }),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 3) }) },
						false),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 1) }) },
						false),
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 1), new Resource(ResourceType.STONE, 1),
						new Resource(ResourceType.COINS, 2) }));
		cards[53] = new Venture("Ospitare i Mendicanti", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 3) }),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.SERVANTS, 4) }) },
						false),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 4) }) },
						false),
				Cost.EMPTY);
		cards[54] = new Venture("Riparare la Chiesa", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 1), new Resource(ResourceType.STONE, 1),
						new Resource(ResourceType.COINS, 1) }),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 1) }) },
						false),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 5) }) },
						false),
				Cost.EMPTY);
		cards[55] = new Venture("Costruire le Mura", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.STONE, 3) }),
				new CardEffect(new Effect[] { new ResourceEffect(new Resource[] {
						new Resource(ResourceType.MILITARY, 2), new Resource(ResourceType.COUNCIL_PRIVILEGES, 1) }) },
						false),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 3) }) },
						false),
				Cost.EMPTY);

		// II Period
		cards[56] = new Venture("Sostegno al Cardinale", PeriodType.II,
				new CostWithPrerequisites(new Resource[] { new Resource(ResourceType.MILITARY, 4) },
						new Resource[] { new Resource(ResourceType.MILITARY, 7) }),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 3) }) },
						false),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 4) }) },
						false),
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 2), new Resource(ResourceType.STONE, 2),
						new Resource(ResourceType.COINS, 3) }));
		cards[57] = new Venture("Crociata", PeriodType.II,
				new CostWithPrerequisites(new Resource[] { new Resource(ResourceType.MILITARY, 4) },
						new Resource[] { new Resource(ResourceType.MILITARY, 8) }),
				new CardEffect(new Effect[] { new ResourceEffect(
						new Resource[] { new Resource(ResourceType.FAITH, 1), new Resource(ResourceType.COINS, 5) }) },
						false),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 5) }) },
						false),
				Cost.EMPTY);
		cards[58] = new Venture("Supporto al Re", PeriodType.II,
				new CostWithPrerequisites(new Resource[] { new Resource(ResourceType.MILITARY, 3) },
						new Resource[] { new Resource(ResourceType.MILITARY, 6) }),
				new CardEffect(new Effect[] { new ResourceEffect(new Resource[] {
						new Resource(ResourceType.COUNCIL_PRIVILEGES, 1), new Resource(ResourceType.COINS, 5) }) },
						false),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 3) }) },
						false),
				Cost.EMPTY);
		cards[59] = new Venture("Scavare Canalizzazioni", PeriodType.II,
				new Cost(
						new Resource[] { new Resource(ResourceType.SERVANTS, 2), new Resource(ResourceType.COINS, 3) }),
				new CardEffect(new Effect[] { new ActionEffect(SlotType.HARVEST, 4) }, false),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 5) }) },
						false),
				Cost.EMPTY);
		cards[60] = new Venture("Costruire i Bastioni", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.STONE, 4) }),
				new CardEffect(new Effect[] { new ResourceEffect(new Resource[] {
						new Resource(ResourceType.MILITARY, 3), new Resource(ResourceType.COUNCIL_PRIVILEGES, 1) }) },
						false),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 2) }) },
						false),
				Cost.EMPTY);
		cards[61] = new Venture("Ingaggiare Soldati", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 6) }),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.MILITARY, 6) }) },
						false),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 5) }) },
						false),
				Cost.EMPTY);
		cards[62] = new Venture("Accogliere gli Stranieri", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 4) }),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.SERVANTS, 5) }) },
						false),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 4) }) },
						false),
				Cost.EMPTY);
		cards[63] = new Venture("Riparare l'Abbazia", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 2), new Resource(ResourceType.COINS, 2),
						new Resource(ResourceType.STONE, 2) }),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 2) }) },
						false),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 6) }) },
						false),
				Cost.EMPTY);

		// III Period
		cards[64] = new Venture("Sostegno al Papa", PeriodType.III,
				new CostWithPrerequisites(new Resource[] { new Resource(ResourceType.MILITARY, 5) },
						new Resource[] { new Resource(ResourceType.MILITARY, 10) }),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 2) }) },
						false),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 10) }) },
						false),
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 3), new Resource(ResourceType.STONE, 3),
						new Resource(ResourceType.COINS, 4) }));
		cards[65] = new Venture("Migliorare le Strade", PeriodType.III,
				new Cost(
						new Resource[] { new Resource(ResourceType.SERVANTS, 3), new Resource(ResourceType.COINS, 4) }),
				new CardEffect(new Effect[] { new ActionEffect(SlotType.HARVEST, 3) }, false),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 5) }) },
						false),
				Cost.EMPTY);
		cards[66] = new Venture("Riparare la Cattedrale", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 3), new Resource(ResourceType.WOOD, 3),
						new Resource(ResourceType.STONE, 3) }),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 1) }),
								new ActionEffect(SlotType.ANY_CARD, 7) },
						false),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 5) }) },
						false),
				Cost.EMPTY);
		cards[67] = new Venture("Conquista Militare", PeriodType.III,
				new CostWithPrerequisites(new Resource[] { new Resource(ResourceType.MILITARY, 6) },
						new Resource[] { new Resource(ResourceType.MILITARY, 12) }),
				new CardEffect(new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.WOOD, 3),
						new Resource(ResourceType.STONE, 3), new Resource(ResourceType.COINS, 3) }) }, false),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 7) }) },
						false),
				Cost.EMPTY);
		cards[68] = new Venture("Guerra Santa", PeriodType.III,
				new CostWithPrerequisites(new Resource[] { new Resource(ResourceType.MILITARY, 8) },
						new Resource[] { new Resource(ResourceType.MILITARY, 15) }),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 4) }) },
						false),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 8) }) },
						false),
				Cost.EMPTY);
		cards[69] = new Venture("Ingaggiare Mercenari", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 8) }),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.MILITARY, 7) }) },
						false),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 6) }) },
						false),
				Cost.EMPTY);
		cards[70] = new Venture("Commissionare Arte Sacra", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 6) }),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 3) }) },
						false),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 3) }) },
						false),
				Cost.EMPTY);
		cards[71] = new Venture("Costruire le Torri", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.STONE, 6) }),
				new CardEffect(new Effect[] { new ResourceEffect(new Resource[] {
						new Resource(ResourceType.MILITARY, 4), new Resource(ResourceType.COUNCIL_PRIVILEGES, 1) }) },
						false),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 4) }) },
						false),
				Cost.EMPTY);

		// Buildings
		// I Period
		cards[72] = new Building("Arco di Trionfo", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.STONE, 6), new Resource(ResourceType.COINS, 6)}),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 6)}) }, false),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource(ResourceType.VENTURE, 1),
						new Resource[] { new Resource(ResourceType.VICTORY, 1) })}, false), 6);
		cards[73] = new Building("Zecca", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.STONE, 3), new Resource(ResourceType.WOOD, 1)}),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 5)}) }, false),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource(ResourceType.BUILDING, 1),
						new Resource[] { new Resource(ResourceType.COINS, 1) })}, false), 5);
		cards[74] = new Building("Esattoria", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.STONE, 1), new Resource(ResourceType.WOOD, 3)}),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 5)}) }, false),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource(ResourceType.TERRITORY, 1),
						new Resource[] { new Resource(ResourceType.COINS, 1) })}, false), 5);
		cards[75] = new Building("Teatro", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 2), new Resource(ResourceType.COINS, 2)}),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 6)}) }, false),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource(ResourceType.CHARACTER, 1),
						new Resource[] { new Resource(ResourceType.VICTORY, 1) })}, false), 6);
		cards[76] = new Building("Residenza", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.STONE, 2)}),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 1)}) }, false),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.COINS, 1) },
						new Resource[] { new Resource(ResourceType.COUNCIL_PRIVILEGES, 1) }) }, false), 1);
		cards[77] = new Building("Cappella", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 2)}),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 1)}) }, false),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.COINS, 1) },
						new Resource[] { new Resource(ResourceType.FAITH, 1) }) }, false), 2);
		cards[78] = new Building("Falegnameria", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 2), new Resource(ResourceType.COINS, 1)}),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 3)}) }, false),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.WOOD, 1) },
						new Resource[] { new Resource(ResourceType.COINS, 3) }),
						new ResourceEffect(new Resource[] { new Resource(ResourceType.WOOD, 2) },
								new Resource[] { new Resource(ResourceType.COINS, 5)})}, true), 4);
		cards[79] = new Building("Tagliapietra", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.STONE, 2), new Resource(ResourceType.COINS, 1)}),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 2)}) }, false),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.STONE, 1) },
						new Resource[] { new Resource(ResourceType.COINS, 3) }),
						new ResourceEffect(new Resource[] { new Resource(ResourceType.STONE, 2) },
								new Resource[] { new Resource(ResourceType.COINS, 5)})}, true), 3);
		
		// II Period
		cards[80] = new Building("Fortezza", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 2), new Resource(ResourceType.STONE, 2), 
						new Resource(ResourceType.COINS, 2)}),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 8)}) }, false),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.MILITARY, 2) },
						new Resource[] { new Resource(ResourceType.VICTORY, 2) }) }, false), 6);
		cards[81] = new Building("Gilda degli Scultori", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.STONE, 4)}),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 6)}) }, false),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.STONE, 1) },
						new Resource[] { new Resource(ResourceType.VICTORY, 3) }),
						new ResourceEffect(new Resource[] { new Resource(ResourceType.STONE, 3) },
								new Resource[] { new Resource(ResourceType.VICTORY, 7)})}, true), 5);
		cards[82] = new Building("Gilda dei Pittori", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 4)}),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 5)}) }, false),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.WOOD, 1) },
						new Resource[] { new Resource(ResourceType.VICTORY, 3) }),
						new ResourceEffect(new Resource[] { new Resource(ResourceType.WOOD, 3) },
								new Resource[] { new Resource(ResourceType.VICTORY, 7)})}, true), 4);
		cards[83] = new Building("Tesoreria", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 3)}),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 4)}) }, false),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.COINS, 1) },
						new Resource[] { new Resource(ResourceType.VICTORY, 3) }),
						new ResourceEffect(new Resource[] { new Resource(ResourceType.COINS, 2) },
								new Resource[] { new Resource(ResourceType.VICTORY, 5)})}, true), 3);
		cards[84] = new Building("Caserma", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 1), new Resource(ResourceType.STONE, 1)}),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 3)}) }, false),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.SERVANTS, 1) },
						new Resource[] { new Resource(ResourceType.MILITARY, 3) })}, false), 1);
		cards[85] = new Building("Gilda dei Costruttori", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 1), new Resource(ResourceType.STONE, 2)}),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 4)}) }, false),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.SERVANTS, 1),
						new Resource(ResourceType.WOOD, 1), new Resource(ResourceType.STONE, 1)},
						new Resource[] { new Resource(ResourceType.VICTORY, 6) })}, false), 4);
		cards[86] = new Building("Mercato", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 2), new Resource(ResourceType.STONE, 1)}),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 3)}) }, false),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.COINS, 3) },
						new Resource[] { new Resource(ResourceType.WOOD, 2), new Resource(ResourceType.STONE, 2)})}, false), 3);
		cards[87] = new Building("Battistero", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.STONE, 3)}),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 2),
						new Resource(ResourceType.FAITH, 1)}) }, false),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 1) },
						new Resource[] { new Resource(ResourceType.VICTORY, 2), new Resource(ResourceType.COINS, 2)})}, false), 2);
		
		// III Period
		cards[88] = new Building("Basilica", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 1), new Resource(ResourceType.STONE, 4)}),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 5),
						new Resource(ResourceType.FAITH, 1)}) }, false),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.WOOD, 1) },
						new Resource[] { new Resource(ResourceType.FAITH, 2) }),
						new ResourceEffect(new Resource[] { new Resource(ResourceType.STONE, 1) },
								new Resource[] { new Resource(ResourceType.FAITH, 2)})}, true), 1);
		cards[89] = new Building("Palazzo", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 3), new Resource(ResourceType.WOOD, 3),
						new Resource(ResourceType.STONE, 1)}),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 9)})}, false),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.COINS, 1) },
						new Resource[] { new Resource(ResourceType.SERVANTS, 2), new Resource(ResourceType.VICTORY, 4)})}, false), 6);
		cards[90] = new Building("Accademia Militare", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.SERVANTS, 1), new Resource(ResourceType.WOOD, 2),
						new Resource(ResourceType.STONE, 2)}),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 7)})}, false),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.SERVANTS, 1) },
						new Resource[] { new Resource(ResourceType.MILITARY, 3), new Resource(ResourceType.VICTORY, 1)})}, false), 3);
		cards[91] = new Building("Fiera", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 4), new Resource(ResourceType.WOOD, 3)}),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 8)})}, false),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.COINS, 4) },
						new Resource[] { new Resource(ResourceType.WOOD, 3), new Resource(ResourceType.STONE, 3)})}, false), 4);
		cards[92] = new Building("Castelletto", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 2), new Resource(ResourceType.WOOD, 2),
						new Resource(ResourceType.STONE, 4)}),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 9)})}, false),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 2), 
						new Resource(ResourceType.COUNCIL_PRIVILEGES, 1)})}, false), 5);
		cards[93] = new Building("Cattedrale", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 4), new Resource(ResourceType.STONE, 4)}),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 7),
						new Resource(ResourceType.FAITH, 3)})}, false),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 1)})}, false), 2);
		cards[94] = new Building("Banca", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 1), new Resource(ResourceType.STONE, 3),
						new Resource(ResourceType.COINS, 3)}),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 7)})}, false),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.COINS, 5)})}, false), 2);
		cards[95] = new Building("Giardino", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 4), new Resource(ResourceType.STONE, 2),
						new Resource(ResourceType.SERVANTS, 2)}),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 10)})}, false),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 3)})}, false), 1);
		
		try {
			for (Card card : cards)
				saveCard(card);

			Map<CardType, List<Card>> deck = loadCards();
			for (CardType cardType : new CardType[] { CardType.TERRITORY, CardType.CHARACTER, CardType.VENTURE, CardType.BUILDING}) {
				System.out.println("");
				System.out.println(cardType + ": ");
				System.out.println("");
				for (Card card : deck.get(cardType))
					System.out.println(card.getName() + " - Period " + card.getPeriodType());

				System.out.println("------------------");
				List<Card> shuffledCards = it.polimi.ingsw.LM45.util.ShuffleHelper.shuffle(deck.get(cardType));
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
