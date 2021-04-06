package tests.forms.constrained;

import com.company.annotations.NotNull;

@SuppressWarnings("unused")
public class NotConstrainedForm {
    @NotNull
    private final String str = null;
}
