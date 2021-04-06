package tests.forms.negative;

import com.company.annotations.Constrained;
import com.company.annotations.Negative;

@SuppressWarnings("unused")
@Constrained
public class NegativeTestForm {
    @Negative
    private final Long a;

    public NegativeTestForm(Long value) {
        a = value;
    }
}