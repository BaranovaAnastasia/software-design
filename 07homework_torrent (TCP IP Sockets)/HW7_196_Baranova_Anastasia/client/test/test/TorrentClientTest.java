package test;

import client.lib.communication.TorrentClient;
import client.lib.request.DownloadRequest;
import client.lib.request.FilesRequest;
import client.lib.request.FinishRequest;
import org.junit.jupiter.api.Test;
import server.TorrentServer;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class TorrentClientTest {

    @Test
    void testConnection() throws URISyntaxException, IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("test/testDirectory");

        assert resource != null;
        File file = new File(resource.toURI());
        assertTrue(file.exists());

        var ts = new TorrentServer(8080, file);
        Thread serverThread = new Thread(ts::run);
        serverThread.start();

        assertDoesNotThrow(() -> {
            TorrentClient tc = new TorrentClient(new Socket(ts.getHost(), 8080));
            ts.close();
            tc.request(new FinishRequest());
        });
    }

    @Test
    void testList() throws URISyntaxException, IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("test/testDirectory");

        assert resource != null;
        File file = new File(resource.toURI());
        assertTrue(file.exists());

        var ts = new TorrentServer(8080, file);
        Thread serverThread = new Thread(ts::run);
        serverThread.start();

        TorrentClient tc = new TorrentClient(new Socket(ts.getHost(), 8080));
        ts.close();
        var request = new FilesRequest();
        request.setCallback(list -> {
            assertEquals(list.size(), 2);
            for (var el : list) {
                assertTrue(el.getName().equals("file1.txt") || el.getName().equals("file2.txt"));
            }
        });
        tc.request(request);
        tc.request(new FinishRequest());
    }

    @Test
    void testDownload() throws URISyntaxException, IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("test/testDirectory");
        URL file1Resource = classLoader.getResource("test/testDirectory/file1.txt");

        assert resource != null;
        File file = new File(resource.toURI());
        assert file1Resource != null;
        File file1 = new File(file1Resource.toURI());
        assertTrue(file.exists());

        var ts = new TorrentServer(8080, file);
        Thread serverThread = new Thread(ts::run);
        serverThread.start();

        TorrentClient tc = new TorrentClient(new Socket(ts.getHost(), 8080));
        ts.close();
        var request = new DownloadRequest(0, "output.txt");
        request.setProgressTracker(val -> {
            if (val == file1.length()) {
                try {
                    assertEquals(
                            Files.readString(Path.of(file1.toURI())),
                            Files.readString(Path.of("output.txt"))
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        tc.request(request);
        tc.request(new FinishRequest());
    }
}
