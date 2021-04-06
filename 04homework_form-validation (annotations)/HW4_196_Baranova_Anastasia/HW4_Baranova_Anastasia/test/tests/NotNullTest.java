package tests;

import com.company.annotations.processors.ProcessorsUtils;
import com.company.validation.ValidationError;
import com.company.validation.ValidationErrorImpl;
import com.company.validation.ValidatorImpl;
import org.junit.jupiter.api.Test;
import tests.forms.notnull.NotNullCombinationTestForm;
import tests.forms.notnull.NotNullTestForm;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NotNullTest {
    @Test
    void notNullOkTest() throws IllegalAccessException {
        var toValidate = new NotNullTestForm("clearly not null string");
        assertEquals(new ValidatorImpl().validate(toValidate).size(), 0);
    }

    @Test
    void notNullNotOkTest() throws IllegalAccessException {
        var toValidate = new NotNullTestForm(null);

        var actual = new ValidatorImpl().validate(toValidate);
        var expected = new HashSet<ValidationError>(Collections.singleton(new ValidationErrorImpl(
                ProcessorsUtils.AnnotationErrorMessages.NOTNULL_MESSAGE, "a", null)));

        assertTrue(TestUtils.CompareSets(actual, expected));
    }

    @Test
    void notNullCombinationTest() throws IllegalAccessException {
        var toValidate = new NotNullCombinationTestForm((short) -5);

        var actual = new ValidatorImpl().validate(toValidate);
        var expected = new HashSet<ValidationError>(Collections.singleton(new ValidationErrorImpl(
                ProcessorsUtils.AnnotationErrorMessages.POSITIVE_MESSAGE, "a", (short) -5)));

        assertTrue(TestUtils.CompareSets(actual, expected));
    }

    @Test
    void notNullCombinationNullTest() throws IllegalAccessException {
        var toValidate = new NotNullCombinationTestForm(null);

        var actual = new ValidatorImpl().validate(toValidate);
        var expected = new HashSet<ValidationError>(Collections.singleton(new ValidationErrorImpl(
                ProcessorsUtils.AnnotationErrorMessages.NOTNULL_MESSAGE, "a", null)));

        assertTrue(TestUtils.CompareSets(actual, expected));
    }
}
