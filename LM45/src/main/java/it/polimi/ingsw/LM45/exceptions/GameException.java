package it.polimi.ingsw.LM45.exceptions;

public class GameException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public GameException(){
		super();
	}
	
	public GameException(String message){
		super(message);
	}
	
}
