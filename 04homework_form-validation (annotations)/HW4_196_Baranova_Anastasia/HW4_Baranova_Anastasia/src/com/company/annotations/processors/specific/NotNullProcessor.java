package com.company.annotations.processors.specific;

import com.company.annotations.processors.AnnotationProcessor;
import com.company.annotations.processors.ProcessorsUtils;
import com.company.validation.ValidationErrorImpl;
import com.company.validation.ValidationError;
import com.company.annotations.NotNull;

/**
 * Processor for NotNull annotation.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 * @see NotNull
 */
public class NotNullProcessor implements AnnotationProcessor {
    /**
     * Processes this NotNull annotation for passed object.
     * Checks if the given object is not null.
     *
     * @param object Annotated object.
     * @param path   Object path.
     * @return ValidationError object if validation failed. If type of an object is not allowed
     * for this annotation (not reference type) ValidationError object is also returned.
     * Returns null if the given object meets the requirements (validation passed) or object is null.
     */
    @Override
    public ValidationError process(Object object, String path) {
        if (object != null) {
            if (!ProcessorsUtils.isReferenceType(object)) {
                return new ValidationErrorImpl(
                        ProcessorsUtils.TypeErrorMessages.REFERENCE_TYPE_VALIDATION_ERROR_MESSAGE, path, object);
            }

            return null;
        }

        return new ValidationErrorImpl(ProcessorsUtils.AnnotationErrorMessages.NOTNULL_MESSAGE, path, null);
    }
}
