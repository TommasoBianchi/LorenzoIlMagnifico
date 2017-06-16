package it.polimi.ingsw.LM45.util;

/**
 * @author Tommy
 *
 * @param <E> the checked exception this function may throw
 */
@FunctionalInterface
public interface CheckedAction<E extends Exception> {
	void apply() throws E;
}