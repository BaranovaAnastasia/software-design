package tests.forms.notnull;

import com.company.annotations.Constrained;
import com.company.annotations.NotNull;
import com.company.annotations.Positive;

@SuppressWarnings("unused")
@Constrained
public class NotNullCombinationTestForm {
    @NotNull
    @Positive
    private final Short a;

    public NotNullCombinationTestForm(Short value) {
        a = value;
    }
}
