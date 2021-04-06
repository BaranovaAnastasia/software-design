package tests;

import com.company.annotations.processors.ProcessorsUtils;
import com.company.validation.ValidationError;
import com.company.validation.ValidationErrorImpl;
import com.company.validation.ValidatorImpl;
import org.junit.jupiter.api.Test;
import tests.forms.list.ListTestForm;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ListTest {
    @Test
    void listOkTest() throws IllegalAccessException {
        List<List<List<Integer>>> list = Arrays.asList(
                Arrays.asList(
                        Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3)
                ),
                Arrays.asList(
                        Arrays.asList(1, 2, 3), Collections.emptyList()
                ),
                Arrays.asList(
                        Collections.singletonList(1), Arrays.asList(1, 2), Arrays.asList(1, 2, 3)
                )
        );

        assertEquals(new ValidatorImpl().validate(new ListTestForm(list)).size(), 0);
    }

    @Test
    void listFirstLevelFailsTest() throws IllegalAccessException, NoSuchFieldException {
        List<List<List<Integer>>> list = Collections.singletonList(
                Arrays.asList(
                        Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3)
                )
        );

        var toValidate = new ListTestForm(list);

        var actual = new ValidatorImpl().validate(toValidate);
        var expected = new HashSet<ValidationError>(Collections.singletonList(new ValidationErrorImpl(
                ProcessorsUtils.AnnotationErrorMessages.getSizeMessage(toValidate.getAnnotation()), "a", list)));

        assertTrue(TestUtils.CompareSets(actual, expected));
    }

    @Test
    void listSecondLevelFailsTest() throws IllegalAccessException {
        List<List<List<Integer>>> list = Arrays.asList(
                Arrays.asList(
                        Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3)
                ),
                Collections.emptyList(),
                Collections.emptyList()
        );

        var toValidate = new ListTestForm(list);

        var actual = new ValidatorImpl().validate(toValidate);
        var expected = new HashSet<ValidationError>(Arrays.asList(new ValidationErrorImpl(
                ProcessorsUtils.AnnotationErrorMessages.NOTEMPTY_MESSAGE, "a[1]", Collections.emptyList()), new ValidationErrorImpl(
                ProcessorsUtils.AnnotationErrorMessages.NOTEMPTY_MESSAGE, "a[2]", Collections.emptyList())));

        assertTrue(TestUtils.CompareSets(actual, expected));
    }

    @Test
    void listFourthLevelFailsTest() throws IllegalAccessException {
        List<List<List<Integer>>> list = Arrays.asList(
                Arrays.asList(
                        Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3), Arrays.asList(-1, 2, 3)
                ),
                Arrays.asList(
                        Arrays.asList(1, 2, 2, -3), Collections.emptyList()
                ),
                Arrays.asList(
                        Collections.singletonList(1), Arrays.asList(-1, -2), Arrays.asList(1, 2, 3)
                )
        );

        var toValidate = new ListTestForm(list);

        var actual = new ValidatorImpl().validate(toValidate);
        var expected = new HashSet<ValidationError>(Arrays.asList(
                new ValidationErrorImpl(ProcessorsUtils.AnnotationErrorMessages.POSITIVE_MESSAGE, "a[0][2][0]", -1),
                new ValidationErrorImpl(ProcessorsUtils.AnnotationErrorMessages.POSITIVE_MESSAGE, "a[1][0][3]", -3),
                new ValidationErrorImpl(ProcessorsUtils.AnnotationErrorMessages.POSITIVE_MESSAGE, "a[2][1][0]", -1),
                new ValidationErrorImpl(ProcessorsUtils.AnnotationErrorMessages.POSITIVE_MESSAGE, "a[2][1][1]", -2)));

        assertTrue(TestUtils.CompareSets(actual, expected));
    }

    @Test
    void listCombinationFailsTest() throws IllegalAccessException, NoSuchFieldException {
        List<List<List<Integer>>> list = Collections.singletonList(
                Arrays.asList(
                        null, Arrays.asList(1, 2, 3), Arrays.asList(-1, 2, 3)
                )
        );

        var toValidate = new ListTestForm(list);

        var actual = new ValidatorImpl().validate(toValidate);
        var expected = new HashSet<ValidationError>(Arrays.asList(
                new ValidationErrorImpl(ProcessorsUtils.AnnotationErrorMessages.POSITIVE_MESSAGE, "a[0][2][0]", -1),
                new ValidationErrorImpl(ProcessorsUtils.AnnotationErrorMessages
                        .getSizeMessage(toValidate.getAnnotation()), "a", list)));

        assertTrue(TestUtils.CompareSets(actual, expected));
    }
}
