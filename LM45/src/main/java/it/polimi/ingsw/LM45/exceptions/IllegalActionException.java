package it.polimi.ingsw.LM45.exceptions;

public class IllegalActionException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public IllegalActionException(){
		super();
	}
	
	public IllegalActionException(String message){
		super(message);
	}
	
	@Override
	public String getMessage(){
		return "ILLEGAL ACTION: " + super.getMessage();
	}
	
}
