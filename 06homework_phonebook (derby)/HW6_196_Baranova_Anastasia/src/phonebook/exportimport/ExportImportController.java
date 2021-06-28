package phonebook.exportimport;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import phonebook.UIUtils;
import phonebook.Utils;
import phonebook.WindowWithResult;
import phonebooklib.Contact;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Export and import forms controller.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
public class ExportImportController implements Initializable, WindowWithResult {
    @FXML
    private TextField pathField;

    @FXML
    private Button browseButton;

    /**
     * List of contact (imported or to export).
     */
    private final ObservableList<Contact> contacts = FXCollections.observableArrayList();

    /**
     * File chooser instance for selecting files for exporting to or for importing from.
     */
    private final FileChooser fileChooser = new FileChooser();

    /**
     * Sets contacts for exporting.
     *
     * @param initials Contacts to export.
     */
    @Override
    public void setInitials(Object[] initials) {
        for (var c : initials) {
            if (c == null) {
                continue;
            }
            contacts.add((Contact) c);
        }
    }

    /**
     * Returns list of result list of contacts.
     */
    @Override
    public Object[] getResult() {
        return contacts.toArray();
    }

    @FXML
    private void importContacts() {
        if (pathField.getText() != null && !pathField.getText().isBlank()) {
            contacts.addAll(Utils.importContacts(pathField.getText(), ";",
                    message -> UIUtils.showAlert("Error",
                            message, "ОК")));
            ((Stage) pathField.getScene().getWindow()).close();
            return;
        }
        UIUtils.showAlert("Error", "Please, provide file path.", "OK");
    }

    @FXML
    private void exportContacts() {
        if (pathField.getText() != null && !pathField.getText().isBlank()) {
            Utils.exportContacts(contacts, pathField.getText(), ";",
                    message -> UIUtils.showAlert("Error", message, "OK"));
            ((Stage) pathField.getScene().getWindow()).close();
            return;
        }
        UIUtils.showAlert("Error", "Please, provide file path.", "OK");
    }

    @FXML
    private void openFileBrowser() {
        var file = fileChooser.showOpenDialog(browseButton.getScene().getWindow());
        if (file != null)
            pathField.setText(file.getAbsolutePath());
    }

    @FXML
    private void cancel() {
        ((Stage) pathField.getScene().getWindow()).close();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv")
        );
    }
}
