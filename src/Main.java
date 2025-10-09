import java.util.Scanner;


public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Board board = new Board();

    public static void main(String[] args) {

        System.out.println("Enter your name: ");
        String playerName = scanner.nextLine();
        Player p1 = new HumanPlayer(playerName, CellState.X);

        String opponentChoice;
        while(true) {
        System.out.println("Who do you want to play against?");
        System.out.println("1. Another player (Player 2)");
        System.out.println("2. CPU");
        System.out.println("Make your choice");
        opponentChoice = scanner.nextLine().trim();

        if (opponentChoice.equals("1") || opponentChoice.equals("2")) {
            break;
        }
        System.out.println("Are you blind?, choose between 1 or 2");
        }

        Player p2;

        if(opponentChoice.equals("1")) {
            System.out.println("Player 2, enter your name:");
            String PlayerName2 = scanner.nextLine();
            p2 = new HumanPlayer(PlayerName2 , CellState.O);
        }   else {
            Difficulty difficulty = askDifficulty();
            p2 = new CpuPlayer("Bot", CellState.O, difficulty);
        }
//        Create DialogManager
        DialogManager dialogManager = new DialogManager();

//        Ask about Dialogue should be on
        boolean enableDialog = askEnableDialog();

        GameController game = new GameController(board, p1, p2, true, dialogManager, enableDialog);

        game.play();
        scanner.close();

    }

    private static Difficulty askDifficulty() {
        while (true) {
            System.out.println(" Chose difficulty:");
            System.out.println(" 1. Easy Peasy");
            System.out.println(" 2. Normal");
            System.out.println(" 3. WRECKLESS");
            System.out.println("Your choice:");
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1": return Difficulty.EASY;
                case "2": return Difficulty.MEDIUM;
                case "3": return Difficulty.HARD;
                default: System.out.println("Invalid choice - try again wiseguy");
            }
        }
    }

    private static boolean askEnableDialog() {
        while (true) {
            System.out.println("Enable dialogue? y/n: ");
            String s = scanner.nextLine().trim().toLowerCase();
            if (s.equals("y") || s.equals("yes")) return true;
            if (s.equals("n") || s.equals("no")) return false;
            System.out.println("Use your eyes, y or n.");
        }
    }
}