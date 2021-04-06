package monopoly;

import monopoly.monopolycells.*;

/**
 * Class representing Monopoly game player
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru"> Anastasia Baranova</a>
 */
public class Player {
    /**
     * Constructor creates new player.
     *
     * @param game    Monopoly game player is about to participate in.
     * @param start   Starting cell of the monopoly field.
     * @param balance Player's balance at the beginning of the game.
     * @throws NullPointerException     If game is null.
     *                                  If starting cell is null.
     * @throws IllegalArgumentException If balance is less than zero.
     */
    public Player(MonopolyGame game, MonopolyCell start, int balance) {
        if (game == null) {
            throw new NullPointerException("game was null.");
        }
        if (start == null) {
            throw new NullPointerException("start was null.");
        }
        if (balance < 0) {
            throw new IllegalArgumentException("Illegal starting balance. Value was less than zero.");
        }

        this.game = game;
        this.currentCell = start;
        this.balance = balance;
        moneySpent = 0;
        debt = 0;
    }

    /**
     * Moves player on the field.
     *
     * @param step Number of cells to move player by.
     * @throws NullPointerException If field was created incorrectly and
     *                              some cell does not have the next one.
     */
    public void move(int step) {
        for (int i = 0; i < step; i++) {
            currentCell = currentCell.getNext();
        }

        game.printField();
        currentCell.hit(this);
    }


    //EmptyCell region

    /**
     * Prints message about player visiting an empty cell.
     *
     * @throws IllegalStateException If player is not on an Empty cell
     */
    public void hitEmptyCell() {
        if (!currentCell.getClass().equals(EmptyCell.class)) {
            throw new IllegalStateException("Unable to execute Empty cell scenario. Player is not at an EmptyCell.");
        }

        System.out.println("You're at the Empty cell. Just relax there.");
    }

    //end EmptyCell region


    //PenaltyCell region

    /**
     * Prints message about player visiting a penalty cell and collects a penalty.
     *
     * @throws IllegalStateException If player is not on a Penalty cell.
     */
    public void hitPenaltyCell() {
        if (!currentCell.getClass().equals(PenaltyCell.class)) {
            throw new IllegalStateException("Unable to execute Penalty cell scenario. Player is not at a PenaltyCell.");
        }

        System.out.printf("You're at the Penalty cell %s. The $%s duty will be debited from your account."
                        + System.lineSeparator(),
                currentCell.getCellIndex(),
                (int) Math.round(balance * PenaltyCell.getPenaltyCoeff()));
        payPenalty();
    }

    /**
     * Collects penalty from player for visiting a PenaltyCell
     *
     * @throws IllegalStateException If player is not on a Penalty cell
     */
    public void payPenalty() {
        if (!currentCell.getClass().equals(PenaltyCell.class)) {
            throw new IllegalStateException("Unable to pay penalty while not being at a PenaltyCell.");
        }

        balance -= (int) Math.round(balance * PenaltyCell.getPenaltyCoeff());
        if (balance < 0) {
            lose();
        }
    }

    //end PenaltyCell region


    //TaxiCell region

    /**
     * Moves player on the field by the random (\in [3, 5]) number of cells.
     *
     * @throws IllegalStateException If player is not on a Taxi cell.
     * @throws NullPointerException  If field was created incorrectly and
     *                               not every cell has the next one.
     */
    public void hitTaxiCell() {
        if (!currentCell.getClass().equals(TaxiCell.class)) {
            throw new IllegalStateException("Unable to execute Taxi cell scenario. Player is not at a TaxiCell.");
        }

        int taxiDistance = (int) (Math.random() * (5 - 3 + 1)) + 3;
        System.out.printf("You're taking a Taxi %s. You will be shifted forward by %s cells." + System.lineSeparator(),
                currentCell.getCellIndex(), taxiDistance);

        System.out.print("Press Enter to continue...");
        MonopolyGame.reader.nextLine();

        move(taxiDistance);
    }

    //end TaxiCell region


    //BankCell region

