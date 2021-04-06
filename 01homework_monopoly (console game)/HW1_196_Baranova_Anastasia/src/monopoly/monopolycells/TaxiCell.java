package monopoly.monopolycells;

import monopoly.*;

/**
 * Class representing Taxi Monopoly cell.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru"> Anastasia Baranova</a>
 */
public class TaxiCell extends MonopolyCell {
    static {
        MAX_COUNT = 8;
        count = 0;
    }

    /**
     * Constructor creates new Taxi Monopoly cell.
     *
     * @throws IllegalStateException If maximum number of Taxi cells
     *                               (8) has already been reached.
     */
    public TaxiCell() {
        super('T');
        if (++count > MAX_COUNT) {
            throw new IllegalStateException("The maximum number of Taxi cells has been reached.");
        }
    }

    /**
     * Moves player by the random number of cells
     * (taxi distance \in [3, 5]).
     *
     * @param player Player who has hit the cell
     * @throws IllegalArgumentException If player is not at the cell.
     * @throws NullPointerException     If player is null.
     *                                  If field was created incorrectly and
     *                                  some cell does not have the next one.
     */
    @Override
    public void hit(Player player) {
        checkPlayerAtTheCell(player);
        player.hitTaxiCell();
    }

    /**
     * Maximum number of TaxiCell class instances
     * (maximum number of Taxi cells on the game field).
     */
    private static final int MAX_COUNT;

    /**
     * Current number of TaxiCell class instances
     * (current number of Taxi cells on the game field).
     */
    private static int count;
}
