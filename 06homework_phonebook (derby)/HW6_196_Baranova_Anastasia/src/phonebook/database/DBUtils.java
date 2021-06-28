package phonebook.database;

import phonebooklib.Contact;

import java.sql.*;
import java.time.LocalDate;

/**
 * Class provides static utility methods for working with database.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
public class DBUtils {
    /**
     * Parses contact from the request result.
     *
     * @param result Request result containing information about the contact.
     * @return Parsed contact.
     */
    public static Contact parseContact(ResultSet result) {
        try {
            var id = result.getString("id");
            var name = result.getString("name");
            var surname = result.getString("surname");
            var patronymic = result.getString("patronymic");
            var homePhone = result.getString("homePhone");
            var mobilePhone = result.getString("mobilePhone");
            var birthDate = result.getString("birthDate");
            var address = result.getString("address");
            var notes = result.getString("notes");

            return Contact.getBuilder()
                    .setId(Integer.parseInt(id))
                    .setName(name)
                    .setSurname(surname)
                    .setPatronymic(patronymic)
                    .setHomePhone(homePhone)
                    .setMobilePhone(mobilePhone)
                    .setBirthDate(birthDate == null ? null : LocalDate.parse(birthDate))
                    .setAddress(address)
                    .setNotes(notes)
                    .build();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Fills PreparedStatement instance with contact's data in the order:
     * surname, name, patronymic, home phone, mobile phone, date of birth, address, notes.
     *
     * @param statement PreparedStatement to fill.
     * @param contact   Contact to insert into PreparedStatement.
     * @return Resulting PreparedStatement.
     * @throws SQLException If number of parameters in statement is different from the required (8) or
     *                      if a database access error occurs or this method is called on a closed PreparedStatement.
     */
    public static PreparedStatement fillStatement(PreparedStatement statement, Contact contact) throws SQLException {
        statement.setString(1, contact.getSurname());
        statement.setString(2, contact.getName());
        statement.setString(3, changeNullToEmpty(contact.getPatronymic()));
        statement.setString(4, changeNullToEmpty(contact.getHomePhone()));
        statement.setString(5, changeNullToEmpty(contact.getMobilePhone()));
        statement.setString(6, contact.getBirthDate() != null ? contact.getBirthDate().toString() : null);
        statement.setString(7, changeNullToEmpty(contact.getAddress()));
        statement.setString(8, changeNullToEmpty(contact.getNotes()));

        return statement;
    }

    /**
     * Returns empty string if the passed one is null or the passed string itself if it is not.
     */
    public static String changeNullToEmpty(String str) {
        return str == null ? "" : str;
    }

    /**
     * Creates an sql request string that selects from the table elements that matches the passed key.
     * <br/>
     * Filter: if all of the words contained in the key are present in some of the element's fields -
     * element will be selected. Otherwise, it will not.
     *
     * @param key       Filter key.
     * @param tableName Name of the data table.
     * @param fields    Fields in the data table.
     */
    public static String createFilterRequest(String key, String tableName, String... fields) {
        StringBuilder request = new StringBuilder("SELECT * FROM " + tableName);
        if (key == null || key.isBlank()) {
            return request.toString();
        }

        request.append(" WHERE ");

        String[] keys = key.trim().toLowerCase().split(" +");

        for (int j = 0; j < keys.length; j++) {
            var k = keys[j];
            request.append("(");
            for (int i = 0; i < fields.length; i++) {
                request.append(fields[i]).append(" LIKE '%").append(k).append("%' ");
                if (i == fields.length - 1) {
                    request.append(") ");
                } else {
                    request.append("OR ");
                }
            }

            if (j != keys.length - 1) {
                request.append(" AND ");
            }
        }

        return request.toString();
    }
}
