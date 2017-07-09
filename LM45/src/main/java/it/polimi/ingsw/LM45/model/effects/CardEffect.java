package it.polimi.ingsw.LM45.model.effects;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.model.effects.modifiers.ActionModifier;

/**
 * A collection of {@link Effect}
 * 
 * @author Tommy
 *
 */
public class CardEffect implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * An empty CardEffect
	 */
	public static final CardEffect EMPTY = new CardEffect(new Effect[] {}, false);

	private Effect[] effects;
	private boolean effectsAreAlternative;
	private boolean effectsArePermanent;

	/**
	 * @param effects
	 *            an array of effects
	 * @param effectsAreAlternative
	 *            true if the effects in the array represents alternatives
	 * @param effectsArePermanent
	 *            true if the effect in the array have to be added to the player that resolves them as permanent effects
	 */
	public CardEffect(Effect[] effects, boolean effectsAreAlternative, boolean effectsArePermanent) {
		this.effects = effects;
		this.effectsAreAlternative = effectsAreAlternative;
		this.effectsArePermanent = effectsArePermanent;
	}

	/**
	 * Creates a CardEffect with non-permanent effects
	 * 
	 * @param effects
	 *            an array of effects
	 * @param effectsAreAlternative
	 *            true if the effects in the array represents alternatives
	 */
	public CardEffect(Effect[] effects, boolean effectsAreAlternative) {
		this(effects, effectsAreAlternative, false);
	}

	/**
	 * Creates a CardEffect with a single (and thus non-alternative) effect
	 * 
	 * @param effect
	 *            a single effect
	 * @param effectsArePermanent
	 *            true if the effect in the array have to be added to the player that resolves them as permanent effects
	 */
	public CardEffect(Effect effect, boolean effectsArePermanent) {
		this(new Effect[] { effect }, false, effectsArePermanent);
	}

	/**
	 * Creates a CardEffect with a single (and thus non-alternative), non-permanent effect
	 * 
	 * @param effect
	 *            a single effect
	 */
	public CardEffect(Effect effect) {
		this(effect, false);
	}

	/**
	 * Resolves all the effects in this cardEffect, including the case in which effects are alternative and the player has to choose between them
	 * 
	 * @param effectResolutor
	 *            the effectResolutor of the player trying to resolve this cardEffect
	 */
	public void resolveEffects(EffectResolutor effectResolutor) {
		if (effectsAreAlternative) {
			Effect[] choosableEffects = Arrays.stream(effects).filter(effect -> effect.canResolveEffect(effectResolutor)).toArray(Effect[]::new);
			if (choosableEffects.length == 0)
				return;
			else if (choosableEffects.length == 1)
				choosableEffects[0].resolveEffect(effectResolutor);
			else
				effectResolutor.chooseFrom(choosableEffects).resolveEffect(effectResolutor);
		}
		else
			Arrays.stream(effects).forEach(effect -> effect.resolveEffect(effectResolutor));
	}

	/**
	 * Get the actionModifier granted by the effects in this cardEffect, including the case in which effects are alternative and the player has to choose between them.
	 * 
	 * @param slotType
	 *            the slotType of the slot on which a player is trying to do an action
	 * @param effectResolutor
	 *            the effectResolutor of the player trying to do that action
	 * @return the actionModifier merging all the actionModifiers of all effects in this cardEffect
	 */
	public ActionModifier getActionModifier(SlotType slotType, EffectResolutor effectResolutor) {
		List<ActionModifier> actionModifiers = Arrays.stream(effects).map(effect -> effect.getActionModifier(slotType)).collect(Collectors.toList());
		if (effectsAreAlternative) {
			// If they are alternative and at least one of them is effective (i.e. generates a non-empty ActionModifier)
			// make the player choose one and return that
			if (actionModifiers.stream().allMatch(ActionModifier::isEmpty))
				return ActionModifier.EMPTY();
			else
				return effectResolutor.chooseFrom(actionModifiers.stream().toArray(ActionModifier[]::new));
		}
		else
			// Otherwise just return the merging of the ActionModifiers of every effect
			return actionModifiers.stream().reduce(ActionModifier.EMPTY(), (accumulator, actionModifier) -> accumulator.merge(actionModifier));
	}

	/**
	 * @return true if the effects in this cardEffects are permanent
	 */
	public boolean getEffectsArePermanent() {
		Arrays.stream(effects).forEach(effect -> effectsArePermanent = effect.changeIsPermanent().apply(effectsArePermanent));
		return effectsArePermanent;
	}

	@Override
	public String toString() {
		if (effectsAreAlternative)
			return Arrays.stream(effects).map(Effect::toString).reduce((a, b) -> a + " or\n" + b).orElse("No Effect");
		else
			return Arrays.stream(effects).map(Effect::toString).reduce((a, b) -> a + " and\n" + b).orElse("No Effect");
	}
}
