package monopoly;

import monopoly.monopolycells.*;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class representing Monopoly game.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru"> Anastasia Baranova</a>
 */
public class MonopolyGame {
    static {
        reader = new Scanner(System.in);
    }

    /**
     * Constructor creates new game.
     *
     * @param height       Field height
     * @param width        Field width
     * @param startBalance Players' starting balance
     * @throws IllegalArgumentException If height or width is non-positive
     *                                  If balance is negative
     */
    public MonopolyGame(int height, int width, int startBalance) {
        if (height <= 0 || width <= 0) {
            throw new IllegalArgumentException("Non-positive field dimension.");
        }
        if (startBalance < 0) {
            throw new IllegalArgumentException("Negative starting balance.");
        }

        field = new GameField(width, height);
        player = new Player(this, field.getCorner(0), startBalance);
        bot = new Bot(this, field.getCorner(0), startBalance);
    }


    /**
     * Starts game and plays it until the the winner is determined.
     *
     * @throws IllegalStateException If Game is over.
     */
    public void play() {
        if (isOver) {
            throw new IllegalStateException("Unable to play the game. Game is already over.");
        }
        for (int i = 0; !isOver; i++) {
            //Prints move info and sets next player to move
            System.out.println(System.lineSeparator().repeat(4) + "- ".repeat(32));
            setCurrentPlayer();
            System.out.println((i + 1) + ". It's " + (currentPlayer == bot ? "Bot's " : "your ") + "move.");

            //Moves player
            int step = (int) (Math.random() * (6 - 1 + 1)) + 1;
            System.out.println("Player moves by " + step + " cells: "
                    + currentPlayer.getCurrentCell().getCellIndex() + " --> "
                    + (currentPlayer.getCurrentCell().getCellIndex() + step) % (2 * field.width + 2 * field.height - 4)
                    + System.lineSeparator());
            printCurrentBalances(true);

            currentPlayer.move(step);

            System.out.print("Press Enter to continue...");
            reader.nextLine();
        }
        reader.close();
    }

    /**
     * Sets the player to make the next move.
     */
    private void setCurrentPlayer() {
        //If its a first move - choose randomly
        if (currentPlayer == null) {
            if (Math.random() < 0.5) {
                currentPlayer = player;
            } else {
                currentPlayer = bot;
            }
            return;
        }

        //If its not a first move - change the player
        if (currentPlayer.equals(bot)) {
            currentPlayer = player;
            return;
        }
        currentPlayer = bot;
    }

    /**
     * Stops the game with current player as a loser.
     *
     * @throws IllegalStateException If current player hadn't lost yet.
     */
    void endGame() {
        if (currentPlayer.getBalance() >= 0) {
            throw new IllegalStateException("Player can only lose with negative balance.");
        }

        isOver = true;
        setCurrentPlayer();
        winner = currentPlayer;
    }

    /**
     * Prints game info.
     */
    public void printGameInfo() {
        System.out.println(System.lineSeparator() + "- ".repeat(32));
        System.out.printf("|\tcreditCoeff = %.3f                "
                + "|\t@ - Both      |\n", BankCell.getCreditCoeff());
        System.out.printf("|\tdebtCoeff = %.3f                  "
                + "|\tU - You       |\n", BankCell.getDebtCoeff());
        System.out.printf("|\tpenaltyCoeff = %.3f               "
                + "|\tB - Bot       |\n", PenaltyCell.getPenaltyCoeff());
        System.out.println("- ".repeat(32));
        printCurrentBalances(true);
    }

    /**
     * Prints current game values info.
     */
    public void printCurrentBalances(boolean printDebt) {
        System.out.print("Your balance: $" + player.getBalance());
        System.out.println("\tBot balance:  $" + bot.getBalance());
        if (printDebt) {
            System.out.println("Your debt:    $" + player.getDebt() + System.lineSeparator());
        }
    }

    public void printField() {
        field.printField();
    }

    /**
     * @return Game winner.
     */
    public Player getWinner() {
        return winner;
    }

    /**
     * Shows if game is over.
     */
    private boolean isOver = false;

    /**
     * Game field.
     */
    GameField field;

    /**
     * Real player.
     */
    private final Player player;
    /**
     * Bot player.
     */
    private final Bot bot;
    /**
     * Player making current move.
     */
    Player currentPlayer = null;
    /**
     * Game winner.
     */
    Player winner = null;

    /**
     * Player's responds reader.
     */
    public static Scanner reader;


    /**
     * Class representing Monopoly game field.
     */
    private class GameField {
        /**
         * Constructor creates new field.
         *
         * @param width  Field width
         * @param height Field height
         */
        GameField(int width, int height) {
            this.width = width;
            this.height = height;
            generateField();
        }

        /**
         * Generates random field of the given size.
         */
        void generateField() {
            setCorners();
            generateRow(cornerUL, cornerUR, width);
            generateRow(cornerUR, cornerLR, height);
            generateRow(cornerLR, cornerLL, width);
            generateRow(cornerLL, cornerUL, height);
        }


        /**
         * Creates corner cells.
         */
        private void setCorners() {
            cornerUL = new EmptyCell();
            cornerUL.setCellIndex(0);

            cornerUR = new EmptyCell();
            cornerUR.setCellIndex(width - 1);

            cornerLR = new EmptyCell();
            cornerLR.setCellIndex(width + height - 2);

            cornerLL = new EmptyCell();
            cornerLL.setCellIndex(2 * width + height - 3);
        }

