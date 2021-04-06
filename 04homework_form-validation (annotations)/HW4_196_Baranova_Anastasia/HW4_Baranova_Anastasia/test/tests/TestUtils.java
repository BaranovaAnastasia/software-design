package tests;

import com.company.validation.ValidationError;

import java.util.Set;

public class TestUtils {
    public static boolean CompareSets(Set<ValidationError> actual, Set<ValidationError> expected) {
        if (actual.size() != expected.size()) {
            return false;
        }

        for (ValidationError r : actual) {
            for (var it = expected.iterator(); it.hasNext(); ) {
                var value = it.next();
                if (r.equals(value)) {
                    break;
                }
                if (!it.hasNext()) {
                    return false;
                }
            }
        }
        return true;
    }
}
