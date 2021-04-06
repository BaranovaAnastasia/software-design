package com.company.annotations.processors.specific;

import com.company.annotations.processors.AnnotationProcessor;
import com.company.annotations.processors.ProcessorsUtils;
import com.company.validation.ValidationErrorImpl;
import com.company.validation.ValidationError;
import com.company.annotations.Positive;

/**
 * Processor for Positive annotation.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 * @see Positive
 */
public class PositiveProcessor implements AnnotationProcessor {
    /**
     * Processes this Positive annotation for passed object.
     * Checks if the given object value is greater than 0.
     *
     * @param object Annotated object.
     * @param path   Object path.
     * @return ValidationError object if validation failed. If type of an object is not allowed
     * for this annotation (not one of the integer types or wrappers) ValidationError object is also returned.
     * Returns null if the given object meets the requirements (validation passed) or object is null.
     */
    @Override
    public ValidationError process(Object object, String path) {
        if (object == null) {
            return null;
        }

        if (ProcessorsUtils.isInteger(object)) {
            if (!ProcessorsUtils.checkIntegerValue(object, (long) 1, null)) {
                return new ValidationErrorImpl(ProcessorsUtils.AnnotationErrorMessages.POSITIVE_MESSAGE, path, object);
            }

            return null;
        }

        return new ValidationErrorImpl(ProcessorsUtils.TypeErrorMessages.INTEGER_TYPE_VALIDATION_ERROR_MESSAGE, path, object);
    }
}
