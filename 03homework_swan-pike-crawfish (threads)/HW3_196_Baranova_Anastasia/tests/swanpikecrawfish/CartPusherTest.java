package swanpikecrawfish;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

import static org.junit.jupiter.api.Assertions.*;

class CartPusherTest {
    @Test
    void createCartPusherOK() {
        Cart cart = new Cart(10.0, 15.0);

        assertDoesNotThrow(() -> {
            new CartPusher(cart, 10, 10);
        });
    }

    @Test
    void createCartPusherNull() {
        assertThrows(NullPointerException.class, () -> new CartPusher(null, 10, 10));
    }

    @Test
    void createCartPusherIncorrectTime() {
        Cart cart = new Cart(10.0, 15.0);

        assertThrows(IllegalArgumentException.class, () -> new CartPusher(cart, 10, 0));

        assertThrows(IllegalArgumentException.class, () -> new CartPusher(cart, 10, -1));
    }

    @Test
    void getCoefficientTest() {
        CartPusher pusher = new CartPusher(new Cart(10.0, 15.0), 0, 10);
        assertTrue(pusher.getCoefficient() >= 1);
        assertTrue(pusher.getCoefficient() < 10);
    }

    @Test
    void pushCart() {
        Cart cart = new Cart(10.0, 15.0);

        CartPusher pusher = new CartPusher(cart, 10, 10);

        assertDoesNotThrow(pusher::push);

        var coordinates = cart.getPosition();
        assertNotEquals(coordinates[0], 10.00);
        assertNotEquals(coordinates[1], 15.00);
    }

    @Test
    void runCartPusher() {
        Cart cart = new Cart(0, 0);

        CartPusher pusher = new CartPusher(cart, 10, 10000);

        // Considering the pusher may fall asleep for 5 seconds or less before getting completely tired.
        assertTimeout(Duration.ofMillis(15000), pusher::run);
    }

    @Test
    void checkWorkAfterGettingTired() {
        Cart cart = new Cart(0, 0);

        CartPusher pusher = new CartPusher(cart, 0, 10000);

        // Considering the pusher may fall asleep for 5 seconds or less before getting completely tired.
        assertTimeout(Duration.ofMillis(15000), pusher::run);

        var coordinates1 = cart.getPosition();
        assertTrue(coordinates1[0] > 0);
        assertEquals(coordinates1[1], 0);

        assertDoesNotThrow(pusher::push);

        var coordinates2 = cart.getPosition();
        assertEquals(coordinates1[0], coordinates2[0]);
        assertEquals(coordinates1[1], coordinates2[1]);
    }

    @Test
    void workAsThread() throws InterruptedException {
        Cart cart = new Cart(0, 0);

        CartPusher pusher = new CartPusher(cart, 0, 5000);

        pusher.start();
        pusher.join();

        var coordinates = cart.getPosition();

        assertTrue(coordinates[0] > 0);
        assertEquals(coordinates[1], 0);
    }

    @Test
    void twoThreads() throws InterruptedException {
        Cart cart = new Cart(0, 0);

        CartPusher pusher1 = new CartPusher(cart, 0, 5000);
        CartPusher pusher2 = new CartPusher(cart, 90, 5000);

        pusher1.start();
        pusher2.start();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                var coordinates = cart.getPosition();

                assertTrue(coordinates[0] > 0);
                assertTrue(coordinates[1] > 0);
            }
        }, 500, 500);

        Timer stop = new Timer();
        stop.schedule(new TimerTask() {
            @Override
            public void run() {
                timer.cancel();
                stop.cancel();
            }
        }, 5000);

        pusher1.join();
        pusher2.join();

        var coordinates = cart.getPosition();
        assertTrue(coordinates[0] > 0);
        assertTrue(coordinates[1] > 0);
    }
}