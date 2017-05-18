package it.polimi.ingsw.LM45.model.effects;

import it.polimi.ingsw.LM45.model.core.Player;

public class CardEffect {
	
	public static final CardEffect EMPTY = new CardEffect(new Effect[]{}, false);
	
	private Effect[] effects;
	private boolean effectsAreAlternative;

	public CardEffect(Effect[] effects, boolean effectsAreAlternative){
		this.effects = effects;
		this.effectsAreAlternative = effectsAreAlternative;
	}
	
	public CardEffect(Effect effect){
		this(new Effect[]{ effect }, false);
	}
	
}
