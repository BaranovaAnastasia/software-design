package phonebook.contacteditor;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import phonebook.UIUtils;
import phonebook.WindowWithResult;
import phonebooklib.Contact;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Contact creator and editor form controller.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
public class ContactFormController implements Initializable, WindowWithResult {
    @FXML
    private TextField name;

    @FXML
    private TextField surname;

    @FXML
    private TextField patronymic;

    @FXML
    private TextField homePhone;

    @FXML
    private TextField mobilePhone;

    @FXML
    private TextField address;

    @FXML
    private DatePicker birthDate;

    @FXML
    private TextArea notes;

    @FXML
    private Button add;

    @FXML
    private Button cancel;

    @FXML
    private Label numberWarn;
    @FXML
    private Text requiredHint;


    /**
     * Contact builder.
     */
    private Contact.Builder builder = Contact.getBuilder();
    /**
     * Processing contact. Contact to edit or contact to add.
     */
    private Contact contact = null;

    /**
     * Returns contact after editing or created new contact.
     */
    @Override
    public Contact getResult() {
        return contact;
    }

    /**
     * Sets an initial contact to edit.
     *
     * @param initials A contact to work with.
     * @throws IndexOutOfBoundsException If no contacts passed.
     * @throws IndexOutOfBoundsException If no contacts passed.
     * @throws NullPointerException      if null is passed.
     */
    @Override
    public void setInitials(Object[] initials) {
        this.contact = (Contact) initials[0];

        builder = Contact.getBuilder()
                .setId(contact.getId())
                .setName(contact.getName())
                .setSurname(contact.getSurname())
                .setPatronymic(contact.getPatronymic())
                .setHomePhone(contact.getHomePhone())
                .setMobilePhone(contact.getMobilePhone())
                .setBirthDate(contact.getBirthDate())
                .setAddress(contact.getAddress())
                .setNotes(contact.getNotes());

        name.setText(contact.getName());
        surname.setText(contact.getSurname());
        patronymic.setText(contact.getPatronymic());
        homePhone.setText(contact.getHomePhone());
        mobilePhone.setText(contact.getMobilePhone());
        birthDate.setValue(contact.getBirthDate());
        address.setText(contact.getAddress());
        notes.setText(contact.getNotes());
    }

    /**
     * Collects data from form elements and builds contact.
     */
    private void build() {
        builder
                .setName(name.getText())
                .setSurname(surname.getText())
                .setPatronymic(patronymic.getText())
                .setHomePhone(homePhone.getText())
                .setMobilePhone(mobilePhone.getText())
                .setBirthDate(birthDate.getValue())
                .setAddress(address.getText())
                .setNotes(notes.getText());
    }

    /**
     * Checks value in TextField to be not null and not blank.
     * Sets warning style to TextField if value is not valid.
     *
     * @return True, if value in TextField is valid, false otherwise.
     */
    private boolean checkFieldAndSetWarningStyle(TextField textField) {
        if (textField.getText() == null || textField.getText().isBlank()) {
            if (!textField.getStyleClass().contains("warning")) {
                textField.getStyleClass().add("warning");
            }
            return false;
        }
        return true;
    }

    /**
     * Checks value in TextFields for phone numbers to be not null and not blank
     * (at least one of them must be not null and not blank).
     * Sets warning style to TextFields for phone numbers if values are not valid.
     *
     * @return True, if values are valid, false otherwise.
     */
    private boolean checkPhoneNumbersAndSetWarningStyle() {
        if ((homePhone.getText() == null || homePhone.getText().isBlank())
                && (mobilePhone.getText() == null || mobilePhone.getText().isBlank())) {
            if (!homePhone.getStyleClass().contains("warning")) {
                homePhone.getStyleClass().add("warning");
            }
            if (!mobilePhone.getStyleClass().contains("warning")) {
                mobilePhone.getStyleClass().add("warning");
            }
            numberWarn.setVisible(true);
            return false;
        }
        return true;
    }


    /**
     * Checks values in fields. Checks that last name, first name, home and mobile phones are provided.
     *
     * @return True, if all of the required values are provided, false otherwise.
     */
    private boolean checkFieldsAndSetWarningStyle() {
        boolean result = checkFieldAndSetWarningStyle(name);
        result &= checkFieldAndSetWarningStyle(surname);
        result &= checkPhoneNumbersAndSetWarningStyle();

        if (!result) {
            requiredHint.setStyle("-fx-fill: red");
        }

        return result;
    }

    /**
     * Removes warning style from element on the form on mouse click.
     */
    @FXML
    private void removeWarning(MouseEvent event) {
        requiredHint.setStyle("-fx-fill: black");
        if (event.getSource().equals(homePhone) || event.getSource().equals(mobilePhone)) {
            homePhone.getStyleClass().remove("warning");
            mobilePhone.getStyleClass().remove("warning");
            numberWarn.setVisible(false);
            return;
        }
        ((TextField) event.getSource()).getStyleClass().remove("warning");
    }

    /**
     * Builds contact with data from fields.
     */
    @FXML
    private void apply() {
        if (!checkFieldsAndSetWarningStyle()) {
            UIUtils.showAlert("Invalid contact data",
                    "Not all of the required fields are filled in.\n\n" +
                            "Please, make sure that first name, last name and at least one of phone numbers are provided",
                    "OK");
            return;
        }
        build();
        Contact contact = builder.build();
        if (contact != null) {
            this.contact = contact;
            Stage stage = (Stage) add.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * Cancels window. Asks for confirmation before.
     */
    @FXML
    private void cancel() {
        UIUtils.showAlert("Window closing", "Discard changes?", "Yes", "No")
                .ifPresent(response -> {
                    if (response.getText().equals("Yes")) {
                        Stage stage = (Stage) cancel.getScene().getWindow();
                        stage.close();
                    }
                });
    }

    /**
     * Prevents from any symbols except for letters, spaces, apostrophe and dash to be present in TextField text.
     */
    private void nameValidate(TextField textField, String value) {
        if (value != null && !value.matches("^[\\p{L} '-]+$")) {
            textField.setText(value.replaceAll("[^[\\p{L} '-]+$]", ""));
        }
    }

    /**
     * Prevents from any symbols except for digits to be present in TextField text.
     */
    private void numberValidate(TextField textField, String value) {
        if (value != null && !value.matches("\\d*")) {
            textField.setText(value.replaceAll("[^\\d]", ""));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name.textProperty().addListener((observable, oldValue, newValue) -> nameValidate(name, newValue));

        surname.textProperty().addListener((observable, oldValue, newValue) -> nameValidate(surname, newValue));

        patronymic.textProperty().addListener((observable, oldValue, newValue) -> nameValidate(patronymic, newValue));

        homePhone.textProperty().addListener((observable, oldValue, newValue) -> {
            numberWarn.setVisible(false);
            numberValidate(homePhone, newValue);
        });
        mobilePhone.textProperty().addListener((observable, oldValue, newValue) -> {
            numberWarn.setVisible(false);
            numberValidate(mobilePhone, newValue);
        });

        address.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("[^;]*")) {
                address.setText(newValue.replaceAll("[;]*", ""));
            }
        });
        notes.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("[^;]*")) {
                notes.setText(newValue.replaceAll("[;]*", ""));
            }
        });
    }
}
