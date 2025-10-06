import java.util.Scanner;


public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Board board = new Board();

    public static void main(String[] args) {


//        Create human player/s
        System.out.println("Enter your name: ");
        String playerName = scanner.nextLine();
        Player p1 = new HumanPlayer(playerName, CellState.X);

        Difficulty difficulty = askDifficulty();
        Player p2 = new CpuPlayer("Bot", CellState.O, difficulty);

        //        Example beneath when yo''l implements HumanPlayer and CpuPlayer

//        Implements by itself
//        Player p1 = new HumanPlayer("Alice", CellState.X);
//        Implements by itself
//        Player p2 = new CpuPlayer ("Bot", CellState.O, new RandomStrategy());

        GameController game = new GameController(board, p1, p2, true);
        game.play();

        scanner.close();
    }

        private static Difficulty askDifficulty() {
            while (true) {
                System.out.println(" Chose Difficulty : ");
                System.out.println(" 1. Easy Peasy ");
                System.out.println(" 2. Normal ");
                System.out.println(" 3. WRECKLESS! ");
                System.out.println(" Your Choice: ");

                String input = scanner.nextLine().trim();

                switch (input) {
                    case "1":
                        return Difficulty.EASY;
                    case "2":
                        return Difficulty.MEDIUM;
                    case "3":
                        return Difficulty.HARD;
                    default:
                        System.out.println("Invalid Choice wiseguy, try again");
                }
            }
        }

    }
