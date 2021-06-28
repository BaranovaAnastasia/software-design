package phonebook.database;

import phonebooklib.Contact;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Class representing a phonebook database.
 * Uses Apache Derby database.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
public class PhonebookDatabase {
    /**
     * Contacts database name.
     */
    private final String databaseName;
    /**
     * Contacts table name.
     */
    private final String tableName;
    /**
     * Connection to database.
     */
    private Connection connection;

    /**
     * Shows if connection to the database is established.
     */
    private boolean connected;

    /**
     * Returns true if connection to the database is established, false otherwise.
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Creates new PhonebookDatabase instance to work with default
     * database 'phonebook' and 'contacts' table.
     *
     * @see PhonebookDatabase#PhonebookDatabase(String, String)
     */
    public PhonebookDatabase() {
        this("phonebook", "contacts");
    }

    /**
     * Creates new PhonebookDatabase instance.
     * Creates (if needed) and connects to the database with the passed name
     * and creates (if needed) data table with the passed name.
     *
     * @param databaseName Name of the database.
     * @param tableName    Name of the data table.
     * @see PhonebookDatabase#createTable()
     */
    public PhonebookDatabase(String databaseName, String tableName) {
        this.databaseName = databaseName;
        this.tableName = tableName;

        String url = "jdbc:derby:" + this.databaseName + ";create=true";

        try {
            connection = DriverManager.getConnection(url);
            createTable();

            connected = true;
        } catch (SQLException e) {
            System.err.println("Cannot connect to " + databaseName + " database. Error: " + e.getMessage());
            e.printStackTrace();
            connected = false;
        }
    }

    /**
     * Tries to create data table.
     */
    private void createTable() {
        try (var statement = connection.createStatement()) {
            //statement.executeUpdate("DROP TABLE " + tableName);
            statement.executeUpdate("CREATE TABLE " + this.tableName
                    + "(id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                    + "surname VARCHAR(35) NOT NULL,"
                    + "name VARCHAR(35) NOT NULL,"
                    + "patronymic VARCHAR(35),"
                    + "homePhone VARCHAR(15),"
                    + "mobilePhone VARCHAR(15),"
                    + "birthDate VARCHAR(10),"
                    + "address VARCHAR(100),"
                    + "notes VARCHAR(300))");

            System.out.println("Table created.");
        } catch (SQLException e) {
            if (!e.getSQLState().equals("X0Y32")) {
                System.err.println("Cannot create table. Error: " + e.getMessage());
            }
        }
    }

    /**
     * Collects all the contacts stored in the data table and returns a collection of found contacts.
     *
     * @return Collection of found contacts. If it is not possible to read
     * contacts empty collection will be returned.
     */
    public Collection<Contact> importContacts() {
        if (!connected) {
            System.err.println("Cannot import contacts. Connection not established.");
            return new ArrayList<>();
        }

        Collection<Contact> contacts = new ArrayList<>();

        try (var statement = connection.createStatement()) {
            var result = statement.executeQuery("SELECT * FROM " + tableName);

            while (result.next()) {
                var contact = DBUtils.parseContact(result);
                if (contact != null) {
                    contacts.add(contact);
                }
            }

        } catch (SQLException e) {
            System.err.println("Cannot import contacts. Error: " + e.getMessage());
        }

        return contacts;
    }

