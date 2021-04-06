package com.company.annotations.processors.specific;

import com.company.annotations.processors.AnnotationProcessor;
import com.company.annotations.processors.ProcessorsUtils;
import com.company.validation.ValidationErrorImpl;
import com.company.validation.ValidationError;
import com.company.annotations.Size;

/**
 * Processor for Size annotation.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 * @see Size
 */
public class SizeProcessor implements AnnotationProcessor {
    /**
     * Annotation to process.
     */
    private final Size annotation;

    /**
     * Constructor creates a new instance of SizeProcessor class.
     *
     * @param annotation Size annotation that will be processed by creating processor.
     * @throws NullPointerException If passed annotation is null.
     */
    public SizeProcessor(Size annotation) {
        if (annotation == null) {
            throw new NullPointerException(ProcessorsUtils.AnnotationErrorMessages.CREATE_PROCESSOR_FOR_NULL_MESSAGE);
        }
        this.annotation = annotation;
    }

    /**
     * Processes this Size annotation for passed object.
     * Checks if the given container object size is in range provided by this Size annotation instance.
     *
     * @param object Annotated object.
     * @param path   Object path.
     * @return ValidationError object if validation failed. If type of an object is not allowed
     * for this annotation (not List<T>, Set<T>, Map<K, V>, String ValidationError object is also returned.
     * Returns null if the given object meets the requirements (validation passed) or object is null.
     */
    @Override
    public ValidationError process(Object object, String path) {
        if (object == null) {
            return null;
        }

        if (ProcessorsUtils.isContainerOrString(object)) {
            int size = ProcessorsUtils.getSize(object);

            if (size >= annotation.min() && size <= annotation.max()) {
                return null;
            }

            return new ValidationErrorImpl(
                    ProcessorsUtils.AnnotationErrorMessages.getSizeMessage(annotation), path, object);
        }

        return new ValidationErrorImpl(ProcessorsUtils.TypeErrorMessages
                .CONTAINER_OR_STRING_TYPE_VALIDATION_ERROR_MESSAGE, path, object);
    }
}
