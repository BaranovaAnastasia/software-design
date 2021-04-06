package tests;

import org.junit.jupiter.api.Test;
import phonebook.Utils;
import phonebooklib.Contact;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestFilter {
    private final List<Contact> contacts = Arrays.asList(
            Contact.getBuilder().setName("Anastasia").setSurname("Baranova")
                    .setPatronymic("Andreevna").setMobilePhone("123456789").build(),

            Contact.getBuilder().setName("Elena").setSurname("Baranova")
                    .setPatronymic("Andreevna").setMobilePhone("54321").build(),

            Contact.getBuilder().setName("Anastasia").setSurname("Bar")
                    .setAddress("Moscow").setMobilePhone("98765").build(),

            Contact.getBuilder().setName("Maria").setSurname("Ivanova")
                    .setAddress("Moscow").setMobilePhone("123456789").build(),
            null);

    @Test
    void testNoResults() {
        String key = "Some random text";
        var filtered = Utils.filterContacts(contacts, key);
        assertEquals(filtered.size(), 0);
    }

    @Test
    void testAllInResult() {
        String key = "";
        var filtered = Utils.filterContacts(contacts, key);
        assertEquals(filtered.size(), contacts.size());

        for (var c : contacts) {
            assertTrue(filtered.contains(c));
        }
    }

    @Test
    void testNameFilter() {
        String key = "Anastasia";
        var filtered = Utils.filterContacts(contacts, key);
        assertEquals(filtered.size(), 2);

        for (var c : contacts) {
            if (c != null && c.getName().equals(key)) {
                assertTrue(filtered.contains(c));
            }
        }
    }

    @Test
    void testNameSurnameFilter() {
        String key = "anasTasia baRanOva";
        var filtered = Utils.filterContacts(contacts, key);
        assertEquals(filtered.size(), 1);

        assertTrue(filtered.contains(contacts.get(0)));
    }

    @Test
    void testOtherFilter() {
        String key = "Moscow 1234";
        var filtered = Utils.filterContacts(contacts, key);
        assertEquals(filtered.size(), 1);

        assertTrue(filtered.contains(contacts.get(3)));
    }
}
