package test;

import org.junit.jupiter.api.Test;
import server.TorrentServer;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

public class ClientServingThreadTest {
    @Test
    void runTest() throws IOException, URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("test/testDirectory");

        assert resource != null;
        File file = new File(resource.toURI());
        TorrentServer ts = new TorrentServer(8888, file);
        Thread serverThread = new Thread(ts::run);
        serverThread.start();

        Socket socket1 = new Socket(ts.getHost(), 8888);
        Socket socket2 = new Socket(ts.getHost(), 8888);
        var out1 = new PrintWriter(socket1.getOutputStream(), true);
        var out2 = new PrintWriter(socket2.getOutputStream(), true);
        var in1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
        var in2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));

        assertFalse(socket1.isClosed());
        assertFalse(socket2.isClosed());

        out1.println("finish");
        while (!in1.ready()) ;
        assertEquals(in1.readLine(), "done");
        socket1.close();

        out2.println("finish");
        while (!in2.ready()) ;
        assertEquals(in2.readLine(), "done");
        socket2.close();

        in1.close();
        in2.close();
        out1.close();
        out2.close();

        ts.close();
    }

    @Test
    void commands() throws IOException, URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("test/testDirectory");

        assert resource != null;
        File file = new File(resource.toURI());
        TorrentServer ts = new TorrentServer(8888, file);
        Thread serverThread = new Thread(ts::run);
        serverThread.start();

        Socket socket = new Socket(ts.getHost(), 8888);
        var out = new PrintWriter(socket.getOutputStream(), true);
        var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        out.println("list");
        for (int i = 0; i < 2; i++) {
            assertEquals(in.readLine().split(" ")[1], "file" + (i + 1) + ".txt");
        }

        ts.close();
    }
}
