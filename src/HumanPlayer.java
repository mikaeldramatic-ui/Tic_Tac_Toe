import java.util.Scanner;

public class HumanPlayer extends Player{

private final Scanner scanner;

public HumanPlayer(String name, CellState symbol) {
    super(name, symbol);
//    Reads from Terminal
    this.scanner = new Scanner(System.in);
}

@Override
    public Move getMove(Board board) {
    while(true) {
        try {
            System.out.println(getName() + " (" + getSymbol() + "), choose row (0-2): ");
            int row = scanner.nextInt();
            System.out.println(getName() + " (" + getSymbol() + "), choose colon (0-2): ");
            int col = scanner.nextInt();

            if (row <0 || row >=3 || col <0 || col >=3) {
                System.out.println("Invalid choice, row and colon must be between 0 and 2.");
//                Will ask again

                continue;
            }

//            Valid move, returns Move
            return new Move(row,col);
        } catch (Exception e) {
//            If user write something that is not a number
            System.out.println("Invalid input, try again.");
//            Cleansing buffert
            scanner.nextLine();
            }
        }
    }
}
