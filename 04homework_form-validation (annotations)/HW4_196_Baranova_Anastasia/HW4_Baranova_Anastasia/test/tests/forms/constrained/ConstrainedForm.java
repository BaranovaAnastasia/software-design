package tests.forms.constrained;

import com.company.annotations.Constrained;
import com.company.annotations.NotNull;

@SuppressWarnings("unused")
@Constrained
public class ConstrainedForm {
    @NotNull
    private final String str = null;
}
