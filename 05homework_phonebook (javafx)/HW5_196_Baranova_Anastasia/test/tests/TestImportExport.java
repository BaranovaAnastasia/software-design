package tests;

import org.junit.jupiter.api.Test;
import phonebook.Utils;
import phonebooklib.Contact;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestImportExport {
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

    private final String path = "test.csv";

    @Test
    void exportContacts() {
        assertDoesNotThrow(() -> Utils.exportContacts(contacts, path, ";", null));

        assertTrue(Utils.doesFileExist(path));
    }

    @Test
    void importContacts() {
        Utils.exportContacts(contacts, path, ";", null);
        var imported = Utils.importContacts(path, ";", null);

        assertEquals(imported.size(), contacts.size());

        for (var c : imported) {
            assertTrue(contacts.contains(c));
            assertTrue(contacts.get(contacts.indexOf(c)).equalsAbsolute(c));
        }
    }

    @Test
    void importContactsInvalidPath() {
        var imported = Utils.importContacts("somePathThatDoesNotExists.csv", ";", null);

        assertEquals(imported.size(), 0);
    }
}
