package it.polimi.ingsw.LM45.util;

/**
 * @author Tommy
 *
 * @param <T> the element this function has to operate on
 * @param <E> the checked exception this function may throw
 */
@FunctionalInterface
public interface CheckedFunction1<T, E extends Exception> {
	void apply(T t) throws E;
}