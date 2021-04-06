package martians;

/**
 * Class representing martian family genealogical tree.
 * Allows to create and process reports on the families of Martians.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru"> Anastasia Baranova</a>
 */
public class GenealogicalTree {
    Martian<?> root;

    /**
     * Creates a new tree with a senior member of the given martian family as a root.
     *
     * @throws NullPointerException If martian is null
     */
    public GenealogicalTree(Martian<?> martian) {
        root = findRoot(martian);
    }

    public Martian<?> getRoot() {
        return root;
    }

    /**
     * Creates a report on the martian family.
     *
     * @throws NullPointerException If martian is null.
     */
    public String getReport() {
        return getReportShifted(root, 0);
    }

    /**
     * Recursively creating a report on a martian family.
     * Assumes that given martian is a family root.
     *
     * @param level Martian level in the family tree (0 for the root).
     * @throws NullPointerException     If martian is null.
     * @throws IllegalArgumentException If level is negative.
     */
    private String getReportShifted(Martian<?> martian, int level) {
        StringBuilder sb = new StringBuilder(" ".repeat(4 * level) + martian.toString() + "\n");
        for (var child : martian.getChildren()) {
            sb.append(getReportShifted(child, level + 1));
        }
        return sb.toString();
    }

    /**
     * Searches for the oldest ancestor of the given martian.
     *
     * @param martian The martian whose ancestor we are looking for
     * @throws NullPointerException If martian is null.
     */
    private Martian<?> findRoot(Martian<?> martian) {
        Martian<?> result = martian;
        while (result.getParent() != null) {
            result = result.getParent();
        }
        return result;
    }

    /**
     * Restores the Martian family according to the report on it.
     *
     * @param report Report on a martian family
     * @return A senior member of the family.
     * @throws NullPointerException     If report is null.
     * @throws IllegalArgumentException If report was invalid.
     */
    public static GenealogicalTree parseReport(String report) {
        // Splitting rows from the report to iterate through them.
        String[] martiansData = report.split("\n");

        // Extracting family types from the first martian in the report.
        String[] requirements = extractData(martiansData[0]);
        String requiredMartianType = requirements[0];
        String requiredCodeType = requirements[1];

        // Parsing family.
        InnovatorMartian<?> result;

        if (requiredCodeType.equals("String")) {
            result = fromReportStringCode(martiansData, requiredMartianType, new Position(0));
        } else if (requiredCodeType.equals("Integer")) {
            result = fromReportIntegerCode(martiansData, requiredMartianType, new Position(0));
        } else {
            result = fromReportDoubleCode(martiansData, requiredMartianType, new Position(0));
        }

        if (requiredMartianType.equals("ConservativeMartian")) {
            return new GenealogicalTree(result.createConservative());
        }
        return new GenealogicalTree(result);
    }

    /**
     * Extracts data about the martian from its string format.
     *
     * @param data Martian string format
     * @return Array of strings: {Martian type, Gene code type, Martian gene code}
     * @throws IllegalArgumentException If martian code type is not supported.
     */
    private static String[] extractData(String data) {
        String martianType, codeType, code;
        try {
            martianType = data.substring(0, data.indexOf(" "));

            codeType = data.substring(data.indexOf("(") + 1,
                    data.indexOf(":"));

            code = data.substring(data.indexOf(":") + 1,
                    data.length() - 1);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Incorrect line: " + data);
        }


        if (!codeType.equals("String") &&
                !codeType.equals("Integer") &&
                !codeType.equals("Double")) {
            throw new IllegalArgumentException("Unsupported code type: " + codeType);
        }

        return new String[]{martianType, codeType, code};
    }

    /**
     * Creates a new martian with String code based on its string format.
     *
     * @param martianData         Martian string format
     * @param requiredMartianType "InnovatorMartian" or "ConservativeMartian"
     * @return Parsed Innovator Martian
     * @throws IndexOutOfBoundsException If martian string format had invalid format.
     * @throws IllegalArgumentException  If martian code type is not supported.
     *                                   Or if martian type or its gene code type was incorrect.
     */
    private static InnovatorMartian<String> parseMartianStringCode(
            String martianData, String requiredMartianType) {

        String[] extractedData = extractData(martianData);

        if (!extractedData[0].equals(requiredMartianType)) {
            throw new IllegalArgumentException("Illegal martian type: " + extractedData[0] +
                    ". Required: " + requiredMartianType);
        }
        if (!extractedData[1].equals("String")) {
            throw new IllegalArgumentException("Illegal code type: " + extractedData[1] +
                    ". Required: String");
        }

        return new InnovatorMartian<>(extractedData[2]);
    }

    /**
     * Creates a new martian with Integer code based on its string format.
     *
     * @param martianData         Martian string format
     * @param requiredMartianType "InnovatorMartian" or "ConservativeMartian"
     * @return Parsed Innovator Martian
     * @throws IndexOutOfBoundsException If martian string format had invalid format.
     * @throws IllegalArgumentException  If martian code type is not supported.
     *                                   Or if martian type or its gene code type was incorrect.
     */
    private static InnovatorMartian<Integer> parseMartianIntegerCode(
            String martianData, String requiredMartianType) {

        String[] extractedData = extractData(martianData);

        if (!extractedData[0].equals(requiredMartianType)) {
            throw new IllegalArgumentException("Illegal martian type: " + extractedData[0] +
                    ". Required: " + requiredMartianType);
        }
        if (!extractedData[1].equals("Integer")) {
            throw new IllegalArgumentException("Illegal code type: " + extractedData[1] +
                    ". Required: Integer");
        }

        return new InnovatorMartian<>(Integer.parseInt(extractedData[2]));
    }

