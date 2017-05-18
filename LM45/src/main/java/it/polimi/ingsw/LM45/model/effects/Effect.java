package it.polimi.ingsw.LM45.model.effects;

import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.SlotType;

public abstract class Effect {

	public abstract void ResolveEffect(Player player);
	
	public ActionModifier getActionModifier(SlotType slotType){
		return new ActionModifier();
	}

}
