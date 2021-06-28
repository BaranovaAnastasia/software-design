package test;

import client.lib.PrimitiveFile;
import client.lib.Utils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UtilsTest {
    private final List<PrimitiveFile> files = Arrays.asList(
            new PrimitiveFile("moscow.txt", 1024, 0),
            new PrimitiveFile("old_moscow.jpg", 50, 1),
            new PrimitiveFile("spb.txt", 1024, 2),
            new PrimitiveFile("something.txt", 1024 * 1024 * 1024, 3),
            null);

    @Test
    void testNoResults() {
        String key = "Some random text";
        var filtered = Utils.filterFiles(files, key);
        assertEquals(filtered.size(), 0);
    }

    @Test
    void testAllInResult() {
        String key = "";
        var filtered = Utils.filterFiles(files, key);
        assertEquals(filtered.size(), filtered.size());

        for (var f : files) {
            assertTrue(filtered.contains(f));
        }
    }

    @Test
    void testNameFilter() {
        String key = "moscow";
        var filtered = Utils.filterFiles(files, key);
        assertEquals(filtered.size(), 2);

        for (var f : files) {
            if (f != null && f.getName().contains(key)) {
                assertTrue(filtered.contains(f));
            }
        }
    }

    @Test
    void testSizeFilter() {
        String key = "1 KB";
        var filtered = Utils.filterFiles(files, key);
        assertEquals(filtered.size(), 2);

        for (var f : files) {
            if (f != null && f.getSize() == 1024) {
                assertTrue(filtered.contains(f));
            }
        }
    }
}