        /**
         * Generates cells for one of the field rows.
         *
         * @param first  First cell of the row (corner cell)
         * @param last   Last cell of the row (corner cell)
         * @param length Row length
         */
        private void generateRow(EmptyCell first, EmptyCell last, int length) {
            ArrayList<MonopolyCell> row = new ArrayList<>(length);
            row.add(first);
            row.add(last);

            row.add(new BankCell());

            for (int i = 0; i < (int) (Math.random() * 3) && i < length - 1; ++i) {
                row.add(new TaxiCell());
            }

            for (int i = 0; i < (int) (Math.random() * 3) && i < length - row.size(); ++i) {
                row.add(new PenaltyCell());
            }

            for (int i = row.size(); i < length; ++i) {
                row.add(new ShopCell());
            }

            connectCells(row);
        }

        /**
         * Randomly connects cells.
         *
         * @param cells Cells to connect (first two items will be the first and the last, resp.)
         */
        private void connectCells(ArrayList<MonopolyCell> cells) {
            //Connect fist cell with the random second one
            MonopolyCell current = cells.remove(0);
            current.setNext(cells.remove((int) (Math.random() * (cells.size() - 1)) + 1));

            MonopolyCell previous = current;
            current = previous.getNext();

            //Randomly connect cells from second to penultimate
            while (cells.size() != 1) {
                current.setNext(cells.remove((int) (Math.random() * (cells.size() - 1)) + 1));
                current.setPrevious(previous);
                current.setCellIndex(current.getPrevious().getCellIndex() + 1);

                previous = current;
                current = current.getNext();
            }

            //Connect penultimate cell with the last one
            current.setNext(cells.remove(0));
            current.setPrevious(previous);
            current.setCellIndex(current.getPrevious().getCellIndex() + 1);
            current.getNext().setPrevious(current);
        }

        /**
         * Prints the field.
         */
        private void printField() {
            printUpperLine();
            printSideLines();
            printLowerLine();
        }

        /**
         * Prints player's symbol if any of the players is currently at the given cell
         * or space if non of them are.
         *
         * @param cell Cell to check
         */
        void printPosition(MonopolyCell cell) {
            if (player.getCurrentCell() == cell && bot.getCurrentCell() == cell) {
                System.out.print("  " + '@' + "  ");
                return;
            }
            if (player.getCurrentCell() == cell) {
                System.out.print("  " + 'U' + "  ");
                return;
            }
            if (bot.getCurrentCell() == cell) {
                System.out.print("  " + 'B' + "  ");
                return;
            }
            System.out.print("     ");
        }

        /**
         * Prints upper line of the field.
         */
        void printUpperLine() {
            System.out.print("     ");
            //Mark players on the field
            MonopolyCell current = cornerUL;
            for (int i = 0; i < width; i++) {
                printPosition(current);
                current = current.getNext();
            }
            System.out.println();

            //Print cells
            current = cornerUL;
            System.out.print("     ");
            for (int i = 0; i < width - 1; i++) {
                System.out.print(current);
                current = current.getNext();
            }
            System.out.println(current);
        }

        /**
         * Prints side lines of the field.
         */
        void printSideLines() {
            MonopolyCell currentLeft = cornerUL.getPrevious();
            MonopolyCell currentRight = cornerUR.getNext();
            for (int i = 0; i < height - 2; i++) {
                //Right cell
                printPosition(currentLeft);
                System.out.print(currentLeft);

                //Empty center space
                System.out.print("     ".repeat(width - 2));

                //Left cell
                System.out.print(currentRight);
                printPosition(currentRight);
                System.out.println();

                currentLeft = currentLeft.getPrevious();
                currentRight = currentRight.getNext();
            }
        }

        /**
         * Prints lower line of the field.
         */
        void printLowerLine() {
            //Print cells
            MonopolyCell current = cornerLL;
            System.out.print("     ");
            for (int i = 0; i < width; i++) {
                System.out.print(current);
                current = current.getPrevious();
            }
            System.out.println();

            //Mark players on the field
            current = cornerLL;
            System.out.print("     ");
            for (int i = 0; i < width; i++) {
                printPosition(current);
                current = current.getPrevious();
            }
            System.out.println();
        }

        /**
         * Field corner.
         *
         * @param cornerIndex Index of the required corner
         */
        MonopolyCell getCorner(int cornerIndex) {
            if (cornerIndex > 3 || cornerIndex < 0) {
                throw new IllegalArgumentException("Index of a corner must be in [0; 3]");
            }
            if (cornerIndex == 0) {
                return cornerUL;
            }
            if (cornerIndex == 1) {
                return cornerUR;
            }
            if (cornerIndex == 2) {
                return cornerLR;
            }
            return cornerLL;
        }

        /**
         * Field width.
         */
        int width;
        /**
         * Field height.
         */
        int height;


        /**
         * Upper left corner of the game field.
         */
        private EmptyCell cornerUL;
        /**
         * Upper right corner of the game field.
         */
        private EmptyCell cornerUR;
        /**
         * Lower right corner of the game field.
         */
        private EmptyCell cornerLR;
        /**
         * Lower left corner of the game field.
         */
        private EmptyCell cornerLL;
    }
}
