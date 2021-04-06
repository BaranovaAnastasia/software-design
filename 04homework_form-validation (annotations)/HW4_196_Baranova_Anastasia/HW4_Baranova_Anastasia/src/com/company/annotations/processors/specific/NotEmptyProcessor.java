package com.company.annotations.processors.specific;

import com.company.annotations.processors.AnnotationProcessor;
import com.company.annotations.processors.ProcessorsUtils;
import com.company.validation.ValidationErrorImpl;
import com.company.validation.ValidationError;
import com.company.annotations.NotEmpty;

/**
 * Processor for NotEmpty annotation.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 * @see NotEmpty
 */
public class NotEmptyProcessor implements AnnotationProcessor {
    /**
     * Processes this NotEmpty annotation for passed object.
     * Checks if the given container object is not empty.
     *
     * @param object Annotated object.
     * @param path   Object path.
     * @return ValidationError object if validation failed. If type of an object is not allowed
     * for this annotation (not List<T>, Set<T>, Map<K, V>, String) ValidationError object is also returned.
     * Returns null if the given object meets the requirements (validation passed) or object is null.
     */
    @Override
    public ValidationError process(Object object, String path) {
        if (object == null) {
            return null;
        }

        if (ProcessorsUtils.isContainerOrString(object)) {
            int size = ProcessorsUtils.getSize(object);
            if (size == 0) {
                return new ValidationErrorImpl(ProcessorsUtils.AnnotationErrorMessages.NOTEMPTY_MESSAGE, path, object);
            }

            return null;
        }

        return new ValidationErrorImpl(
                ProcessorsUtils.TypeErrorMessages.CONTAINER_OR_STRING_TYPE_VALIDATION_ERROR_MESSAGE, path, object);
    }
}
