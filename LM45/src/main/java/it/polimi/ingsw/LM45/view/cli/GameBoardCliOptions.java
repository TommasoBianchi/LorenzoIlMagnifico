package it.polimi.ingsw.LM45.view.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.util.Pair;

public class GameBoardCliOptions {

	public enum Stage {
		MAIN,
		TOWERS,
		SINGLE_TOWER,
		OTHER_SLOTS
	}
	
	private static Map<Stage, List<Pair<Consumer<GameBoardCli>, String>>> allOptions;
	
	private static void initialize(){
		allOptions = new EnumMap<>(Stage.class);

		allOptions.put(Stage.MAIN, Arrays.asList(
				new Pair<Consumer<GameBoardCli>, String>(GameBoardCli::showTowers, "Show towers"),
				new Pair<Consumer<GameBoardCli>, String>(GameBoardCli::showOtherSlots, "Show other slots"),
				new Pair<Consumer<GameBoardCli>, String>(GameBoardCli::showPersonalBoards, "Show personal boards"))
		);
		
		List<Pair<Consumer<GameBoardCli>, String>> towersOptions = new ArrayList<>();
		for(CardType cardType : new CardType[]{ CardType.TERRITORY, CardType.CHARACTER, CardType.BUILDING, CardType.VENTURE })
			towersOptions.add(new Pair<Consumer<GameBoardCli>, String>(gameBoard -> gameBoard.showTower(cardType), "Show " + cardType + " tower"));
		towersOptions.add(new Pair<Consumer<GameBoardCli>, String>(GameBoardCli::showMain, "Back"));
		allOptions.put(Stage.TOWERS, towersOptions);
		
		allOptions.put(Stage.SINGLE_TOWER, Arrays.asList(
				new Pair<Consumer<GameBoardCli>, String>(GameBoardCli::showTowers, "Back"))
		);
		
		allOptions.put(Stage.OTHER_SLOTS, Arrays.asList(
				new Pair<Consumer<GameBoardCli>, String>(GameBoardCli::showMain, "Back"))
		);
	}
	
	public static void navigate(Stage currentStage, GameBoardCli gameBoard){
		if(allOptions == null)
			initialize();
		
		try {
			ConsoleReader.readOption(allOptions.get(currentStage)).accept(gameBoard);
		}
		catch (InterruptedException e) {
			// TODO think about this
			e.printStackTrace();
			gameBoard.showMain();
		}
	}
	
}