    /**
     * Adds a new contacts to the data table.
     *
     * @param contact Contact to add.
     */
    public void addContact(Contact contact) {
        if (!connected) {
            System.err.println("Cannot add contact. Connection not established.");
            return;
        }

        try (var statement = connection.prepareStatement(
                "INSERT INTO " + tableName
                        + " (surname, name, patronymic, homePhone, mobilePhone, birthDate, address, notes)"
                        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
            DBUtils.fillStatement(statement, contact).executeUpdate();

            System.out.println("Contact '" + contact + "' added.");
        } catch (SQLException e) {
            System.err.println("Cannot add contact '" + contact + "' to the database.");
        }
    }

    /**
     * Changes an old contact to a new one.
     *
     * @param oldContact Contact to edit.
     * @param newContact Contact to change the old one to.
     */
    public void editContact(Contact oldContact, Contact newContact) {
        if (!connected) {
            System.err.println("Cannot edit contact. Connection not established.");
            return;
        }

        try (var statement =
                     connection.prepareStatement("UPDATE " + tableName +
                             " SET " +
                             "surname = ?, name = ?, patronymic = ?," +
                             "homePhone = ?, mobilePhone = ?," +
                             "birthDate = ?, address = ?, notes = ? " +
                             "WHERE " +
                             "id = ?")) {

            DBUtils.fillStatement(statement, newContact);
            statement.setString(9, oldContact.getId().toString());
            statement.executeUpdate();

            System.out.println("Contact edited.");
        } catch (SQLException e) {
            System.err.println("Cannot edit contact. Error: " + e.getMessage());
        }
    }

    /**
     * Removes contact from the database.
     *
     * @param contact Contact to remove.
     */
    public void deleteContact(Contact contact) {
        if (!connected) {
            System.err.println("Cannot delete contact. Connection not established.");
            return;
        }

        try (var statement
                     = connection.prepareStatement("DELETE FROM " + tableName + " WHERE id = ?")) {

            statement.setString(1, contact.getId().toString());
            statement.executeUpdate();

            System.out.println("Contact '" + contact + "' deleted.");
        } catch (SQLException e) {
            System.err.println("Cannot delete contact. Error:" + e.getMessage());
        }
    }

    /**
     * Looks for conflicts with the passed contact in the database.
     *
     * @param contact Contact to find conflicts with.
     * @return Conflicting contact from the database. Since we assume that
     * the contacts in the database do not conflict with each other,
     * only one conflicting contact can be found. If nothing found null will be returned.
     */
    public Contact findConflict(Contact contact) {
        if (!connected) {
            System.err.println("Cannot work with the database. Connection not established.");
            return null;
        }

        try (var statement = connection.prepareStatement(
                "SELECT * FROM " + tableName
                        + " WHERE name = ? AND surname = ? AND patronymic = ?")) {

            statement.setString(1, contact.getName());
            statement.setString(2, contact.getSurname());
            statement.setString(3, DBUtils.changeNullToEmpty(contact.getPatronymic()));

            var res = statement.executeQuery();
            while (res.next()) {
                var conflict = DBUtils.parseContact(res);
                if (conflict == null || (contact.getId() != null && contact.getId().equals(conflict.getId()))) {
                    continue;
                }
                return conflict;
            }
        } catch (SQLException exception) {
            System.err.println(exception.getMessage());
        }
        return null;
    }

    /**
     * Filters contacts from database according to the passed key.
     *
     * @param key Filter key.
     * @return Collection of contacts that matches the key.
     */
    public Collection<Contact> filter(String key) {
        if (!connected) {
            System.err.println("Cannot work with the database. Connection not established.");
            return new ArrayList<>();
        }

        Collection<Contact> contacts = new ArrayList<>();

        try (var statement = connection.createStatement()) {
            var result = statement.executeQuery(DBUtils.createFilterRequest(key, tableName,
                    "surname", "name", "patronymic", "homePhone", "mobilePhone", "birthDate", "address", "notes"));

            while (result.next()) {
                var contact = DBUtils.parseContact(result);
                if (contact != null) {
                    contacts.add(contact);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contacts;
    }

    /**
     * Closes the connection and shutdowns the database.
     */
    public void close() {
        connected = false;
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Cannot close connection. Error: " + e.getMessage());
        }

        try {
            DriverManager.getConnection("jdbc:derby:" + databaseName + ";shutdown=true");
        } catch (SQLException e) {
            if (e.getSQLState().equals("XJ015")) {
                System.err.println("Cannot close database. Error: " + e.getMessage());
            } else {
                System.out.println("Database " + databaseName + " closed.");
            }
        }
    }

    /**
     * Drops the table, closes the connection and shutdown the database.
     */
    public void dropAndClose() {
        connected = false;
        if(connection != null) {
            try (var statement = connection.createStatement()) {
                statement.executeUpdate("DROP TABLE " + tableName);
            } catch (SQLException e) {
                System.err.println("Cannot drop table. Error: " + e.getMessage());
            }

            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Cannot close connection. Error: " + e.getMessage());
            }
        }

        try {
            DriverManager.getConnection("jdbc:derby:" + databaseName + ";shutdown=true");
        } catch (SQLException e) {
            if (e.getSQLState().equals("XJ015")) {
                System.err.println("Cannot close database. Error: " + e.getMessage());
            } else {
                System.out.println("Database " + databaseName + " closed.");
            }
        }
    }
}
