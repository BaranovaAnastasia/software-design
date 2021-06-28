package client.lib;

import java.util.Objects;

/**
 * Describes a file with a name, size and id.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
public class PrimitiveFile {

    private final String name;
    private final long size;
    private final int id;

    /**
     * File size in readable format. <br/>
     * Contains file size in the biggest units possible (with indication of units).
     */
    private final String sizeReadable;

    /**
     * Returns the name of the file.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns size of the file (in bytes).
     */
    public long getSize() {
        return size;
    }

    /**
     * Returns the id of the file.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns file size in readable format (file size in the biggest
     * units possible (with indication of units)).
     */
    public String getSizeReadable() {
        return sizeReadable;
    }

    /**
     * Constructs a new instance of PrimitiveFile.
     *
     * @param name File name.
     * @param size File size (in bytes).
     * @param id   File id.
     * @throws NullPointerException     If name is null.
     * @throws IllegalArgumentException If size or/and id are less than 0.
     */
    public PrimitiveFile(String name, long size, int id) {
        this.name = Objects.requireNonNull(name, "Name was null.");
        if (size < 0) {
            throw new IllegalArgumentException("Size was negative.");
        }
        if (id < 0) {
            throw new IllegalArgumentException("Id was negative.");
        }

        this.size = size;
        this.id = id;

        this.sizeReadable = getSizeReadable(this.size);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PrimitiveFile)) return false;
        PrimitiveFile that = (PrimitiveFile) o;
        return getSize() == that.getSize() &&
                getId() == that.getId() &&
                getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getSize(), getId());
    }

    /**
     * Parses string into a PrimitiveFile object.
     *
     * @param s String in format "{id} {name} {size}".
     * @throws NumberFormatException    if id or/and size read from a string are not valid numbers.
     * @throws IllegalArgumentException If s had incorrect format and therefore cannot be parsed into a file.
     */
    public static PrimitiveFile parseString(String s) {
        try {
            int firstSpace = s.indexOf(" ");
            int lastSpace = s.lastIndexOf(" ");
            String[] data = {
                    s.substring(0, firstSpace),
                    s.substring(firstSpace + 1, lastSpace),
                    s.substring(lastSpace + 1)
            };
            return new PrimitiveFile(data[1], Long.parseLong(data[2]), Integer.parseInt(data[0]));
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Passed string had invalid format.");
        }
    }

    /**
     * Returns size in readable format. <br/>
     * Result contains file size in the biggest units possible (with indication of units).
     *
     * @param size Size in bytes.
     */
    public static String getSizeReadable(long size) {
        double doubleSize = size;
        if (doubleSize < 1024) {
            return String.format("%.2f B", doubleSize);
        }
        doubleSize /= 1024;
        if (doubleSize < 1024) {
            return String.format("%.2f KB", doubleSize);
        }
        doubleSize /= 1024;
        if (doubleSize < 1024) {
            return String.format("%.2f MB", doubleSize);
        }
        doubleSize /= 1024;
        return String.format("%.2f GB", doubleSize);
    }
}