    /**
     * Collects player's debt or offers a credit for them.
     *
     * @throws IllegalStateException If player is not on a Bank cell
     */
    public void hitBankCell() {
        if (!currentCell.getClass().equals(BankCell.class)) {
            throw new IllegalStateException("Unable to execute Bank cell scenario. Player is not at a BankCell.");
        }

        System.out.println("You are in the bank office " + currentCell.getCellIndex() + ".");
        if (debt > 0) {
            ((BankCell) currentCell).payDebt(this);
            return;
        }
        ((BankCell) currentCell).offerCredit(this);
    }

    /**
     * Returns debt to the bank.
     *
     * @throws IllegalStateException If player is not on a Bank cell.
     */
    public void payDebt() {
        if (!currentCell.getClass().equals(BankCell.class)) {
            throw new IllegalStateException("Unable to pay debt while not being at a BankCell.");
        }

        balance -= debt;
        System.out.printf("You had a $%s debt. It is payed now."
                + System.lineSeparator(), debt, balance);
        debt = 0;
        if (balance < 0) {
            lose();
        }
    }

    /**
     * Takes a credit from the Bank.
     *
     * @param creditSum Sum to take from bank.
     * @throws IllegalStateException    If player is not on a Bank cell.
     * @throws IllegalArgumentException If sum is less or equal to zero.
     */
    public void getCredit(int creditSum) {
        if (!currentCell.getClass().equals(BankCell.class)) {
            throw new IllegalStateException("Unable to take a loan while not being at a BankCell.");
        }

        if (creditSum <= 0) {
            throw new IllegalArgumentException("Credit sum should be grater then zero.");
        }

        balance += creditSum;
        debt += (int) Math.round(creditSum * BankCell.getDebtCoeff());
        System.out.printf("You have successfully received a credit. Your balance: $%s, your debt: $%s."
                + System.lineSeparator(), balance, debt);
    }

    //end BankCell region


    //ShopCell region

    /**
     * Executes Shop hitting scenario.
     *
     * @throws IllegalStateException If player is not on a Shop cell.
     */
    public void hitShopCell() {
        if (!currentCell.getClass().equals(ShopCell.class)) {
            throw new IllegalStateException("Unable to execute Shop cell scenario. Player is not at a ShopCell.");
        }

        System.out.println("You're at the Shop " + currentCell.getCellIndex() + ".");
        onShop();
    }

    /**
     * Shop scenario structure.
     * Allows player to buy the shop (if it does not have an owner) or improve their shop.
     * Collects compensation for visiting to the opponent's shop.
     *
     * @throws IllegalStateException If player is not on a Shop cell.
     */
    protected void onShop() {
        if (!currentCell.getClass().equals(ShopCell.class)) {
            throw new IllegalStateException("Unable to execute Shop cell scenario. Player is not at a ShopCell.");
        }

        ((ShopCell) currentCell).printInfo();

        if (((ShopCell) currentCell).getOwner() == null) {
            //Suggest to buy
            hitShopNoOwner();
        } else {
            if (((ShopCell) currentCell).getOwner().getClass().equals(this.getClass())) {
                //Suggest to improve
                hitMyShop();
            } else {
                //Pay compensation
                hitOpponentShop();
            }
        }
    }

    /**
     * Allows to buy the shop that does not has its owner yet.
     *
     * @throws IllegalStateException If player is not on a Shop cell.
     *                               If the shop already has an owner.
     */
    protected void hitShopNoOwner() {
        if (!currentCell.getClass().equals(ShopCell.class)) {
            throw new IllegalStateException("Unable to execute Shop cell scenario. Player is not at a ShopCell.");
        }

        if (((ShopCell) currentCell).getOwner() != null) {
            throw new IllegalStateException("Shop already has its owner.");
        }
        ((ShopCell) currentCell).suggestToBuy(this);
    }

    /**
     * Allows to improve the shop.
     *
     * @throws IllegalStateException If player is not on a Shop cell.
     *                               If player is not an owner.
     */
    protected void hitMyShop() {
        if (!currentCell.getClass().equals(ShopCell.class)) {
            throw new IllegalStateException("Unable to execute Shop cell scenario. Player is not at a ShopCell.");
        }

        if (!((ShopCell) currentCell).getOwner().equals(this)) {
            throw new IllegalStateException("Player is not an owner.");
        }
        ((ShopCell) currentCell).suggestToImprove(this);
    }

