package swanpikecrawfish;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author <a href="mailto:aabaranova_3@edu.hse.ru"> Anastasia Baranova</a>
 */
public class Main {
    private static Timer cartPositionUpdateTimer;

    private static final int PROCESS_TIME = 25000;
    private static final int UPDATES_PERIOD = 2000;

    public static void main(String[] args) throws InterruptedException {
        int[] coordinates = parseCoordinates(args);
        if (coordinates == null) {
            return;
        }

        Cart cart = new Cart(coordinates[0], coordinates[1]);
        CartPusher swan = new CartPusher(cart, 60, PROCESS_TIME);
        CartPusher pike = new CartPusher(cart, 180, PROCESS_TIME);
        CartPusher crawfish = new CartPusher(cart, 300, PROCESS_TIME);

        printCoefficients(swan, pike, crawfish);

        startProcess(cart, swan, pike, crawfish);
    }

    /**
     * Starts swan, pike, and crawfish work on pushing a cart.
     *
     * @param cart     Cart that is pushing.
     * @param swan     Swan that pushes the cart.
     * @param pike     Pike that pushes the cart.
     * @param crawfish Crawfish that pushes the cart.
     */
    private static void startProcess(Cart cart, CartPusher swan, CartPusher pike, CartPusher crawfish)
            throws InterruptedException {

        System.out.println(System.lineSeparator() + "Start: " + cart);
        startCartUpdates(cart);

        swan.start();
        pike.start();
        crawfish.start();

        swan.join();
        pike.join();
        crawfish.join();

        cartPositionUpdateTimer.cancel();
        System.out.println("Finish: " + cart);
    }

    /**
     * Parses coordinates from an array of strings.
     *
     * @param coordinates Data about coordinates on a plane in string format.
     * @return Array of two elements - x and y coordinates if data was parsed successfully. Null otherwise.
     */
    private static int[] parseCoordinates(String[] coordinates) {
        int[] result = new int[2];

        if (coordinates.length == 0) {
            return result;
        }

        try {
            result[0] = Integer.parseInt(coordinates[0]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid parameter x = " + coordinates[0] + ". Must be an integer value.");
            return null;
        }

        if (coordinates.length == 1) {
            return result;
        }

        try {
            result[1] = Integer.parseInt(coordinates[1]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid parameter y = " + coordinates[1] + ". Must be an integer value.");
            return null;
        }

        return result;
    }

    /**
     * Starts updating on the position of the given cart.
     */
    private static void startCartUpdates(Cart cart) {
        cartPositionUpdateTimer = new Timer();

        cartPositionUpdateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(cart);
            }
        }, 0, UPDATES_PERIOD);
    }

    /**
     * Prints pushing coefficients of the given swan, pike, and crawfish.
     */
    private static void printCoefficients(CartPusher swan, CartPusher pike, CartPusher crawfish) {
        System.out.printf("Swan coefficient = %.2f" + System.lineSeparator(), swan.getCoefficient());
        System.out.printf("Pike coefficient = %.2f" + System.lineSeparator(), pike.getCoefficient());
        System.out.printf("Crawfish coefficient = %.2f" + System.lineSeparator(), crawfish.getCoefficient());
    }
}
