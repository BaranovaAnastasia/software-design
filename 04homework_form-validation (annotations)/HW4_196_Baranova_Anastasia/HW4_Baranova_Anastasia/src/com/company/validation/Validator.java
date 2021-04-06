package com.company.validation;

import java.util.Set;

/**
 * Interface for classes that validate objects.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
public interface Validator {
    /**
     * Validates object.
     *
     * @param object Object to validate.
     * @return Set of ValidationError objects containing all of the validation errors found.
     */
    Set<ValidationError> validate(Object object) throws IllegalAccessException;
}

