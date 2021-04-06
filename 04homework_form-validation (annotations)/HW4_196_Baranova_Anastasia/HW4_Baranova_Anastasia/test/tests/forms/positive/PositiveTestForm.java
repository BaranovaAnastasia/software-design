package tests.forms.positive;

import com.company.annotations.Constrained;
import com.company.annotations.Positive;

@SuppressWarnings("unused")
@Constrained
public class PositiveTestForm {
    @Positive
    private final Integer a;

    @Positive
    private final Byte b;

    public PositiveTestForm(Integer value) {
        a = value;
        b = null;
    }

    public PositiveTestForm(Byte value) {
        a = (int) value;
        b = value;
    }
}
