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
import it.polimi.ingsw.LM45.model.cards.Cost;
import it.polimi.ingsw.LM45.model.cards.PeriodType;
import it.polimi.ingsw.LM45.model.effects.CardEffect;
import it.polimi.ingsw.LM45.model.effects.Effect;

// Necessari per il main di test
import it.polimi.ingsw.LM45.model.effects.ResourceEffect;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.model.cards.Territory;

public class FileManager {

	private static final String BASE_PATH = "Assets/Json";	
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting()
			  .registerTypeAdapter(Effect.class, new GsonTypeAdapter<Effect>())
			  .registerTypeAdapter(Card.class, new GsonTypeAdapter<Card>())
			  .create();
	
	public static void saveCard(Card card) throws IOException {
		String path = BASE_PATH + "/Cards/" + card.getCardType();
		File directory = new File(path);
		
		if(!directory.exists()){
			directory.mkdirs();
		}
		
		FileWriter writer = new FileWriter(path + "/" + card.getName() + ".json");
		GSON.toJson(card, Card.class, writer);
		writer.close();
	}
	
	public static Map<CardType, List<Card>> loadCards() throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		Map<CardType, List<Card>> deck = new HashMap<CardType, List<Card>>();
		File folder = new File(BASE_PATH + "/Cards/");
		
		for(File dir : folder.listFiles()){
			if(dir.isDirectory()){
				CardType cardType = CardType.valueOf(dir.getName());
				List<Card> cards = new ArrayList<Card>();
				
				for(File file : dir.listFiles()){
					if(file.isFile()){
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
	public static void main(String[] args){
		Effect effect = new ResourceEffect(new Resource[]{ }, null, new Resource[]{ new Resource(ResourceType.COINS, 3) });
		Cost cost = new Cost(new Resource[]{ new Resource(ResourceType.STONE, 1), new Resource(ResourceType.WOOD, 2) });
		Card card1 = new Territory("Carta di test1", PeriodType.I, cost, CardEffect.EMPTY, new CardEffect(effect), 2);
		Card card2 = new Territory("Carta di test2", PeriodType.I, cost, new CardEffect(effect), CardEffect.EMPTY, 4);
		
		try {
			saveCard(card1);
			saveCard(card2);
			Map<CardType, List<Card>> deck = loadCards();
			for(Card card : deck.get(CardType.TERRITORY))
				System.out.println(card.getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
