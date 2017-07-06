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

import it.polimi.ingsw.LM45.config.Configuration;
import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.model.cards.Cost;
import it.polimi.ingsw.LM45.model.cards.Excommunication;
import it.polimi.ingsw.LM45.model.cards.LeaderCard;
import it.polimi.ingsw.LM45.model.cards.PeriodType;
import it.polimi.ingsw.LM45.model.effects.Effect;

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

}
