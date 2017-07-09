package it.polimi.ingsw.LM45.model.effects;

import java.io.Serializable;
import java.util.function.Function;

import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.model.effects.modifiers.ActionModifier;

/**
 * Base abstract class representing all effects in the game
 * 
 * @author Tommy
 *
 */
public abstract class Effect implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * By default this method just returns true, but it can be overrided by specific effects that cannot be resolved in any moment
	 * 
	 * @param effectResolutor
	 *            the effectResolutor of the player that tries to resolve this effect
	 * @return true if it is legal for the player to resolve this effect now
	 */
	public boolean canResolveEffect(EffectResolutor effectResolutor) {
		return true;
	}

	/**
	 * @param effectResolutor the effectResolutor of the player that resolves this effect
	 */
	public abstract void resolveEffect(EffectResolutor effectResolutor);

	/**
	 * By default this method just returns an empty actionModifier, but it can be overwritten by specific effects that
	 * modify the behavior of other actions.
	 * 
	 * @param slotType the slotType of the slot on which the player is trying to do an action
	 * @return the actionModifier describing modification on the action due to some permanent effect
	 */
	public ActionModifier getActionModifier(SlotType slotType) {
		return ActionModifier.EMPTY();
	}

	/**
	 * By default this method just returns the identity function, but it can be overwritten by specific effects that
	 * need to modify their own cardEffect's effectsArePermanent property
	 * 
	 * @return a function that can change if the cardEffect containing this effect is permanent or not
	 */
	public Function<Boolean, Boolean> changeIsPermanent() {
		return x -> x;
	}

}
