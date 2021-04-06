package tests;

import com.company.annotations.processors.ProcessorsUtils;
import com.company.validation.ValidationError;
import com.company.validation.ValidationErrorImpl;
import com.company.validation.ValidatorImpl;
import org.junit.jupiter.api.Test;
import tests.forms.notempty.NotEmptyInvalidTypeTestForm;
import tests.forms.notempty.NotEmptyTestForm;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NotEmptyTest {
    @Test
    void notEmptyOkTest() throws IllegalAccessException {
        var toValidate = new NotEmptyTestForm(10);
        assertEquals(new ValidatorImpl().validate(toValidate).size(), 0);

        toValidate = new NotEmptyTestForm(1);
        assertEquals(new ValidatorImpl().validate(toValidate).size(), 0);
    }

    @Test
    void notEmptyNullTest() throws IllegalAccessException {
        var toValidate = new NotEmptyTestForm((Map<Integer, Integer>) null);
        assertEquals(new ValidatorImpl().validate(toValidate).size(), 0);
    }

    @Test
    void notEmptyEmptyTest() throws IllegalAccessException {
        var toValidate = new NotEmptyTestForm(0);

        var actual = new ValidatorImpl().validate(toValidate);
        var expected = new HashSet<ValidationError>(Collections.singleton(new ValidationErrorImpl(
                ProcessorsUtils.AnnotationErrorMessages.NOTEMPTY_MESSAGE, "a", new HashMap<>())));

        assertTrue(TestUtils.CompareSets(actual, expected));
    }

    @Test
    void notEmptyInvalidTypeTest() throws IllegalAccessException {
        var toValidate = new NotEmptyInvalidTypeTestForm(15);

        var actual = new ValidatorImpl().validate(toValidate);
        var expected = new HashSet<ValidationError>(Collections.singleton(new ValidationErrorImpl(
                ProcessorsUtils.TypeErrorMessages.CONTAINER_OR_STRING_TYPE_VALIDATION_ERROR_MESSAGE,
                "a", 15)));

        assertTrue(TestUtils.CompareSets(actual, expected));
    }

    @Test
    void notEmptyInvalidTypeNullTest() throws IllegalAccessException {
        var toValidate = new NotEmptyInvalidTypeTestForm(null);
        var actual = new ValidatorImpl().validate(toValidate);
        assertEquals(actual.size(), 0);
    }
}
