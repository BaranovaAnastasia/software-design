package server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;

/**
 * Client's requests processor intended to be executed by a thread. <br/>
 * Listens for client's requests and responds to them. <br/>
 * Class is package private so that only TorrentServer can access it.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
class ClientServingThread implements Runnable {

    private final Socket socket;
    private final File directory;

    private BufferedReader in;
    private PrintWriter out;

    /**
     * Constructor creates a new instance of ClientServingThread.
     *
     * @throws NullPointerException If any of the passed arguments are null.
     */
    ClientServingThread(Socket socket, File directory) {
        this.socket = Objects.requireNonNull(socket, "Socket was null.");
        this.directory = Objects.requireNonNull(directory, "Directory was null.");
    }

    /**
     * While it is possible reads clients requests and responds to them.
     */
    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (!socket.isClosed()) {
                try {
                    String input = in.readLine();
                    System.out.println("Received request: " + input);
                    processRequest(input);
                } catch (SocketException e) {
                    System.err.println("Connection lost.");
                    return;
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            end(false);
        }
    }

    /**
     * Processes client's request.
     *
     * @param request String containing a line received by client.
     */
    private void processRequest(String request) {
        String[] words = request.split(" ");

        switch (words[0]) {
            case "list":
                sendListOfFiles();
                break;
            case "download":
                downloadFile(Integer.parseInt(words[1]));
                break;
            case "finish":
                end(true);
                break;
        }
    }

    /**
     * Sends information about every file in directory to client. <br/>
     * Response format: "{id} {file name} {file size}", <br/>
     * where id - index of the file in the list of files in directory.
     */
    private void sendListOfFiles() {
        var listOfFiles = Objects.requireNonNull(directory.listFiles());
        for (int id = 0; id < listOfFiles.length; id++) {
            if (!listOfFiles[id].isDirectory() && listOfFiles[id].length() <= 137_438_883_103L) {
                out.println(id + " " + listOfFiles[id].getName() + " " + listOfFiles[id].length());
            }
        }
        out.println("done");
    }

    /**
     * Reads file and sends it byte by byte to the client.
     *
     * @param id Index of the file in the list of files in directory.
     */
    private void downloadFile(int id) {
        var file = Objects.requireNonNull(directory.listFiles())[id];

        try (InputStream inputStream = new FileInputStream(file)) {
            int readByte;
            while ((readByte = inputStream.read()) != -1) {
                out.println(readByte);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        out.println("done");
    }

    /**
     * Closes all the resources.
     *
     * @param planned true, if client requested finishing, false otherwise.
     */
    private void end(boolean planned) {
        if(socket.isClosed()) {
            return;
        }
        try {
            out.println(planned ? "done" : "aborted");
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
