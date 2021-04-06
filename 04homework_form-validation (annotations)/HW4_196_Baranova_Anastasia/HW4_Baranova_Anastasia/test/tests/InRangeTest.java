package tests;

import com.company.annotations.processors.specific.InRangeProcessor;
import com.company.annotations.processors.ProcessorsUtils;
import com.company.validation.ValidationError;
import com.company.validation.ValidationErrorImpl;
import com.company.validation.ValidatorImpl;
import org.junit.jupiter.api.Test;
import tests.forms.inrange.InRangeInvalidTypeTestForm;
import tests.forms.inrange.InRangeTestForm;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class InRangeTest {
    @Test
    void inRangeOkTest() throws IllegalAccessException {
        var toValidate = new InRangeTestForm((short) -10);
        assertEquals(new ValidatorImpl().validate(toValidate).size(), 0);

        toValidate = new InRangeTestForm((short) 10);
        assertEquals(new ValidatorImpl().validate(toValidate).size(), 0);

        toValidate = new InRangeTestForm((short) 0);
        assertEquals(new ValidatorImpl().validate(toValidate).size(), 0);
    }

    @Test
    void inRangeNullTest() throws IllegalAccessException {
        var toValidate = new InRangeTestForm(null);
        assertEquals(new ValidatorImpl().validate(toValidate).size(), 0);
    }

    @Test
    void inRangeGreaterTest() throws IllegalAccessException, NoSuchFieldException {
        var toValidate = new InRangeTestForm((short) 11);

        var actual = new ValidatorImpl().validate(toValidate);
        var expected = new HashSet<ValidationError>(Collections.singletonList(
                new ValidationErrorImpl(ProcessorsUtils.AnnotationErrorMessages
                        .getInRangeMessage(toValidate.getAnnotation()), "a", (short) 11)));

        assertTrue(TestUtils.CompareSets(actual, expected));
    }

    @Test
    void inRangeLessTest() throws IllegalAccessException, NoSuchFieldException {
        var toValidate = new InRangeTestForm((short) -11);

        var actual = new ValidatorImpl().validate(toValidate);
        var expected = new HashSet<ValidationError>(Collections.singletonList(
                new ValidationErrorImpl(ProcessorsUtils.AnnotationErrorMessages
                        .getInRangeMessage(toValidate.getAnnotation()), "a", (short) -11)));

        assertTrue(TestUtils.CompareSets(actual, expected));
    }

    @Test
    void inRangeInvalidTypeTest() throws IllegalAccessException {
        var toValidate = new InRangeInvalidTypeTestForm("text");

        var actual = new ValidatorImpl().validate(toValidate);
        var expected = new HashSet<ValidationError>(Collections.singletonList(
                new ValidationErrorImpl(ProcessorsUtils.TypeErrorMessages.INTEGER_TYPE_VALIDATION_ERROR_MESSAGE,
                        "a", "text")));

        assertTrue(TestUtils.CompareSets(actual, expected));
    }

    @Test
    void inRangeInvalidTypeNullTest() throws IllegalAccessException {
        var toValidate = new InRangeInvalidTypeTestForm(null);
        var actual = new ValidatorImpl().validate(toValidate);
        assertEquals(actual.size(), 0);
    }

    @Test
    void createNullProcessorTest() {
        assertThrows(NullPointerException.class, () -> new InRangeProcessor(null));
    }
}
