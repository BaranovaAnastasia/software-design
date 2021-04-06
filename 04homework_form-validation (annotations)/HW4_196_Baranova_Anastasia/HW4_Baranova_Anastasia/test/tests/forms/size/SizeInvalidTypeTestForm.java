package tests.forms.size;

import com.company.annotations.Constrained;
import com.company.annotations.Size;

@SuppressWarnings("unused")
@Constrained
public class SizeInvalidTypeTestForm {
    @Size(min = 10, max = 20)
    private final Integer a;

    public SizeInvalidTypeTestForm(Integer value) {
        a = value;
    }
}
