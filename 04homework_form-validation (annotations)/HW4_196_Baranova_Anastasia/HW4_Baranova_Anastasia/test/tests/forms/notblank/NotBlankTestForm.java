package tests.forms.notblank;

import com.company.annotations.Constrained;
import com.company.annotations.NotBlank;

@SuppressWarnings("unused")
@Constrained
public class NotBlankTestForm {
    @NotBlank
    private final String a;

    public NotBlankTestForm(String value) {
        a = value;
    }
}