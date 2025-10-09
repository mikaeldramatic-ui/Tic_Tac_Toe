import java.util.Scanner;

public class HumanPlayer extends Player {

    private final Scanner scanner;

    public HumanPlayer(String name, CellState symbol) {
        super(name, symbol);
//    Reads from Terminal
        this.scanner = new Scanner(System.in);
    }

    @Override
    public Move getMove(Board board) {
        int row;
        int col;

        while (true) {

            while (true) {
                System.out.println(getName() + " (" + getSymbol() + "), choose between (0-" + (Board.SIZE - 1) + "): ");
                String rowLine = scanner.nextLine().trim();

                if(rowLine.equalsIgnoreCase("q")) {
                    System.out.println(" choosed to quit, thanks for playing!");
                    System.exit(0);
                }

                try {
                    row = Integer.parseInt(rowLine);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid" + getName() + ", choose a number for row");
                    continue;
                }
                if (row < 0 || row >= Board.SIZE) {
                    System.out.println("Invalid row" + getName() + ", choose between number 0-" + (Board.SIZE - 1) + ".");
                    continue;
                }
                break;
            }
            while (true) {
                System.out.println(getName() + " (" + getSymbol() + "), choose between (0-" + (Board.SIZE - 1)
                        + ") or type 'r' to choose row again or 'q' to quit: ");
                String colLine = scanner.nextLine().trim();

                if(colLine.equalsIgnoreCase("q")) {
                    System.out.println(" choosed to quit, thanks for playing!");
                    System.exit(0);
                }

                if (colLine.equalsIgnoreCase("r")) {
                    System.out.println("Return to choose row.");
                    break;

                }
                try {
                    col = Integer.parseInt(colLine);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid" + getName() + ", choose a number for colon");
                    continue;
                }
                if (col < 0 || col >= Board.SIZE) {
                    System.out.println("Invalid colon" + getName() + ", choose between number 0-" + (Board.SIZE - 1) + ".");
                    continue;
                }
                if (!board.isEmpty(row, col)) {
                    System.out.println("Square (" + row + "," + col + ") is already taken, choose another colon or press 'r' to choose row again");
                    continue;
                }

                return new Move(row, col);
            }
        }
    }
}