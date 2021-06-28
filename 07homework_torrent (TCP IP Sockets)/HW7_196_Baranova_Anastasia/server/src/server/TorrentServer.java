package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.NotDirectoryException;

/**
 * Server for Simple Torrent. <br/>
 * Server has a directory with files available to clients for download.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
public class TorrentServer {
    /**
     * Default port. Used if the user has not specified it.
     */
    private static final int DEFAULT_PORT = 5115;

    private final File directory;
    private final ServerSocket serverSocket;

    /**
     * Constructor creates a new instance of TorrentServer.
     *
     * @param port      Number of port on which server will be running.
     * @param directory Directory with files that will be available for clients.
     * @throws IllegalArgumentException If port number is out of range from 0 and 65535, inclusive.
     * @throws NullPointerException     If directory is null.
     * @throws FileNotFoundException    If directory does not exists.
     * @throws NotDirectoryException    If passed file is not a directory.
     * @throws IOException              If an error error occurs when opening the socket.
     * @throws SecurityException        If security manager doesn't allow the socket creation.
     */
    public TorrentServer(int port, File directory) throws IOException {

        checkDirectory(directory);
        checkPort(port);

        this.directory = directory;
        this.serverSocket = tryCreateServerSocket(port);
    }

    /**
     * Constructor creates a new instance of TorrentServer using default port.
     *
     * @param directory Directory with files that will be available for clients.
     * @throws NullPointerException  If directory is null.
     * @throws FileNotFoundException If directory does not exists.
     * @throws NotDirectoryException If passed file is not a directory.
     * @throws IOException           If an error error occurs when opening the socket.
     * @throws SecurityException     If security manager doesn't allow the socket creation.
     * @see TorrentServer#DEFAULT_PORT
     * @see TorrentServer#TorrentServer(int, File)
     */
    public TorrentServer(File directory) throws IOException {
        this(DEFAULT_PORT, directory);
    }

    /**
     * Runs the server. <br/>
     * In an endless loop, accepts new clients. For each client launches a separate thread to serve them. <br/>
     * If an error occurs when accepting a client, the server will be stopped.
     */
    public void run() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                ClientServingThread client = new ClientServingThread(socket, directory);
                Thread thread = new Thread(client);
                thread.start();
            }
        } catch (IOException e) {
            if (serverSocket.isClosed()) {
                return;
            }
            System.err.println("An I/O error occurs when waiting for a connection.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Accept failed.");
            e.printStackTrace();
        } finally {
            close();
        }
    }

    /**
     * Returns a server's port number.
     */
    public int getPort() {
        return serverSocket.getLocalPort();
    }

    /**
     * Returns a server's host.
     */
    public String getHost() {
        return serverSocket.getInetAddress().getHostAddress();
    }

    /**
     * Closes server socket.
     */
    public void close() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("Cannot close server socket.");
            e.printStackTrace();
        }
    }

    /**
     * Checks port number to be between 0 and 65535, inclusive.
     *
     * @throws IllegalArgumentException If port number is illegal (less than 0 or bigger than 65535).
     */
    private static void checkPort(int port) {
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("Illegal port: " + port + ". Must be between 0 and 65535, inclusive.");
        }
    }

    /**
     * Checks directory to be not null, existing and an actual directory (not just a file).
     *
     * @throws NullPointerException  If directory is null.
     * @throws FileNotFoundException If directory does not exists.
     * @throws NotDirectoryException If passed file is not a directory.
     */
    private static void checkDirectory(File directory) throws FileNotFoundException, NotDirectoryException {
        if (directory == null) {
            throw new NullPointerException("Directory was null.");
        }
        if (!directory.exists()) {
            throw new FileNotFoundException("Cannot find directory.");
        }
        if (!directory.isDirectory()) {
            throw new NotDirectoryException("Not a directory.");
        }
    }

    /**
     * Tries to create a server socket.
     *
     * @param port Server's port.
     * @throws IOException       If an error error occurs when opening the socket.
     * @throws SecurityException If security manager doesn't allow the socket creation.
     */
    private static ServerSocket tryCreateServerSocket(int port) throws IOException {
        try {
            return new ServerSocket(port);
        } catch (IOException e) {
            throw new IOException("Cannot open a server socket.");
        } catch (SecurityException e) {
            throw new SecurityException("Cannot listen on port: " + port);
        }
    }
}
