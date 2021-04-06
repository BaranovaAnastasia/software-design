package com.company.annotations.processors;

import com.company.annotations.processors.AnnotationProcessor;
import com.company.annotations.processors.AnnotationProcessorFactory;
import com.company.validation.ValidationError;

import java.lang.annotation.Annotation;

/**
 * Class provides static method for processing annotations of the library.
 * Only suitable for processing annotations with ElementType.TYPE_USE Target.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
public class AnnotationProcessorManager {
    /**
     * Processes passed annotation for passed object.
     * Checks if the given object meets the requirements of the annotation.
     *
     * @param object     Annotated object.
     * @param annotation Annotation to process.
     * @param path       Object path.
     * @return ValidationError object if validation failed. If type of an object is not allowed
     * for the given annotation ValidationError object is also returned.
     * Returns null if the given object meets the conditions (validation passed).
     * @throws IllegalArgumentException If annotation is not supported.
     */
    public static ValidationError process(Object object, Annotation annotation, String path) {
        AnnotationProcessor processor = AnnotationProcessorFactory.getProcessor(annotation);

        if (processor != null) {
            return processor.process(object, path);
        }

        throw new IllegalArgumentException("Annotation is not supported.");
    }
}
