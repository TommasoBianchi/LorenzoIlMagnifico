package it.polimi.ingsw.LM45.serialization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import it.polimi.ingsw.LM45.config.BoardConfiguration;
import it.polimi.ingsw.LM45.config.Configuration;
import it.polimi.ingsw.LM45.config.PersonalBonusTilesConfiguration;
import it.polimi.ingsw.LM45.model.cards.Building;
import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.model.cards.Character;
import it.polimi.ingsw.LM45.model.cards.Cost;
import it.polimi.ingsw.LM45.model.cards.CostWithPrerequisites;
import it.polimi.ingsw.LM45.model.cards.Excommunication;
import it.polimi.ingsw.LM45.model.cards.LeaderCard;
import it.polimi.ingsw.LM45.model.cards.PeriodType;
import it.polimi.ingsw.LM45.model.cards.Territory;
import it.polimi.ingsw.LM45.model.cards.Venture;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.PersonalBonusTile;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.model.effects.ActionEffect;
import it.polimi.ingsw.LM45.model.effects.CardEffect;
import it.polimi.ingsw.LM45.model.effects.ChurchSupportBonusEffect;
import it.polimi.ingsw.LM45.model.effects.CopyEffect;
import it.polimi.ingsw.LM45.model.effects.CostModifierEffect;
import it.polimi.ingsw.LM45.model.effects.Effect;
import it.polimi.ingsw.LM45.model.effects.FamiliarEffect;
import it.polimi.ingsw.LM45.model.effects.GainModifierEffect;
import it.polimi.ingsw.LM45.model.effects.JumpFirstTurnEffect;
import it.polimi.ingsw.LM45.model.effects.NoTerritoryRequisiteEffect;
import it.polimi.ingsw.LM45.model.effects.ResourceEffect;
import it.polimi.ingsw.LM45.model.effects.SlotModifierEffect;
import it.polimi.ingsw.LM45.model.effects.VictoryPointsFromCardsEffect;

public class FileManager {

