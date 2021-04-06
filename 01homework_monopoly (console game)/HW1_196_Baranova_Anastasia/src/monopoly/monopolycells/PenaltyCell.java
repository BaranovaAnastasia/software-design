package monopoly.monopolycells;

import monopoly.*;

/**
 * Class representing Penalty Monopoly cell.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru"> Anastasia Baranova</a>
 */
public class PenaltyCell extends MonopolyCell {
    static {
        penaltyCoeff = ((int) (Math.random() * (100 - 10 + 1)) + 10) * 1.0 / 1000;
        MAX_COUNT = 8;
        count = 0;
    }

    /**
     * Constructor creates new Penalty Monopoly cell.
     *
     * @throws IllegalStateException If maximum number of Penalty cells
     *                               (8) has been reached.
     */
    public PenaltyCell() {
        super('%');
        if (++count > MAX_COUNT) {
            throw new IllegalStateException("The maximum number of Penalty cells has been reached.");
        }
    }

    /**
     * Collects a penalty from the player for visiting the cell.
     *
     * @param player Player who has hit the cell
     * @throws NullPointerException     If player is null.
     * @throws IllegalArgumentException If player is not at the cell.
     */
    @Override
    public void hit(Player player) {
        checkPlayerAtTheCell(player);
        player.hitPenaltyCell();
        player.getGame().printCurrentBalances(false);
    }

    /**
     * @return Penalty coefficient.
     */
    public static double getPenaltyCoeff() {
        return penaltyCoeff;
    }


    /**
     * Penalty coefficient.
     */
    private static final double penaltyCoeff;

    /**
     * Maximum number of PenaltyCell class instances
     * (maximum number of Penalty cells on the game field).
     */
    private static final int MAX_COUNT;

    /**
     * Current number of PenaltyCell class instances
     * (current number of Penalty cells on the game field).
     */
    private static int count;
}
