package tests.forms.inrange;

import com.company.annotations.Constrained;
import com.company.annotations.InRange;

@SuppressWarnings("unused")
@Constrained
public class InRangeInvalidTypeTestForm {
    @InRange(min = -10, max = 10)
    private final String a;

    public InRangeInvalidTypeTestForm(String value) {
        a = value;
    }
}
