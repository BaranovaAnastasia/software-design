package tests;

import org.junit.jupiter.api.Test;
import phonebooklib.Contact;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TestContactEditing {
    @Test
    void testInvalidEdit() {
        Contact contact = Contact.getBuilder()
                .setName("A")
                .setSurname("B")
                .setHomePhone("54321").build();

        assertNull(contact.getThisBuilder().setName("").build());
        assertEquals(contact.getName(), "A");
    }

    @Test
    void editName() {
        Contact contact = Contact.getBuilder()
                .setName("A")
                .setSurname("B")
                .setHomePhone("54321").build();

        contact = contact.getThisBuilder().setName("C").build();

        assertNotNull(contact);
        assertEquals(contact.getName(), "C");
    }

    @Test
    void editSurname() {
        Contact contact = Contact.getBuilder()
                .setName("A")
                .setSurname("B")
                .setHomePhone("54321").build();

        contact = contact.getThisBuilder().setSurname("C").build();

        assertNotNull(contact);
        assertEquals(contact.getSurname(), "C");
    }

    @Test
    void editPatronymic() {
        Contact contact = Contact.getBuilder()
                .setName("A")
                .setSurname("B")
                .setHomePhone("54321").build();

        assertNull(contact.getPatronymic());

        contact = contact.getThisBuilder().setPatronymic("C").build();

        assertNotNull(contact);
        assertEquals(contact.getPatronymic(), "C");
    }

    @Test
    void editHomePhoneIncorrectly() {
        Contact contact = Contact.getBuilder()
                .setName("A")
                .setSurname("B")
                .setHomePhone("54321").build();

        contact = contact.getThisBuilder().setHomePhone("C").build();

        assertNotNull(contact);
        assertEquals(contact.getHomePhone(), "54321");
    }

    @Test
    void editMobilePhoneIncorrectly() {
        Contact contact = Contact.getBuilder()
                .setName("A")
                .setSurname("B")
                .setHomePhone("54321").build();

        assertNull(contact.getMobilePhone());

        contact = contact.getThisBuilder().setMobilePhone("C").build();

        assertNotNull(contact);
        assertNull(contact.getMobilePhone());
    }

    @Test
    void editHomePhone() {
        Contact contact = Contact.getBuilder()
                .setName("A")
                .setSurname("B")
                .setHomePhone("54321").build();

        contact = contact.getThisBuilder().setHomePhone("1").build();

        assertNotNull(contact);
        assertEquals(contact.getHomePhone(), "1");
    }

    @Test
    void editMobilePhone() {
        Contact contact = Contact.getBuilder()
                .setName("A")
                .setSurname("B")
                .setHomePhone("54321").build();

        assertNull(contact.getMobilePhone());

        contact = contact.getThisBuilder().setMobilePhone("12").build();

        assertNotNull(contact);
        assertEquals(contact.getMobilePhone(), "12");
    }

    @Test
    void editBirthDate() {
        Contact contact = Contact.getBuilder()
                .setName("A")
                .setSurname("B")
                .setHomePhone("54321").build();

        assertNull(contact.getBirthDate());

        contact = contact.getThisBuilder().setBirthDate(LocalDate.of(2002, 1, 23)).build();

        assertNotNull(contact);
        assertEquals(contact.getBirthDate(), LocalDate.of(2002, 1, 23));
    }

    @Test
    void editAddress() {
        Contact contact = Contact.getBuilder()
                .setName("A")
                .setSurname("B")
                .setHomePhone("54321").build();

        assertNull(contact.getAddress());

        contact = contact.getThisBuilder().setAddress("Krasnodar").build();

        assertNotNull(contact);
        assertEquals(contact.getAddress(), "Krasnodar");
    }

    @Test
    void editNotes() {
        Contact contact = Contact.getBuilder()
                .setName("A")
                .setSurname("B")
                .setHomePhone("54321").build();

        assertNull(contact.getNotes());

        contact = contact.getThisBuilder().setNotes("note note").build();

        assertNotNull(contact);
        assertEquals(contact.getNotes(), "note note");
    }
}
