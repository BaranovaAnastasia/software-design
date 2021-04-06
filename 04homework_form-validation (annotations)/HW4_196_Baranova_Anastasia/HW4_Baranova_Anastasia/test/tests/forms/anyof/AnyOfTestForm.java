package tests.forms.anyof;

import com.company.annotations.AnyOf;
import com.company.annotations.Constrained;

@Constrained
public class AnyOfTestForm {
    public AnyOf getAnnotation() throws NoSuchFieldException {
        return (AnyOf) (AnyOfTestForm.class.getDeclaredField("a").getAnnotatedType().getDeclaredAnnotations()[0]);
    }

    @AnyOf({"1", "2", "3", "4", "5"})
    private final String a;

    public AnyOfTestForm(String value) {
        a = value;
    }
}
