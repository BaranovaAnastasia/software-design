package client.lib;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Class provides static utility methods for working with files.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
public class Utils {
    /**
     * From files in the passed collection selects ones that match the key.
     *
     * @param files Collection of files to filter.
     * @param key   Filter key.
     * @return Filtered collection i.e collection of the elements that match the key.
     * @throws NullPointerException If files collection is null.
     */
    public static Collection<PrimitiveFile> filterFiles(Collection<PrimitiveFile> files, String key) {
        return files.stream().filter(file -> {
            if (key == null || key.length() == 0) {
                return true;
            }

            if (file == null) {
                return false;
            }

            String[] keys = key.toLowerCase().split(" +");
            String from = (file.getName() + " " + file.getSizeReadable()).toLowerCase();

            for (var k : keys) {
                if (!from.contains(k)) {
                    return false;
                }
            }

            return true;
        }).collect(Collectors.toList());
    }
}
