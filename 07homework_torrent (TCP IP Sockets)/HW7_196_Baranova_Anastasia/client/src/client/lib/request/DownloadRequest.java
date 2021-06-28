package client.lib.request;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Request for file download.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
public class DownloadRequest extends Request {
    private final File file;
    private final int id;

    private OutputStream outputStream;
    private Consumer<Long> progressTracker;

    /**
     * Constructs a new instance of DownloadRequest.
     *
     * @param id   File id.
     * @param path Path to the output file.
     * @throws IllegalArgumentException If id is less than 0.
     * @throws NullPointerException     If path is null.
     */
    public DownloadRequest(int id, String path) {
        super("download");

        if (id < 0) {
            throw new IllegalArgumentException("Id must not be negative.");
        }
        Objects.requireNonNull(path, "Path was null.");

        this.id = id;
        this.file = new File(path);
    }

    /**
     * Returns the id of the file.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets a consumer that will be executed with number of downloaded
     * bytes during the entire downloading process. <br/>
     * Consumer is supposed to track the progress.
     */
    public void setProgressTracker(Consumer<Long> progressTracker) {
        this.progressTracker = progressTracker;
    }

    /**
     * Recreates file and opens a stream to write to it. <br/>
     * Should be called before receiving bytes.
     *
     * @throws IOException       If an I/O error occurred.
     * @throws SecurityException If file cannot be deleted or created.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void prepare() throws IOException {
        if (!file.createNewFile()) {
            file.delete();
            file.createNewFile();
        }
        outputStream = new FileOutputStream(file);
    }

    /**
     * Writes a byte received as a part of response to the file. <br/>
     * Cannot be called before the call of prepare() method or after the call() of done method.
     *
     * @param readByte Received byte.
     * @param index    Index of the in the array of bytes read from file
     *                 (number of the byte to see how many bytes have already been downloaded).
     * @throws IOException If an I/O error occurred.
     * @see DownloadRequest#prepare()
     * @see DownloadRequest#done()
     */
    public void receiveByte(int readByte, long index) throws IOException {
        outputStream.write(readByte);

        if (progressTracker != null) {
            progressTracker.accept(index);
        }
    }

    /**
     * Closes resources. <br/>
     * Must be called after receiveByte() was called for each byte of file.
     *
     * @see DownloadRequest#receiveByte(int, long)
     */
    public void done() {
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
