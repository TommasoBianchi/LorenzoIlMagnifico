package it.polimi.ingsw.LM45.view.cli;

public class ConsoleWriter {
	
	private static final String blue = (char)27 + "[34m";
	private static final String green = (char)27 + "[32m";
	private static final String yellow = (char)27 + "[33m";
	private static final String format = "\u001B[0m";
	
	public static void printBlue (String msg) {
		System.out.println(blue + msg + format);
	}
	
	public static void printGreen (String msg) {
		System.out.println(green + msg + format);
	}
	
	public static void printYellow (String msg) {
		System.out.println(yellow + msg);
	}
	
	public static void main (String[] args) {
		String msg = "ciao";
		printBlue(msg);
	}

}
