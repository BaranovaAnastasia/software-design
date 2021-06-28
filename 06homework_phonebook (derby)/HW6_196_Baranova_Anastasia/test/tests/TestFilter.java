package tests;

import org.junit.jupiter.api.Test;
import phonebook.database.PhonebookDatabase;

import static org.junit.jupiter.api.Assertions.*;

public class TestFilter {

    @Test
    void testNoResults() {
        PhonebookDatabase database = new PhonebookDatabase("testDB", "testFilterNoResults");
        assertTrue(database.isConnected());

        for (var c : TestUtils.contacts) {
            database.addContact(c);
        }

        assertTrue(database.filter("some random key").isEmpty());

        database.dropAndClose();
    }

    @Test
    void testAllInResult() {
        PhonebookDatabase database = new PhonebookDatabase("testDB", "testFilterAllInResult");
        assertTrue(database.isConnected());

        for (var c : TestUtils.contacts) {
            database.addContact(c);
        }

        assertEquals(database.filter("").size(), TestUtils.contacts.size());

        database.dropAndClose();
    }

    @Test
    void testFilter() {
        PhonebookDatabase database = new PhonebookDatabase("testDB", "testFilter");
        assertTrue(database.isConnected());

        for (var c : TestUtils.contacts) {
            database.addContact(c);
        }

        var filtered = database.filter("Anastasia");

        for (var c : filtered) {
            assertTrue(c.equalsAbsolute(TestUtils.contacts.get(0))
                    || c.equalsAbsolute(TestUtils.contacts.get(2)));
        }

        database.dropAndClose();
    }
}
