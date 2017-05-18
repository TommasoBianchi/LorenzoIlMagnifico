package it.polimi.ingsw.LM45.model.effects;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.LM45.model.core.Resource;

public class ActionModifier {
	
	private List<Resource> costModifiers;
	private List<Resource> gainModifiers;
	private int diceBonus;
	private boolean blockImmediateResources;
	private boolean canPlaceMultipleFamiliars;
	private boolean canPlaceFamiliars;
	
	public ActionModifier(){
		this.costModifiers = new ArrayList<Resource>();
		this.gainModifiers = new ArrayList<Resource>();
		this.diceBonus = 0;
		this.blockImmediateResources = false;
		this.canPlaceMultipleFamiliars = false;
		this.canPlaceFamiliars = true;
	}
}
