package com.company.validation;

import com.company.annotations.processors.AnnotationProcessorManager;
import com.company.annotations.Constrained;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class validator provides method for validating objects according to their annotations.
 * Only validates library's annotations.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
public class ValidatorImpl implements Validator {
    /**
     * Validates object according to its annotations.
     * Checks if the given object and its fields meet the requirements of the annotations.
     *
     * @param object Object to validate.
     * @return Set of ValidationError objects containing all of the validation errors found.
     * If passed object is null empty set is returned.
     * @throws IllegalArgumentException if class of passed object is not annotated Constrained.
     */
    @Override
    public Set<ValidationError> validate(Object object) throws IllegalAccessException {
        if (object == null) {
            return new HashSet<>();
        }
        if (!object.getClass().isAnnotationPresent(Constrained.class)) {
            throw new IllegalArgumentException("Cannot validate object of a class that is not annotated Constrained");
        }

        return validate(object, "");
    }

    /**
     * Validates object according to its annotations.
     * Checks if the given object and its fields meet the requirements of the annotations.
     * Supports recursive validation.
     *
     * @param object     Object to validate.
     * @param parentPath Path to object's patent.
     * @return Set of ValidationError objects containing all of the validation errors found.
     */
    private Set<ValidationError> validate(Object object, String parentPath) throws IllegalAccessException {
        Set<ValidationError> errors = new HashSet<>();

        // Processing every field of the given object.
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            Object value = field.get(object);
            String path = parentPath.isEmpty() ? field.getName() : parentPath + "." + field.getName();

            // Separately processing lists and other types.
            if (value instanceof List) {
                errors.addAll(validateList(value, field.getAnnotatedType(), path));
            } else {
                errors.addAll(processOne(value, field.getAnnotatedType().getDeclaredAnnotations(), path));
                errors.addAll(processInner(value, path));
            }
        }

        return errors;
    }

    /**
     * Validates field of List type according to its annotations.
     * Checks if the given list and its content meet the conditions of the annotations.
     * Recursively inspects list and its content.
     *
     * @param object        List to inspect.
     * @param annotatedType AnnotatedType of passed list.
     * @param path          Path to passed object.
     * @return Set of ValidationError objects containing all of the validation errors found.
     */
    private Set<ValidationError> validateList(Object object, AnnotatedType annotatedType, String path) throws IllegalAccessException {
        // Processing list itself.
        Set<ValidationError> errors = new HashSet<>(processOne(object, annotatedType.getDeclaredAnnotations(), path));

        // Processing list content.
        AnnotatedType annotatedTypeNext = ((AnnotatedParameterizedType) annotatedType).getAnnotatedActualTypeArguments()[0];
        Annotation[] annotations = annotatedTypeNext.getAnnotations();

        List<?> list = (List<?>) object;
        for (int i = 0; i < list.size(); i++) {
            String currentPath = path + "[" + i + "]";

            // Recursively processing inner lists.
            if (list.get(i) instanceof List) {
                errors.addAll(validateList(list.get(i), annotatedTypeNext, currentPath));
            } else {
                errors.addAll(processOne(list.get(i), annotations, currentPath));
            }

            errors.addAll(processInner(list.get(i), currentPath));
        }

        return errors;
    }

    /**
     * Processes annotations for passed object.
     *
     * @param value       Annotated object.
     * @param annotations Object's annotations.
     * @param path        Path to passed object.
     * @return Set of ValidationError objects containing all of the validation errors found.
     */
    private Set<ValidationError> processOne(Object value, Annotation[] annotations, String path) {
        Set<ValidationError> errors = new HashSet<>();

        for (Annotation annotation : annotations) {
            ValidationError error = AnnotationProcessorManager.process(value, annotation, path);
            if (error != null) {
                errors.add(error);
            }
        }

        return errors;
    }

    /**
     * Processes inner annotations of passed object.
     *
     * @param object Object to validate.
     * @param path   Path to passed object.
     * @return Set of ValidationError objects containing all of the validation errors found.
     */
    private Set<ValidationError> processInner(Object object, String path) throws IllegalAccessException {
        if (object == null || !object.getClass().isAnnotationPresent(Constrained.class)) {
            return new HashSet<>();
        }
        return validate(object, path);
    }
}