	private static final String BASE_PATH = "./Assets/Json";
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Effect.class, new GsonTypeAdapter<Effect>())
			.registerTypeAdapter(Card.class, new GsonTypeAdapter<Card>())
			.registerTypeAdapter(Cost.class, new GsonConcreteTypeAdapter<Cost>())
			.create();
	
	private FileManager(){}

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

	public static void saveLeaderCard(LeaderCard leaderCard) throws IOException {
		String path = BASE_PATH + "/LeaderCards";
		File directory = new File(path);

		if (!directory.exists()) {
			directory.mkdirs();
		}

		FileWriter writer = new FileWriter(path + "/" + leaderCard.getName() + ".json");
		GSON.toJson(leaderCard, LeaderCard.class, writer);
		writer.close();
	}
	
	public static void saveExcommunication(Excommunication excommunication) throws IOException {
		String path = BASE_PATH + "/Excommunications/" + excommunication.getPeriodType().name();
		File directory = new File(path);

		if (!directory.exists()) {
			directory.mkdirs();
		}

		FileWriter writer = new FileWriter(path + "/" + excommunication.getName() + ".json");
		GSON.toJson(excommunication, Excommunication.class, writer);
		writer.close();
	}

	public static <T extends Configuration> void saveConfiguration(T configuration) throws IOException {
		String path = BASE_PATH + "/Config";
		File directory = new File(path);

		if (!directory.exists()) {
			directory.mkdirs();
		}

		FileWriter writer = new FileWriter(path + "/" + configuration.getClass().getSimpleName() + ".json");
		GSON.toJson(configuration, writer);
		writer.close();
	}

	public static Map<CardType, List<Card>> loadCards() throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		Map<CardType, List<Card>> deck = new EnumMap<>(CardType.class);
		File folder = new File(BASE_PATH + "/Cards/");

		for (File dir : folder.listFiles()) {
			if (dir.isDirectory()) {
				CardType cardType = CardType.valueOf(dir.getName());
				List<Card> cards = new ArrayList<>();

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

	public static List<LeaderCard> loadLeaderCards() throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		List<LeaderCard> leaderCards = new ArrayList<>();
		File folder = new File(BASE_PATH + "/LeaderCards/");

		for (File file : folder.listFiles()) {
			if (file.isFile()) {
				LeaderCard leaderCard = GSON.fromJson(new FileReader(file), LeaderCard.class);
				leaderCards.add(leaderCard);
			}
		}

		return leaderCards;
	}
	
	public static Map<PeriodType, List<Excommunication>> loadExcommunications() throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		Map<PeriodType, List<Excommunication>> deck = new EnumMap<>(PeriodType.class);
		File folder = new File(BASE_PATH + "/Excommunications/");

		for (File dir : folder.listFiles()) {
			if (dir.isDirectory()) {
				PeriodType periodType = PeriodType.valueOf(dir.getName());
				List<Excommunication> excommunications = new ArrayList<>();

				for (File file : dir.listFiles()) {
					if (file.isFile()) {
						Excommunication excommunication = GSON.fromJson(new FileReader(file), Excommunication.class);
						excommunications.add(excommunication);
					}
				}

				deck.put(periodType, excommunications);
			}
		}

		return deck;
	}

	public static <T extends Configuration> T loadConfiguration(Class<T> cl) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		File file = new File(BASE_PATH + "/Config/" + cl.getSimpleName()+ ".json");
		
		return GSON.fromJson(new FileReader(file), cl);
	}

	// Main di test
	public static void main(String[] args) {
		Card[] cards = new Card[96];
		
		try {
			// Save serverConfiguration
			//saveConfiguration(new ServerConfiguration(2, 25000, 2000, 7000));

			// Save boardConfiguration
			Map<SlotType, Resource[][]> slotsConfiguration = new EnumMap<>(SlotType.class);
			slotsConfiguration.put(SlotType.TERRITORY, 
					new Resource[][]{ new Resource[]{}, new Resource[]{}, 
				new Resource[]{ new Resource(ResourceType.WOOD, 1) }, new Resource[]{ new Resource(ResourceType.WOOD, 2) } });
			slotsConfiguration.put(SlotType.CHARACTER, 
					new Resource[][]{ new Resource[]{}, new Resource[]{}, 
				new Resource[]{ new Resource(ResourceType.STONE, 1) }, new Resource[]{ new Resource(ResourceType.STONE, 2) } });
			slotsConfiguration.put(SlotType.BUILDING, 
					new Resource[][]{ new Resource[]{}, new Resource[]{}, 
				new Resource[]{ new Resource(ResourceType.MILITARY, 1) }, new Resource[]{ new Resource(ResourceType.MILITARY, 2) } });
			slotsConfiguration.put(SlotType.VENTURE, 
					new Resource[][]{ new Resource[]{}, new Resource[]{}, 
				new Resource[]{ new Resource(ResourceType.COINS, 1) }, new Resource[]{ new Resource(ResourceType.COINS, 2) } });

			slotsConfiguration.put(SlotType.MARKET, 
					new Resource[][]{ new Resource[]{ new Resource(ResourceType.COINS, 5) }, 
				new Resource[]{ new Resource(ResourceType.SERVANTS, 5) }, 
				new Resource[]{ new Resource(ResourceType.MILITARY, 3), new Resource(ResourceType.COINS, 2) }, 
				new Resource[]{ new Resource(ResourceType.COUNCIL_PRIVILEGES, 2) } });

			slotsConfiguration.put(SlotType.COUNCIL, 
					new Resource[][]{ new Resource[]{ new Resource(ResourceType.COINS, 1), new Resource(ResourceType.COUNCIL_PRIVILEGES, 1) } });
			
			Resource[][] churchSupportResources = new Resource[][]{
				new Resource[]{ new Resource(ResourceType.VICTORY, 0) },
				new Resource[]{ new Resource(ResourceType.VICTORY, 1) },
				new Resource[]{ new Resource(ResourceType.VICTORY, 2) },
				new Resource[]{ new Resource(ResourceType.VICTORY, 3) },
				new Resource[]{ new Resource(ResourceType.VICTORY, 4) },
				new Resource[]{ new Resource(ResourceType.VICTORY, 5) },
				new Resource[]{ new Resource(ResourceType.VICTORY, 7) },
				new Resource[]{ new Resource(ResourceType.VICTORY, 9) },
				new Resource[]{ new Resource(ResourceType.VICTORY, 11) },
				new Resource[]{ new Resource(ResourceType.VICTORY, 13) },
				new Resource[]{ new Resource(ResourceType.VICTORY, 15) },
				new Resource[]{ new Resource(ResourceType.VICTORY, 17) },
				new Resource[]{ new Resource(ResourceType.VICTORY, 19) },
				new Resource[]{ new Resource(ResourceType.VICTORY, 22) },
				new Resource[]{ new Resource(ResourceType.VICTORY, 25) },
				new Resource[]{ new Resource(ResourceType.VICTORY, 30) }
			};
			
			saveConfiguration(new BoardConfiguration(slotsConfiguration, churchSupportResources));

			// Save PersonalBonusTilesConfiguration
			PersonalBonusTile[] personalBonusTiles = new PersonalBonusTile[]{
					new PersonalBonusTile(new Resource[]{ new Resource(ResourceType.SERVANTS, 1), new Resource(ResourceType.COINS, 2) },
							new Resource[]{ new Resource(ResourceType.WOOD, 1), new Resource(ResourceType.STONE, 1), new Resource(ResourceType.MILITARY, 1), }),
					new PersonalBonusTile(new Resource[]{ new Resource(ResourceType.SERVANTS, 1), new Resource(ResourceType.MILITARY, 2) },
							new Resource[]{ new Resource(ResourceType.WOOD, 1), new Resource(ResourceType.STONE, 1), new Resource(ResourceType.COINS, 1), }),
					new PersonalBonusTile(new Resource[]{ new Resource(ResourceType.SERVANTS, 2), new Resource(ResourceType.COINS, 1) },
							new Resource[]{ new Resource(ResourceType.WOOD, 1), new Resource(ResourceType.STONE, 1), new Resource(ResourceType.MILITARY, 1), }),
					new PersonalBonusTile(new Resource[]{ new Resource(ResourceType.MILITARY, 2), new Resource(ResourceType.COINS, 1) },
							new Resource[]{ new Resource(ResourceType.WOOD, 1), new Resource(ResourceType.STONE, 1), new Resource(ResourceType.SERVANTS, 1), })
			};
			
			saveConfiguration(new PersonalBonusTilesConfiguration(personalBonusTiles));
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Territories
		// I Period
		cards[0] = new Territory("Commercial Hub", PeriodType.I,
				CardEffect.EMPTY,
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.COINS, 1) })),
				1);
		cards[1] = new Territory("Woods", PeriodType.I,
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.WOOD, 1) })),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.WOOD, 1) })),
				2);
		cards[2] = new Territory("Village", PeriodType.I,
				CardEffect.EMPTY,
				new CardEffect(new ResourceEffect(new Resource[] {
						new Resource(ResourceType.COINS, 1), new Resource(ResourceType.SERVANTS, 1) })),
				3);
		cards[3] = new Territory("Gravel Pit", PeriodType.I,
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.STONE, 2) })),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.STONE, 2) })),
				4);
		cards[4] = new Territory("Forest", PeriodType.I,
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.WOOD, 1) })),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.WOOD, 3) })),
				5);
		cards[5] = new Territory("Monastery", PeriodType.I,
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.MILITARY, 2),
								new Resource(ResourceType.SERVANTS, 1) })),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 1),
						new Resource(ResourceType.STONE, 1) })), 6);
		cards[6] = new Territory("Citadel", PeriodType.I,
				CardEffect.EMPTY,
				new CardEffect(new ResourceEffect(new Resource[] {
						new Resource(ResourceType.MILITARY, 2), new Resource(ResourceType.STONE, 1) })),
				5);
		cards[7] = new Territory("City", PeriodType.I,
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.COINS, 3) })),
				new CardEffect(new ResourceEffect(
						new Resource[] { new Resource(ResourceType.COUNCIL_PRIVILEGES, 1) })), 6);
		// II Period
		cards[8] = new Territory("Gold Mine", PeriodType.II,
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.COINS, 1) })),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.COINS, 2) })),
				1);
		cards[9] = new Territory("Mountain Town", PeriodType.II,
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.SERVANTS, 1) })),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.MILITARY, 1),
						new Resource(ResourceType.WOOD, 2) })), 3);
		cards[10] = new Territory("Mining Town", PeriodType.II,
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.SERVANTS, 2),
								new Resource(ResourceType.STONE, 1) })),
				new CardEffect(new ResourceEffect(new Resource[] {
						new Resource(ResourceType.SERVANTS, 1), new Resource(ResourceType.STONE, 2) })),
				4);
		cards[11] = new Territory("Rock Pit", PeriodType.II,
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.WOOD, 1) })),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.STONE, 3) })),
				3);
		cards[12] = new Territory("Estate", PeriodType.II,
				new CardEffect(new ResourceEffect(
						new Resource[] { new Resource(ResourceType.SERVANTS, 2), new Resource(ResourceType.WOOD, 1) })),
				new CardEffect(new ResourceEffect(
						new Resource[] { new Resource(ResourceType.COINS, 1), new Resource(ResourceType.WOOD, 2) })),
				4);
		cards[13] = new Territory("Hermitage", PeriodType.II,
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 1) })),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 1) })),
				2);
		cards[14] = new Territory("Manor House", PeriodType.II,
				CardEffect.EMPTY,
				new CardEffect(new ResourceEffect(new Resource[] {
						new Resource(ResourceType.MILITARY, 2), new Resource(ResourceType.SERVANTS, 2) })),
				5);
		cards[15] = new Territory("Dukedom", PeriodType.II,
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.COINS, 4) })),
				new CardEffect(new ResourceEffect(
						new Resource[] { new Resource(ResourceType.COINS, 1), new Resource(ResourceType.STONE, 1),
								new Resource(ResourceType.WOOD, 2) })),
				6);
		// III Period
		cards[16] = new Territory("Trading Town", PeriodType.III,
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.COINS, 1),
								new Resource(ResourceType.SERVANTS, 1) })),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.COINS, 3) })),
				1);
		cards[17] = new Territory("Farm", PeriodType.III,
				new CardEffect(new ResourceEffect(
						new Resource[] { new Resource(ResourceType.VICTORY, 1), new Resource(ResourceType.WOOD, 1) })),
				new CardEffect(new ResourceEffect(
						new Resource[] { new Resource(ResourceType.VICTORY, 2), new Resource(ResourceType.WOOD, 2) })),
				3);
		cards[18] = new Territory("Colony", PeriodType.III,
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.MILITARY, 2) })),
				new CardEffect(new ResourceEffect(
						new Resource[] { new Resource(ResourceType.VICTORY, 4), new Resource(ResourceType.WOOD, 1) })),
				5);
		cards[19] = new Territory("Marble Pit", PeriodType.III,
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 3) })),
				new CardEffect(new ResourceEffect(
						new Resource[] { new Resource(ResourceType.VICTORY, 1), new Resource(ResourceType.STONE, 2) })),
				2);
		cards[20] = new Territory("Province", PeriodType.III,
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.COUNCIL_PRIVILEGES, 1),
								new Resource(ResourceType.STONE, 1) })),
				new CardEffect(new ResourceEffect(
						new Resource[] { new Resource(ResourceType.VICTORY, 4), new Resource(ResourceType.STONE, 1) })),
				6);
		cards[21] = new Territory("Sanctuary", PeriodType.III,
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 1) })),
				new CardEffect(new ResourceEffect(
						new Resource[] { new Resource(ResourceType.COINS, 1), new Resource(ResourceType.FAITH, 1) })),
				1);
		cards[22] = new Territory("Castle", PeriodType.III,
				new CardEffect(new ResourceEffect(
						new Resource[] { new Resource(ResourceType.VICTORY, 2), new Resource(ResourceType.COINS, 2) })),
				new CardEffect(new ResourceEffect(new Resource[] {
						new Resource(ResourceType.MILITARY, 3), new Resource(ResourceType.SERVANTS, 1) })),
				4);
		cards[23] = new Territory("Fortified Town", PeriodType.III,
				new CardEffect(new ResourceEffect(new Resource[] { 
						new Resource(ResourceType.MILITARY, 2),new Resource(ResourceType.SERVANTS, 1) })),
				new CardEffect(new ResourceEffect(new Resource[] {
						new Resource(ResourceType.MILITARY, 1), new Resource(ResourceType.SERVANTS, 2) })),
				2);

		// Characters
		// I Period
		cards[24] = new Character("Preacher", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 2) }),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 4) })),
				new CardEffect(new SlotModifierEffect(SlotType.ANY_CARD, true, false, false), true));
		cards[25] = new Character("Stonemason", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 4) }),
				CardEffect.EMPTY,
				new CardEffect(new Effect[] {
						new ActionEffect(SlotType.BUILDING, 2, new Resource[] { new Resource(ResourceType.WOOD, 1) }, false),
						new ActionEffect(SlotType.BUILDING, 2, new Resource[] { new Resource(ResourceType.STONE, 1) }, false) },
						true, true));
		cards[26] = new Character("Dame", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 4) }),
				CardEffect.EMPTY,
				new CardEffect(new ActionEffect(SlotType.CHARACTER, 2,
						new Resource[] { new Resource(ResourceType.COINS, 1) }, false), true));
		cards[27] = new Character("Knight", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 2) }),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.COUNCIL_PRIVILEGES, 1) })),
				new CardEffect(new ActionEffect(SlotType.VENTURE, 2, false), true));
		cards[28] = new Character("Warlord", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 2) }),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.MILITARY, 3) })),
				new CardEffect(new ActionEffect(SlotType.TERRITORY, 2, false), true));
		cards[29] = new Character("Artisan", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 3) }), CardEffect.EMPTY,
				new CardEffect(new ActionEffect(SlotType.PRODUCTION, 2, false), true));
		cards[30] = new Character("Farmer", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 3) }), CardEffect.EMPTY,
				new CardEffect(new ActionEffect(SlotType.HARVEST, 2, false), true));
		cards[31] = new Character("Abbess", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 3) }),
				new CardEffect(new Effect[] { new ActionEffect(SlotType.ANY_CARD, 4, true),
						new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 1) }) }, false),
				CardEffect.EMPTY);
		
		// II Period
		cards[32] = new Character("Patron", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 3) }),
				new CardEffect(new ActionEffect(SlotType.CHARACTER, 6,
						new Resource[] { new Resource(ResourceType.COINS, 2) }, true)),
				CardEffect.EMPTY);
		cards[33] = new Character("Architect", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 4) }),
				new CardEffect(new ActionEffect(SlotType.BUILDING, 6,
						new Resource[] { new Resource(ResourceType.WOOD, 1), new Resource(ResourceType.STONE, 1) }, true)),
				CardEffect.EMPTY);
		cards[34] = new Character("Hero", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 4) }),
				new CardEffect(new Effect[] { new ActionEffect(SlotType.VENTURE, 6, true),
						new ResourceEffect(new Resource[] { new Resource(ResourceType.COUNCIL_PRIVILEGES, 1) }) },
						false),
				CardEffect.EMPTY);
		cards[35] = new Character("Captain", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 4) }),
				new CardEffect(new Effect[] { new ActionEffect(SlotType.TERRITORY, 6, true),
						new ResourceEffect(new Resource[] { new Resource(ResourceType.MILITARY, 2) }) }, false),
				CardEffect.EMPTY);
		cards[36] = new Character("Scholar", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 4) }),
				CardEffect.EMPTY,
				new CardEffect(new ActionEffect(SlotType.PRODUCTION, 3, false), true));
		cards[37] = new Character("Royal Messenger", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 5) }),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.COUNCIL_PRIVILEGES, 3) })),
				CardEffect.EMPTY);
		cards[38] = new Character("Papal Messenger", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 5) }),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 3) })),
				CardEffect.EMPTY);
		cards[39] = new Character("Peasant", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 4) }),
				CardEffect.EMPTY,
				new CardEffect(new ActionEffect(SlotType.HARVEST, 3, false), true));

		// III Period
		cards[40] = new Character("Ambassador", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 6) }),
				new CardEffect(new Effect[] { new ActionEffect(SlotType.ANY_CARD, 7, true),
						new ResourceEffect(new Resource[] { new Resource(ResourceType.COUNCIL_PRIVILEGES, 1) }) },
						false),
				CardEffect.EMPTY);
		cards[41] = new Character("General", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 5) }),
				new CardEffect(new ResourceEffect(new Resource(ResourceType.MILITARY, 2),
						new Resource[] { new Resource(ResourceType.VICTORY, 1) })),
				CardEffect.EMPTY);
		cards[42] = new Character("Paramour", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 7) }),
				new CardEffect(new ResourceEffect(new Resource(ResourceType.CHARACTER, 1),
						new Resource[] { new Resource(ResourceType.VICTORY, 2) })),
				CardEffect.EMPTY);
		cards[43] = new Character("Herald", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 6) }),
				new CardEffect(new ResourceEffect(new Resource(ResourceType.VENTURE, 1),
						new Resource[] { new Resource(ResourceType.VICTORY, 2) })),
				CardEffect.EMPTY);
		cards[44] = new Character("Noble", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 6) }),
				new CardEffect(new ResourceEffect(new Resource(ResourceType.TERRITORY, 1),
						new Resource[] { new Resource(ResourceType.VICTORY, 2) })),
				CardEffect.EMPTY);
		cards[45] = new Character("Governor", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 6) }),
				new CardEffect(new ResourceEffect(new Resource(ResourceType.BUILDING, 1),
						new Resource[] { new Resource(ResourceType.VICTORY, 2) })),
				CardEffect.EMPTY);
		cards[46] = new Character("Cardinal", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 4) }),
				new CardEffect(new Effect[] { new ActionEffect(SlotType.HARVEST, 4, true),
						new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 2) }) }, false),
				CardEffect.EMPTY);
		cards[47] = new Character("Bishop", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 5) }),
				new CardEffect(new Effect[] { new ActionEffect(SlotType.PRODUCTION, 4, true),
						new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 1) }) }, false),
				CardEffect.EMPTY);

		// Ventures
		// I Period
		cards[48] = new Venture("Military Campaign", PeriodType.I,
				new CostWithPrerequisites(new Resource[] { new Resource(ResourceType.MILITARY, 2) },
						new Resource[] { new Resource(ResourceType.MILITARY, 3) }),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.COINS, 3) })),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 5) })));
		cards[49] = new Venture("Hiring Recruits", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 4) }),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.MILITARY, 5) })),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 4) })));
		cards[50] = new Venture("Raising a Statue", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 2), new Resource(ResourceType.STONE, 2) }),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.COUNCIL_PRIVILEGES, 2) })),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 4) })));
		cards[51] = new Venture("Fighting Heresies", PeriodType.I,
				new CostWithPrerequisites(new Resource[] { new Resource(ResourceType.MILITARY, 3) },
						new Resource[] { new Resource(ResourceType.MILITARY, 5) }),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 2) })),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 5) })));
		cards[52] = new Venture("Support to the Bishop", PeriodType.I,
				new CostWithPrerequisites(new Resource[] { new Resource(ResourceType.MILITARY, 2) },
						new Resource[] { new Resource(ResourceType.MILITARY, 4) }),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 3) })),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 1) })),
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 1), new Resource(ResourceType.STONE, 1),
						new Resource(ResourceType.COINS, 2) }));
		cards[53] = new Venture("Hosting Panhandlers", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 3) }),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.SERVANTS, 4) })),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 4) })));
		cards[54] = new Venture("Rapairing the Church", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 1), new Resource(ResourceType.STONE, 1),
						new Resource(ResourceType.COINS, 1) }),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 1) })),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 5) })));
		cards[55] = new Venture("Building the Walls", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.STONE, 3) }),
				new CardEffect(new ResourceEffect(new Resource[] {
						new Resource(ResourceType.MILITARY, 2), new Resource(ResourceType.COUNCIL_PRIVILEGES, 1) })),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 3) })));

		// II Period
		cards[56] = new Venture("Support to the Cardinal", PeriodType.II,
				new CostWithPrerequisites(new Resource[] { new Resource(ResourceType.MILITARY, 4) },
						new Resource[] { new Resource(ResourceType.MILITARY, 7) }),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 3) })),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 4) })),
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 2), new Resource(ResourceType.STONE, 2),
						new Resource(ResourceType.COINS, 3) }));
		cards[57] = new Venture("Crusade", PeriodType.II,
				new CostWithPrerequisites(new Resource[] { new Resource(ResourceType.MILITARY, 4) },
						new Resource[] { new Resource(ResourceType.MILITARY, 8) }),
				new CardEffect(new ResourceEffect(
						new Resource[] { new Resource(ResourceType.FAITH, 1), new Resource(ResourceType.COINS, 5) })),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 5) })));
		cards[58] = new Venture("Support to the King", PeriodType.II,
				new CostWithPrerequisites(new Resource[] { new Resource(ResourceType.MILITARY, 3) },
						new Resource[] { new Resource(ResourceType.MILITARY, 6) }),
				new CardEffect(new ResourceEffect(new Resource[] {
						new Resource(ResourceType.COUNCIL_PRIVILEGES, 1), new Resource(ResourceType.COINS, 5) })),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 3) })));
		cards[59] = new Venture("Improving the Canals", PeriodType.II,
				new Cost(
						new Resource[] { new Resource(ResourceType.SERVANTS, 2), new Resource(ResourceType.COINS, 3) }),
				new CardEffect(new ActionEffect(SlotType.HARVEST, 4, true)),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 5) })));
		cards[60] = new Venture("Building the Bastions", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.STONE, 4) }),
				new CardEffect(new ResourceEffect(new Resource[] {
						new Resource(ResourceType.MILITARY, 3), new Resource(ResourceType.COUNCIL_PRIVILEGES, 1) })),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 2) })));
		cards[61] = new Venture("Hiring Soldiers", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 6) }),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.MILITARY, 6) })),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 5) })));
		cards[62] = new Venture("Hosting Foreigners", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 4) }),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.SERVANTS, 5) })),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 4) })));
		cards[63] = new Venture("Repairing the Abbey", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 2), new Resource(ResourceType.COINS, 2),
						new Resource(ResourceType.STONE, 2) }),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 2) })),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 6) })));

		// III Period
		cards[64] = new Venture("Support to the Pope", PeriodType.III,
				new CostWithPrerequisites(new Resource[] { new Resource(ResourceType.MILITARY, 5) },
						new Resource[] { new Resource(ResourceType.MILITARY, 10) }),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 2) })),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 10) })),
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 3), new Resource(ResourceType.STONE, 3),
						new Resource(ResourceType.COINS, 4) }));
		cards[65] = new Venture("Improving the Roads", PeriodType.III,
				new Cost(
						new Resource[] { new Resource(ResourceType.SERVANTS, 3), new Resource(ResourceType.COINS, 4) }),
				new CardEffect(new ActionEffect(SlotType.HARVEST, 3, true)),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 5) })));
		cards[66] = new Venture("Repairing the Cathedral", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 3), new Resource(ResourceType.WOOD, 3),
						new Resource(ResourceType.STONE, 3) }),
				new CardEffect(
						new Effect[] { new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 1) }),
								new ActionEffect(SlotType.ANY_CARD, 7, true) },
						false),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 5) })));
		cards[67] = new Venture("Military Conquest", PeriodType.III,
				new CostWithPrerequisites(new Resource[] { new Resource(ResourceType.MILITARY, 6) },
						new Resource[] { new Resource(ResourceType.MILITARY, 12) }),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.WOOD, 3),
						new Resource(ResourceType.STONE, 3), new Resource(ResourceType.COINS, 3) })),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 7) })));
		cards[68] = new Venture("Sacred War", PeriodType.III,
				new CostWithPrerequisites(new Resource[] { new Resource(ResourceType.MILITARY, 8) },
						new Resource[] { new Resource(ResourceType.MILITARY, 15) }),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 4) })),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 8) })));
		cards[69] = new Venture("Hiring Mercenaries", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 8) }),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.MILITARY, 7) })),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 6) })));
		cards[70] = new Venture("Promoting Sacred Art", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 6) }),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 3) })),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 3) })));
		cards[71] = new Venture("Building the Towers", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.STONE, 6) }),
				new CardEffect(new ResourceEffect(new Resource[] {
						new Resource(ResourceType.MILITARY, 4), new Resource(ResourceType.COUNCIL_PRIVILEGES, 1) })),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 4) })));

		// Buildings
		// I Period
		cards[72] = new Building("Triumphal Arch", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.STONE, 2), new Resource(ResourceType.COINS, 2)}),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 6)})),
				new CardEffect(new ResourceEffect(new Resource(ResourceType.VENTURE, 1),
						new Resource[] { new Resource(ResourceType.VICTORY, 1) })), 6);
		cards[73] = new Building("Mint", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.STONE, 3), new Resource(ResourceType.WOOD, 1)}),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 5)})),
				new CardEffect(new ResourceEffect(new Resource(ResourceType.BUILDING, 1),
						new Resource[] { new Resource(ResourceType.COINS, 1) })), 5);
		cards[74] = new Building("Tax Office", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.STONE, 1), new Resource(ResourceType.WOOD, 3)}),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 5)})),
				new CardEffect(new ResourceEffect(new Resource(ResourceType.TERRITORY, 1),
						new Resource[] { new Resource(ResourceType.COINS, 1) })), 5);
		cards[75] = new Building("Theater", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 2), new Resource(ResourceType.COINS, 2)}),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 6)})),
				new CardEffect(new ResourceEffect(new Resource(ResourceType.CHARACTER, 1),
						new Resource[] { new Resource(ResourceType.VICTORY, 1) })), 6);
		cards[76] = new Building("Residence", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.STONE, 2)}),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 1)})),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.COINS, 1) },
						new Resource[] { new Resource(ResourceType.COUNCIL_PRIVILEGES, 1) })), 1);
		cards[77] = new Building("Chapel", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 2)}),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 1)})),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.COINS, 1) },
						new Resource[] { new Resource(ResourceType.FAITH, 1) })), 2);
		cards[78] = new Building("Carpenter's Shop", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 2), new Resource(ResourceType.COINS, 1)}),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 3)})),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.WOOD, 1) },
						new Resource[] { new Resource(ResourceType.COINS, 3) }),		
						new ResourceEffect(new Resource[] { new Resource(ResourceType.WOOD, 2) },
								new Resource[] { new Resource(ResourceType.COINS, 5)})}, true), 4);
		cards[79] = new Building("Stonemason's Shop", PeriodType.I,
				new Cost(new Resource[] { new Resource(ResourceType.STONE, 2), new Resource(ResourceType.COINS, 1)}),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 2)})),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.STONE, 1) },
						new Resource[] { new Resource(ResourceType.COINS, 3) }),
						new ResourceEffect(new Resource[] { new Resource(ResourceType.STONE, 2) },
								new Resource[] { new Resource(ResourceType.COINS, 5) }) },
						true),
				3);

		// II Period
		cards[80] = new Building("Stronghold", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 2), new Resource(ResourceType.STONE, 2), 
						new Resource(ResourceType.COINS, 2)}),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 8)})),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.MILITARY, 2),
						new Resource(ResourceType.VICTORY, 2) })), 6);
		cards[81] = new Building("Sculptors' Guild", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.STONE, 4)}),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 6)})),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.STONE, 1) },
						new Resource[] { new Resource(ResourceType.VICTORY, 3) }),
						new ResourceEffect(new Resource[] { new Resource(ResourceType.STONE, 3) },
								new Resource[] { new Resource(ResourceType.VICTORY, 7)})}, true), 5);
		cards[82] = new Building("Painters' Guild", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 4)}),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 5)})),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.WOOD, 1) },
						new Resource[] { new Resource(ResourceType.VICTORY, 3) }),
						new ResourceEffect(new Resource[] { new Resource(ResourceType.WOOD, 3) },
								new Resource[] { new Resource(ResourceType.VICTORY, 7)})}, true), 4);
		cards[83] = new Building("Treasury", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 3)}),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 4)})),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.COINS, 1) },
						new Resource[] { new Resource(ResourceType.VICTORY, 3) }),
						new ResourceEffect(new Resource[] { new Resource(ResourceType.COINS, 2) },
								new Resource[] { new Resource(ResourceType.VICTORY, 5)})}, true), 3);
		cards[84] = new Building("Barracks", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 1), new Resource(ResourceType.STONE, 1)}),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 3)})),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.SERVANTS, 1) },
						new Resource[] { new Resource(ResourceType.MILITARY, 3) })), 1);
		cards[85] = new Building("Stonemasons' Guild", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 1), new Resource(ResourceType.STONE, 2)}),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 4)})),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.SERVANTS, 1),
						new Resource(ResourceType.WOOD, 1), new Resource(ResourceType.STONE, 1)},
						new Resource[] { new Resource(ResourceType.VICTORY, 6) })), 4);
		cards[86] = new Building("Marketplace", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 2), new Resource(ResourceType.STONE, 1)}),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 3)})),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.COINS, 3) },
						new Resource[] { new Resource(ResourceType.WOOD, 2), new Resource(ResourceType.STONE, 2)})), 3);
		cards[87] = new Building("Baptistery", PeriodType.II,
				new Cost(new Resource[] { new Resource(ResourceType.STONE, 3)}),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 2),
						new Resource(ResourceType.FAITH, 1)})),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 1) },
						new Resource[] { new Resource(ResourceType.VICTORY, 2), new Resource(ResourceType.COINS, 2)})), 2);
		
		// III Period
		cards[88] = new Building("Church", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 1), new Resource(ResourceType.STONE, 4)}),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 5),
						new Resource(ResourceType.FAITH, 1)})),
				new CardEffect(new Effect[] {new ResourceEffect(new Resource[] { new Resource(ResourceType.WOOD, 1) },
						new Resource[] { new Resource(ResourceType.FAITH, 2) }),
						new ResourceEffect(new Resource[] { new Resource(ResourceType.STONE, 1) },
								new Resource[] { new Resource(ResourceType.FAITH, 2)})}, true), 1);
		cards[89] = new Building("Palace", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 3), new Resource(ResourceType.WOOD, 3),
						new Resource(ResourceType.STONE, 1)}),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 9)})),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.COINS, 1) },
						new Resource[] { new Resource(ResourceType.SERVANTS, 2), new Resource(ResourceType.VICTORY, 4)})), 6);
		cards[90] = new Building("Military Academy", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.SERVANTS, 1), new Resource(ResourceType.WOOD, 2),
						new Resource(ResourceType.STONE, 2)}),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 7)})),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.SERVANTS, 1) },
						new Resource[] { new Resource(ResourceType.MILITARY, 3), new Resource(ResourceType.VICTORY, 1)})), 3);
		cards[91] = new Building("Fair", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 4), new Resource(ResourceType.WOOD, 3)}),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 8)})),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.COINS, 4) },
						new Resource[] { new Resource(ResourceType.WOOD, 3), new Resource(ResourceType.STONE, 3)})), 4);
		cards[92] = new Building("Fortress", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.COINS, 2), new Resource(ResourceType.WOOD, 2),
						new Resource(ResourceType.STONE, 4)}),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 9)})),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 2), 
						new Resource(ResourceType.COUNCIL_PRIVILEGES, 1)})), 5);
		cards[93] = new Building("Cathedral", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 4), new Resource(ResourceType.STONE, 4)}),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 7),
						new Resource(ResourceType.FAITH, 3)})),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 1)})), 2);
		cards[94] = new Building("Bank", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 1), new Resource(ResourceType.STONE, 3),
						new Resource(ResourceType.COINS, 3)}),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 7)})),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.COINS, 5)})), 2);
		cards[95] = new Building("Garden", PeriodType.III,
				new Cost(new Resource[] { new Resource(ResourceType.WOOD, 4), new Resource(ResourceType.STONE, 2),
						new Resource(ResourceType.SERVANTS, 2)}),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 10)})),
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 3)})), 1);

		LeaderCard[] leaderCards = new LeaderCard[20];

		leaderCards[0] = new LeaderCard("Francesco Sforza", new CardEffect(new ActionEffect(SlotType.HARVEST, 1, true)),
				new Resource[] { new Resource(ResourceType.VENTURE, 5) });
		leaderCards[1] = new LeaderCard("Ludovico Ariosto",
				new CardEffect(new SlotModifierEffect(SlotType.ANY, true, true, true), true),
				new Resource[] { new Resource(ResourceType.CHARACTER, 5) });
		leaderCards[2] = new LeaderCard("Filippo Brunelleschi",
				new CardEffect(new CostModifierEffect(true), true),
				new Resource[] { new Resource(ResourceType.BUILDING, 5) });
		leaderCards[3] = new LeaderCard("Sigismondo Malatesta",
				new CardEffect(new FamiliarEffect(3, true, new FamiliarColor[] { FamiliarColor.UNCOLORED }, 1), true),
				new Resource[] { new Resource(ResourceType.MILITARY, 7), new Resource(ResourceType.FAITH, 3) });
		leaderCards[4] = new LeaderCard("Girolamo Savonarola",
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.FAITH, 1) })),
				new Resource[] { new Resource(ResourceType.COINS, 18) });
		leaderCards[5] = new LeaderCard("Michelangelo Buonarroti",
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.COINS, 3) })),
				new Resource[] { new Resource(ResourceType.STONE, 10) });
		leaderCards[6] = new LeaderCard("Giovanni dalle Bande Nere",
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.COINS, 1),
						new Resource(ResourceType.WOOD, 1), new Resource(ResourceType.STONE, 1) })),
				new Resource[] { new Resource(ResourceType.MILITARY, 12) });
		leaderCards[7] = new LeaderCard("Leonardo da Vinci", new CardEffect(new ActionEffect(SlotType.PRODUCTION, 0, true)),
				new Resource[] { new Resource(ResourceType.CHARACTER, 4), new Resource(ResourceType.TERRITORY, 2) });
		leaderCards[8] = new LeaderCard("Sandro Botticelli", new CardEffect(new ResourceEffect(
				new Resource[] { new Resource(ResourceType.MILITARY, 2), new Resource(ResourceType.VICTORY, 1) })),
				new Resource[] { new Resource(ResourceType.WOOD, 10) });
		leaderCards[9] = new LeaderCard("Ludovico il Moro",
				new CardEffect(new FamiliarEffect(5, false,
						new FamiliarColor[] { FamiliarColor.BLACK, FamiliarColor.ORANGE, FamiliarColor.WHITE }, 1), true),
				new Resource[] { new Resource(ResourceType.CHARACTER, 2), new Resource(ResourceType.BUILDING, 2),
						new Resource(ResourceType.TERRITORY, 2), new Resource(ResourceType.VENTURE, 2) });
		leaderCards[10] = new LeaderCard("Lucrezia Borgia",
				new CardEffect(new FamiliarEffect(2, true,
						new FamiliarColor[] { FamiliarColor.BLACK, FamiliarColor.ORANGE, FamiliarColor.WHITE }, 1), true),
				new Resource[] { new Resource(ResourceType.ANY_CARD, 6) });
		leaderCards[11] = new LeaderCard("Federico da Montefeltro",
				new CardEffect(
						new Effect[] { new FamiliarEffect(6, false, new FamiliarColor[] { FamiliarColor.BLACK }, 1),
								new FamiliarEffect(6, false, new FamiliarColor[] { FamiliarColor.WHITE }, 1),
								new FamiliarEffect(6, false, new FamiliarColor[] { FamiliarColor.ORANGE }, 1) },
						true),
				new Resource[] { new Resource(ResourceType.TERRITORY, 5) });
		leaderCards[12] = new LeaderCard("Lorenzo de' Medici", new CardEffect(new CopyEffect(), true),
				new Resource[] { new Resource(ResourceType.VICTORY, 35) });
		leaderCards[13] = new LeaderCard("Sisto IV",
				new CardEffect(new ChurchSupportBonusEffect(new Resource(ResourceType.VICTORY, 5)), true),
				new Resource[] { new Resource(ResourceType.STONE, 6), new Resource(ResourceType.WOOD, 6),
						new Resource(ResourceType.COINS, 6), new Resource(ResourceType.SERVANTS, 6), });
		leaderCards[14] = new LeaderCard("Cesare Borgia", new CardEffect(new NoTerritoryRequisiteEffect(), true),
				new Resource[] { new Resource(ResourceType.BUILDING, 3), new Resource(ResourceType.COINS, 12),
						new Resource(ResourceType.FAITH, 2) });
		leaderCards[15] = new LeaderCard("Santa Rita",
				new CardEffect(
						new Effect[] { new GainModifierEffect(new Resource(ResourceType.COINS, 2), true),
								new GainModifierEffect(new Resource(ResourceType.STONE, 2), true),
								new GainModifierEffect(new Resource(ResourceType.WOOD, 2), true),
								new GainModifierEffect(new Resource(ResourceType.SERVANTS, 2), true) },
						false, true),
				new Resource[] { new Resource(ResourceType.FAITH, 8) });
		leaderCards[16] = new LeaderCard("Cosimo de' Medici", new CardEffect(new ResourceEffect(
				new Resource[] { new Resource(ResourceType.VICTORY, 1), new Resource(ResourceType.SERVANTS, 3) })),
				new Resource[] { new Resource(ResourceType.BUILDING, 4), new Resource(ResourceType.CHARACTER, 2) });
		leaderCards[17] = new LeaderCard("Bartolomeo Colleoni",
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.VICTORY, 4) })),
				new Resource[] { new Resource(ResourceType.TERRITORY, 4), new Resource(ResourceType.VENTURE, 2) });
		leaderCards[18] = new LeaderCard("Ludovico III Gonzaga",
				new CardEffect(new ResourceEffect(new Resource[] { new Resource(ResourceType.COUNCIL_PRIVILEGES, 1) })),
				new Resource[] { new Resource(ResourceType.SERVANTS, 15) });
		leaderCards[19] = new LeaderCard("Pico della Mirandola",
				new CardEffect(new CostModifierEffect(new Resource(ResourceType.COINS, -3), false), true),
				new Resource[] { new Resource(ResourceType.VENTURE, 4), new Resource(ResourceType.BUILDING, 2) });
		
		Excommunication[] excommunications = new Excommunication[21];
		
		//I Period
		excommunications[0] = new Excommunication("1_1", PeriodType.I,
				new CardEffect(new GainModifierEffect(new Resource(ResourceType.MILITARY, -1), false), true));
		excommunications[1] = new Excommunication("1_2", PeriodType.I,
				new CardEffect(new GainModifierEffect(new Resource(ResourceType.COINS, -1), false), true));
		excommunications[2] = new Excommunication("1_3", PeriodType.I,
				new CardEffect(new GainModifierEffect(new Resource(ResourceType.SERVANTS, -1), false), true));
		excommunications[3] = new Excommunication("1_4", PeriodType.I,
				new CardEffect(new Effect[] {
						new GainModifierEffect(new Resource(ResourceType.STONE, -1), false),
						new GainModifierEffect(new Resource(ResourceType.WOOD, -1), false)}, false, true));
		excommunications[4] = new Excommunication("1_5", PeriodType.I,
				new CardEffect(new ActionEffect(SlotType.HARVEST, -3, false), true));
		excommunications[5] = new Excommunication("1_6", PeriodType.I,
				new CardEffect(new ActionEffect(SlotType.PRODUCTION, -3, false), true));
		excommunications[6] = new Excommunication("1_7", PeriodType.I,
				new CardEffect(new FamiliarEffect(-1, false, new FamiliarColor[]{FamiliarColor.BLACK, FamiliarColor.ORANGE,
						FamiliarColor.WHITE}, 1)));
		
		//II Period
		excommunications[7] = new Excommunication("2_1", PeriodType.II,
				new CardEffect(new ActionEffect(SlotType.TERRITORY, -4, false), true));
		excommunications[8] = new Excommunication("2_2", PeriodType.II,
				new CardEffect(new ActionEffect(SlotType.BUILDING, -4, false), true));
		excommunications[9] = new Excommunication("2_3", PeriodType.II,
				new CardEffect(new ActionEffect(SlotType.CHARACTER, -4, false), true));
		excommunications[10] = new Excommunication("2_4", PeriodType.II,
				new CardEffect(new ActionEffect(SlotType.VENTURE, -4, false), true));
		excommunications[11] = new Excommunication("2_5", PeriodType.II,
				new CardEffect(new SlotModifierEffect(SlotType.MARKET, false, false, false), true));
		excommunications[12] = new Excommunication("2_6", PeriodType.II,
				new CardEffect(new FamiliarEffect(0, false, new FamiliarColor[]{}, 2)));
		excommunications[13] = new Excommunication("2_7", PeriodType.II,
				new CardEffect(new JumpFirstTurnEffect()));
		
		//III Period
		excommunications[14] = new Excommunication("3_1", PeriodType.III,
				new CardEffect(new VictoryPointsFromCardsEffect(CardType.CHARACTER)));
		excommunications[15] = new Excommunication("3_2", PeriodType.III,
				new CardEffect(new VictoryPointsFromCardsEffect(CardType.VENTURE)));
		excommunications[16] = new Excommunication("3_3", PeriodType.III,
				new CardEffect(new VictoryPointsFromCardsEffect(CardType.TERRITORY)));
		excommunications[17] = new Excommunication("3_4", PeriodType.III,
				new CardEffect(new ResourceEffect(new Resource(ResourceType.VICTORY, 5),
						new Resource[]{new Resource(ResourceType.VICTORY, -1)})));
		excommunications[18] = new Excommunication("3_5", PeriodType.III,
				new CardEffect(new ResourceEffect(new Resource(ResourceType.MILITARY, 1),
						new Resource[]{new Resource(ResourceType.VICTORY, -1)})));
		excommunications[19] = new Excommunication("3_6", PeriodType.III,
				new CardEffect(new VictoryPointsFromCardsEffect(CardType.BUILDING)));
		excommunications[20] = new Excommunication("3_7", PeriodType.III,
				new CardEffect(new Effect[]{new ResourceEffect(new Resource(ResourceType.COINS, 1),
						new Resource[]{new Resource(ResourceType.VICTORY, -1)}),
						new ResourceEffect(new Resource(ResourceType.WOOD, 1),
								new Resource[]{new Resource(ResourceType.VICTORY, -1)}),
						new ResourceEffect(new Resource(ResourceType.STONE, 1),
								new Resource[]{new Resource(ResourceType.VICTORY, -1)}),
						new ResourceEffect(new Resource(ResourceType.SERVANTS, 1),
								new Resource[]{new Resource(ResourceType.VICTORY, -1)})}, false));
		

		try {
			for (Card card : cards)
				saveCard(card);
			for(LeaderCard leaderCard : leaderCards)
				saveLeaderCard(leaderCard);
			for (Excommunication excommunication : excommunications)
				saveExcommunication(excommunication);

			Map<CardType, List<Card>> deck = loadCards();
			for (CardType cardType : new CardType[] { CardType.TERRITORY, CardType.CHARACTER, CardType.VENTURE,
					CardType.BUILDING }) {
				System.out.println(cardType);
				System.out.println("");
				List<Card> shuffledByPeriodCards = it.polimi.ingsw.LM45.util.ShuffleHelper
						.shuffleByPeriod(deck.get(cardType));
				for (Card card : shuffledByPeriodCards)
					System.out.println("\n" + card.toString() + "\n");
				System.out.println("------------------");
				System.out.println("");
			}

			System.out.println("Leader Cards:");
			System.out.println("");
			for(LeaderCard leaderCard : loadLeaderCards()){
				System.out.println("\n" + leaderCard.toString() + "\n");
			}
			
			System.out.println("Excommunications :");
			Map<PeriodType, List<Excommunication>> excommunicationDeck = loadExcommunications();
			for (PeriodType periodType : new PeriodType[] { PeriodType.I, PeriodType.II, PeriodType.III}) {
				System.out.println(periodType);
				for(Excommunication excommunication : excommunicationDeck.get(periodType)){
					System.out.println("\n" + excommunication.toString() + "\n");
					System.out.println("------------------");
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
