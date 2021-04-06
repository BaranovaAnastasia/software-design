package tests.forms.constrained;

import com.company.annotations.Constrained;
import com.company.annotations.NotNull;

@SuppressWarnings("unused")
@Constrained
public class ConstrainedFormPlusInner {
    @NotNull
    private final String str = null;

    private final NotConstrainedForm ncc = new NotConstrainedForm();
    private final ConstrainedForm cc = new ConstrainedForm();
}