    /**
     * Collects compensation for visiting other player's shop.
     *
     * @throws IllegalStateException If player is not on a Shop cell.
     *                               If the shop does not belong to the opponent.
     */
    protected void hitOpponentShop() {
        if (!currentCell.getClass().equals(ShopCell.class)) {
            throw new IllegalStateException("Unable to execute Shop cell scenario. Player is not at a ShopCell.");
        }

        if (((ShopCell) currentCell).getOwner() == null &&
                !((ShopCell) currentCell).getOwner().getClass().equals(Bot.class)) {
            throw new IllegalStateException("It is not your opponent's shop.");
        }

        System.out.println("It is your opponent's shop. You have to pay compensation in the amount of $"
                + ((ShopCell) currentCell).getCompensation() + "." + System.lineSeparator());
        payShopCompensation(((ShopCell) currentCell).getOwner());
    }

    /**
     * Pays for buying or improving the shop.
     *
     * @throws IllegalStateException If player is not on a Shop cell.
     *                               If player don't have enough money to complete the operation.
     */
    public void myShopOperation() {
        if (!currentCell.getClass().equals(ShopCell.class)) {
            throw new IllegalStateException("Unable to buy or improve a shop while not being at a ShopCell.");
        }

        double price = ((ShopCell) currentCell).getPrice();
        if (balance < price) {
            throw new IllegalStateException("Not enough money.");
        }

        balance -= price;
        moneySpent += price;
    }

    /**
     * Pays compensation for visiting other player's shop.
     *
     * @param owner Shop owner.
     * @throws IllegalStateException If player is not on a Shop cell.
     *                               If owner does not owe the shop.
     * @throws NullPointerException  If owner value is null.
     */
    protected void payShopCompensation(Player owner) {
        if (!currentCell.getClass().equals(ShopCell.class)) {
            throw new IllegalStateException("Unable to pay compensation while not being at a ShopCell.");
        }

        if (owner == null) {
            throw new NullPointerException("owner was null.");
        }

        if (!((ShopCell) currentCell).getOwner().equals(owner)) {
            throw new IllegalStateException("Unable to send compensation for a player who does not owe the shop.");
        }

        balance -= ((ShopCell) currentCell).getCompensation();
        owner.receiveCompensation(this);
        if (balance < 0) {
            lose();
        }
    }

    /**
     * Receives compensation from other player.
     *
     * @param opponent Player sending compensation.
     * @throws IllegalStateException If opponent is not on a Shop cell.
     *                               If player does not owe the shop.
     * @throws NullPointerException  If opponent is null.
     */
    private void receiveCompensation(Player opponent) {
        if (!opponent.getCurrentCell().getClass().equals(ShopCell.class)) {
            throw new IllegalStateException("Unable to receive compensation while opponent is not at a ShopCell");
        }

        if (!((ShopCell) opponent.currentCell).getOwner().equals(this)) {
            throw new IllegalStateException("Unable to receive compensation for a shop that doesn't belong to the player.");
        }

        balance += ((ShopCell) opponent.currentCell).getCompensation();
    }

    //end ShopCell region


    /**
     * Ends game with this player as a loser.
     *
     * @throws IllegalStateException If player is not a loser.
     */
    private void lose() {
        if (balance >= 0) {
            throw new IllegalStateException("Player can't lose without losing all of their money.");
        }
        game.endGame();
    }


    /**
     * @return Cell on the field where the player currently is.
     */
    public MonopolyCell getCurrentCell() {
        return currentCell;
    }

    /**
     * @return Player's debt.
     */
    public int getDebt() {
        return debt;
    }

    /**
     * @return Maximum sum that can be received from a bank.
     */
    public int getMaxCreditSum() {
        return (int) Math.round(BankCell.getCreditCoeff() * moneySpent);
    }

    /**
     * @return Player's balance.
     */
    public int getBalance() {
        return balance;
    }

    /**
     * @return Game the player is participating.
     */
    public MonopolyGame getGame() {
        return game;
    }

    /**
     * Game the player is participating
     */
    private final MonopolyGame game;
    /**
     * Cell on the field where the player currently is
     */
    protected MonopolyCell currentCell;
    /**
     * Player's debt
     */
    private int debt;
    /**
     * Amount of money spent by player on
     * buying and improving their shops
     */
    private int moneySpent;
    /**
     * Player's balance
     */
    protected int balance;
}
