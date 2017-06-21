package it.polimi.ingsw.LM45.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.PeriodType;

public class ShuffleHelper {
	
	// Durstenfeld algorithm
	public static <T> List<T> shuffle(Collection<T> list){
		Random random = new Random();
		@SuppressWarnings("unchecked") // Because of Java's dumb implementation of generics with type erasure
		T[] result = (T[])list.toArray();
		for(int i = list.size() - 1; i >= 1; i--){
			int j = random.nextInt(i + 1);
			T tmp = result[i];
			result[i] = result[j];
			result[j] = tmp;
		}
		return Arrays.asList(result);
	}
	
	public static List<Card> shuffleByPeriod(List<Card> cards){
		List<Card> result = new ArrayList<>();
		for(PeriodType periodType : PeriodType.values()){
			List<Card> cardsOfThisPeriod = cards.stream().filter(c -> c.getPeriodType() == periodType).collect(Collectors.toList());
			result.addAll(shuffle(cardsOfThisPeriod));	
		}
		return result;
	}
	
}
