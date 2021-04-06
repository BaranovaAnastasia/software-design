package com.company.annotations.processors;

import com.company.annotations.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class provides utility static methods for annotations processing.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
public class ProcessorsUtils {
    /**
     * Class contains static strings with messages about type mismatch.
     */
    static public class TypeErrorMessages {
        /**
         * Message containing main information about errors in type checking.
         */
        private static final String TYPE_VALIDATION_ERROR_MESSAGE = "Not an instance of any of the allowed types: ";

        /**
         * Message when type checking for reference types fails.
         */
        public static final String REFERENCE_TYPE_VALIDATION_ERROR_MESSAGE
                = TYPE_VALIDATION_ERROR_MESSAGE + "any reference type";

        /**
         * Message when type checking for integer types fails.
         */
        public static final String INTEGER_TYPE_VALIDATION_ERROR_MESSAGE
                = TYPE_VALIDATION_ERROR_MESSAGE + "byte, short, int, long or wrappers";

        /**
         * Message when type checking for container types fails.
         */
        public static final String CONTAINER_OR_STRING_TYPE_VALIDATION_ERROR_MESSAGE
                = TYPE_VALIDATION_ERROR_MESSAGE + "List, Set, Map, String";

        /**
         * Message when type checking for string type fails.
         */
        public static final String STRING_TYPE_VALIDATION_ERROR_MESSAGE
                = TYPE_VALIDATION_ERROR_MESSAGE + "String";
    }

    /**
     * Class contains static strings with messages about failing annotation validation
     * and static methods for generating messages for annotations with parameters.
     */
    static public class AnnotationErrorMessages {
        /**
         * Message when attempt to crete a processor for null annotation.
         */
        public static final String CREATE_PROCESSOR_FOR_NULL_MESSAGE = "Cannot create a processor for null annotation.";

        /**
         * NotNull annotation validation fail message.
         *
         * @see NotNull
         */
        public static final String NOTNULL_MESSAGE = "Must not be null";

        /**
         * Positive annotation validation fail message.
         *
         * @see Positive
         */
        public static final String POSITIVE_MESSAGE = "Mast be positive";

        /**
         * Negative annotation validation fail message.
         *
         * @see Negative
         */
        public static final String NEGATIVE_MESSAGE = "Mast be negative";

        /**
         * NotBlank annotation validation fail message.
         *
         * @see NotBlank
         */
        public static final String NOTBLANK_MESSAGE = "Must not be blank";

        /**
         * NotEmpty annotation validation fail message.
         *
         * @see NotEmpty
         */
        public static final String NOTEMPTY_MESSAGE = "Must not be empty";

        /**
         * Generates Size annotation validation fail message.
         *
         * @return Size annotation validation fail message.
         * @see Size
         */
        public static String getSizeMessage(Size annotation) {
            return "Must be of size in [" + annotation.min() + "; " + annotation.max() + "]";
        }

        /**
         * Generates InRange annotation validation fail message.
         *
         * @return InRange annotation validation fail message.
         * @see InRange
         */
        public static String getInRangeMessage(InRange annotation) {
            return "Value must be in range [" + annotation.min() + "; " + annotation.max() + "]";
        }

        /**
         * Generates AnyOf annotation validation fail message.
         *
         * @return AnyOf annotation validation fail message.
         * @see AnyOf
         */
        public static String getAnyOfMessage(AnyOf annotation) {
            return "Value must be one of the allowed ones: " + anyOfToString(annotation);
        }
    }

    /**
     * Checks if the given object is of one of the types: byte, short, int, long or wrappers.
     *
     * @param object Object to check.
     * @return true if object is of one of the types: byte, short, int, long or wrappers; false otherwise.
     */
    public static boolean isInteger(Object object) {
        return object instanceof Byte || object instanceof Short || object instanceof Integer || object instanceof Long;
    }

    /**
     * Checks if the given object is of one of the types: List<T>, Set<T>, Map<K, V>, String.
     *
     * @param object Object to check.
     * @return true if object is of one of the types: List<T>, Set<T>, Map<K, V>, String; false otherwise.
     */
    public static boolean isContainerOrString(Object object) {
        return object instanceof List || object instanceof Set || object instanceof Map || object instanceof String;
    }

    /**
     * Checks if the given object is of the String type.
     *
     * @param object Object to check.
     * @return true if object is of the String type, false otherwise.
     */
    public static boolean isString(Object object) {
        return object instanceof String;
    }

    /**
     * Checks if the given object is of a reference type.
     *
     * @param object Object to check.
     * @return true if object is of reference type, false otherwise (in fact, always true).
     * @throws NullPointerException If object is null.
     */
    public static boolean isReferenceType(Object object) {
        return !object.getClass().isPrimitive();
    }

    /**
     * Checks if passed object is an integer and its value is in between passed min and max values.
     *
     * @param object Object to check.
     * @param min    Minimum value.
     * @param max    Maximum value.
     * @return true if object is an integer and its value is in [min; max], false otherwise.
     */
    public static boolean checkIntegerValue(Object object, Long min, Long max) {
        if (object instanceof Byte) {
            byte integer = (Byte) object;
            return (min == null || integer >= min) && (max == null || integer <= max);
        }

        if (object instanceof Short) {
            short integer = (Short) object;
            return (min == null || integer >= min) && (max == null || integer <= max);
        }

        if (object instanceof Integer) {
            int integer = (Integer) object;
            return (min == null || integer >= min) && (max == null || integer <= max);
        }

        if (object instanceof Long) {
            long integer = (Long) object;
            return (min == null || integer >= min) && (max == null || integer <= max);
        }

        return false;
    }

    /**
     * Finds size of the passed object if it is a container (List<T>, Set<T>, Map<K, V>, String).
     *
     * @param object Object to find its size.
     * @return Size of the passed object, -1 if object is not an instance of any of supported containers.
     */
    public static int getSize(Object object) {
        if (object instanceof String) {
            return ((String) object).length();
        }

        if (object instanceof List) {
            return ((List<?>) object).size();
        }

        if (object instanceof Set) {
            return ((Set<?>) object).size();
        }

        if (object instanceof Map) {
            return ((Map<?, ?>) object).size();
        }

        return -1;
    }

    /**
     * Builds a string listing AnyOf values separated by '; '.
     *
     * @see AnyOf
     */
    public static String anyOfToString(AnyOf annotation) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < annotation.value().length; i++) {
            result.append(annotation.value()[i]);
            if (i < annotation.value().length - 1) {
                result.append("; ");
            }
        }

        return result.toString();
    }
}
