package tests;

import org.junit.jupiter.api.Test;
import phonebook.database.PhonebookDatabase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestSave {
    @Test
    void testSave() {
        PhonebookDatabase database = new PhonebookDatabase("testDB", "editAddress");
        assertTrue(database.isConnected());
        for (var c : TestUtils.contacts) {
            database.addContact(c);
        }

        database.close();

        PhonebookDatabase database1 = new PhonebookDatabase("testDB", "editAddress");
        assertTrue(database1.isConnected());
        var saved = database1.importContacts();
        assertEquals(saved.size(), TestUtils.contacts.size());

        int i = 1;
        for (var c : saved) {
            assertTrue(c.equalsAbsolute(TestUtils.contacts.get(i - 1)));
            assertEquals(c.getId(), i);
            ++i;
        }

        database1.dropAndClose();
    }
}
