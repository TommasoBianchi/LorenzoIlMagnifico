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
	private List<Pair<FamiliarColor, PlayerColor>> familiars;
	private Resource[] immediateResources;

	public SlotCli(SlotType slotType, Resource[] immediateResources) {
		this.slotType = slotType;
		this.familiars = new ArrayList<>();
		this.immediateResources = immediateResources.clone();
	}

	public void print() {
		ConsoleWriter.println(slotType + " slot");

		if (immediateResources.length > 0){
			ConsoleWriter.println("");
			ConsoleWriter.println("If you place a familiar on this slot you will receive "
					+ Arrays.stream(immediateResources).map(Resource::toString).reduce("", (a, b) -> a + " " + b));
		}

		ConsoleWriter.println("");

		if (familiars.size() > 0)
			ConsoleWriter.println("Familiars: "
					+ familiars.stream().map(pair -> "familiar " + pair._1() + " by player " + pair._2()).reduce("", (a, b) -> a + " " + b));
		else
			ConsoleWriter.println("No familiars on this slot");
	}

	public void placeFamiliar(FamiliarColor familiarColor, PlayerColor playerColor) {
		this.familiars.add(new Pair<FamiliarColor, PlayerColor>(familiarColor, playerColor));
	}

	public void clearFamiliars() {
		this.familiars.clear();
	}
	
}
