package swanpikecrawfish;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class that represents an object that can push a cart.
 * Designed as a thread since each object must work with the cart in parallel.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru"> Anastasia Baranova</a>
 * @see Cart
 */
public class CartPusher extends Thread {
    /**
     * Constructor creates a new CartPusher object.
     *
     * @param cart          Cart with which the CartPusher object will work.
     * @param angle         Angle in degrees at which the cart will be pushing.
     * @param operatingTime The operating time of the object in milliseconds,
     *                      after which the pusher gets tired and can no longer move the cart.
     * @throws NullPointerException     If cart is null.
     * @throws IllegalArgumentException if operatingTime is not positive.
     */
    public CartPusher(Cart cart, int angle, int operatingTime) {
        if (cart == null) {
            throw new NullPointerException("Argument cart was null.");
        }
        if (operatingTime <= 0) {
            throw new IllegalArgumentException("Operating time was zero or less.");
        }
        this.cart = cart;
        this.angle = angle * Math.PI / 180;
        this.operatingTime = operatingTime;
        this.coefficient = getRandomDouble(1, 10);
    }

    /**
     * Returns pushing coefficient.
     */
    public double getCoefficient() {
        return coefficient;
    }

    /**
     * If pusher is not yet tired pushes the cart and then makes the pusher rest.
     *
     * @see Cart#move(double, double)
     */
    public void push() throws InterruptedException {
        if (isTired) {
            return;
        }
        cart.move(coefficient * Math.cos(angle), coefficient * Math.sin(angle));
        sleep((long) getRandomDouble(1000, 5000));
    }

    /**
     * Pushes the cart until the pusher is tired.
     */
    @Override
    public void run() {
        // Timer for tracking working time.
        var timer = new Timer();
        // Will make pusher tired in operatingTime ms
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                isTired = true;
            }
        }, operatingTime);

        while (!isTired) {
            try {
                push();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        timer.cancel();
    }

    /**
     * Cart with which this object is working.
     */
    private final Cart cart;
    /**
     * Angle in radians at which the object is pushing the cart.
     */
    private final double angle;
    /**
     * The operating time of the object in milliseconds,
     * after which the pusher gets tired and can no longer move the cart.
     */
    private final int operatingTime;
    /**
     * Pushing coefficient.
     */
    private final double coefficient;

    /**
     * Shows if the pusher is tired.
     * When the value is true the cart can no longer be pushed by this object.
     */
    private boolean isTired = false;

    private static final Random rnd = new Random();

    /**
     * Returns a random double value from [min, max).
     */
    private static double getRandomDouble(double min, double max) {
        return min + (max - min) * rnd.nextDouble();
    }
}
