package monopoly.monopolycells;

import monopoly.*;

/**
 * Class representing Shop Monopoly cell.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru"> Anastasia Baranova</a>
 */
public class ShopCell extends MonopolyCell {
    //Set static variables
    static {
        MESSAGE_NO_OWNER = "This shop has no owner. " +
                "Would you like to buy it for $%s?" + System.lineSeparator() +
                "Input 'Yes' if you agree or 'No' otherwise." + System.lineSeparator();
        MESSAGE_YOUR_SHOP = "It is your shop. Would you like to upgrade it for $%s?"
                + System.lineSeparator() + "Input 'Yes' if you agree or 'No' otherwise."
                + System.lineSeparator();
        MESSAGE_WRONG_INPUT = "Wrong input. Input 'Yes' if you agree or 'No' otherwise.";
        SHOP_INFO = "Shop %s: price = $%s, compensation = $%s." + System.lineSeparator();
    }

    /**
     * Constructor creates new Shop Monopoly cell.
     * Randomly sets shop coefficients.
     */
    public ShopCell() {
        super('S');
        compensationCoeff = ((int) (Math.random() * (1000 - 100 + 1)) + 100) * 1.0 / 1000;
        improvementCoeff = ((int) (Math.random() * (2000 - 100 + 1)) + 100) * 1.0 / 1000;
        price = (int) (Math.random() * (500 - 50 + 1)) + 50;
        double coefficient = Math.random() * (0.9 - 0.5) + 0.5;
        compensation = (int) (coefficient * price) > 0.5 * price
                ? (int) (coefficient * price)
                : Math.random() < 0.5
                ? (int) Math.ceil(0.5 * price)
                : (int) Math.floor(0.9 * price);
    }

    /**
     * If shop has no owner - suggests to buy the shop.
     * If player is the shop's owner - suggests to improve it.
     * If shop belongs to the players opponent - collects a
     * compensation from the player for visiting the cell.
     *
     * @param player Player who has hit the cell.
     * @throws NullPointerException     If player is null.
     * @throws IllegalArgumentException If player is not at the cell.
     */
    @Override
    public void hit(Player player) {
        checkPlayerAtTheCell(player);
        player.hitShopCell();
        player.getGame().printCurrentBalances(false);
    }

    /**
     * Suggests player to buy the shop, collects their response
     * and sell shop if player wants to buy it.
     *
     * @param player Player being suggested to buy the shop.
     * @throws NullPointerException     If player is null.
     * @throws IllegalArgumentException If player is not at the cell.
     */
    public void suggestToBuy(Player player) {
        checkPlayerAtTheCell(player);

        System.out.printf(MESSAGE_NO_OWNER, price);
        if (getPlayerResponse()) {
            if (player.getBalance() < price) {
                System.out.println("Not enough money.");
                return;
            }
            buy(player);
            System.out.println("Now it is your shop.");
        }
    }

    /**
     * Sells the shop to the player.
     *
     * @param player Player buying the shop.
     * @throws NullPointerException     If player is null.
     * @throws IllegalArgumentException If player is not at the cell.
     * @throws IllegalStateException    If the shop already has its owner.
     */
    public void buy(Player player) {
        checkPlayerAtTheCell(player);

        if (player.getCurrentCell() != this) {
            throw new IllegalArgumentException("Player can't buy shop while not being in it.");
        }
        if (owner != null) {
            throw new IllegalStateException("Shop already has an owner.");
        }
        player.myShopOperation();
        owner = player;

        if (player.getClass() == Bot.class) {
            cellSymbol = 'O';
        } else {
            cellSymbol = 'M';
        }
    }

    /**
     * Suggests player to improve the shop, collects their response
     * and improve shop if player wants to.
     *
     * @param player Player being suggested to improve their shop
     * @throws NullPointerException     If player is null.
     * @throws IllegalArgumentException If player is not at the cell.
     *                                  If player is not an owner.
     */
    public void suggestToImprove(Player player) {
        checkPlayerAtTheCell(player);
        if (player != owner) {
            throw new IllegalArgumentException("Only owner can improve their shop.");
        }

        System.out.printf(MESSAGE_YOUR_SHOP, Math.round(price + improvementCoeff * price));
        if (getPlayerResponse()) {
            if (player.getBalance() < Math.round(price + improvementCoeff * price)) {
                System.out.println("Not enough money.");
                return;
            }
            improve(player);
            System.out.println("Shop was improved.");
            printInfo();
        }
    }

    /**
     * Improves the shop.
     *
     * @param player Player being improving their shop
     * @throws NullPointerException     If player is null.
     * @throws IllegalArgumentException If player is not at the cell.
     *                                  If player is not an owner.
     */
    public void improve(Player player) {
        checkPlayerAtTheCell(player);

        if (player != owner) {
            throw new IllegalArgumentException("Only owner can improve their shop.");
        }

        if (player.getBalance() < Math.round(price + improvementCoeff * price)) {
            throw new IllegalStateException("Not enough money to improve the shop.");
        }

        price += Math.round(price * improvementCoeff);
        compensation += Math.round(compensation * compensationCoeff);
        player.myShopOperation();
    }

    /**
     * Read player's response from console.
     *
     * @return true - "Yes", false - "No".
     */
    private boolean getPlayerResponse() {
        //Ask until the response is correct
        while (true) {
            System.out.print("> ");
            String response = MonopolyGame.reader.nextLine().toLowerCase();
            if (response.equals("no")) {
                return false;
            }
            if (response.equals("yes")) {
                return true;
            }
            System.out.println(MESSAGE_WRONG_INPUT);
        }
    }

    /**
     * Prints shop information.
     */
    public void printInfo() {
        System.out.printf(SHOP_INFO, cellIndex, price, compensation);
    }


    /**
     * @return Shop price.
     */
    public int getPrice() {
        return price;
    }

    /**
     * @return Compensation for visiting this shop
     */
    public int getCompensation() {
        return compensation;
    }

    /**
     * @return Owner of the shop
     */
    public Player getOwner() {
        return owner;
    }


    // Fields

    /**
     * Owner of the shop
     */
    private Player owner = null;
    /**
     * Shop price
     */
    private int price;
    /**
     * Compensation for visiting this shop
     */
    private int compensation;
    /**
     * Shop improvement coefficient
     */
    private final double improvementCoeff;
    /**
     * Shop compensation improvement coefficient
     */
    private final double compensationCoeff;


    //Static fields

    /**
     * Message displaying for the player when they enter the shop with no owner
     */
    private static final String MESSAGE_NO_OWNER;
    /**
     * Message displaying for the player when they enter their own shop
     */
    private static final String MESSAGE_YOUR_SHOP;
    /**
     * Message displaying if player gave invalid response
     */
    private static final String MESSAGE_WRONG_INPUT;
    /**
     * Format string for displaying shop information
     */
    private static final String SHOP_INFO;
}
