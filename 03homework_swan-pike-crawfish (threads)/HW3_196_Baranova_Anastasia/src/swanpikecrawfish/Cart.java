package swanpikecrawfish;

/**
 * Class that represents a cart that can be moved on a plane.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru"> Anastasia Baranova</a>
 */
public class Cart {
    /**
     * Current cart position on a plane.
     * Contains x and y coordinates.
     */
    private final CartPosition position;

    /**
     * Constructor creates a new cart at a given position on a plane.
     *
     * @param x x coordinate of a cart.
     * @param y y coordinate of a cart.
     * @see CartPosition#CartPosition(double, double)
     */
    public Cart(double x, double y) {
        position = new CartPosition(x, y);
    }

    /**
     * Moves the cart along the plane at the given distances.
     *
     * @param dx x-offset.
     * @param dy y-offset.
     * @see CartPosition#move(double, double)
     */
    public void move(double dx, double dy) {
        synchronized (position) {
            position.move(dx, dy);
        }
    }

    /**
     * Returns an array of two elements.
     * First one is an x coordinate of the cart position on a plane
     * and the second one is an y coordinate.
     *
     * @see CartPosition
     */
    public double[] getPosition() {
        synchronized (position) {
            return new double[]{position.getX(), position.getY()};
        }
    }

    /**
     * Returns string format of the cart.
     * The result string provides information about the position of the cart.
     */
    @Override
    public String toString() {
        synchronized (position) {
            return String.format("Cart is at (%.2f; %.2f).", position.getX(), position.getY());
        }
    }

    /**
     * Class representing the position of the cart on a plane.
     */
    private static class CartPosition {
        /**
         * Constructor creates a new position at a given point on the plane.
         *
         * @param x x coordinate.
         * @param y y coordinate.
         */
        private CartPosition(double x, double y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Returns x coordinate of the position.
         */
        private double getX() {
            return x;
        }

        /**
         * Returns y coordinate of the position.
         */
        private double getY() {
            return y;
        }

        /**
         * Changes the position by moving it along the plane at the given distances.
         *
         * @param dx x-offset.
         * @param dy y-offset.
         */
        private void move(double dx, double dy) {
            x += dx;
            y += dy;
        }

        /**
         * x coordinate of the position.
         */
        private double x;
        /**
         * y coordinate of the position.
         */
        private double y;
    }
}
