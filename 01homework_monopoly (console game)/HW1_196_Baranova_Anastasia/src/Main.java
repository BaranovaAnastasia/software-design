import monopoly.*;

public class Main {
    public static void main(String[] args) {
        int[] parameters = parseInput(args);
        if (parameters == null || !checkInput(parameters)) {
            return;
        }
        playMonopoly(parameters);
    }

    /**
     * Parse program input arguments into array of
     * field height, width and players' budget values.
     *
     * @param args Program input arguments in order: {height, width, money}
     * @return Int array containing monopoly game data:
     * {height, width, money} or null if arguments have invalid format
     */
    private static int[] parseInput(String[] args) {
        //Check the amount of arguments
        if (args.length < 3) {
            System.out.println("Not enough input argument. Unable to start the game.");
            return null;
        }

        int[] result = new int[3];
        //Parse each argument
        result[0] = parseGameParameter(args[0]);
        result[1] = parseGameParameter(args[1]);
        result[2] = parseGameParameter(args[2]);
        if (result[0] == -1 || result[1] == -1 || result[2] == -1) {
            return null;
        }
        return result;
    }

    /**
     * Parse string value into number.
     *
     * @param arg Game parameter
     * @return Integer game parameter or -1 if value was invalid
     */
    private static int parseGameParameter(String arg) {
        int result;
        try {
            result = Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input argument 'height': " + arg + ". Unable to start the game.");
            return -1;
        }
        return result;
    }

    /**
     * Checks if parsed program input arguments satisfy its requirements.
     *
     * @param args Arguments in order: {height, width, money}
     * @return true if arguments are ok, false otherwise
     */
    private static boolean checkInput(int[] args) {
        if (args[0] < 6 || args[0] > 30) {
            System.out.println("Argument 1 'height' out of range [6, 30]: " + args[0] + ". Unable to start the game.");
            return false;
        }
        if (args[1] < 6 || args[1] > 30) {
            System.out.println("Argument 2 'width' out of range [6, 30]: " + args[1] + ". Unable to start the game.");
            return false;
        }
        if (args[2] < 500 || args[2] > 15000) {
            System.out.println("Argument 3 'money' out of range [500, 15000]: " + args[2] + ". Unable to start the game.");
            return false;
        }
        return true;
    }

    /**
     * Creates Monopoly game with given parameters,
     * plays it and outputs the winner.
     *
     * @param gameParameters Arguments in order: {height, width, money}
     */
    private static void playMonopoly(int[] gameParameters) {
        MonopolyGame game = new MonopolyGame(gameParameters[0], gameParameters[1], gameParameters[2]);

        game.printGameInfo();
        game.printField();

        System.out.print("Press Enter to start the game...");
        MonopolyGame.reader.nextLine();

        game.play();

        printWinner(game);
    }

    /**
     * Prints game winner.
     *
     * @param game Monopoly game which winner's needed to be output.
     * @throws NullPointerException If winner has not been determined yet.
     */
    private static void printWinner(MonopolyGame game) {
        Player winner = game.getWinner();
        if (winner.getClass().equals(Bot.class)) {
            System.out.println("Game over.\tWinner: Bot.");
            return;
        }
        System.out.println("Game over.\tWinner: You!");
    }
}
