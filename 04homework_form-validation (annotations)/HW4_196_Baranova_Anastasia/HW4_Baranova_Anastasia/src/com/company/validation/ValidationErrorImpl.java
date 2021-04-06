package com.company.validation;

import java.util.Objects;

/**
 * Class representing an error in validating objects according to their annotations.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
public class ValidationErrorImpl implements ValidationError {
    /**
     * Error message.
     */
    private final String message;

    /**
     * Path to the object that failed validation.
     */
    private final String path;

    /**
     * Value of the object that failed validation.
     */
    private final Object value;

    /**
     * Constructor creates a new instance of the FormValidationError class.
     *
     * @param message Error message.
     * @param path    Path to the object that failed validation.
     * @param value   Value of the object that failed validation.
     */
    public ValidationErrorImpl(String message, String path, Object value) {
        this.message = message;
        this.path = path;
        this.value = value;
    }

    /**
     * Returns error message.
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * Returns path to the object that failed validation.
     */
    @Override
    public String getPath() {
        return path;
    }

    /**
     * Returns value of the object that failed validation.
     */
    @Override
    public Object getFailedValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidationErrorImpl that = (ValidationErrorImpl) o;
        return Objects.equals(message, that.message) &&
                Objects.equals(path, that.path) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, path, value);
    }
}
