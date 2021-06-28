package phonebook.conflictsolver;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;
import phonebook.UIUtils;
import phonebook.WindowWithResult;
import phonebooklib.Contact;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

/**
 * Conflict solver window controller.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
public class ConflictController implements Initializable, WindowWithResult {

    @FXML
    private Label nameOld;

    @FXML
    private Label surnameOld;

    @FXML
    private Label patronymicOld;

    @FXML
    private Label homeOld;

    @FXML
    private RadioButton homeOldButton;

    @FXML
    private Label mobileNew;

    @FXML
    private RadioButton mobileOldButton;

    @FXML
    private Label birthDateOld;

    @FXML
    private RadioButton birthOldButton;

    @FXML
    private Label addressOld;

    @FXML
    private RadioButton addressOldButton;

    @FXML
    private Label notesOld;

    @FXML
    private RadioButton notesOldButton;

    @FXML
    private Label nameNew;

    @FXML
    private Label surnameNew;

    @FXML
    private Label patronymicNew;

    @FXML
    private Label homeNew;

    @FXML
    private RadioButton homeNewButton;

    @FXML
    private Label mobileOld;

    @FXML
    private RadioButton mobileNewButton;

    @FXML
    private Label birthDateNew;

    @FXML
    private RadioButton birthNewButton;

    @FXML
    private Label addressNew;

    @FXML
    private RadioButton addressNewButton;

    @FXML
    private Label notesNew;

    @FXML
    private RadioButton notesNewButton;

    @FXML
    private Button solve;

    /**
     * Result contact after all conflicts are solved.
     */
    private Contact result = null;

    /**
     * Sets initial 2 contacts that are conflicting.
     *
     * @param initials 2 conflicting contacts.
     * @throws IndexOutOfBoundsException If not enough arguments are passed.
     * @throws NullPointerException      If null is passed.
     */
    @Override
    public void setInitials(Object[] initials) {
        fillFields((Contact) initials[0], nameOld, surnameOld, patronymicOld, homeOld, mobileOld, birthDateOld, addressOld, notesOld);

        fillFields((Contact) initials[1], nameNew, surnameNew, patronymicNew, homeNew, mobileNew, birthDateNew, addressNew, notesNew);

        findConflicts((Contact) initials[0], (Contact) initials[1]);
    }

    /**
     * Returns result contact after all conflicts are solved.
     */
    @Override
    public Contact getResult() {
        return result;
    }

    /**
     * Fills passed labels with contacts data.
     *
     * @param contact Contact fill labels with.
     * @param fields  8 labels to fill.
     * @throws IndexOutOfBoundsException If not enough labels passed.
     * @throws NullPointerException      If null is passed.
     */
    private void fillFields(Contact contact, Label... fields) {
        fields[0].setText(contact.getName());
        fields[1].setText(contact.getSurname());
        fields[2].setText(contact.getPatronymic());
        fields[3].setText(contact.getHomePhone());
        fields[4].setText(contact.getMobilePhone());
        fields[5].setText(contact.getBirthDate() == null ? "" : contact.getBirthDate().toString());
        fields[6].setText(contact.getAddress());
        fields[7].setText(contact.getNotes());
    }

    /**
     * Finds conflicting fields in two passed contacts and prepares window for solving these conflicts.
     *
     * @throws NullPointerException If any of passed contacts is null.
     */
    private void findConflicts(Contact one, Contact two) {
        homeOldButton.setVisible(!Objects.equals(one.getHomePhone(), two.getHomePhone()));
        homeNewButton.setVisible(!Objects.equals(one.getHomePhone(), two.getHomePhone()));

        mobileOldButton.setVisible(!Objects.equals(one.getMobilePhone(), two.getMobilePhone()));
        mobileNewButton.setVisible(!Objects.equals(one.getMobilePhone(), two.getMobilePhone()));

        birthOldButton.setVisible(!Objects.equals(one.getBirthDate(), two.getBirthDate()));
        birthNewButton.setVisible(!Objects.equals(one.getBirthDate(), two.getBirthDate()));

        addressOldButton.setVisible(!Objects.equals(one.getAddress(), two.getAddress()));
        addressNewButton.setVisible(!Objects.equals(one.getAddress(), two.getAddress()));

        notesOldButton.setVisible(!Objects.equals(one.getNotes(), two.getNotes()));
        notesNewButton.setVisible(!Objects.equals(one.getNotes(), two.getNotes()));
    }

    /**
     * Checks if all conflicts are solved.
     */
    private boolean isSolved() {
        return (!homeOldButton.isVisible() || homeOldButton.isSelected() || homeNewButton.isSelected())
                && (!mobileOldButton.isVisible() || mobileOldButton.isSelected() || mobileNewButton.isSelected())
                && (!birthOldButton.isVisible() || birthOldButton.isSelected() || birthNewButton.isSelected())
                && (!addressOldButton.isVisible() || addressOldButton.isSelected() || addressNewButton.isSelected())
                && (!notesOldButton.isVisible() || notesOldButton.isSelected() || notesNewButton.isSelected());
    }

    /**
     * Determines selected date.
     */
    private LocalDate getDate() {
        if (!birthOldButton.isVisible() || !birthOldButton.isSelected() && !birthNewButton.isSelected()) {
            return null;
        }

        if (birthOldButton.isSelected()) {
            return birthDateOld.getText() == null || birthDateOld.getText().isBlank()
                    ? null : LocalDate.parse(birthDateOld.getText());
        }

        return birthDateNew.getText() == null || birthDateNew.getText().isBlank()
                ? null : LocalDate.parse(birthDateNew.getText());
    }

    /**
     * If conflicts are solved, builds and saves result contact. Closes window if conflicts are solved successfully.
     */
    @FXML
    private void solve() {
        if (!isSolved()) {
            UIUtils.showAlert("Conflict not solved", "Conflict has not been solved yet. " +
                    "Please, select preferred value for each field.", "OK");
            return;
        }

        Contact pre = Contact.getBuilder()
                .setName(nameOld.getText())
                .setSurname(surnameOld.getText())
                .setPatronymic(patronymicOld.getText())

                .setHomePhone(homeOldButton.isSelected() ? homeOld.getText() : homeNew.getText())
                .setMobilePhone(mobileOldButton.isSelected() ? mobileOld.getText() : mobileNew.getText())
                .setBirthDate(getDate())
                .setAddress(addressOldButton.isSelected() ? addressOld.getText() : addressNew.getText())
                .setNotes(notesOldButton.isSelected() ? notesOld.getText() : notesNew.getText())
                .build();

        if (pre == null) {
            UIUtils.showAlert("Conflict solved incorrectly", "Probably, selected options resulted in invalid contact.\n\n" +
                            "Please, make sure that first name, last name and at least one of phone numbers are provided (i.e not blank).",
                    "OK");
            return;
        }

        result = pre;
        ((Stage) solve.getScene().getWindow()).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
