package com.company.annotations.processors.specific;

import com.company.annotations.processors.AnnotationProcessor;
import com.company.annotations.processors.ProcessorsUtils;
import com.company.validation.ValidationErrorImpl;
import com.company.validation.ValidationError;
import com.company.annotations.NotBlank;

/**
 * Processor for NotBlank annotation.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 * @see NotBlank
 */
public class NotBlankProcessor implements AnnotationProcessor {
    /**
     * Processes this NotBlank annotation for passed object.
     * Checks if the given string object is not blank.
     *
     * @param object Annotated object.
     * @param path   Object path.
     * @return ValidationError object if validation failed. If type of an object is not allowed
     * for this annotation (not String) ValidationError object is also returned.
     * Returns null if the given object meets the requirements (validation passed) or object is null.
     */
    @Override
    public ValidationError process(Object object, String path) {
        if (object == null) {
            return null;
        }

        if (ProcessorsUtils.isString(object)) {
            if (((String) object).isBlank()) {
                return new ValidationErrorImpl(ProcessorsUtils.AnnotationErrorMessages.NOTBLANK_MESSAGE, path, object);
            }

            return null;
        }

        return new ValidationErrorImpl(ProcessorsUtils.TypeErrorMessages.STRING_TYPE_VALIDATION_ERROR_MESSAGE, path, object);
    }
}
