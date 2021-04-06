package tests.forms.notempty;

import com.company.annotations.Constrained;
import com.company.annotations.NotEmpty;

@SuppressWarnings("unused")
@Constrained
public class NotEmptyInvalidTypeTestForm {
    @NotEmpty
    private final Integer a;

    public NotEmptyInvalidTypeTestForm(Integer value) {
        a = value;
    }
}