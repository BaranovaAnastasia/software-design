package phonebook;

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
import java.net.URL;
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
     * Sets initial data for work in window, opens window, waits for result and returns the result of work in window.
     * Only works with windows controlled by controllers that implement WindowWithResult interface.
     *
     * @param url                      URL of window fxml resource.
     * @param canBeClosedWithoutResult true, if window can be manually closed before result is ready, false otherwise.
     * @param initialData              Initial data for window's work.
     */
    public static Object openWindowForResult(URL url, boolean canBeClosedWithoutResult, Object[] initialData) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            Parent root = fxmlLoader.load();

            if (initialData != null && initialData.length > 0) {
                ((WindowWithResult) fxmlLoader.getController()).setInitials(initialData);
            }

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setFullScreen(false);
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            if (!canBeClosedWithoutResult) {
                stage.setOnCloseRequest(Event::consume);
            }
            stage.showAndWait();

            return ((WindowWithResult) fxmlLoader.getController()).getResult();
        } catch (IOException e) {
            UIUtils.showAlert("Error", "Error occurred. Unable to load form.\n\nPlease, try again.", "OK");
        }

        return null;
    }
}
