package tests.forms.example;

import com.company.annotations.AnyOf;
import com.company.annotations.Constrained;
import com.company.annotations.NotNull;
import com.company.annotations.Size;

import java.util.List;

@SuppressWarnings("unused")
@Constrained
public class BookingForm {
    @NotNull
    @Size(min = 1, max = 5)
    private final List<@NotNull GuestForm> guests;
    @NotNull
    private final List<@AnyOf({"TV", "Kitchen"}) String> amenities;
    @NotNull
    @AnyOf({"House", "Hostel"})
    private final String propertyType;
    @NotNull
    private final Unrelated unrelated;

    public BookingForm(List<GuestForm> guests, List<String> amenities, String
            propertyType, Unrelated unrelated) {
        this.guests = guests;
        this.amenities = amenities;
        this.unrelated = unrelated;
        this.propertyType = propertyType;
    }
}