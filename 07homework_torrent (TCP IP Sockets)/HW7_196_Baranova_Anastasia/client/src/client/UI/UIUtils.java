package client.UI;

import client.UI.download.DownloadDialogController;
import client.UI.download.progress.ProgressTrackerController;
import client.UI.porthost.PortHostPickerController;
import client.lib.PrimitiveFile;
import client.lib.request.DownloadRequest;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

/**
 * Class provides static utility methods for working with UI.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
public class UIUtils {
    /**
     * Shows alert dialog with passed title, message and buttons.
     *
     * @param title   Dialog title.
     * @param message Dialog message.
     * @param buttons Dialog buttons.
     * @return Pressed button.
     * @throws NullPointerException If buttons is null
     */
    public static Optional<ButtonType> showAlert(String title, String message, String... buttons) {
        ButtonType[] buttonTypes = new ButtonType[buttons.length];
        for (int i = 0; i < buttons.length; i++) {
            buttonTypes[i] = new ButtonType(buttons[i]);
        }

        Alert a = new Alert(Alert.AlertType.NONE, message, buttonTypes);
        a.setTitle(title);
        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        a.setResizable(true);
        return a.showAndWait();
    }

    /**
     * Opens a window to select host and port.
     *
     * @return Socket to connect to the serer.
     */
    public static Socket openWindowForSocket() {
        URL url = UIUtils.class.getResource("porthost/porthost.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        Parent root = tryLoad(fxmlLoader);
        if (root == null) {
            return null;
        }

        Stage stage = new Stage();
        setStageSettings(stage, root);
        stage.showAndWait();

        return ((PortHostPickerController) fxmlLoader.getController()).getSocket();
    }

    /**
     * Opens a window to select path for download.
     *
     * @param file File that will be downloaded to selecting path.
     * @return Selected path.
     * @throws NullPointerException If file is null.
     */
    public static String openDownloadFormForPath(PrimitiveFile file) {
        Objects.requireNonNull(file, "File was null.");
        URL url = UIUtils.class.getResource("download/downloadDialog.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        Parent root = tryLoad(fxmlLoader);
        if (root == null) {
            return null;
        }

        var controller = ((DownloadDialogController) fxmlLoader.getController());
        controller.setFile(file);

        Stage stage = new Stage();
        setStageSettings(stage, root);
        stage.showAndWait();

        return controller.getPath();
    }

    /**
     * Prepares a progress tracker to track file downloading process.
     *
     * @param file       Downloading file.
     * @param request    Download request.
     * @param onFinished Action that will be run when file is downloaded.
     * @return Stage with progress tracker.
     * @throws NullPointerException If file or/and request are null.
     */
    public static Stage prepareProgressTracker(PrimitiveFile file, DownloadRequest request, Runnable onFinished) {
        Objects.requireNonNull(file, "File was null.");
        Objects.requireNonNull(request, "Request was null.");

        URL url = UIUtils.class.getResource("download/progress/progressTracker.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        Parent root = tryLoad(fxmlLoader);
        if (root == null) {
            return null;
        }

        var controller = ((ProgressTrackerController) fxmlLoader.getController());
        controller.setFile(file);
        controller.setOnFinished(onFinished);
        request.setProgressTracker(controller::update);

        Stage stage = new Stage();
        setStageSettings(stage, root);
        stage.setOnCloseRequest(Event::consume);

        return stage;
    }

    /**
     * Prepares stage before showing.
     */
    private static void setStageSettings(Stage stage, Parent root) {
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setFullScreen(false);
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.setTitle("Simple Torrent");
    }

    /**
     * Tries to load form.
     */
    private static Parent tryLoad(FXMLLoader fxmlLoader) {
        try {
            return fxmlLoader.load();
        } catch (IOException e) {
            UIUtils.showAlert("Error", "Error occurred. Unable to load form.\n\nPlease, try again.", "Cancel");
            e.printStackTrace();
        }
        return null;
    }
}
