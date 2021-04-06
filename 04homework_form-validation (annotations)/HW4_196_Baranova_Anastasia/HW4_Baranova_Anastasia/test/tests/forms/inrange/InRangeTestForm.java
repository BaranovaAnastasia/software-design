package tests.forms.inrange;

import com.company.annotations.Constrained;
import com.company.annotations.InRange;

@SuppressWarnings("unused")
@Constrained
public class InRangeTestForm {
    public InRange getAnnotation() throws NoSuchFieldException {
        return (InRange) (InRangeTestForm.class.getDeclaredField("a").getAnnotatedType().getDeclaredAnnotations()[0]);
    }

    @InRange(min = -10, max = 10)
    private final Short a;

    public InRangeTestForm(Short value) {
        a = value;
    }
}