package swanpikecrawfish;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {
    @Test
    void createCart() {
        assertDoesNotThrow(() -> {
            new Cart(10.0, 15.0);
        });
    }

    @Test
    void cartString() {
        String cartString = (new Cart(10, 15)).toString().replace(',', '.');
        assertEquals("Cart is at (10.00; 15.00).", cartString);
    }

    @Test
    void getCartPosition() {
        var coordinates = (new Cart(10, 15)).getPosition();
        assertEquals(coordinates[0], 10);
        assertEquals(coordinates[1], 15);
    }

    @Test
    void moveCart() {
        Cart cart = new Cart(12.3, 5.01);

        cart.move(10, 10);

        var coordinates = cart.getPosition();
        assertEquals(coordinates[0], 22.30);
        assertEquals(coordinates[1], 15.01);
    }

    @Test
    void twoThreads() throws InterruptedException {
        Cart cart = new Cart(0, 0);

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 100000000; i++) {
                cart.move(1, 1);
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 100000000; i++) {
                cart.move(1, 1);
            }
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        var coordinates = cart.getPosition();
        assertEquals(coordinates[0], 100000000 + 100000000);
        assertEquals(coordinates[1], 100000000 + 100000000);
    }
}