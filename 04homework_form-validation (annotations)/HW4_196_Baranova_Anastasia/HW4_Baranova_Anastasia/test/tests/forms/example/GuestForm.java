package tests.forms.example;

import com.company.annotations.Constrained;
import com.company.annotations.InRange;
import com.company.annotations.NotBlank;
import com.company.annotations.NotNull;

@SuppressWarnings("unused")
@Constrained
public class GuestForm {
    @NotNull
    @NotBlank
    private final String firstName;
    @NotBlank
    @NotNull
    private final String lastName;
    @InRange(min = 0, max = 200)
    private final int age;

    public GuestForm(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }
}
