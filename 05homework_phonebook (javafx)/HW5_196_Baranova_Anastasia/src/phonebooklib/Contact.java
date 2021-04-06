package phonebooklib;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Class representing a phonebook contact.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
public class Contact {
    /**
     * Contact's surname.
     */
    private String surname;
    /**
     * Contact's name.
     */
    private String name;
    /**
     * Contact's patronymic.
     */
    private String patronymic;

    /**
     * Contact's home phone number.
     */
    private String homePhone;
    /**
     * Contact's mobile phone number.
     */
    private String mobilePhone;

    /**
     * Contact's date of birth.
     */
    private LocalDate birthDate;
    /**
     * Contact's address.
     */
    private String address;
    /**
     * Notes about a contact.
     */
    private String notes;


    /**
     * Returns a surname of a contact.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Returns a name of a contact.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a patronymic of a contact.
     */
    public String getPatronymic() {
        return patronymic;
    }

    /**
     * Returns a home phone of a contact.
     */
    public String getHomePhone() {
        return homePhone;
    }

    /**
     * Returns a mobile phone of a contact.
     */
    public String getMobilePhone() {
        return mobilePhone;
    }

    /**
     * Returns a date of birth of a contact.
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Returns an address of a contact.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns notes about a contact.
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Private constructor prevents contact creation without using a builder.
     */
    private Contact() {
    }

    /**
     * Returns a new contact builder instance.
     *
     * @see Contact.Builder
     */
    public static Builder getBuilder() {
        return new Contact().new Builder();
    }

    /**
     * Returns a contact builder instance with this contact data already set.
     *
     * @see Contact.Builder
     */
    public Builder getThisBuilder() {
        return new Contact().new Builder()
                .setName(name)
                .setSurname(surname)
                .setPatronymic(patronymic)
                .setHomePhone(homePhone)
                .setMobilePhone(mobilePhone)
                .setBirthDate(birthDate)
                .setAddress(address)
                .setNotes(notes);
    }

    /**
     * Parses a contact from CSV formatted line.
     *
     * @param contact   Contact string representation. CSV string with passed separator.
     *                  Values order: first name, last name (surname), patronymic, home phone number,
     *                  mobile phone number, date of birth, address, notes.
     * @param separator CSV separator.
     * @return Contact.Builder instance with values extracted from passed string set.
     * Can be built if parsed contact is valid.
     * @throws IndexOutOfBoundsException               If passed CSV string contains less values than needed (8).
     * @throws java.time.format.DateTimeParseException If extracted date of contact's birth is incorrect and
     *                                                 cannot be parsed.
     * @throws NullPointerException                    If contact string and/or separator are/is null.
     * @see #toCSVString(String)
     * @see Contact.Builder
     */
    public static Builder parseFromCSV(String contact, String separator) {
        String[] data = contact.split(separator, -1);

        return getBuilder()
                .setName(data[0])
                .setSurname(data[1])
                .setPatronymic(data[2])
                .setHomePhone(data[3])
                .setMobilePhone(data[4])
                .setBirthDate(data[5].isBlank() ? null : LocalDate.parse(data[5]))
                .setAddress(data[6])
                .setNotes(data[7]);
    }

    /**
     * Generates CSV string representation of this contact.
     * Values order: first name, last name (surname), patronymic, home phone number,
     * mobile phone number, date of birth, address, notes.
     *
     * @param separator CSV separator.
     * @return Generated CSV line representing this contact.
     */
    public String toCSVString(String separator) {
        return name + separator +
                surname + separator +
                (patronymic == null ? "" : patronymic) + separator +
                homePhone + separator +
                mobilePhone + separator +
                (birthDate == null ? "" : birthDate) + separator +
                (address == null ? "" : address) + separator +
                (notes == null ? "" : notes);
    }

    /**
     * Checks if contact is valid i.e surname, name and one of phone numbers (home and/or mobile) are provided.
     *
     * @return true, if contact is valid, false otherwise.
     */
    private boolean isValid() {
        return !(surname == null || surname.isBlank()
                || name == null || name.isBlank()
                || ((homePhone == null || homePhone.isBlank())
                && (mobilePhone == null || mobilePhone.isBlank())));
    }

