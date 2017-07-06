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
		OTHER_SLOTS,
		EXCOMMUNICATIONS,
		PERSONAL_BOARDS,
		SINGLE_PERSONAL_BOARD
	}
	
	private static Map<Stage, List<Pair<Consumer<GameBoardCli>, String>>> allOptions;
	
	private static void initialize(){
		allOptions = new EnumMap<>(Stage.class);

		allOptions.put(Stage.MAIN, Arrays.asList(
				new Pair<Consumer<GameBoardCli>, String>(GameBoardCli::showTowers, "Show towers"),
				new Pair<Consumer<GameBoardCli>, String>(GameBoardCli::showOtherSlots, "Show other slots"),
				new Pair<Consumer<GameBoardCli>, String>(GameBoardCli::showExcommunications, "Show excommunications"),
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
		
		allOptions.put(Stage.EXCOMMUNICATIONS, Arrays.asList(
				new Pair<Consumer<GameBoardCli>, String>(GameBoardCli::showMain, "Back"))
		);
		
		allOptions.put(Stage.PERSONAL_BOARDS, Arrays.asList(
				new Pair<Consumer<GameBoardCli>, String>(GameBoardCli::showMain, "Back"))
		);
		
		allOptions.put(Stage.SINGLE_PERSONAL_BOARD, Arrays.asList(
				new Pair<Consumer<GameBoardCli>, String>(GameBoardCli::showPersonalBoards, "Back"))
		);
	}
	
	public static void navigate(GameBoardCli gameBoard, List<Pair<Consumer<GameBoardCli>, String>> options){
		try {
			ConsoleReader.readOption(options, true).accept(gameBoard);
		}
		catch (InterruptedException e) {
			// We can arrive here for two reasons:
			// 1. the server sent us a message like "hey dude, it is you turn now" and we decided to abort
			// 	  whatever menu we were in and to prompt a new decision from the main menu
			// 2. something TERRIBLY wrong has appened and our thread are burning orribly
			// Hope the second one never happens
		}
	}
	
	public static void navigate(Stage currentStage, GameBoardCli gameBoard, List<Pair<Consumer<GameBoardCli>, String>> additionalOptions){
		if(allOptions == null)
			initialize();

		List<Pair<Consumer<GameBoardCli>, String>> extendedOptions = new ArrayList<>(allOptions.get(currentStage));
		extendedOptions.addAll(additionalOptions);
		
		navigate(gameBoard, extendedOptions);
	}
	
	public static void navigate(Stage currentStage, GameBoardCli gameBoard){
		navigate(currentStage, gameBoard, new ArrayList<>());
	}
	
}
