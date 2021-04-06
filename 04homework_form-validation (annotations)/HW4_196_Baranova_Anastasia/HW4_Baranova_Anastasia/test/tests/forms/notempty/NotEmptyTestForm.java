package tests.forms.notempty;

import com.company.annotations.Constrained;
import com.company.annotations.NotEmpty;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
@Constrained
public class NotEmptyTestForm {
    @NotEmpty
    private Map<Integer, Integer> a = new HashMap<>();

    public NotEmptyTestForm(Integer setNotEmpty) {
        for (int i = 0; i < setNotEmpty; i++) {
            a.put(i, i);
        }
    }

    public NotEmptyTestForm(Map<Integer, Integer> value) {
        a = value;
    }
}
