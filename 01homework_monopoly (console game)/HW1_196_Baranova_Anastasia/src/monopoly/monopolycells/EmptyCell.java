package monopoly.monopolycells;

import monopoly.*;

/**
 * Class representing Empty Monopoly cell.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru"> Anastasia Baranova</a>
 */
public class EmptyCell extends MonopolyCell {
    static {
        MAX_COUNT = 4;
        count = 0;
    }

    /**
     * Constructor creates new Empty Monopoly cell.
     *
     * @throws IllegalStateException If maximum number of Empty cells
     *                               (4) has already been reached.
     */
    public EmptyCell() {
        super('E');
        if (++count > MAX_COUNT) {
            throw new IllegalStateException("The maximum number of Empty cells has been reached.");
        }
    }

    /**
     * Prints EmptyCell message for the user.
     *
     * @param player Player who has hit the cell
     * @throws NullPointerException     If player is null.
     * @throws IllegalArgumentException If player is not at the cell.
     */
    @Override
    public void hit(Player player) {
        checkPlayerAtTheCell(player);
        player.hitEmptyCell();
    }

    /**
     * Maximum number of EmptyCell class instances
     * (maximum number of Empty cells on the game field)
     */
    private static final int MAX_COUNT;

    /**
     * Current number of EmptyCell class instances
     * (current number of Empty cells on the game field)
     */
    private static int count;
}
