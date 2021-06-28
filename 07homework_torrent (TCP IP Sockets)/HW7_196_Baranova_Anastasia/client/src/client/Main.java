package client;

import client.UI.Controller;
import client.UI.UIUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.Socket;

/**
 * Main class of the client app.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
public class Main extends Application {

    /**
     * Starts the application. Opens a window for selecting host and port and then opens main window.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("UI/main.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Simple Torrent");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.setMinHeight(440);
        primaryStage.setMinWidth(704);

        primaryStage.setOnCloseRequest(windowEvent -> {
            ((Controller) fxmlLoader.getController()).exit();
            windowEvent.consume();
        });

        Socket socket = UIUtils.openWindowForSocket();

        if (socket == null) {
            return;
        }

        ((Controller) fxmlLoader.getController()).setSocket(socket);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
