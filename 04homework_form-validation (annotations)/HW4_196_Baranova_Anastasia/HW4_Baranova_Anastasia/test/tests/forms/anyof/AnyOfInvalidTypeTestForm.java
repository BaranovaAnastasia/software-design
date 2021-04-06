package tests.forms.anyof;

import com.company.annotations.AnyOf;
import com.company.annotations.Constrained;

@SuppressWarnings("unused")
@Constrained
public class AnyOfInvalidTypeTestForm {
    @AnyOf({"1", "2", "3", "4", "5"})
    private final Integer a;

    public AnyOfInvalidTypeTestForm(Integer value) {
        a = value;
    }
}