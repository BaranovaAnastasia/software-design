package test;

import org.junit.jupiter.api.Test;
import server.TorrentServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.NotDirectoryException;

import static org.junit.jupiter.api.Assertions.*;

public class TorrentServerTest {
    @Test
    void incorrectCreationNull() {
        assertThrows(NullPointerException.class, () -> new TorrentServer(null));
        assertThrows(NullPointerException.class, () -> new TorrentServer(5000, null));
    }


    @Test
    void incorrectCreationNotDirectory() throws URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("test/testDirectory/file1.txt");

        File file = new File(resource.toURI());
        assertTrue(file.exists());
        assertThrows(NotDirectoryException.class, () -> new TorrentServer(file));
        assertThrows(NotDirectoryException.class, () -> new TorrentServer(5000, file));
    }

    @Test
    void incorrectCreationNotFound() {
        File file = new File("some/random/path");
        assertFalse(file.exists());
        assertThrows(FileNotFoundException.class, () -> new TorrentServer(file));
        assertThrows(FileNotFoundException.class, () -> new TorrentServer(5000, file));
    }

    @Test
    void incorrectCreationInvalidPort() throws URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("test/testDirectory");

        File file = new File(resource.toURI());
        assertTrue(file.exists());
        assertThrows(IllegalArgumentException.class, () -> new TorrentServer(-1, file));
        assertThrows(IllegalArgumentException.class, () -> new TorrentServer(12345678, file));
    }

    @Test
    void correctCreation() throws URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("test/testDirectory");

        File file = new File(resource.toURI());
        assertTrue(file.exists());

        assertDoesNotThrow(() -> {
            var ts = new TorrentServer(8080, file);
            assertEquals(ts.getPort(), 8080);
            ts.close();
        });
    }
}
