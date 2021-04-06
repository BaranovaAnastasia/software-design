package tests;

import com.company.validation.ValidationError;
import com.company.validation.ValidationErrorImpl;
import com.company.validation.ValidatorImpl;
import org.junit.jupiter.api.Test;
import tests.forms.constrained.ConstrainedForm;
import tests.forms.constrained.ConstrainedFormPlusInner;
import tests.forms.constrained.NotConstrainedForm;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ConstrainedTest {
    @Test
    void notConstrainedTest() {
        var toValidate = new NotConstrainedForm();
        assertThrows(IllegalArgumentException.class, () -> new ValidatorImpl().validate(toValidate));
    }

    @Test
    void constrainedTest() throws IllegalAccessException {
        var toValidate = new ConstrainedForm();

        var actual = new ValidatorImpl().validate(toValidate);
        var expected = new HashSet<ValidationError>(Collections.singleton(
                new ValidationErrorImpl("Must not be null", "str", null)));

        assertTrue(TestUtils.CompareSets(actual, expected));
    }

    @Test
    void innerConstrainedTest() throws IllegalAccessException {
        var toValidate = new ConstrainedFormPlusInner();

        var actual = new ValidatorImpl().validate(toValidate);
        Set<ValidationError> expected = new HashSet<>(Arrays.asList(
                new ValidationErrorImpl("Must not be null", "str", null),
                new ValidationErrorImpl("Must not be null", "cc.str", null)
        ));

        assertTrue(TestUtils.CompareSets(actual, expected));
    }

    @Test
    void constrainedNullTest() throws IllegalAccessException {
        Set<ValidationError> actual = new ValidatorImpl().validate(null);
        assertEquals(actual.size(), 0);
    }
}
