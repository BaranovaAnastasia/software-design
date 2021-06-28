package client.lib.communication;

import client.lib.request.FinishRequest;
import client.lib.request.Request;

import java.net.Socket;
import java.util.Objects;

/**
 * Client for Simple Torrent.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
public class TorrentClient {
    private final ServerCommunicatingThread server;

    /**
     * Constructor creates a new instance of TorrentClient. <br/>
     * Creates and starts ServerCommunicatingThread in another thread.
     *
     * @param socket Socket to connect to the server.
     * @throws NullPointerException if socket is null.
     */
    public TorrentClient(Socket socket) {
        Objects.requireNonNull(socket, "Socket was null.");

        this.server = new ServerCommunicatingThread(socket);
        Thread thread = new Thread(server);
        thread.start();
    }

    /**
     * Sends request to ServerCommunicatingThread that then sends it to the server.
     *
     * @param request Request to send.
     * @throws NullPointerException If request is null.
     * @see ServerCommunicatingThread#request(Request)
     */
    public void request(Request request) {
        Objects.requireNonNull(request, "Request was null.");
        server.request(request);
    }

    /**
     * Informs the server about the end of work. <br/>
     * From ServerCommunicatingThread close() will be called to close resources.
     */
    public void close() {
        Request request = new FinishRequest();
        server.request(request);
    }
}
