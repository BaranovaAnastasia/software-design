package tests.forms.list;

import com.company.annotations.Constrained;
import com.company.annotations.NotEmpty;
import com.company.annotations.Positive;
import com.company.annotations.Size;

import java.util.List;

@Constrained
public class ListTestForm {
    public Size getAnnotation() throws NoSuchFieldException {
        return (Size) (ListTestForm.class.getDeclaredField("a").getAnnotatedType().getDeclaredAnnotations()[0]);
    }

    @Size(min = 2, max = 5)
    private final List<@NotEmpty List<List<@Positive Integer>>> a;

    public ListTestForm(List<List<List<Integer>>> value) {
        a = value;
    }
}
