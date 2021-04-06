package phonebook;

import phonebooklib.Contact;

import java.io.*;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Class provides static utility methods for working with contacts.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
public class Utils {
    /**
     * Searches contacts collection for specific contact.
     * Uses Contact.equals() to detect conflicts.
     *
     * @param contact Contact to find conflict with.
     * @param from    Collection of contacts from which to find contacts that conflict with the passed one.
     * @return Contact from passed collection that conflicts with the passed contact.
     * If non of the contacts of collection conflict with the passed contact, null is returned
     * @throws NullPointerException If any of passed arguments is null.
     * @see Contact#equals(Object)
     */
    public static Contact findConflict(Contact contact, Collection<Contact> from) {
        for (var c : from) {
            if (contact.equals(c)) {
                return c;
            }
        }

        return null;
    }

    /**
     * Checks if file in the passed path exists and is not a directory.
     *
     * @param path Path to the file to check.
     * @return true, if file exists and is not a directory, false otherwise.
     * @throws NullPointerException If path is null.
     */
    public static boolean doesFileExist(String path) {
        File f = new File(path);
        return f.exists() && !f.isDirectory();
    }

    /**
     * Opens file in the given path, reads contacts from it and returns read collection of contacts.
     *
     * @param path        Path to the file with contacts.
     * @param separator   CSV separator.
     * @param handleError Errors handler. Will be executed with error message.
     *                    If null, errors will be ignored.
     * @return Read collection of contacts. If file in the passed path does not exist or is a directory,
     * empty collection will be returned.
     * @throws NullPointerException If path and/or separator are/is null.
     * @see Contact#parseFromCSV(String, String)
     */
    public static Collection<Contact> importContacts(String path, String separator,
                                                     Consumer<String> handleError) {
        Collection<Contact> contacts = new ArrayList<>();
        // Check if file exists.
        if (!doesFileExist(path)) {
            return contacts;
        }

        // Read contacts.
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String s;
            while ((s = br.readLine()) != null) {
                Contact newContact = parseContact(s, separator, handleError);
                if (newContact != null) {
                    contacts.add(newContact);
                }
            }
        } catch (IOException e) {
            if (handleError != null) {
                handleError.accept("Error occurred. Cannot import contacts." +
                        "\n\nMessage: " + e.getMessage() + ".");
            }
        }

        return contacts;
    }

    /**
     * Parses passed CSV line into contact.
     *
     * @param contactCSV  CSV string contact representation.
     * @param separator   CSV separator.
     * @param handleError Errors handler. Will be executed with error message.
     *                    If null, errors will be ignored.
     * @return Parsed contact. If string is incorrect null will be returned.
     * @throws NullPointerException If contactCSV and/or separator are/is null.
     * @see Contact#parseFromCSV(String, String)
     */
    private static Contact parseContact(String contactCSV, String separator, Consumer<String> handleError) {
        Contact contact;
        String baseErrorMsg = "Cannot create contact from line:\n\t" + contactCSV + "\n\nProblem: ";

        try {
            contact = Contact.parseFromCSV(contactCSV, separator).build();

            if (contact == null && handleError != null) {
                handleError.accept(baseErrorMsg + "not all of the required contact's parameters are provided.");
            }

            return contact;
        } catch (IndexOutOfBoundsException e) {
            if (handleError != null) {
                handleError.accept(baseErrorMsg + "invalid string, not enough arguments." +
                        "\nMessage: " + e.getMessage() + ".");
            }
        } catch (DateTimeParseException e) {
            if (handleError != null) {
                handleError.accept(baseErrorMsg + "invalid date: " + e.getParsedString() + "." +
                        "\nMessage: " + e.getMessage() + ".");
            }
        }

        return null;
    }

    /**
     * Writes collection of contacts to file in the passed path.
     * Contacts will be written in CSV format with passed separator.
     *
     * @param contacts    Collection of contacts to export.
     * @param path        Path to output file.
     * @param separator   CSV separator.
     * @param handleError Errors handler. Will be executed with error message.
     *                    If null, errors will be ignored.
     * @throws NullPointerException If contacts collection and/or path and/or separator are/is null.
     * @see Contact#toCSVString(String)
     */
    public static void exportContacts(Collection<Contact> contacts, String path,
                                      String separator, Consumer<String> handleError) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (var contact : contacts) {
                bw.write(contact.toCSVString(separator));
                bw.write('\n');
            }
        } catch (IOException e) {
            if (handleError != null) {
                handleError.accept("Error occurred. Cannot export contacts." +
                        "\nMessage: " + e.getMessage() + ".");
            }
        }
    }

    /**
     * From contacts in the passed collection selects ones that match the key.
     *
     * @param contacts Collection of contacts to filter.
     * @param key      Filter key.
     * @return Filtered collection i.e collection of the elements that match the key.
     * @throws NullPointerException If contacts collection is null.
     */
    public static Collection<Contact> filterContacts(Collection<Contact> contacts, String key) {
        return contacts.stream().filter(contact -> {
            if (key == null || key.length() == 0) {
                return true;
            }

            if (contact == null) {
                return false;
            }

            String[] keys = key.toLowerCase().split(" +");
            String from = contact.toCSVString(" ").toLowerCase();

            for (var k : keys) {
                if (!from.contains(k)) {
                    return false;
                }
            }

            return true;
        }).collect(Collectors.toList());
    }
}
