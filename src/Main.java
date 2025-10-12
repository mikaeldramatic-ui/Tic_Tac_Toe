import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        String playerName = Welcome.show(scanner);
        Player p1 = new HumanPlayer(playerName, CellState.X);

        String opponentChoice;
        while (true) {
            System.out.println("Who do you want to play against?");
            System.out.println();
            System.out.println("1. Another player (Player 2)");
            System.out.println("2. CPU");
            System.out.println();
            System.out.print("Make your choice: ");
            opponentChoice = scanner.nextLine().trim();

            if (opponentChoice.equals("1") || opponentChoice.equals("2")) break;
            System.out.println("Are you blind? Choose 1 or 2.");
        }

        Player p2;
        if (opponentChoice.equals("1")) {
            System.out.print("Player 2, enter your name: ");
            String playerName2 = scanner.nextLine().trim();
            if (playerName2.isEmpty()) playerName2 = "Player2";
            p2 = new HumanPlayer(playerName2, CellState.O);
        } else {
            Difficulty difficulty = askDifficulty();
            p2 = new CpuPlayer("Bot", CellState.O, difficulty);
        }

        DialogManager dialogManager = new DialogManager();
        boolean enableDialog = askEnableDialog();


        menuLoop:
        while (true) {
            System.out.println();
            System.out.println("=== MAIN MENU ===");
            System.out.println("1. Quick Match");
            System.out.println("2. Campaign (Story)");
            System.out.println("3. View Highscores");
            System.out.println("4. Quit");
            System.out.println();
            System.out.print("Choose: ");

            String raw = scanner.nextLine();
            if (raw == null) continue;
            String choice = raw.trim();
            if (choice.isEmpty()) {
                continue;
            }

            switch (choice) {
                case "1": {

                    Board board = new Board();
                    GameController game = new GameController(board, p1, p2, true, dialogManager, enableDialog);
                    game.play();
                    break;
                }
                case "2": {

                    StoryManager story = new StoryManager(dialogManager, enableDialog, scanner);
                    story.startCampaign(p1, p2);
                    break;
                }
                case "3": {
                    HighscoreManager hs = new HighscoreManager();
                    System.out.println();
                    System.out.println("=== HIGHSCORES ===");
                    System.out.println();

                    for (HighscoreManager.HighscoreEntry e : hs.loadTop(10)) {
                        System.out.println(e);
                    }
                    break;
                }
                case "4": {
                    System.out.println("Goodbye!");
                    break menuLoop;
                }
                default:
                    System.out.println("Invalid choice, try again.");
            }
        }

        scanner.close();
    }

    private static Difficulty askDifficulty() {
        while (true) {
            System.out.println(" Choose difficulty:");
            System.out.println();
            System.out.println("1. Easy Peasy");
            System.out.println("2. Normal");
            System.out.println("3. WRECKLESS");
            System.out.println();
            System.out.print("Your choice: ");
            String input = scanner.nextLine().trim();
            System.out.println();
            switch (input) {
                case "1": return Difficulty.EASY;
                case "2": return Difficulty.MEDIUM;
                case "3": return Difficulty.HARD;
                default: System.out.println("Invalid choice - try again.");
            }
        }
    }

    private static boolean askEnableDialog() {
        while (true) {
            System.out.print("Enable dialogue? y/n: ");
            System.out.println();
            String s = scanner.nextLine().trim().toLowerCase();
            if (s.equals("y") || s.equals("yes")) return true;
            if (s.equals("n") || s.equals("no")) return false;
            System.out.println("Please answer y or n.");

        }
    }
}