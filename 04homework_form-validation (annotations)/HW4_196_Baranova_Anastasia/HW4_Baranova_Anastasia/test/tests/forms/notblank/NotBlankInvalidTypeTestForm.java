package tests.forms.notblank;

import com.company.annotations.Constrained;
import com.company.annotations.NotBlank;

@SuppressWarnings("unused")
@Constrained
public class NotBlankInvalidTypeTestForm {
    @NotBlank
    private final Integer a;

    public NotBlankInvalidTypeTestForm(Integer value) {
        a = value;
    }
}