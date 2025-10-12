import java.util.Optional;
import java.util.Scanner;

public class GameController {
    private Board board;
    private Player currentPlayer;
    private Player otherPlayer;
    private final DialogManager dialogManager;
    private boolean showDialogueToConsole;
    private boolean verbose;

    public GameController(Board board, Player player1, Player player2,
                          boolean verbose, DialogManager dialogManager, boolean showDialogueToConsole) {
        this.board = board;
        this.currentPlayer = player1;
        this.otherPlayer = player2;
        this.verbose = verbose;
        this.dialogManager = dialogManager;
        this.showDialogueToConsole = showDialogueToConsole;
    }
    public GameController(Board board, Player player1, Player player2,
                          boolean verbose, DialogManager dialogManager) {
        this(board, player1, player2, verbose, dialogManager, true);
    }

    public void setShowDialogueToConsole(boolean enabled) {
        this.showDialogueToConsole = enabled;
    }

    public boolean isShowDialogueToConsole() {
        return showDialogueToConsole;
    }


    public void play() {
        boolean keepPlaying = true;
        Scanner sc = new Scanner(System.in);

        while (keepPlaying) {
            board.initialize();

            if (verbose) {
                System.out.println("Let the game begin!");
                printBoard();
            }

            Optional<CellState> maybeWinner = playSingleRound();

            keepPlaying = askPlayAgain(sc);
            if (keepPlaying) {
                swapPlayers();
            } else {
                System.out.println("Thanks for playing!");
            }
        }
    }

    public Optional<CellState> playSingleRound() {

        while (true) {
            String pre = dialogManager.getLine(currentPlayer, otherPlayer, DialogEvent.PRE_MOVE, null);
            if (!pre.isEmpty() && showDialogueToConsole) {
                System.out.println(currentPlayer.getName() + " says: " + pre);
            }

            Move move = currentPlayer.getMove(board);

            boolean success = board.setMove(move.getRow(), move.getCol(), currentPlayer.getSymbol());
            if (!success) {
                if (verbose) {
                    System.out.println(currentPlayer.getName() + " made invalid move " + move + ". Try again!");
                }
                String illegal = dialogManager.getLine(currentPlayer, otherPlayer, DialogEvent.ILLEGAL_MOVE, move);
                if (!illegal.isEmpty() && showDialogueToConsole) {
                    System.out.println(currentPlayer.getName() + " says: " + illegal);
                }
                continue;
            }

            String post = dialogManager.getLine(currentPlayer, otherPlayer, DialogEvent.POST_MOVE, move);
            if (!post.isEmpty() && showDialogueToConsole) {
                System.out.println(currentPlayer.getName() + " says: " + post);
            }

            if (verbose) {
                System.out.println(currentPlayer.getName() + " played " + move + " as " + currentPlayer.getSymbol());
                printBoard();
            }

            Optional<CellState> maybeWinner = board.checkWinner();
            if (maybeWinner.isPresent()) {
                CellState winnerSymbol = maybeWinner.get();
                if (verbose) {
                    System.out.println("We have a winner: ");
                }
                onGameEnd(winnerSymbol);
                return Optional.of(winnerSymbol);
            }


            if (board.isFull()) {
                if (verbose) {
                    System.out.println("It's a TIE!");
                }
                onGameEnd(null);
                return Optional.empty();
            }

            swapPlayers();
        }
    }
    private boolean askPlayAgain(Scanner sc) {
        while (true) {
            System.out.print("Play again? (y/n): ");
            String line = sc.nextLine().trim().toLowerCase();
            if (line.equals("y") || line.equals("yes")) {
                return true;
            }
            if (line.equals("n") || line.equals("no")) {
                return false;
            }
            System.out.println("Please answer 'y' or 'n'.");
        }
    }

    private void onGameEnd(CellState winnerSymbol) {
        if (winnerSymbol == null) {
            System.out.println("Result: TIE!");
            String tie1 = dialogManager.getLine(currentPlayer, otherPlayer, DialogEvent.TIE, null);
            String tie2 = dialogManager.getLine(otherPlayer, currentPlayer, DialogEvent.TIE, null);
            if (!tie1.isEmpty() && showDialogueToConsole) System.out.println(currentPlayer.getName() + " says: " + tie1);
            if (!tie2.isEmpty() && showDialogueToConsole) System.out.println(otherPlayer.getName() + " says: " + tie2);
            return;
        }
        Player winner = (currentPlayer.getSymbol() == winnerSymbol) ? currentPlayer : otherPlayer;
        Player loser = (winner == currentPlayer) ? otherPlayer : currentPlayer;

        System.out.println("Congrats " + winner.getName() + "! You won as " + winner.getSymbol());

        String win = dialogManager.getLine(winner, loser, DialogEvent.WIN, null);
        if (!win.isEmpty() && showDialogueToConsole) System.out.println(winner.getName() + " says: " + win);

        String lose = dialogManager.getLine(loser, winner, DialogEvent.LOSE, null);
        if (!lose.isEmpty() && showDialogueToConsole) System.out.println(loser.getName() + " says: " + lose);
    }

    private void swapPlayers() {
        Player tmp = currentPlayer;
        currentPlayer = otherPlayer;
        otherPlayer = tmp;
    }

    private void printBoard() {
        System.out.println(board.toString());
    }
}