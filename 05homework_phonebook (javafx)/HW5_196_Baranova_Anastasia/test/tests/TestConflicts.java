package tests;

import org.junit.jupiter.api.Test;
import phonebook.Utils;
import phonebooklib.Contact;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestConflicts {
    private final List<Contact> contacts = Arrays.asList(
            Contact.getBuilder().setName("Anastasia").setSurname("Baranova")
                    .setPatronymic("Andreevna").setMobilePhone("123456789").build(),

            Contact.getBuilder().setName("Elena").setSurname("Baranova")
                    .setPatronymic("Andreevna").setMobilePhone("54321").build(),

            Contact.getBuilder().setName("Anastasia").setSurname("Bar")
                    .setAddress("Moscow").setMobilePhone("98765").build(),

            Contact.getBuilder().setName("Maria").setSurname("Ivanova")
                    .setAddress("Moscow").setMobilePhone("123456789")
                    .setBirthDate(LocalDate.of(2000, 1, 1)).build());

    @Test
    void findAllConflicts() {
        for (var c : contacts) {
            assertEquals(Utils.findConflict(c, contacts), c);
        }
    }

    @Test
    void findNoConflicts() {
        assertNull(Utils.findConflict(Contact.getBuilder()
                .setName("name").setSurname("name").setHomePhone("1").build(), contacts));
    }
}
