package it.polimi.ingsw.LM45.util;

public class Pair <T, U> {

	private T t;
	private U u;
	
	public Pair(T t, U u){
		this.t = t;
		this.u = u;
	}
	
	public T _1(){
		return t;
	}
	
	public U _2(){
		return u;
	}
	
}
