package client.UI.download;

import client.UI.UIUtils;
import client.lib.PrimitiveFile;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller of the window for selecting path for download.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
public class DownloadDialogController implements Initializable {

    @FXML
    private TextField pathField;

    @FXML
    private Button download;

    @FXML
    private TextField fileName;

    @FXML
    private TextField fileSize;

    @FXML
    private AnchorPane pane;


    private String path;

    private final FileChooser fileChooser = new FileChooser();

    /**
     * Returns selected path.
     */
    public String getPath() {
        return path;
    }

    /**
     * Fills TextFields with information about the file.
     */
    public void setFile(PrimitiveFile file) {
        Objects.requireNonNull(file);

        path = System.getProperty("user.dir") + System.getProperty("file.separator") + file.getName();

        fileName.setText(file.getName());
        fileSize.setText(file.getSizeReadable());
        pathField.setText(path);

        String extension = file.getName().substring(file.getName().lastIndexOf('.'));
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter(extension + " files", "*" + extension);
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(extFilter);
    }

    /**
     * Opens file chooser for choosing the file.
     */
    @FXML
    private void openFileBrowser() {
        var file = fileChooser.showSaveDialog(download.getScene().getWindow());
        if (file != null) {
            pathField.setText(file.getAbsolutePath());
        }
    }

    /**
     * Closes the window (path not selected).
     */
    @FXML
    void cancel() {
        path = null;
        Stage stage = (Stage) download.getScene().getWindow();
        stage.close();
    }

    /**
     * Closes the window (path selected).
     */
    @FXML
    void selected() {
        path = pathField.getText();
        if (path.isBlank()) {
            UIUtils.showAlert("Invalid path", "Please, select a path.", "Cancel");
            return;
        }
        Stage stage = (Stage) download.getScene().getWindow();
        stage.close();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> pane.requestFocus());
    }
}
