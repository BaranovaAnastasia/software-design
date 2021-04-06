package monopoly.monopolycells;

import monopoly.*;

/**
 * Class representing Bank office Monopoly cell.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru"> Anastasia Baranova</a>
 */
public class BankCell extends MonopolyCell {
    //Sets static variables
    static {
        double coefficient = ((int) (Math.random() * (3000 - 1000 + 1)) + 1000) * 1.0 / 1000;
        debtCoeff = coefficient == 1 ? 3 : coefficient;
        creditCoeff = ((int) (Math.random() * (200 - 2 + 1)) + 2) * 1.0 / 1000;
        MAX_COUNT = 4;
        count = 0;

        MESSAGE_FOR_DEBTOR = "You owe the bank $%s. Your debt will be paid now.";
        MESSAGE_OFFER = "Would you like to get a credit? Input how many you want to get or 'No'."
                + System.lineSeparator() + "Notice that maximum sum for you is $%s." + System.lineSeparator();
        MESSAGE_CREDIT_TAKEN = "You received a loan in the amount of $%s. Now your debt is $%s."
                + System.lineSeparator();
        MESSAGE_WRONG_INPUT = "Wrong input. Input how many you want to get or 'No'.";
    }

    /**
     * Constructor creates new Bank Monopoly cell.
     *
     * @throws IllegalStateException If maximum number of Bank cells
     *                               (4) has been reached.
     */
    public BankCell() {
        super('$');
        if (++count > MAX_COUNT) {
            throw new RuntimeException("The maximum number of Banks has been reached.");
        }
    }

    /**
     * Collects a debt from the player if they have one.
     * Offer the player a credit if they don't have one.
     *
     * @param player Player who has hit the cell
     * @throws NullPointerException     If player is null.
     * @throws IllegalArgumentException If player is not at the cell.
     */
    @Override
    public void hit(Player player) {
        checkPlayerAtTheCell(player);
        player.hitBankCell();
    }

    /**
     * Collects a debt from the player if they have one.
     *
     * @param player Debtor
     * @throws NullPointerException     If player is null.
     * @throws IllegalArgumentException If player is not at the bank cell.
     */
    public void payDebt(Player player) {
        checkPlayerAtTheCell(player);

        if (player.getDebt() > 0) {
            System.out.printf(MESSAGE_FOR_DEBTOR, player.getDebt());
            player.payDebt();
        }
    }

    /**
     * Offer the player a credit if they don't have one.
     *
     * @param player Player being offered a debt
     * @throws NullPointerException     If player is null.
     * @throws IllegalArgumentException If player is not at the bank cell.
     */
    public void offerCredit(Player player) {
        checkPlayerAtTheCell(player);

        if (player.getMaxCreditSum() == 0) {
            System.out.println("Can't get a credit yet. Spend some money on your shops first.");
            return;
        }

        System.out.printf(MESSAGE_OFFER, player.getMaxCreditSum());
        int request = getResponse(player);
        if (request == -1) {
            return;
        }
        player.getCredit(request);
        System.out.printf(MESSAGE_CREDIT_TAKEN, request, player.getDebt());
    }

    /**
     * Read player's response to the offer.
     *
     * @param player Player being offered a debt
     * @return -1 if player don't want a credit. Requested sum if they want.
     * @throws NullPointerException If player is null.
     */
    private static int getResponse(Player player) {
        //Ask player until their response is correct
        while (true) {
            System.out.print("> ");
            String response = MonopolyGame.reader.nextLine().toLowerCase();
            if (response.equals("no")) {
                return -1;
            }
            if (isNumeric(response)) {
                int request = Integer.parseInt(response);
                if (request <= player.getMaxCreditSum() && request > 0) {
                    return request;
                }
            }
            System.out.println(MESSAGE_WRONG_INPUT);
        }
    }

    /**
     * Checks if string is numeric.
     *
     * @param str String to test
     * @return true if string is numeric, false otherwise.
     */
    private static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * @return Bank debt coefficient.
     */
    public static double getDebtCoeff() {
        return debtCoeff;
    }

    /**
     * @return Bank credit coefficient.
     */
    public static double getCreditCoeff() {
        return creditCoeff;
    }


    /**
     * Maximum number of BankCell class instances
     * (maximum number of Bank cells on the game field).
     */
    private static final int MAX_COUNT;
    /**
     * Current number of BankCell class instances
     * (current number of Bank cells on the game field).
     */
    private static int count;

    /**
     * <p>Value that determines the amount of overpayment on a credit.</p>
     * <p>Amount of money to be returned to the bank:
     * the borrowed sum * debtCoeff.</p>
     */
    private static final double debtCoeff;
    /**
     * <p>Value that determines maximum possible credit sum for a player</p>
     * <p>max sum = creditCoeff * (amount of money spent by player
     * for every shop they bought or improved)</p>
     */
    private static final double creditCoeff;


    /**
     * Message displaying when player entered the cell has a debt.
     */
    private static final String MESSAGE_FOR_DEBTOR;
    /**
     * Message displaying to offer player a credit.
     */
    private static final String MESSAGE_OFFER;
    /**
     * Message displaying information about received credit.
     */
    private static final String MESSAGE_CREDIT_TAKEN;
    /**
     * Message displaying if player gave invalid response.
     */
    private static final String MESSAGE_WRONG_INPUT;
}
