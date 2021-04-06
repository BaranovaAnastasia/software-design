package phonebook;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import phonebooklib.Contact;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Main window controller.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
public class Controller implements Initializable {

    @FXML
    private TableView<Contact> table;

    @FXML
    private TableColumn<Contact, String> name;

    @FXML
    private TableColumn<Contact, String> surname;

    @FXML
    private TableColumn<Contact, String> patronymic;

    @FXML
    private TableColumn<Contact, String> homePhone;

    @FXML
    private TableColumn<Contact, String> mobilePhone;

    @FXML
    private TableColumn<Contact, LocalDate> birthDate;

    @FXML
    private TableColumn<Contact, String> address;

    @FXML
    private TableColumn<Contact, String> notes;

    @FXML
    private MenuItem deleteMenu;

    @FXML
    private MenuItem editMenu;

    @FXML
    private Button edit;

    @FXML
    private Button delete;

    @FXML
    private TextField search;


    /**
     * List of registered contacts.
     */
    private final ObservableList<Contact> contacts = FXCollections.observableArrayList();

    /**
     * Looks for conflicts in existing list of contacts with passed contact.
     * Opens conflict solver window and solves the conflict.
     *
     * @param contact Contact to solve conflict with.
     * @param except  Contact with which conflicts are allowed (e.g. when editing,
     *                old version does not conflict with the new one since it will be replaced).
     * @return Result contact.
     * @throws NullPointerException If contact is null.
     */
    private Contact solveConflict(Contact contact, Contact except) {
        var conflict = Utils.findConflict(contact, contacts);
        if (conflict != null && !conflict.equals(except)) {
            if (contact.equalsAbsolute(conflict)) {
                contacts.remove(conflict);
                return contact;
            }

            Contact result = (Contact) UIUtils.openWindowForResult(
                    getClass().getResource("conflictsolver/conflictForm.fxml"),
                    false, new Object[]{conflict, contact});
            contacts.remove(conflict);

            return result;
        }

        return contact;
    }

    /**
     * For each passed contact solves conflicts and adds result to table.
     *
     * @throws NullPointerException If null is passed.
     */
    private void solveConflictsAndAdd(Object[] newContacts) {
        for (var c : newContacts) {
            contacts.add(solveConflict((Contact) c, null));
        }
        refreshTable();
    }

    /**
     * Opens contacts editor to edit selected in table contact.
     * Edits contact and displays result in table.
     */
    @FXML
    private void editContact() {
        var old = table.getSelectionModel().getSelectedItem();
        if (old == null) {
            return;
        }

        Contact edited = (Contact) UIUtils.openWindowForResult(
                getClass().getResource("contacteditor/editForm.fxml"),
                false, new Object[]{old});
        if (edited == null) {
            return;
        }

        edited = solveConflict(edited, old);

        contacts.set(contacts.indexOf(old), edited);
        refreshTable();
        table.getSelectionModel().clearSelection();
    }

    /**
     * Opens contacts creator to crete a new contact.
     * Adds new contact to table.
     */
    @FXML
    public void addContact() {
        var newContact = UIUtils.openWindowForResult(
                getClass().getResource("contacteditor/addForm.fxml"),
                false, null);
        if (newContact == null) {
            return;
        }

        newContact = solveConflict((Contact) newContact, null);
        contacts.add((Contact) newContact);
        refreshTable();
    }

    /**
     * Deletes selected contact and removes it from table.
     */
    @FXML
    public void deleteContact() {
        var toDelete = table.getSelectionModel().getSelectedItem();

        UIUtils.showAlert("Contact deletion", "Are you sure you want to delete contact "
                + toDelete.getSurname() + " " + toDelete.getName() + "?", "Yes", "No")
                .ifPresent(response -> {
                    if (response.getText().equals("Yes")) {
                        contacts.remove(toDelete);
                        refreshTable();
                    }
                });

        table.getSelectionModel().clearSelection();
    }

