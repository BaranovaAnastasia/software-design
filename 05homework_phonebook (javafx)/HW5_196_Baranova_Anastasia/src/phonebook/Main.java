package phonebook;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("PhoneBook");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.setMinHeight(440);
        primaryStage.setMinWidth(704);

        primaryStage.setOnCloseRequest(windowEvent -> {
            ((Controller) loader.getController()).exit();
            windowEvent.consume();
        });

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
