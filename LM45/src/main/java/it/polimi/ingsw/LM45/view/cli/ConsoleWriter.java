package it.polimi.ingsw.LM45.view.cli;

import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi.Attribute;
import com.diogonunes.jcdp.color.api.Ansi.BColor;
import com.diogonunes.jcdp.color.api.Ansi.FColor;

/**
 * Class this different methods to print with different colors.
 * It has also inside an Enum "ConsoleColor" with all colors
 * that can be used to print
 * 
 * @author Kostandin
 *
 */
public class ConsoleWriter {
	
	public enum ConsoleColor {
		BLUE(FColor.BLUE, BColor.BLUE),
		GREEN(FColor.GREEN, BColor.GREEN),
		YELLOW(FColor.YELLOW, BColor.YELLOW),
		RED(FColor.RED, BColor.RED),
		MAGENTA(FColor.MAGENTA, BColor.MAGENTA),
		CYAN(FColor.CYAN, BColor.CYAN),
		WHITE(FColor.WHITE, BColor.WHITE),
		BLACK(FColor.BLACK, BColor.BLACK),
		NONE(FColor.NONE, BColor.NONE);
		
		private FColor fontColor;
		private BColor backgroundColor;
		
		/**
		 * @param fontColor the color of the font
		 * @param backgroundColor the color of the background
		 */
		private ConsoleColor(FColor fontColor, BColor backgroundColor){
			this.fontColor = fontColor;
			this.backgroundColor = backgroundColor;
		}
		
		/**
		 * @return the fontColor from a ConsoleColor
		 */
		protected FColor toFontColor(){
			return this.fontColor;
		}
		
		/**
		 * @return the backgroundColor from a ConsoleColor
		 */
		protected BColor toBackgroundColor() {
			return this.backgroundColor;
		}
	}
	
	private static ColoredPrinter coloredPrinter = new ColoredPrinter.Builder(1, false).build();
	
	/**
	 * @param message the message to print
	 * @param color the consoleColor to use for fontColor
	 * @param backgroundColor the consoleColor to use for backgroundColor
	 */
	public static void println(String message, ConsoleColor color, ConsoleColor backgroundColor){
		coloredPrinter.println(message, Attribute.NONE, color.toFontColor(), backgroundColor.toBackgroundColor());
		coloredPrinter.clear();
	}
	
	/**
	 * @param message the message to print
	 * @param color the consoleColor to use for fontColor
	 */
	public static void println(String message, ConsoleColor color){
		println(message, color, ConsoleColor.BLACK);
	}
	
	/**
	 * @param message the message to print
	 */
	public static void println(String message){
		println(message, ConsoleColor.NONE);
	}
	
	/**
	 * @param message the message to print
	 */
	public static void printCommand(String message) {
		println(message, ConsoleColor.CYAN);
	}
	
	/**
	 * @param message the message to print
	 */
	public static void printShowInfo(String message) {
		println(message, ConsoleColor.YELLOW);
	}
	
	/**
	 * @param message the message to print
	 */
	public static void printError(String message) {
		println(message, ConsoleColor.RED);
	}
	
	/**
	 * @param message the message to print
	 */
	public static void printValidInput(String message) {
		println(message, ConsoleColor.GREEN);
	}
	
	/**
	 * @param message the message to print
	 */
	public static void printChoice(String message) {
		println(message, ConsoleColor.WHITE);
	}

}
