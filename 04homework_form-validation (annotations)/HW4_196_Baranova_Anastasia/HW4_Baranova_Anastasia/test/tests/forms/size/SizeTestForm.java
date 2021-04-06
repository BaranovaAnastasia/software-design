package tests.forms.size;

import com.company.annotations.Constrained;
import com.company.annotations.Size;

import java.util.HashSet;
import java.util.Set;

@Constrained
public class SizeTestForm {
    public Size getAnnotation(int index) throws NoSuchFieldException {
        if (index == 0) {
            return (Size) (SizeTestForm.class.getDeclaredField("a").getAnnotatedType().getDeclaredAnnotations()[0]);
        }
        return (Size) (SizeTestForm.class.getDeclaredField("str").getAnnotatedType().getDeclaredAnnotations()[0]);
    }

    @Size(min = 10, max = 20)
    private Set<Integer> a = new HashSet<>();

    @Size(min = 3, max = 5)
    private String str = "1234";

    public SizeTestForm(Integer setSize) {
        for (int i = 0; i < setSize; i++) {
            a.add(i);
        }
    }

    public SizeTestForm(Set<Integer> value) {
        a = value;
    }

    public SizeTestForm(String value) {
        a = null;
        str = value;
    }
}
