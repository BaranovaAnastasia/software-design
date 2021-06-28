package client.UI;

import client.lib.PrimitiveFile;
import client.lib.Utils;
import client.lib.communication.TorrentClient;
import client.lib.request.DownloadRequest;
import client.lib.request.FilesRequest;
import client.lib.request.FinishRequest;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller of the main window.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
public class Controller implements Initializable {
    @FXML
    private TableView<PrimitiveFile> table;

    @FXML
    private TableColumn<PrimitiveFile, String> name;

    @FXML
    private TableColumn<PrimitiveFile, String> size;

    @FXML
    private TableView<PrimitiveFile> tableDownloaded;

    @FXML
    private TableColumn<PrimitiveFile, String> nameDownloaded;

    @FXML
    private TableColumn<PrimitiveFile, String> sizeDownloaded;

    @FXML
    private Button download;

    @FXML
    private TextField search;

    @FXML
    private MenuItem downloadMenu;

    @FXML
    private BorderPane pane;


    private TorrentClient client = null;

    /**
     * List of available files at server.
     */
    private ObservableList<PrimitiveFile> files;
    /**
     * List of downloaded files.
     */
    private final ObservableList<PrimitiveFile> downloaded = FXCollections.observableArrayList();

    /**
     * Sets socket to connect to the server. <br/>
     * Creates TorrentClient instance and requests list of files from server.
     */
    public void setSocket(Socket socket) {
        this.client = new TorrentClient(socket);
        FilesRequest filesRequest = new FilesRequest();
        filesRequest.setCallback(primitiveFiles -> {
            files = FXCollections.observableArrayList(primitiveFiles);
            table.setItems(files);
        });

        this.client.request(filesRequest);
    }

    /**
     * Starts the download process of the selected file. <br/>
     * Selects path for saving, sends request and runs tracking the progress.
     */
    @FXML
    private void download() {
        var file = table.getSelectionModel().getSelectedItem();

        String path = UIUtils.openDownloadFormForPath(file);
        if (path == null) {
            return;
        }

        PrimitiveFile copy = new PrimitiveFile(file.getName(), file.getSize(), file.getId());

        DownloadRequest request = new DownloadRequest(file.getId(), path);
        Stage stage = UIUtils.prepareProgressTracker(file, request,
                () -> tableDownloaded.refresh());

        if (stage == null) {
            return;
        }

        this.client.request(request);
        downloaded.add(copy);
        stage.showAndWait();
    }

    /**
     * Filters files in table by the passed key.
     */
    private void filter(String key) {
        var filtered = FXCollections.observableArrayList(Utils.filterFiles(files, key));
        if (filtered.size() == files.size()) {
            table.setItems(files);
        } else {
            table.setItems(filtered);
        }
        table.refresh();
    }

    /**
     * Opens a window to change port and host.
     */
    @FXML
    private void newHostPort() {
        Socket socket = UIUtils.openWindowForSocket();
        if (socket == null) {
            return;
        }
        client.request(new FinishRequest());
        setSocket(socket);
        downloaded.clear();
    }

    /**
     * Closes the app.
     */
    @FXML
    public void exit() {
        UIUtils.showAlert("Exit", "Are you sure you want to exit Simple Torrent?", "Yes", "No")
                .ifPresent(response -> {
                    if (response.getText().equals("Yes")) {
                        client.close();
                        ((Stage) table.getScene().getWindow()).close();
                    }
                });
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> pane.requestFocus());

        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        size.setCellValueFactory(new PropertyValueFactory<>("sizeReadable"));
        nameDownloaded.setCellValueFactory(new PropertyValueFactory<>("name"));
        sizeDownloaded.setCellValueFactory(new PropertyValueFactory<>("sizeReadable"));

        name.setReorderable(false);
        size.setReorderable(false);
        nameDownloaded.setReorderable(false);
        sizeDownloaded.setReorderable(false);

        table.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    download.setDisable(newSelection == null);
                    downloadMenu.setDisable(newSelection == null);
                });
        table.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && !download.isFocused()) {
                table.getSelectionModel().clearSelection();
            }
        });

        tableDownloaded.setItems(downloaded);

        search.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                filter(newVal);
            }
        });
    }


    /**
     * Shows information about the application.
     */
    @FXML
    private void showAbout() {
        UIUtils.showAlert("About",
                "Simple Torrent application.\n" +
                        "Application for downloading files from the server." +
                        "\n\nDeveloped by Anastasia A. Baranova as a homework for " +
                        "\"Software Design\" course.\n\nemail.: aabranova_3@edu.hse.ru\n\n" +
                        "Higher school of economics, faculty of Computer Science.\nMoscow, 2021",
                "OK");
    }
}
