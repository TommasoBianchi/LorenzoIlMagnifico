package it.polimi.ingsw.LM45.view.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.PlayerColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.util.Pair;

public class SlotCli {

	private SlotType slotType;
	private int slotID;
	private int diceValue;
	private int diceModifier;
	private List<Pair<FamiliarColor, PlayerColor>> familiars;
	private Resource[] immediateResources;

	public SlotCli(SlotType slotType, int slotID, int diceValue, int diceModifier, Resource[] immediateResources) {
		this.slotType = slotType;
		this.slotID = slotID;
		this.familiars = new ArrayList<>();
		this.immediateResources = immediateResources.clone();
		this.diceModifier = diceModifier;
		this.diceValue = diceValue;
	}
	
	public SlotCli(SlotType slotType, int slotID, Resource[] immediateResources) {
		this(slotType,slotID,1,0,immediateResources);
	}

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

	public void placeFamiliar(FamiliarColor familiarColor, PlayerColor playerColor) {
		this.familiars.add(new Pair<FamiliarColor, PlayerColor>(familiarColor, playerColor));
	}

	public void clearFamiliars() {
		this.familiars.clear();
	}
	
	public SlotType getSlotType(){
		return this.slotType;
	}
	
	public int getID(){
		return this.slotID;
	}
	
	public String getNamedID(){
		return slotType + " " + slotID;
	}
	
}
