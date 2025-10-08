import java.util.Scanner;


public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Board board = new Board();

    public static void main(String[] args) {
        System.out.println("Enter your name: ");
        String playerName = scanner.nextLine();
        Player p1 = new HumanPlayer(playerName, CellState.X);

        Difficulty difficulty = askDifficulty();
        Player p2 = new CpuPlayer("Bot", CellState.O, difficulty);

//        Create DialogueManager
        DialogueManager dialogueManager = new DialogueManager();

//        Ask about Dialogue should be on
        boolean enableDialogue = askEnableDialogue();

        GameController game = new GameController(board, p1, p2 ,true, DialogueManager, enableDialogue);

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

    private static boolean askEnableDialogue() {
        while (true) {
            System.out.println("Enable dialogue? y/n: ");
            String s = scanner.nextLine().trim().toLowerCase();
            if (s.equals("y") || s.equals("yes")) return true;
            if (s.equals("n") || s.equals("no")) return false;
            System.out.println("Please answer y or n.");
        }
    }
}