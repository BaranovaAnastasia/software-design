package tests;

import com.company.annotations.processors.ProcessorsUtils;
import com.company.validation.ValidationError;
import com.company.validation.ValidationErrorImpl;
import com.company.validation.ValidatorImpl;
import org.junit.jupiter.api.Test;
import tests.forms.example.BookingForm;
import tests.forms.example.GuestForm;
import tests.forms.example.Unrelated;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExampleTest {
    @Test
    void test() throws IllegalAccessException {
        List<GuestForm> guests = List.of(
                new GuestForm(/*firstName*/ null, /*lastName*/ "Def", /*age*/ 21),
                new GuestForm(/*firstName*/ "", /*lastName*/ "Ijk", /*age*/ -3)
        );
        Unrelated unrelated = new Unrelated(-1);
        BookingForm bookingForm = new BookingForm(
                guests,
                /*amenities*/ List.of("TV", "Piano"),
                /*propertyType*/ "Apartment",
                unrelated
        );
        var validationErrors = new ValidatorImpl().validate(bookingForm);

        Set<ValidationError> expected = new HashSet<>();
        expected.add(new ValidationErrorImpl(ProcessorsUtils.AnnotationErrorMessages.NOTNULL_MESSAGE,
                "guests[0].firstName", null));
        expected.add(new ValidationErrorImpl("Value must be in range [0; 200]",
                "guests[1].age", -3));
        expected.add(new ValidationErrorImpl(ProcessorsUtils.AnnotationErrorMessages.NOTBLANK_MESSAGE,
                "guests[1].firstName", ""));
        expected.add(new ValidationErrorImpl("Value must be one of the allowed ones: TV; Kitchen",
                "amenities[1]", "Piano"));
        expected.add(new ValidationErrorImpl("Value must be one of the allowed ones: House; Hostel",
                "propertyType", "Apartment"));

        assertTrue(TestUtils.CompareSets(validationErrors, expected));
    }
}
