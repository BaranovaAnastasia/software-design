package com.company.validation;

/**
 * Interface for classes representing an error in validating.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
public interface ValidationError {
    /**
     * Returns error message.
     */
    String getMessage();

    /**
     * Returns path to the object that failed validation.
     */
    String getPath();

    /**
     * Returns value of the object that failed validation.
     */
    Object getFailedValue();
}

