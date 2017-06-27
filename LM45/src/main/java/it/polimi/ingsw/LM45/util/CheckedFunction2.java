package it.polimi.ingsw.LM45.util;

/**
 * @author Tommy
 *
 * @param <T> the element this function has to operate on
 * @param <E> the checked exception this function may throw
 */
@FunctionalInterface
public interface CheckedFunction2<T, R, E extends Exception> {
	void apply(T t, R r) throws E;
}