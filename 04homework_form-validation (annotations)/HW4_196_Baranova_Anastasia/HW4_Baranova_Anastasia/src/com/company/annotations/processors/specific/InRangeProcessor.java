package com.company.annotations.processors.specific;

import com.company.annotations.processors.AnnotationProcessor;
import com.company.annotations.processors.ProcessorsUtils;
import com.company.validation.ValidationErrorImpl;
import com.company.validation.ValidationError;
import com.company.annotations.InRange;

/**
 * Processor for InRange annotation.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 * @see InRange
 */
public class InRangeProcessor implements AnnotationProcessor {
    /**
     * Annotation to process.
     */
    private final InRange annotation;

    /**
     * Constructor creates a new instance of InRangeProcessor class.
     *
     * @param annotation InRange annotation that will be processed by creating processor.
     * @throws NullPointerException If passed annotation is null.
     */
    public InRangeProcessor(InRange annotation) {
        if (annotation == null) {
            throw new NullPointerException(ProcessorsUtils.AnnotationErrorMessages.CREATE_PROCESSOR_FOR_NULL_MESSAGE);
        }
        this.annotation = annotation;
    }

    /**
     * Processes this InRange annotation for passed object.
     * Checks if the given object value is in the range provided by this InRange annotation instance.
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
            if (ProcessorsUtils.checkIntegerValue(object, annotation.min(), annotation.max())) {
                return null;
            }

            return new ValidationErrorImpl(
                    ProcessorsUtils.AnnotationErrorMessages.getInRangeMessage(annotation), path, object);
        }

        return new ValidationErrorImpl(ProcessorsUtils.TypeErrorMessages.INTEGER_TYPE_VALIDATION_ERROR_MESSAGE, path, object);
    }
}
