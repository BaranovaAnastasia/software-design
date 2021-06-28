package server;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        if (args.length <= 0) {
            System.out.println("Specify the path to the directory.");
            return;
        }

        File directory = new File(args[0]);

        try {
            TorrentServer server;
            if (args.length == 1) {
                server = new TorrentServer(directory);
            } else {
                int port = Integer.parseInt(args[1]);
                server = new TorrentServer(port, directory);
            }
            System.out.println("Socket created.");
            System.out.println("Host: " + server.getHost());
            System.out.println("Port: " + server.getPort());
            server.run();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
