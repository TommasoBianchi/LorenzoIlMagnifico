package it.polimi.ingsw.LM45.model.effects;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import it.polimi.ingsw.LM45.model.core.SlotType;

public class CardEffect implements Serializable {

	private static final long serialVersionUID = 1L;
	
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
		if(effectsAreAlternative)
			effectResolutor.chooseFrom(effects).resolveEffect(effectResolutor);
		else
			Arrays.stream(effects).forEach(effect -> effect.resolveEffect(effectResolutor));
	}
	
	public ActionModifier getActionModifier(SlotType slotType, EffectResolutor effectResolutor){
		List<ActionModifier> actionModifiers = Arrays.stream(effects).map(effect -> effect.getActionModifier(slotType)).collect(Collectors.toList());
		if(effectsAreAlternative){
			// If they are alternative and at least one of them is effective (i.e. generates a non-empty ActionModifier)
			// make the player choose one and return that
			if(actionModifiers.stream().allMatch(ActionModifier::isEmpty))
				return ActionModifier.EMPTY();
			else
				return effectResolutor.chooseFrom(actionModifiers.stream().toArray(ActionModifier[]::new));
		}
		else
			// Otherwise just return the merging of the ActionModifiers of every effect
			return actionModifiers.stream().reduce(ActionModifier.EMPTY(), (accumulator, actionModifier) -> accumulator.merge(actionModifier));
	}
	
	public boolean getEffectsArePermanent(){
		return effectsArePermanent;
	}
	
	@Override
	public String toString() {
		if(effectsAreAlternative)
			return Arrays.stream(effects).map(Effect::toString).reduce((a,b) -> a + " or\n" + b).orElse("No Effect");
		else
			return Arrays.stream(effects).map(Effect::toString).reduce((a,b) -> a + " and\n" + b).orElse("No Effect");
	}
}
