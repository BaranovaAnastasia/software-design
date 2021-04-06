package tests;

import com.company.annotations.processors.specific.SizeProcessor;
import com.company.annotations.processors.ProcessorsUtils;
import com.company.validation.ValidationError;
import com.company.validation.ValidationErrorImpl;
import com.company.validation.ValidatorImpl;
import org.junit.jupiter.api.Test;
import tests.forms.size.SizeInvalidTypeTestForm;
import tests.forms.size.SizeTestForm;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SizeTest {
    @Test
    void sizeOkTest() throws IllegalAccessException {
        var toValidate = new SizeTestForm(10);
        assertEquals(new ValidatorImpl().validate(toValidate).size(), 0);

        toValidate = new SizeTestForm(15);
        assertEquals(new ValidatorImpl().validate(toValidate).size(), 0);

        toValidate = new SizeTestForm(20);
        assertEquals(new ValidatorImpl().validate(toValidate).size(), 0);

        toValidate = new SizeTestForm("123");
        assertEquals(new ValidatorImpl().validate(toValidate).size(), 0);
    }

    @Test
    void sizeNullTest() throws IllegalAccessException {
        var toValidate = new SizeTestForm((Set<Integer>) null);
        assertEquals(new ValidatorImpl().validate(toValidate).size(), 0);
    }

    @Test
    void sizeGreaterTest() throws IllegalAccessException, NoSuchFieldException {
        var toValidate = new SizeTestForm(21);

        var actual = new ValidatorImpl().validate(toValidate);
        var expected = new HashSet<ValidationError>(Collections.singleton(
                new ValidationErrorImpl(ProcessorsUtils.AnnotationErrorMessages
                        .getSizeMessage(toValidate.getAnnotation(0)),
                        "a", new HashSet<>(new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
                        11, 12, 13, 14, 15, 16, 17, 18, 19, 20))))));

        assertTrue(TestUtils.CompareSets(actual, expected));
    }

    @Test
    void sizeStringTest() throws IllegalAccessException, NoSuchFieldException {
        var toValidate = new SizeTestForm("1");

        var actual = new ValidatorImpl().validate(toValidate);
        var expected = new HashSet<ValidationError>(Collections.singleton(
                new ValidationErrorImpl(ProcessorsUtils.AnnotationErrorMessages
                        .getSizeMessage(toValidate.getAnnotation(1)), "str", "1")));

        assertTrue(TestUtils.CompareSets(actual, expected));
    }

    @Test
    void sizeLessTest() throws IllegalAccessException, NoSuchFieldException {
        var toValidate = new SizeTestForm(9);

        var actual = new ValidatorImpl().validate(toValidate);
        var expected = new HashSet<ValidationError>(Collections.singleton(
                new ValidationErrorImpl(ProcessorsUtils.AnnotationErrorMessages
                        .getSizeMessage(toValidate.getAnnotation(0)), "a",
                        new HashSet<>(new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8))))));

        assertTrue(TestUtils.CompareSets(actual, expected));
    }

    @Test
    void sizeInvalidTypeTest() throws IllegalAccessException {
        var toValidate = new SizeInvalidTypeTestForm(15);

        var actual = new ValidatorImpl().validate(toValidate);
        var expected = new HashSet<ValidationError>(Collections.singleton(
                new ValidationErrorImpl(ProcessorsUtils.TypeErrorMessages.CONTAINER_OR_STRING_TYPE_VALIDATION_ERROR_MESSAGE,
                        "a", 15)));

        assertTrue(TestUtils.CompareSets(actual, expected));
    }

    @Test
    void sizeInvalidTypeNullTest() throws IllegalAccessException {
        var toValidate = new SizeInvalidTypeTestForm(null);
        var actual = new ValidatorImpl().validate(toValidate);
        assertEquals(actual.size(), 0);
    }

    @Test
    void createNullProcessor() {
        assertThrows(NullPointerException.class, () -> new SizeProcessor(null));
    }
}
