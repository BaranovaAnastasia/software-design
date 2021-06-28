package tests;

import org.junit.jupiter.api.Test;
import phonebook.database.PhonebookDatabase;

import static org.junit.jupiter.api.Assertions.*;

public class TestContactEditing {
    @Test
    void editAddress() {
        PhonebookDatabase database = new PhonebookDatabase("testDB", "editAddress");
        assertTrue(database.isConnected());

        database.addContact(TestUtils.contacts.get(0));
        var newContact = TestUtils.contacts.get(0).getThisBuilder().setAddress("new address").build();

        var read = database.importContacts();
        assertEquals(read.size(), 1);

        for (var c : read) {
            database.editContact(c, newContact);
        }

        read = database.importContacts();
        assertEquals(read.size(), 1);
        for (var c : read) {
            assertTrue(c.equalsAbsolute(newContact));
        }

        database.dropAndClose();
    }

    @Test
    void editName() {
        PhonebookDatabase database = new PhonebookDatabase("testDB", "editName");
        assertTrue(database.isConnected());

        database.addContact(TestUtils.contacts.get(0));
        var newContact = TestUtils.contacts.get(0).getThisBuilder().setName("new name").build();

        var read = database.importContacts();
        assertEquals(read.size(), 1);

        for (var c : read) {
            database.editContact(c, newContact);
        }

        read = database.importContacts();
        assertEquals(read.size(), 1);
        for (var c : read) {
            assertTrue(c.equalsAbsolute(newContact));
        }

        database.dropAndClose();
    }

    @Test
    void editPatronymic() {
        PhonebookDatabase database = new PhonebookDatabase("testDB", "editPatronymic");
        assertTrue(database.isConnected());

        database.addContact(TestUtils.contacts.get(0));
        var newContact = TestUtils.contacts.get(0).getThisBuilder().setPatronymic("new patronymic").build();

        var read = database.importContacts();
        assertEquals(read.size(), 1);

        for (var c : read) {
            database.editContact(c, newContact);
        }

        read = database.importContacts();
        assertEquals(read.size(), 1);
        for (var c : read) {
            assertTrue(c.equalsAbsolute(newContact));
        }

        database.dropAndClose();
    }

    @Test
    void editNote() {
        PhonebookDatabase database = new PhonebookDatabase("testDB", "editNote");
        assertTrue(database.isConnected());

        database.addContact(TestUtils.contacts.get(0));
        var newContact = TestUtils.contacts.get(0).getThisBuilder().setNotes("").build();

        var read = database.importContacts();
        assertEquals(read.size(), 1);

        for (var c : read) {
            database.editContact(c, newContact);
        }

        read = database.importContacts();
        assertEquals(read.size(), 1);
        for (var c : read) {
            assertTrue(c.equalsAbsolute(newContact));
        }

        database.dropAndClose();
    }
}
