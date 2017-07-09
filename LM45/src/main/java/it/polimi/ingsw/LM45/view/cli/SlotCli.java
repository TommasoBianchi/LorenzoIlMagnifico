package it.polimi.ingsw.LM45.view.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.util.Pair;

/**
 * Class to save Slot info for the CLI
 * 
 * @author Kostandin
 *
 */
public class SlotCli {

	private SlotType slotType;
	private int slotID;
	private int diceValue;
	private int diceModifier;
	private List<Pair<FamiliarColor, PlayerColor>> familiars;
	private Resource[] immediateResources;

	/**
	 * @param slotType the type of the slot
	 * @param slotID the slot position
	 * @param diceValue the value of the dice to make an action in that slot
	 * @param diceModifier a dice modifier that changes slot's diceValue
	 * @param immediateResources an array of resources
	 */
	public SlotCli(SlotType slotType, int slotID, int diceValue, int diceModifier, Resource[] immediateResources) {
		this.slotType = slotType;
		this.slotID = slotID;
		this.familiars = new ArrayList<>();
		this.immediateResources = immediateResources.clone();
		this.diceModifier = diceModifier;
		this.diceValue = diceValue;
	}
	
	/**
	 * @param slotType the type of the slot
	 * @param slotID the slot position
	 * @param immediateResources an array of resources
	 */
	public SlotCli(SlotType slotType, int slotID, Resource[] immediateResources) {
		this(slotType,slotID,1,0,immediateResources);
	}

	/**
	 * It prints all slot's information
	 */
	public void print() {
		ConsoleWriter.printShowInfo(slotType + " slot " + slotID + " (dice value " + diceValue + ")");
		if(diceModifier != 0)
			ConsoleWriter.printShowInfo("dice modifier -- " + diceModifier);
		if (immediateResources.length > 0){
			ConsoleWriter.println("");
			ConsoleWriter.printShowInfo("If you place a familiar on this slot you will receive : "
					+ Arrays.stream(immediateResources).map(Resource::toString).reduce("", (a, b) -> a + " " + b));
		}

		ConsoleWriter.println("");

		if (!familiars.isEmpty())
			ConsoleWriter.printShowInfo("Familiars : "
					+ familiars.stream().map(pair -> "familiar " + pair._1() + " by player " + pair._2()).reduce("", (a, b) -> a + " " + b));
		else
			ConsoleWriter.printShowInfo("No familiars on this slot");
	}

	/**
	 * @param familiarColor the familiar color
	 * @param playerColor the player color
	 */
	public void placeFamiliar(FamiliarColor familiarColor, PlayerColor playerColor) {
		this.familiars.add(new Pair<FamiliarColor, PlayerColor>(familiarColor, playerColor));
	}

	/**
	 * clears all familiars from slot
	 */
	public void clearFamiliars() {
		this.familiars.clear();
	}
	
	/**
	 * @return the slottype of the slot
	 */
	public SlotType getSlotType(){
		return this.slotType;
	}
	
	/**
	 * @return the slot's position
	 */
	public int getID(){
		return this.slotID;
	}
	
	/**
	 * @return a string with slottype and slot's position
	 */
	public String getNamedID(){
		return slotType + " " + slotID;
	}
	
}
