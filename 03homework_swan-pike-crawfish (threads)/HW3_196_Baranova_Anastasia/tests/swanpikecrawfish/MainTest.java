package swanpikecrawfish;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    @Test
    void testMain() {
        assertDoesNotThrow(() -> Main.main(new String[]{"0", "0"}));
    }

    @Test
    void testMainOneParameter() {
        assertDoesNotThrow(() -> Main.main(new String[]{"12"}));
    }

    @Test
    void testMainNoParameters() {
        assertDoesNotThrow(() -> Main.main(new String[0]));
    }

    @Test
    void testMainInvalidParameters() {
        assertDoesNotThrow(() -> Main.main(new String[]{"12", "a"}));

        assertDoesNotThrow(() -> Main.main(new String[]{"b", "10"}));

        assertDoesNotThrow(() -> Main.main(new String[]{"c", "d"}));
    }
}