    /**
     * Checks if this contact is fully equal to the passed one (all of the values are the same).
     *
     * @return true, if contacts are absolutely the same, false otherwise.
     */
    public boolean equalsAbsolute(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contact)) return false;
        Contact contact = (Contact) o;
        return getSurname().equals(contact.getSurname()) &&
                getName().equals(contact.getName()) &&
                Objects.equals(getHomePhone(), contact.getHomePhone()) &&
                Objects.equals(getMobilePhone(), contact.getMobilePhone()) &&

                Objects.equals(getPatronymic(), contact.getPatronymic()) &&
                Objects.equals(getBirthDate(), contact.getBirthDate()) &&
                Objects.equals(getAddress(), contact.getAddress()) &&
                Objects.equals(getNotes(), contact.getNotes());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contact)) return false;
        Contact contact = (Contact) o;
        return getSurname().equals(contact.getSurname()) &&
                getName().equals(contact.getName()) &&
                Objects.equals(getPatronymic(), contact.getPatronymic());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSurname(), getName(), getPatronymic());
    }

    /**
     * Contact builder. Provides functionality for creating contact.
     * Prevents creating illegal (incomplete) contacts i.e contacts without
     * first and last names and at least one of phone numbers (home and/or mobile).
     *
     * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
     */
    public class Builder {
        private Builder() {
        }

        /**
         * Sets a surname for a building contact.
         */
        public Builder setSurname(String surname) {
            Contact.this.surname = surname;
            return this;
        }

        /**
         * Sets a name for a building contact.
         */
        public Builder setName(String name) {
            Contact.this.name = name;
            return this;
        }

        /**
         * Sets a patronymic for a building contact.
         */
        public Builder setPatronymic(String patronymic) {
            if (patronymic != null && patronymic.isEmpty()) {
                Contact.this.patronymic = null;
                return this;
            }
            Contact.this.patronymic = patronymic;
            return this;
        }

        /**
         * Sets a home phone number for a building contact.
         * Invalid strings (strings containing symbols that are not digits) will be ignored.
         *
         * @throws NullPointerException If passed string is null.
         */
        public Builder setHomePhone(String homePhone) {
            if (isNumeric(homePhone)) {
                Contact.this.homePhone = homePhone;
            }
            return this;
        }

        /**
         * Sets a mobile phone number for a building contact.
         * Invalid strings (strings containing symbols that are not digits) will be ignored.
         *
         * @throws NullPointerException If passed string is null.
         */
        public Builder setMobilePhone(String mobilePhone) {
            if (isNumeric(mobilePhone)) {
                Contact.this.mobilePhone = mobilePhone;
            }
            return this;
        }

        /**
         * Sets a date of birth for a building contact.
         */
        public Builder setBirthDate(LocalDate birthDate) {
            Contact.this.birthDate = birthDate;
            return this;
        }

        /**
         * Sets an address for a building contact.
         */
        public Builder setAddress(String address) {
            if (address != null && address.isEmpty()) {
                Contact.this.address = null;
                return this;
            }
            Contact.this.address = address;
            return this;
        }

        /**
         * Sets notes about a building contact.
         */
        public Builder setNotes(String notes) {
            if (notes != null && notes.isEmpty()) {
                Contact.this.notes = null;
                return this;
            }
            Contact.this.notes = notes;
            return this;
        }

        /**
         * Builds a contact with previously set data.
         *
         * @return Built contact. If built contact is invalid (first name, last name
         * and phone numbers (neither home nor mobile) are not provided) null will be returned.
         */
        public Contact build() {
            if (!Contact.this.isValid()) {
                return null;
            }
            return Contact.this;
        }

        /**
         * Checks if passed string is numeric (consists of digits only).
         *
         * @return true, if string contains digits only, false otherwise.
         */
        private boolean isNumeric(String str) {
            if (str == null) {
                return false;
            }
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) < '0' || str.charAt(i) > '9') {
                    return false;
                }
            }
            return true;
        }
    }
}
