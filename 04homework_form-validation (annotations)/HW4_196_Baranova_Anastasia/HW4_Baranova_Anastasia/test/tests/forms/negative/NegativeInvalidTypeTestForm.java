package tests.forms.negative;

import com.company.annotations.Constrained;
import com.company.annotations.Negative;

@SuppressWarnings("unused")
@Constrained
public class NegativeInvalidTypeTestForm {
    @Negative
    private final String a;

    public NegativeInvalidTypeTestForm(String value) {
        a = value;
    }
}