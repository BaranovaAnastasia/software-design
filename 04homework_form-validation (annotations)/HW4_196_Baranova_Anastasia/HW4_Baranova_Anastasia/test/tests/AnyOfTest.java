package tests;

import com.company.annotations.processors.specific.AnyOfProcessor;
import com.company.annotations.processors.ProcessorsUtils;
import com.company.validation.ValidationError;
import com.company.validation.ValidationErrorImpl;
import com.company.validation.ValidatorImpl;
import org.junit.jupiter.api.Test;
import tests.forms.anyof.AnyOfInvalidTypeTestForm;
import tests.forms.anyof.AnyOfTestForm;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class AnyOfTest {
    @Test
    void anyOfOkTest() throws IllegalAccessException {
        var toValidate = new AnyOfTestForm("1");
        assertEquals(new ValidatorImpl().validate(toValidate).size(), 0);

        toValidate = new AnyOfTestForm("3");
        assertEquals(new ValidatorImpl().validate(toValidate).size(), 0);

        toValidate = new AnyOfTestForm("5");
        assertEquals(new ValidatorImpl().validate(toValidate).size(), 0);
    }

    @Test
    void anyOfNullTest() throws IllegalAccessException {
        var toValidate = new AnyOfTestForm(null);
        assertEquals(new ValidatorImpl().validate(toValidate).size(), 0);
    }

    @Test
    void anyOfNotOkTest() throws IllegalAccessException, NoSuchFieldException {
        var toValidate = new AnyOfTestForm("some str");

        var actual = new ValidatorImpl().validate(toValidate);
        var expected = new HashSet<ValidationError>(Collections.singletonList(
                new ValidationErrorImpl(ProcessorsUtils.AnnotationErrorMessages
                        .getAnyOfMessage(toValidate.getAnnotation()), "a", "some str")));

        assertTrue(TestUtils.CompareSets(actual, expected));
    }

    @Test
    void anyOfInvalidTypeTest() throws IllegalAccessException {
        var toValidate = new AnyOfInvalidTypeTestForm(1);
        var actual = new ValidatorImpl().validate(toValidate);
        var expected = new HashSet<ValidationError>(Collections.singletonList(
                new ValidationErrorImpl(ProcessorsUtils.TypeErrorMessages
                        .STRING_TYPE_VALIDATION_ERROR_MESSAGE, "a", 1)));

        assertTrue(TestUtils.CompareSets(actual, expected));
    }

    @Test
    void anyOfInvalidTypeNullTest() throws IllegalAccessException {
        var toValidate = new AnyOfInvalidTypeTestForm(null);
        assertEquals(new ValidatorImpl().validate(toValidate).size(), 0);
    }

    @Test
    void createNullProcessorTest() {
        assertThrows(NullPointerException.class, () -> new AnyOfProcessor(null));
    }
}
