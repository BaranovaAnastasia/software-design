package client.UI.porthost;

import client.UI.UIUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller of the window for selecting port and host.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
public class PortHostPickerController implements Initializable {

    @FXML
    private BorderPane pane;

    @FXML
    private TextField host;

    @FXML
    private TextField port;

    @FXML
    private Button ok;


    private Socket socket = null;

    /**
     * Returns created socket.
     *
     * @return Socket created using selected port and host.
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Checks input port and host, tries to create socket. <br/>
     * If input data is valid and socket is created, window will bw closed.
     */
    @FXML
    private void apply() {
        boolean check = checkPort();
        check &= checkHost();
        if (!check) {
            return;
        }

        String hostValue = host.getText();
        int portValue = Integer.parseInt(port.getText());

        try {
            socket = new Socket(hostValue, portValue);
            ((Stage) ok.getScene().getWindow()).close();
        } catch (IOException e) {
            UIUtils.showAlert("Error",
                    "Cannot create socket.\nTry another host and/or port.", "Cancel");
        } catch (SecurityException e) {
            UIUtils.showAlert("Error", "Cannot connect to: " + host + ":" + port +
                    ".\nTry another host and/or port.", "Cancel");
        }
    }

    /**
     * Checks input port value. <br/>
     * Port must be an integer between 0 and 65535, inclusive.
     *
     * @return true, if provided port number is valid, false otherwise.
     */
    private boolean checkPort() {
        String portText = port.getText();

        if (portText == null || portText.isBlank()) {
            UIUtils.showAlert("Invalid port",
                    "Please, provide a valid port number.",
                    "Cancel");
            displayWarning(port);
            return false;
        }

        int portValue = Integer.parseInt(portText);
        if (portValue < 0 || portValue > 65535) {
            UIUtils.showAlert("Invalid port",
                    "Please, provide a valid port number.\nMust be between 0 and 65535, inclusive.",
                    "Cancel");
            displayWarning(port);
            return false;
        }

        return true;
    }

    /**
     * Checks input host value.
     * Host must be not empty.
     *
     * @return true, if provided host is valid, false otherwise.
     */
    private boolean checkHost() {
        String hostText = host.getText();

        if (hostText == null || hostText.isBlank()) {
            UIUtils.showAlert("Invalid host",
                    "Please, provide a valid host.",
                    "Cancel");
            displayWarning(host);
            return false;
        }

        return true;
    }

    /**
     * Highlights TextField red.
     */
    private void displayWarning(TextField textField) {
        if (!textField.getStyleClass().contains("warning")) {
            textField.getStyleClass().add("warning");
        }
    }

    /**
     * Removes red highlight on TextField.
     */
    @FXML
    private void removeWarning(MouseEvent event) {
        ((TextField) event.getSource()).getStyleClass().remove("warning");
    }

    /**
     * Allows only digits to be present in a TextField.
     */
    private void numberValidate(TextField textField, String value) {
        if (value != null && !value.matches("\\d*")) {
            textField.setText(value.replaceAll("[^\\d]", ""));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> pane.requestFocus());

        port.textProperty().addListener((observable, oldValue, newValue) -> numberValidate(port, newValue));
    }
}
