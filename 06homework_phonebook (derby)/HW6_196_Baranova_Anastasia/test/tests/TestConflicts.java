package tests;

import org.junit.jupiter.api.Test;
import phonebook.database.PhonebookDatabase;
import phonebooklib.Contact;

import static org.junit.jupiter.api.Assertions.*;

public class TestConflicts {

    @Test
    void findAllConflicts() {
        PhonebookDatabase database = new PhonebookDatabase("testDB", "findAllConflicts");

        assertTrue(database.isConnected());

        for (var c : TestUtils.contacts) {
            database.addContact(c);
        }

        for (var c : TestUtils.contacts) {
            var conflict = database.findConflict(c);
            assertTrue(conflict.equalsAbsolute(c));
        }

        database.dropAndClose();
    }

    @Test
    void findNoConflicts() {
        PhonebookDatabase database = new PhonebookDatabase("testDB", "findNoConflicts");

        assertTrue(database.isConnected());

        for (var c : TestUtils.contacts) {
            database.addContact(c);
        }

        assertNull(database.findConflict(Contact.getBuilder().setName("name").setSurname("name").setHomePhone("1").build()));

        database.dropAndClose();
    }
}
