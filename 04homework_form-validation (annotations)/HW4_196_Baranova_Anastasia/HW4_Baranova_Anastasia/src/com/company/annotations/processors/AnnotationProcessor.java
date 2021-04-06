package com.company.annotations.processors;

import com.company.validation.ValidationError;

/**
 * Interface for library's annotations processors.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
public interface AnnotationProcessor {
    /**
     * Processes passed annotation.
     * Checks if the given object meets the requirements of the annotation.
     *
     * @param object Annotated object.
     * @param path   Object path.
     * @return ValidationError object if validation failed. If type of an object is not allowed
     * for the given annotation ValidationError object is also returned.
     * Returns null if the given object meets the conditions (validation passed).
     */
    ValidationError process(Object object, String path);
}
