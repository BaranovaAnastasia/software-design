package client.UI.download.progress;

import client.lib.PrimitiveFile;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller of the window for tracking the progress of file downloading.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
public class ProgressTrackerController implements Initializable {
    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label percentage;

    @FXML
    private TextField fileName;

    @FXML
    private TextField fileSize;

    @FXML
    private AnchorPane pane;


    private PrimitiveFile file;
    private Runnable onFinished;
    private final DoubleProperty progress = new SimpleDoubleProperty(.0);

    /**
     * Sets a runnable object that will be run when download is finished.
     */
    public void setOnFinished(Runnable onFinished) {
        this.onFinished = onFinished;
    }

    /**
     * Sets a downloading file. Fills TextFields with file data.
     *
     * @throws NullPointerException if file is null.
     */
    public void setFile(PrimitiveFile file) {
        this.file = Objects.requireNonNull(file, "File was null.");

        fileName.setText(file.getName());
        fileSize.setText(file.getSizeReadable());
    }

    /**
     * Updates progress bar.
     *
     * @param downloaded Number of downloaded bytes.
     * @throws IllegalArgumentException If downloaded < 0.
     */
    public void update(long downloaded) {
        if (downloaded < 0) {
            throw new IllegalArgumentException("Number of downloaded bytes was negative");
        }

        double progress = downloaded * 1.0 / file.getSize();
        Platform.runLater(() ->
        {
            this.progress.setValue(progress);
            percentage.setText(PrimitiveFile.getSizeReadable(downloaded) + " / " + file.getSizeReadable()
                    + " (" + Math.round(progress * 100) + "%)");
            if (downloaded == file.getSize()) {
                onFinished.run();
                ((Stage) progressBar.getScene().getWindow()).close();
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> pane.requestFocus());

        progressBar.progressProperty().bind(progress);

        percentage.setText("0 B (0%)");
    }
}