    /**
     * Opens import window and imports contacts to table.
     * Solves all of occurred conflicts.
     */
    @FXML
    private void importContacts() {
        var newContacts = (Object[]) UIUtils.openWindowForResult(
                getClass().getResource("exportimport/importForm.fxml"),
                true, null);
        if (newContacts == null || newContacts.length == 0) {
            return;
        }

        UIUtils.showAlert("Contacts import", "Do you want to overwrite your contacts " +
                "list or add new contacts from file to the table? Overwritten contacts will be lost.", "Overwrite", "Add")
                .ifPresent(response -> {
                    if (response.getText().equals("Overwrite")) {
                        contacts.clear();
                    }
                });

        solveConflictsAndAdd(newContacts);
    }

    /**
     * Opens export window to export contacts.
     */
    @FXML
    private void exportContacts() {
        UIUtils.openWindowForResult(getClass().getResource("exportimport/exportForm.fxml"),
                true, contacts.toArray());
    }

    /**
     * Filters contacts in table by the passed key.
     */
    private void filter(String key) {
        var filtered = FXCollections.observableArrayList(Utils.filterContacts(contacts, key));
        if (filtered.size() == contacts.size()) {
            table.setItems(contacts);
        } else {
            table.setItems(filtered);
        }
        table.refresh();
    }

    /**
     * Closes application.
     */
    @FXML
    void exit() {
        UIUtils.showAlert("Exit", "Are you sure you want to exit PhoneBook?", "Yes", "No")
                .ifPresent(response -> {
                    if (response.getText().equals("Yes")) {
                        Utils.exportContacts(contacts, "saved.csv", ";",
                                message -> UIUtils.showAlert("Error", message, "OK"));
                        ((Stage) table.getScene().getWindow()).close();
                    }
                });
    }

    /**
     * Enables buttons for editing content (editing and deleting contacts).
     */
    private void enableEditor() {
        editMenu.setDisable(false);
        edit.setDisable(false);
        deleteMenu.setDisable(false);
        delete.setDisable(false);
    }

    /**
     * Disables buttons for editing content (editing and deleting contacts).
     */
    private void disableEditor() {
        editMenu.setDisable(true);
        edit.setDisable(true);
        deleteMenu.setDisable(true);
        delete.setDisable(true);
    }

    /**
     * Shows information about the application.
     */
    @FXML
    private void showAbout() {
        UIUtils.showAlert("About",
                "Phonebook application.\n\nDeveloped by Anastasia A. Baranova as a homework for " +
                        "\"Software Design\" course.\n\nemail.: aabranova_3@edu.hse.ru\n\n\n" +
                        "Higher school of economics, faculty of Computer Science.\nMoscow, 2021",
                "OK");
    }

    /**
     * Refreshes table. If search line is not empty, re-filters contacts.
     */
    private void refreshTable() {
        if (search.getText() != null && !search.getText().isBlank()) {
            filter(search.getText());
        } else {
            table.refresh();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                enableEditor();
            } else {
                disableEditor();
            }
        });

        table.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && !edit.isFocused() && !delete.isFocused()) {
                table.getSelectionModel().clearSelection();
            }
        });

        search.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                filter(newVal);
            }
        });

        solveConflictsAndAdd(Utils.importContacts("saved.csv", ";",
                message -> UIUtils.showAlert("Error", message, "ОК")).toArray());

        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        surname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        patronymic.setCellValueFactory(new PropertyValueFactory<>("patronymic"));
        homePhone.setCellValueFactory(new PropertyValueFactory<>("homePhone"));
        mobilePhone.setCellValueFactory(new PropertyValueFactory<>("mobilePhone"));
        birthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        notes.setCellValueFactory(new PropertyValueFactory<>("notes"));

        name.setReorderable(false);
        surname.setReorderable(false);
        patronymic.setReorderable(false);
        homePhone.setReorderable(false);
        mobilePhone.setReorderable(false);
        birthDate.setReorderable(false);
        address.setReorderable(false);
        notes.setReorderable(false);

        table.setItems(contacts);
    }
}
