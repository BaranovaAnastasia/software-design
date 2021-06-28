package test;

import client.lib.PrimitiveFile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PrimitiveFileTest {
    @Test
    void testParseInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> PrimitiveFile.parseString(""));
    }

    @Test
    void testParseInvalidOrder() {
        assertThrows(NumberFormatException.class, () -> PrimitiveFile.parseString("name 12 1200"));
    }

    @Test
    void testParseInvalidSize() {
        assertThrows(NumberFormatException.class, () -> PrimitiveFile.parseString("0 name size"));
    }

    @Test
    void testParseInvalidIndex() {
        assertThrows(NumberFormatException.class, () -> PrimitiveFile.parseString("id name 12345"));
    }
}
