package tests;

import org.junit.jupiter.api.Test;
import phonebook.database.PhonebookDatabase;

import static org.junit.jupiter.api.Assertions.*;

public class TestAddDeleteContact {
    @Test
    void addTest() {
        PhonebookDatabase database = new PhonebookDatabase("testDB", "addTest");

        assertTrue(database.isConnected());

        for (var c : TestUtils.contacts) {
            database.addContact(c);
        }

        var read = database.importContacts();

        assertEquals(read.size(), TestUtils.contacts.size());

        int i = 1;
        for (var c: read) {
            assertTrue(c.equalsAbsolute(TestUtils.contacts.get(i - 1)));
            assertEquals(c.getId(), i);
            ++i;
        }

        database.dropAndClose();
    }

    @Test
    void deleteTest() {
        PhonebookDatabase database = new PhonebookDatabase("testDB", "deleteTest");

        assertTrue(database.isConnected());

        for (var c : TestUtils.contacts) {
            database.addContact(c);
        }

        var allFromDB = database.importContacts();

        int i = 1;
        for (var c : allFromDB) {
            database.deleteContact(c);

            var read = database.importContacts();

            assertEquals(read.size(), TestUtils.contacts.size() - i);

            int j = i + 1;
            for (var contact: read) {
                assertTrue(contact.equalsAbsolute(TestUtils.contacts.get(j - 1)));
                assertEquals(contact.getId(), j);
                ++j;
            }
            ++i;
        }

        database.dropAndClose();
    }
}
