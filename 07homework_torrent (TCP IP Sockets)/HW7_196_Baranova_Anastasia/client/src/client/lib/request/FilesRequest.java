package client.lib.request;

import client.lib.PrimitiveFile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * List of available files request.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
public class FilesRequest extends Request {
    /**
     * Consumer to be executed when all files are received.
     */
    private Consumer<Collection<PrimitiveFile>> callback;

    /**
     * List of files (stores files received from server).
     */
    private final ArrayList<PrimitiveFile> listOfFiles = new ArrayList<>();

    /**
     * Constructs a new instance of FilesRequest.
     */
    public FilesRequest() {
        super("list");
    }

    /**
     * Sets a consumer to be executed when all files are received.
     */
    public void setCallback(Consumer<Collection<PrimitiveFile>> callback) {
        this.callback = callback;
    }

    /**
     * Adds file to the list of files. Null files are ignored.
     *
     * @param file File to add. File received as a part of response.
     */
    public void receiveFile(PrimitiveFile file) {
        if (file != null) {
            listOfFiles.add(file);
        }
    }

    /**
     * Executes callback. Supposed to be called when all the files are received.
     *
     * @see FilesRequest#callback
     */
    public void done() {
        if (callback != null) {
            callback.accept(listOfFiles);
        }
    }
}
