package tests;

import org.junit.jupiter.api.Test;
import phonebooklib.Contact;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TestContactCreation {
    @Test
    void invalidContactCreationName() {
        // (name) (surname) (home number) (mobile number)
        assertNull(Contact.getBuilder().build());

        // name (surname) (home number) (mobile number)
        assertNull(Contact.getBuilder().setName("str").build());

        // name surname (home number) (mobile number)
        assertNull(Contact.getBuilder().setName("str").setSurname("str").build());

        // name (surname) home number (mobile number)
        assertNull(Contact.getBuilder().setName("str").setHomePhone("1").build());

        // name (surname) (home number) mobile number
        assertNull(Contact.getBuilder().setName("str").setMobilePhone("1").build());

        // name (surname) home number mobile number
        assertNull(Contact.getBuilder().setName("str").setHomePhone("1").setMobilePhone("1").build());
    }

    @Test
    void invalidContactCreationSurname() {
        // (name) surname (home number) (mobile number)
        assertNull(Contact.getBuilder().setSurname("str").build());

        // (name) surname home number (mobile number)
        assertNull(Contact.getBuilder().setSurname("str").setHomePhone("1").build());

        // (name) surname (home number) mobile number
        assertNull(Contact.getBuilder().setSurname("str").setMobilePhone("1").build());

        // (name) surname home number mobile number
        assertNull(Contact.getBuilder().setSurname("str").setHomePhone("1").setMobilePhone("1").build());
    }

    @Test
    void invalidContactCreationPhone() {
        // (name) (surname) home number (mobile number)
        assertNull(Contact.getBuilder().setHomePhone("1").build());

        // (name) (surname) (home number) mobile number
        assertNull(Contact.getBuilder().setMobilePhone("1").build());

        // (name) (surname) home number mobile number
        assertNull(Contact.getBuilder().setHomePhone("1").setMobilePhone("1").build());
    }

    @Test
    void invalidPhoneNumber() {
        var builder = Contact.getBuilder().setName("name").setSurname("name");

        assertNull(builder.setMobilePhone("not digits").build());
        assertNull(builder.setHomePhone("definitely not digits").build());
    }

    @Test
    void validContact() {
        assertNotNull(Contact.getBuilder()
                .setName("name")
                .setSurname("name")
                .setHomePhone("1").build());

        assertNotNull(Contact.getBuilder()
                .setName("name")
                .setSurname("name")
                .setMobilePhone("1").build());

        assertNotNull(Contact.getBuilder()
                .setName("name")
                .setSurname("name")
                .setHomePhone("1")
                .setMobilePhone("1").build());
    }

    @Test
    void testRequired() {
        Contact contact = Contact.getBuilder()
                .setName("A")
                .setSurname("B")
                .setHomePhone("54321")
                .setMobilePhone("12345").build();

        assertEquals(contact.getName(), "A");
        assertEquals(contact.getSurname(), "B");
        assertEquals(contact.getHomePhone(), "54321");
        assertEquals(contact.getMobilePhone(), "12345");
    }

    @Test
    void testPatronymic() {
        Contact contact = Contact.getBuilder()
                .setName("A")
                .setSurname("B")
                .setHomePhone("54321")

                .setPatronymic("dad's name").build();

        assertEquals(contact.getPatronymic(), "dad's name");
    }

    @Test
    void testBirthDate() {
        Contact contact = Contact.getBuilder()
                .setName("A")
                .setSurname("B")
                .setHomePhone("54321")

                .setBirthDate(LocalDate.of(2002, 1, 23)).build();

        assertEquals(contact.getBirthDate(), LocalDate.of(2002, 1, 23));
    }

    @Test
    void testAddress() {
        Contact contact = Contact.getBuilder()
                .setName("A")
                .setSurname("B")
                .setHomePhone("54321")

                .setAddress("Moscow").build();

        assertEquals(contact.getAddress(), "Moscow");
    }

    @Test
    void testNotes() {
        Contact contact = Contact.getBuilder()
                .setName("A")
                .setSurname("B")
                .setHomePhone("54321")

                .setNotes("some notes").build();

        assertEquals(contact.getNotes(), "some notes");
    }
}
