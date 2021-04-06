package com.company.annotations.processors.specific;

import com.company.annotations.processors.AnnotationProcessor;
import com.company.annotations.processors.ProcessorsUtils;
import com.company.validation.ValidationErrorImpl;
import com.company.validation.ValidationError;
import com.company.annotations.AnyOf;

import java.util.Arrays;

/**
 * Processor for AnyOf annotation.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 * @see AnyOf
 */
public class AnyOfProcessor implements AnnotationProcessor {
    /**
     * Annotation to process.
     */
    private final AnyOf annotation;

    /**
     * Constructor creates a new instance of AnyOfProcessor class.
     *
     * @param annotation AnyOf annotation that will be processed by creating processor.
     * @throws NullPointerException If passed annotation is null.
     */
    public AnyOfProcessor(AnyOf annotation) {
        if (annotation == null) {
            throw new NullPointerException(ProcessorsUtils.AnnotationErrorMessages.CREATE_PROCESSOR_FOR_NULL_MESSAGE);
        }
        this.annotation = annotation;
    }

    /**
     * Processes this AnyOf annotation for passed object.
     * Checks if the passed object is one of the strings of AnyOf values.
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
            String str = (String) object;
            if (Arrays.asList(annotation.value()).contains(str)) {
                return null;
            }

            return new ValidationErrorImpl(
                    ProcessorsUtils.AnnotationErrorMessages.getAnyOfMessage(annotation), path, object);
        }

        return new ValidationErrorImpl(ProcessorsUtils.TypeErrorMessages.STRING_TYPE_VALIDATION_ERROR_MESSAGE, path, object);
    }
}
