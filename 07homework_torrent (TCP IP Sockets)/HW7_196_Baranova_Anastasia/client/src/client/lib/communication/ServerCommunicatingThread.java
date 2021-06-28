package client.lib.communication;

import client.lib.PrimitiveFile;
import client.lib.request.DownloadRequest;
import client.lib.request.FilesRequest;
import client.lib.request.FinishRequest;
import client.lib.request.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Class that describes an object for communicating with a server.
 * Intended to be executed by a thread. <br/>
 * Sends request to the server, reads and processes responses. <br/>
 * Class is package private so that only TorrentClient can access it.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
class ServerCommunicatingThread implements Runnable {
    private final Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    /**
     * List of request to send. <br/>
     * New requests are added to the list and then removed when executed.
     */
    private final LinkedList<Request> requests = new LinkedList<>();

    /**
     * Constructs a new instance of ServerCommunicatingThread.
     *
     * @param socket Socket to connect to the server.
     * @throws NullPointerException if socket is null.
     */
    ServerCommunicatingThread(Socket socket) {
        Objects.requireNonNull(socket, "Cannot create server with null socket.");
        this.socket = socket;
    }

    /**
     * While it is possible looks if there are any new requests and sends them to the server. <br/>
     * Also looks at other messages from the server to see if any errors occurred on the server.
     *
     * @see ServerCommunicatingThread#oneCycle()
     */
    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (!socket.isClosed()) {
                if (!oneCycle()) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    /**
     * Looks if there are any new requests and sends them to the server. <br/>
     * Looks at other messages from the server to see if any errors occurred on the server.
     *
     * @return true, if cycle ended successfully, false otherwise.
     */
    private boolean oneCycle() {
        try {
            if (in.ready()) {
                String input = in.readLine();
                if (input != null && input.equals("aborted")) {
                    close();
                    return false;
                }
            }

            execute();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void request(Request request) {
        requests.addLast(request);
    }

    /**
     * Processes one request from list. <br/>
     * If there are no pending requests, the method will do nothing.
     */
    private void execute() {
        if (requests.isEmpty()) {
            return;
        }
        Request request = requests.getFirst();
        requests.removeFirst();

        if (request instanceof FilesRequest) {
            requestList((FilesRequest) request);
            return;
        }
        if (request instanceof DownloadRequest) {
            download((DownloadRequest) request);
            return;
        }

        if (request instanceof FinishRequest) {
            close((FinishRequest) request);
        }
    }

    /**
     * Requests list of available files from server.
     */
    private void requestList(FilesRequest request) {
        out.println(request.getRequest());

        try {
            String fileData;
            while (!(fileData = in.readLine()).equals("done")) {
                request.receiveFile(PrimitiveFile.parseString(fileData));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            request.done();
        }
    }

    /**
     * Sends request to the server. <br/>
     * Reads server response (sequence of bytes) and saves it to the file.
     */
    private void download(DownloadRequest request) {
        out.println(request.getRequest() + " " + request.getId());

        try {
            request.prepare();

            String read;
            long i = 1;

            while (!(read = in.readLine()).equals("done")) {
                request.receiveByte(Integer.parseInt(read), i++);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            request.done();
        }
    }

    /**
     * Informs server about the end of work and then closes resources.
     */
    private void close(FinishRequest request) {
        out.println(request.getRequest());
        try {
            socket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes resources without informing the server. <br/>
     * Used to close resources after an error on server.
     */
    private void close() {
        try {
            socket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
