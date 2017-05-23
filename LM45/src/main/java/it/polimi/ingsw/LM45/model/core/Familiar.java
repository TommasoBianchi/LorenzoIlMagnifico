package it.polimi.ingsw.LM45.model.core;

public class Familiar {

	private Player player;
	private FamiliarColor familiarColor;
	private int value;
	private int servantBonus;
	private int otherBonuses;
	private boolean valueIsStatic;
	private int servantBonusCost;
	private boolean isPlaced;
	
	public void clearServantsBonus(){
		servantBonus = 0;
	}
	
	public int getValue(){
		return value + servantBonus + otherBonuses;
	}
	
	public void setValue(int value, boolean valueIsStatic){
		if(!valueIsStatic){
			this.value = value;
			this.valueIsStatic = valueIsStatic;
		}
	}
	
	public void setValue(int value){
		setValue(value, false);
	}
	
	public void addServantsBonus(){
		servantBonus++;
	}
	
	public void setIsPlaced(boolean value){
		this.isPlaced = value;
	}
	
}
