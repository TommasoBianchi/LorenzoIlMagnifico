package it.polimi.ingsw.LM45.model.effects;

import java.util.Arrays;
import java.util.stream.Stream;

import it.polimi.ingsw.LM45.model.core.SlotType;

public class CardEffect {
	
	public static final CardEffect EMPTY = new CardEffect(new Effect[]{}, false);
	
	private Effect[] effects;
	private boolean effectsAreAlternative;
	private boolean effectsArePermanent;

	public CardEffect(Effect[] effects, boolean effectsAreAlternative, boolean effectsArePermanent){
		this.effects = effects;
		this.effectsAreAlternative = effectsAreAlternative;
		this.effectsArePermanent = effectsArePermanent;
	}
	
	public CardEffect(Effect[] effects, boolean effectsAreAlternative){
		this(effects, effectsAreAlternative, false);
	}
	
	public CardEffect(Effect effect, boolean effectsArePermanent){
		this(new Effect[]{ effect }, false, effectsArePermanent);
	}
	
	public CardEffect(Effect effect){
		this(effect, false);
	}
	
	public void resolveEffects(EffectResolutor effectResolutor){
		if(effectsArePermanent)
			effectResolutor.addPermanentEffect(this);
		else
			if(effectsAreAlternative)
				effectResolutor.chooseFrom(effects).resolveEffect(effectResolutor);
			else
				Arrays.stream(effects).forEach(effect -> effect.resolveEffect(effectResolutor));
	}
	
	public ActionModifier getActionModifier(SlotType slotType, EffectResolutor effectResolutor){
		Stream<ActionModifier> actionModifiers = Arrays.stream(effects).map(effect -> effect.getActionModifier(slotType));
		if(effectsAreAlternative){
			// If they are alternative and at least one of them is effective (i.e. generates a non-empty ActionModifier)
			// make the player choose one and return that
			if(actionModifiers.allMatch(actionModifier -> actionModifier.isEmpty()))
				return ActionModifier.EMPTY;
			else
				return effectResolutor.chooseFrom(actionModifiers.toArray(ActionModifier[]::new));
		}
		else
			// Otherwise just return the merging of the ActionModifiers of every effect
			return actionModifiers.reduce(ActionModifier.EMPTY, (accumulator, actionModifier) -> accumulator.merge(actionModifier));
	}
	
}
