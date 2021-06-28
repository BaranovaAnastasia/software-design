package tests;

import phonebooklib.Contact;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class TestUtils {
    public static final List<Contact> contacts = Arrays.asList(
            Contact.getBuilder().setName("Anastasia").setSurname("Baranova")
                    .setPatronymic("Andreevna").setMobilePhone("123456789").setNotes("note").build(),

            Contact.getBuilder().setName("Elena").setSurname("Baranova")
                    .setPatronymic("Andreevna").setMobilePhone("54321").build(),

            Contact.getBuilder().setName("Anastasia").setSurname("Bar")
                    .setAddress("Moscow").setMobilePhone("98765").build(),

            Contact.getBuilder().setName("Maria").setSurname("Ivanova")
                    .setAddress("Moscow").setMobilePhone("123456789")
                    .setBirthDate(LocalDate.of(2000, 1, 1)).build());
}
