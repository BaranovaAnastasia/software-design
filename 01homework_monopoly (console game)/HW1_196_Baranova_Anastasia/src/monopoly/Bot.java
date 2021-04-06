package monopoly;

import monopoly.monopolycells.*;

/**
 * Class representing Monopoly game bot player.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru"> Anastasia Baranova</a>
 */
public class Bot extends Player {
    /**
     * Constructor creates new bot.
     *
     * @param game    Monopoly game player is about to participate in.
     * @param start   Starting cell of the monopoly field.
     * @param balance Player's balance at the beginning of the game.
     * @throws NullPointerException     If game is null.
     *                                  If starting cell is null.
     * @throws IllegalArgumentException If balance is less than zero.
     */
    public Bot(MonopolyGame game, MonopolyCell start, int balance) {
        super(game, start, balance);
    }

    /**
     * Prints message about bot visiting an empty cell.
     *
     * @throws IllegalStateException If player is not on an Empty cell.
     */
    @Override
    public void hitEmptyCell() {
        if (!currentCell.getClass().equals(EmptyCell.class)) {
            throw new IllegalStateException("Unable to execute Empty cell scenario. Player is not at an EmptyCell.");
        }
        System.out.println("Bot is currently relaxing at an empty cell.");
    }

    /**
     * Prints message about bot visiting a penalty cell and collects a penalty.
     *
     * @throws IllegalStateException If player is not on an Empty cell.
     */
    @Override
    public void hitPenaltyCell() {
        if (!currentCell.getClass().equals(PenaltyCell.class)) {
            throw new IllegalStateException("Unable to execute Penalty cell scenario. Player is not at a PenaltyCell.");
        }

        System.out.printf("Bot is at the Penalty cell %s. The $%s duty will be debited from its account."
                        + System.lineSeparator(), currentCell.getCellIndex(),
                (int) Math.round(balance * PenaltyCell.getPenaltyCoeff()));
        payPenalty();
    }

    /**
     * Moves bot on the field by the random (\in [3, 5]) number of cells.
     *
     * @throws IllegalStateException If player is not on a Taxi cell.
     * @throws NullPointerException  If field was created incorrectly and
     *                               not every cell has the next one.
     */
    @Override
    public void hitTaxiCell() {
        if (!currentCell.getClass().equals(TaxiCell.class)) {
            throw new IllegalStateException("Unable to execute Taxi cell scenario. Player is not at a TaxiCell.");
        }

        int taxiDistance = (int) (Math.random() * (5 - 3 + 1)) + 3;
        System.out.printf("Bot is taking a Taxi %s. It will be shifted forward by %s cells." + System.lineSeparator(),
                currentCell.getCellIndex(), taxiDistance);
        System.out.println("Press any key to continue...");
        MonopolyGame.reader.nextLine();

        move(taxiDistance);
    }

    /**
     * Prints a message about bot visiting a bank office.
     *
     * @throws IllegalStateException If player is not on a Bank cell
     */
    @Override
    public void hitBankCell() {
        if (!currentCell.getClass().equals(BankCell.class)) {
            throw new IllegalStateException("Unable to execute Bank cell scenario. Player is not at a BankCell.");
        }
        System.out.println("Bot is on a bank cell. It can't use it.");
    }

    /**
     * Executes Shop hitting scenario.
     *
     * @throws IllegalStateException If player is not on a Shop cell.
     */
    @Override
    public void hitShopCell() {
        if (!currentCell.getClass().equals(ShopCell.class)) {
            throw new IllegalStateException("Unable to execute Shop cell scenario. Player is not at a ShopCell.");
        }
        System.out.println("Bot is on the Shop " + currentCell.getCellIndex() + ".");
        onShop();
    }

    /**
     * Randomly decides to buy the shop or not.
     *
     * @throws IllegalStateException If player is not on a Shop cell.
     *                               If the shop already has an owner.
     */
    @Override
    protected void hitShopNoOwner() {
        if (!currentCell.getClass().equals(ShopCell.class)) {
            throw new IllegalStateException("Unable to execute Shop cell scenario. Player is not at a ShopCell.");
        }
        if (((ShopCell) currentCell).getOwner() != null) {
            throw new IllegalStateException("Shop has its owner");
        }

        if (balance >= ((ShopCell) currentCell).getPrice() && Math.random() < 0.5) {
            ((ShopCell) currentCell).buy(this);
            System.out.println("Bot has bought the Shop.");
            return;
        }
        System.out.println("Bot decided not to buy it.");
    }

    /**
     * Randomly decides to buy the shop or not.
     *
     * @throws IllegalStateException If player is not on a Shop cell.
     *                               If player is not an owner.
     */
    @Override
    protected void hitMyShop() {
        if (!currentCell.getClass().equals(ShopCell.class)) {
            throw new IllegalStateException("Unable to execute Shop cell scenario. Player is not at a ShopCell.");
        }
        if (!((ShopCell) currentCell).getOwner().equals(this)) {
            throw new IllegalStateException("Player is not an owner");
        }

        if (balance >= ((ShopCell) currentCell).getPrice() && Math.random() < 0.5) {
            System.out.println("Bot is at its shop. Bot decided to improve it.");
            ((ShopCell) currentCell).improve(this);
            return;
        }
        System.out.println("Bot is at its shop. Bot decided not to improve it.");
    }

    /**
     * Collects compensation for visiting other player's shop.
     *
     * @throws IllegalStateException If player is not on a Shop cell.
     *                               If the shop does not belong to the opponent.
     */
    @Override
    protected void hitOpponentShop() {
        if (!currentCell.getClass().equals(ShopCell.class)) {
            throw new IllegalStateException("Unable to execute Shop cell scenario. Player is not at a ShopCell.");
        }
        if (((ShopCell) currentCell).getOwner() == null ||
                ((ShopCell) currentCell).getOwner().getClass().equals(Bot.class)) {
            throw new IllegalStateException("It is not your opponent shop");
        }

        System.out.println("The shop is yours. You're receiving a compensation $"
                + ((ShopCell) currentCell).getCompensation() + ".");
        payShopCompensation(((ShopCell) currentCell).getOwner());
    }
}
