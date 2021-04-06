package tests;

import com.company.annotations.processors.ProcessorsUtils;
import com.company.validation.ValidationError;
import com.company.validation.ValidationErrorImpl;
import com.company.validation.ValidatorImpl;
import org.junit.jupiter.api.Test;
import tests.forms.positive.PositiveInvalidTypeTestForm;
import tests.forms.positive.PositiveTestForm;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PositiveTest {
    @Test
    void positiveOkTest() throws IllegalAccessException {
        var toValidate = new PositiveTestForm(5);
        assertEquals(new ValidatorImpl().validate(toValidate).size(), 0);
    }

    @Test
    void positiveByteTest() throws IllegalAccessException {
        var toValidate = new PositiveTestForm((byte) 5);
        var actual = new ValidatorImpl().validate(toValidate);
        assertEquals(actual.size(), 0);
    }

    @Test
    void positiveNullTest() throws IllegalAccessException {
        var toValidate = new PositiveTestForm((Integer) null);
        assertEquals(new ValidatorImpl().validate(toValidate).size(), 0);
    }

    @Test
    void positiveZeroTest() throws IllegalAccessException {
        var toValidate = new PositiveTestForm(0);

        var actual = new ValidatorImpl().validate(toValidate);
        var expected = new HashSet<ValidationError>(Collections.singleton(
                new ValidationErrorImpl(ProcessorsUtils.AnnotationErrorMessages.POSITIVE_MESSAGE, "a", 0)));

        assertTrue(TestUtils.CompareSets(actual, expected));
    }

    @Test
    void positiveNegativeTest() throws IllegalAccessException {
        var toValidate = new PositiveTestForm(-5);

        var actual = new ValidatorImpl().validate(toValidate);
        var expected = new HashSet<ValidationError>(Collections.singleton(
                new ValidationErrorImpl(ProcessorsUtils.AnnotationErrorMessages.POSITIVE_MESSAGE, "a", -5)));

        assertTrue(TestUtils.CompareSets(actual, expected));
    }

    @Test
    void positiveInvalidTypeTest() throws IllegalAccessException {
        var toValidate = new PositiveInvalidTypeTestForm("text");

        var actual = new ValidatorImpl().validate(toValidate);
        var expected = new HashSet<ValidationError>(Collections.singleton(
                new ValidationErrorImpl(ProcessorsUtils.TypeErrorMessages.INTEGER_TYPE_VALIDATION_ERROR_MESSAGE,
                        "a", "text")));

        assertTrue(TestUtils.CompareSets(actual, expected));
    }

    @Test
    void positiveInvalidTypeNullTest() throws IllegalAccessException {
        var toValidate = new PositiveInvalidTypeTestForm(null);
        var actual = new ValidatorImpl().validate(toValidate);
        assertEquals(actual.size(), 0);
    }
}
