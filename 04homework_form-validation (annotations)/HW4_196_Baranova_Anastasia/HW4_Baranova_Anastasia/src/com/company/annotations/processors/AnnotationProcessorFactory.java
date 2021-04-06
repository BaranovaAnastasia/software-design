package com.company.annotations.processors;

import com.company.annotations.*;
import com.company.annotations.processors.specific.*;

import java.lang.annotation.Annotation;

/**
 * Factory class provides method for creating annotations processors.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 * @see AnnotationProcessor
 */
public class AnnotationProcessorFactory {
    /**
     * Creates a processor to process passed annotation.
     *
     * @param annotation Annotation that could be processed by creating processor.
     * @return Annotation processor or null is passed annotation is not supported.
     */
    public static AnnotationProcessor getProcessor(Annotation annotation) {
        if (annotation instanceof NotNull) {
            return new NotNullProcessor();
        }

        if (annotation instanceof Positive) {
            return new PositiveProcessor();
        }

        if (annotation instanceof Negative) {
            return new NegativeProcessor();
        }

        if (annotation instanceof NotBlank) {
            return new NotBlankProcessor();
        }

        if (annotation instanceof NotEmpty) {
            return new NotEmptyProcessor();
        }

        if (annotation instanceof Size) {
            return new SizeProcessor((Size) annotation);
        }

        if (annotation instanceof InRange) {
            return new InRangeProcessor((InRange) annotation);
        }

        if (annotation instanceof AnyOf) {
            return new AnyOfProcessor((AnyOf) annotation);
        }

        return null;
    }
}
