package tests;

import com.company.annotations.processors.ProcessorsUtils;
import com.company.validation.ValidationError;
import com.company.validation.ValidationErrorImpl;
import com.company.validation.ValidatorImpl;
import org.junit.jupiter.api.Test;
import tests.forms.negative.NegativeInvalidTypeTestForm;
import tests.forms.negative.NegativeTestForm;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NegativeTest {
    @Test
    void negativeOkTest() throws IllegalAccessException {
        var nt = new NegativeTestForm((long) -5);
        assertEquals(new ValidatorImpl().validate(nt).size(), 0);
    }

    @Test
    void negativeNullTest() throws IllegalAccessException {
        var nt = new NegativeTestForm(null);
        assertEquals(new ValidatorImpl().validate(nt).size(), 0);
    }

    @Test
    void negativeZeroTest() throws IllegalAccessException {
        var toValidate = new NegativeTestForm((long) 0);

        var actual = new ValidatorImpl().validate(toValidate);
        var expected = new HashSet<ValidationError>(Collections.singleton(
                new ValidationErrorImpl(ProcessorsUtils.AnnotationErrorMessages.NEGATIVE_MESSAGE, "a", (long) 0)));

        assertTrue(TestUtils.CompareSets(actual, expected));
    }

    @Test
    void negativePositiveTest() throws IllegalAccessException {
        var toValidate = new NegativeTestForm((long) 5);

        var actual = new ValidatorImpl().validate(toValidate);
        var expected = new HashSet<ValidationError>(Collections.singleton(
                new ValidationErrorImpl(ProcessorsUtils.AnnotationErrorMessages.NEGATIVE_MESSAGE, "a", (long) 5)));

        assertTrue(TestUtils.CompareSets(actual, expected));
    }

    @Test
    void negativeInvalidTypeTest() throws IllegalAccessException {
        var toValidate = new NegativeInvalidTypeTestForm("text");

        var actual = new ValidatorImpl().validate(toValidate);
        var expected = new HashSet<ValidationError>(Collections.singleton(
                new ValidationErrorImpl(ProcessorsUtils.TypeErrorMessages.INTEGER_TYPE_VALIDATION_ERROR_MESSAGE,
                        "a", "text")));

        assertTrue(TestUtils.CompareSets(actual, expected));
    }

    @Test
    void negativeInvalidTypeNullTest() throws IllegalAccessException {
        var toValidate = new NegativeInvalidTypeTestForm(null);
        var actual = new ValidatorImpl().validate(toValidate);
        assertEquals(actual.size(), 0);
    }
}
