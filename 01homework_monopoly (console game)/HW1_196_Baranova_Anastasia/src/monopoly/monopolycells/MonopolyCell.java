package monopoly.monopolycells;

import monopoly.*;

/**
 * Superclass for all of the classes representing monopoly cells.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru"> Anastasia Baranova</a>
 */
public abstract class MonopolyCell {
    /**
     * Class constructor specifying cell symbol.
     *
     * @param cellSymbol symbol representing the cell on the game field
     */
    MonopolyCell(char cellSymbol) {
        this.cellSymbol = cellSymbol;
    }

    /**
     * Executes cell's interaction with the player.
     *
     * @param player Player who has hit the cell
     * @throws NullPointerException     If player is null.
     * @throws IllegalArgumentException If player is not at the cell.
     */
    public abstract void hit(Player player);

    /**
     * Sets cell index on a game field.
     *
     * @param index Cell index
     * @throws IllegalStateException    If cell already has its index.
     * @throws IllegalArgumentException If index is negative.
     */
    public void setCellIndex(int index) {
        if (cellIndex != -1) {
            throw new IllegalStateException("Unable to change cell index.");
        }
        if (index < 0) {
            throw new IllegalArgumentException("Index cant be negative.");
        }
        cellIndex = index;
    }

    /**
     * Sets the cell following the given one on the game field.
     *
     * @param nextCell Reference to the next cell on a field
     * @throws IllegalStateException    If cell already has the next one.
     * @throws IllegalArgumentException If nextCell is null.
     */
    public void setNext(MonopolyCell nextCell) {
        if (next != null) {
            throw new IllegalStateException("Unable to change the next cell.");
        }
        if (nextCell == null) {
            throw new IllegalArgumentException("Cell on a game field can't be null.");
        }
        next = nextCell;
    }

    /**
     * Sets the cell going before the given one on a field.
     *
     * @param previousCell Reference to the previous cell on a field
     * @throws IllegalStateException    If cell already has the previous one.
     * @throws IllegalArgumentException If previousCell is null.
     */
    public void setPrevious(MonopolyCell previousCell) {
        if (previous != null) {
            throw new IllegalStateException("Unable to change the previous cell.");
        }
        if (previousCell == null) {
            throw new IllegalArgumentException("Cell on a game field can't be null.");
        }
        previous = previousCell;
    }

    /**
     * Sets the cell going before the given one on a field.
     *
     * @param player Player to check
     * @throws NullPointerException     If player is null.
     * @throws IllegalArgumentException If player is not at the cell.
     */
    protected void checkPlayerAtTheCell(Player player) {
        if (player == null) {
            throw new NullPointerException("Player was null.");
        }
        if (!player.getCurrentCell().equals(this)) {
            throw new IllegalArgumentException("Player was not at the cell of type " + this.getClass().getName() + ".");
        }
    }


    /**
     * Returns the string representation of the cell.
     */
    @Override
    public String toString() {
        return "| " + cellSymbol + " |";
    }


    /**
     * @return The cell following the given one on a field.
     */
    public MonopolyCell getNext() {
        return next;
    }

    /**
     * @return The cell going before the given one on a field.
     */
    public MonopolyCell getPrevious() {
        return previous;
    }

    /**
     * @return Cell index.
     */
    public int getCellIndex() {
        return cellIndex;
    }


    /**
     * Symbol representing the cell on the game field.
     */
    protected char cellSymbol;
    /**
     * Cell index.
     */
    protected int cellIndex = -1;
    /**
     * Reference to the next cell on a field.
     */
    protected MonopolyCell next = null;
    /**
     * Reference to the previous cell on a field.
     */
    protected MonopolyCell previous = null;
}
