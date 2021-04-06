package tests.forms.positive;

import com.company.annotations.Constrained;
import com.company.annotations.Positive;

@SuppressWarnings("unused")
@Constrained
public class PositiveInvalidTypeTestForm {
    @Positive
    private final String a;

    public PositiveInvalidTypeTestForm(String value) {
        a = value;
    }
}
