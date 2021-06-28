package phonebook;

/**
 * Interface for windows that require some initial data for their work and/or provide some result of their work.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
public interface WindowWithResult {
    void setInitials(Object[] initials);

    Object getResult();
}
