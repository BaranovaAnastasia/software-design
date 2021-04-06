package martians;

import java.util.Collection;

/**
 * Interface describing the basic capabilities of any Martian.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru"> Anastasia Baranova</a>
 */
public interface Martian<T> {
    /**
     * Returns martian gene code.
     */
    T getCode();

    /**
     * Returns martian parent.
     */
    Martian<T> getParent();

    /**
     * Returns list of martian children.
     */
    Collection<Martian<T>> getChildren();

    /**
     * Returns list of martian descendants.
     */
    Collection<Martian<T>> getDescendants();

    /**
     * Checks if martian has a child with a given gene code.
     */
    boolean hasChildWithValue(T value);

    /**
     * Checks if martian has a descendant with a given gene code.
     */
    boolean hasDescendantWithValue(T value);

    /**
     * Returns a report on martian family.
     */
    String giveReport();
}