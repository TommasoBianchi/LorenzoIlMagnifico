package it.polimi.ingsw.LM45.view.cli;

import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi.Attribute;
import com.diogonunes.jcdp.color.api.Ansi.BColor;
import com.diogonunes.jcdp.color.api.Ansi.FColor;

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
		
		private ConsoleColor(FColor fontColor, BColor backgroundColor){
			this.fontColor = fontColor;
			this.backgroundColor = backgroundColor;
		}
		
		protected FColor toFontColor(){
			return this.fontColor;
		}
		
		protected BColor toBackgroundColor() {
			return this.backgroundColor;
		}
	}
	
	private static ColoredPrinter coloredPrinter = new ColoredPrinter.Builder(1, false).build();
	
	public static void println(String message, ConsoleColor color, ConsoleColor backgroundColor){
		coloredPrinter.println(message, Attribute.NONE, color.toFontColor(), backgroundColor.toBackgroundColor());
		coloredPrinter.clear();
	}
	
	public static void println(String message, ConsoleColor color){
		println(message, color, ConsoleColor.NONE);
	}
	
	public static void println(String message){
		println(message, ConsoleColor.NONE);
	}

}
