package it.polimi.ingsw.LM45.model.effects;

import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.SlotType;

public class ActionEffect extends Effect {
	
	private SlotType slotType;
	private int diceNumber;
	private Resource[] discount;
	
	public ActionEffect(SlotType slotType, int diceNumber, Resource[] discount){
		this.slotType = slotType;
		this.diceNumber = diceNumber;
		this.discount = discount;
	}

	@Override
	public void ResolveEffect(Player player) {
		// TODO Auto-generated method stub

	}

}
