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
	
	public Familiar(Player player, FamiliarColor familiarColor){
		this.player = player;
		this.familiarColor = familiarColor;
		this.value = 0;
		this.servantBonus = 0;
		this.otherBonuses = 0;
		this.valueIsStatic = false;
		this.servantBonusCost = 1;
		this.isPlaced = false;
	}
	
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
	
	public void addBonus(int bonus){
		this.otherBonuses += bonus;
	}
	
	public Player getPlayer(){
		return this.player;
	}
	
	public FamiliarColor getFamiliarColor(){
		return this.familiarColor;
	}
	
	public void addServantsBonus(){
		servantBonus++;
		this.player.addResources(new Resource(ResourceType.SERVANTS, -servantBonusCost));
	}
	
	public boolean getIsPlaced(){
		return this.isPlaced;
	}
	
	public void setIsPlaced(boolean value){
		this.isPlaced = value;
	}
	
	public void setServantBonusCost(int cost){
		this.servantBonusCost = cost;
	}
	
}
