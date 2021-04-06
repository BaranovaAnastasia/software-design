package tests;

import com.company.annotations.processors.ProcessorsUtils;
import com.company.validation.ValidationError;
import com.company.validation.ValidationErrorImpl;
import com.company.validation.ValidatorImpl;
import org.junit.jupiter.api.Test;
import tests.forms.notblank.NotBlankInvalidTypeTestForm;
import tests.forms.notblank.NotBlankTestForm;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NotBlankTest {
    @Test
    void notBlankOkTest() throws IllegalAccessException {
        var toValidate = new NotBlankTestForm("text");
        assertEquals(new ValidatorImpl().validate(toValidate).size(), 0);
    }

    @Test
    void notBlankNullTest() throws IllegalAccessException {
        var toValidate = new NotBlankTestForm(null);
        assertEquals(new ValidatorImpl().validate(toValidate).size(), 0);
    }

    @Test
    void notBlankEmptyTest() throws IllegalAccessException {
        var toValidate = new NotBlankTestForm("");

        var actual = new ValidatorImpl().validate(toValidate);
        var expected = new HashSet<ValidationError>(Collections.singleton(new ValidationErrorImpl(
                ProcessorsUtils.AnnotationErrorMessages.NOTBLANK_MESSAGE, "a", "")));

        assertTrue(TestUtils.CompareSets(actual, expected));
    }

    @Test
    void notBlankSpacesTest() throws IllegalAccessException {
        var toValidate = new NotBlankTestForm("   ");

        var actual = new ValidatorImpl().validate(toValidate);
        var expected = new HashSet<ValidationError>(Collections.singleton(new ValidationErrorImpl(
                ProcessorsUtils.AnnotationErrorMessages.NOTBLANK_MESSAGE, "a", "   ")));

        assertTrue(TestUtils.CompareSets(actual, expected));
    }

    @Test
    void notBlankTabTest() throws IllegalAccessException {
        var toValidate = new NotBlankTestForm("\t\t");

        var actual = new ValidatorImpl().validate(toValidate);
        var expected = new HashSet<ValidationError>(Collections.singleton(new ValidationErrorImpl(
                ProcessorsUtils.AnnotationErrorMessages.NOTBLANK_MESSAGE, "a", "\t\t")));

        assertTrue(TestUtils.CompareSets(actual, expected));
    }

    @Test
    void notBlankNewLineTest() throws IllegalAccessException {
        var toValidate = new NotBlankTestForm("\n\n");

        var actual = new ValidatorImpl().validate(toValidate);
        var expected = new HashSet<ValidationError>(Collections.singleton(new ValidationErrorImpl(
                ProcessorsUtils.AnnotationErrorMessages.NOTBLANK_MESSAGE, "a", "\n\n")));

        assertTrue(TestUtils.CompareSets(actual, expected));
    }

    @Test
    void notBlankInvalidTypeTest() throws IllegalAccessException {
        var toValidate = new NotBlankInvalidTypeTestForm(10);

        var actual = new ValidatorImpl().validate(toValidate);
        var expected = new HashSet<ValidationError>(Collections.singleton(new ValidationErrorImpl(
                ProcessorsUtils.TypeErrorMessages.STRING_TYPE_VALIDATION_ERROR_MESSAGE, "a", 10)));

        assertTrue(TestUtils.CompareSets(actual, expected));
    }
}
