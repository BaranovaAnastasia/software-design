package tests.forms.notnull;

import com.company.annotations.Constrained;
import com.company.annotations.NotNull;

@SuppressWarnings("unused")
@Constrained
public class NotNullTestForm {
    @NotNull
    private final String a;

    public NotNullTestForm(String value) {
        a = value;
    }
}