    /**
     * Creates a new martian with Double code based on its string format.
     *
     * @param martianData         Martian string format
     * @param requiredMartianType "InnovatorMartian" or "ConservativeMartian"
     * @return Parsed Innovator Martian
     * @throws IndexOutOfBoundsException If martian string format had invalid format.
     * @throws IllegalArgumentException  If martian code type is not supported.
     *                                   Or if martian type or its gene code type was incorrect.
     */
    private static InnovatorMartian<Double> parseMartianDoubleCode(
            String martianData, String requiredMartianType) {

        String[] extractedData = extractData(martianData);

        if (!extractedData[0].equals(requiredMartianType)) {
            throw new IllegalArgumentException("Illegal martian type: " + extractedData[0] +
                    ". Required: " + requiredMartianType);
        }
        if (!extractedData[1].equals("Double")) {
            throw new IllegalArgumentException("Illegal code type: " + extractedData[1] +
                    ". Required: Double");
        }

        return new InnovatorMartian<>(Double.parseDouble(extractedData[2]));
    }

    /**
     * Calculates level of a martian in a family tree based on its line in a report.
     *
     * @param line Report line
     * @throws IllegalArgumentException When detected that the report is invalid.
     */
    private static int getLevel(String line) {
        int shift = 0;
        for (int i = 0; i < line.length() && (line.charAt(i) == ' ' || line.charAt(i) == '\t'); i++) {
            if (line.charAt(i) == ' ') {
                shift++;
            } else {
                shift += 4;
            }
        }
        if (shift % 4 != 0) {
            throw new IllegalArgumentException("Invalid report. Line: " + line);
        }
        return shift / 4;
    }

    /**
     * Checks if the given position in a given array of
     * report lines is the end of a report or family tree level.
     *
     * @param martiansData Lines of a report
     * @param position     Position in a report
     * @param prevLevel    Level in the family tree of the previous martian in the report.
     * @throws IllegalArgumentException When detected that the report is invalid.
     */
    private static boolean isEnd(String[] martiansData, Position position, int prevLevel) {
        // Checking if position is out of the array bounds.
        if (position.get() >= martiansData.length) {
            return true;
        }

        int level = getLevel(martiansData[position.get()]);
        // Checking if its a child.
        if (level - prevLevel == 1) {
            return false;
        }
        // Checking if line is incorrect.
        if (level > prevLevel || level == 0 && position.get() != 0) {
            throw new IllegalArgumentException("Invalid report. Line: " + position.get());
        }
        return true;
    }

    /**
     * Recursively fills martian family.
     * For a family with String gene code.
     *
     * @param martiansData Lines of a report
     * @param position     Position in a report.
     * @throws IllegalArgumentException When detected that the report is invalid.
     */
    private static InnovatorMartian<String> fromReportStringCode(
            String[] martiansData, String requiredMartianType, Position position) {

        int level = getLevel(martiansData[position.get()]);

        InnovatorMartian<String> root
                = parseMartianStringCode(martiansData[position.get()].trim(), requiredMartianType);

        position.inc();

        while (true) {
            if (isEnd(martiansData, position, level)) {
                return root;
            }
            root.addChild(fromReportStringCode(martiansData, requiredMartianType, position));
        }
    }

    /**
     * Recursively fills martian family.
     * For a family with Integer gene code.
     *
     * @param martiansData Lines of a report
     * @param position     Position in a report.
     * @throws IndexOutOfBoundsException If some of the report lines had invalid format.
     * @throws IllegalArgumentException  When detected that the report is invalid.
     */
    private static InnovatorMartian<Integer> fromReportIntegerCode(
            String[] martiansData, String requiredMartianType, Position position) {

        int level = getLevel(martiansData[position.get()]);

        InnovatorMartian<Integer> root
                = parseMartianIntegerCode(martiansData[position.get()].trim(), requiredMartianType);

        position.inc();

        while (true) {
            if (isEnd(martiansData, position, level)) {
                return root;
            }
            root.addChild(fromReportIntegerCode(martiansData, requiredMartianType, position));
        }
    }

    /**
     * Recursively fills martian family.
     * For a family with Double gene code.
     *
     * @param martiansData Lines of a report
     * @param position     Position in a report.
     * @throws IndexOutOfBoundsException If some of the report lines had invalid format.
     * @throws IllegalArgumentException  When detected that the report is invalid.
     */
    private static InnovatorMartian<Double> fromReportDoubleCode(
            String[] martiansData, String requiredMartianType, Position position) {

        int level = getLevel(martiansData[position.get()]);

        InnovatorMartian<Double> root
                = parseMartianDoubleCode(martiansData[position.get()].trim(), requiredMartianType);

        position.inc();

        while (true) {
            if (isEnd(martiansData, position, level)) {
                return root;
            }
            root.addChild(fromReportDoubleCode(martiansData, requiredMartianType, position));
        }
    }

    /**
     * Utility Class allows to iterate through the report lines.
     */
    static class Position {
        private int position;

        Position(int a) {
            this.position = a;
        }

        void inc() {
            position++;
        }

        int get() {
            return position;
        }
    }
}

