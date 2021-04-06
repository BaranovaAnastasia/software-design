package tests.forms.example;

import com.company.annotations.Positive;

@SuppressWarnings("unused")
public class Unrelated {
    @Positive
    private final int x;

    public Unrelated(int x) {
        this.x = x;
    }
}