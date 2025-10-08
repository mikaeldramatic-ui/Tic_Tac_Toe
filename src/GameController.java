import java.util.Optional;

public class GameController {
    private Board board;
    private Player currentPlayer;
    private Player otherPlayer;
    private final DialogueManager dialogueManager;
    private boolean showDialogueToConsole;
    private boolean verbose;

    public GameController(Board board, Player currentPlayer, Player otherPlayer, DialogueManager dialogueManager, boolean showDialogueToConsole, boolean verbose) {
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.otherPlayer = otherPlayer;
        this.dialogueManager = dialogueManager;
        this.showDialogueToConsole = showDialogueToConsole;
        this.verbose = verbose;
    }

    public GameController(Board board, Player player1, Player player2,
                          boolean verbose, DialogueManager dialogueManager) {
        this(board, player1, player2, verbose, dialogueManager, true);
    }

    public void setShowDialogueToConsole(boolean enabled) {
        this.showDialogueToConsole = enabled;
    }

    public boolean isShowDialogueToConsole() {
        return showDialogueToConsole;
    }

    public void play() {
        board.initialize();

        if (verbose) {
            System.out.println("Let the game begin!");
            printBoard();
        }

        while (true) {
//            Pre Move dialogue
            String pre = dialogueManager.getLine(currentPlayer, otherPlayer, DialogueEvent.PRE_MOVE, null);
            if (!pre.isEmpty() && showDialogueToConsole) {
                System.out.println(currentPlayer.getName() + " says: " + pre);
            }
            Move move = currentPlayer.getMove(board);

            boolean success = board.setMove(move.getRow(), move.getCol(), currentPlayer.getSymbol());

            if (!success) {
                if (verbose) {
                    System.out.println(currentPlayer.getName() + "Made invalid move " + move + " Try again wiseguy!");
                }
                String illegal = dialogueManager.getLine(currentPlayer, otherPlayer, DialogueEvent.ILLEGAL_MOVE, move);
                if (!illegal.isEmpty() && showDialogueToConsole) {
                    System.out.println(currentPlayer.getName() + " says " + illegal);
                }
                continue;
            }
//            Post move dialogue
            String post = dialogueManager.getLine(currentPlayer, otherPlayer, DialogueEvent.POST_MOVE, move);
            if (!post.isEmpty() && showDialogueToConsole) {
                System.out.println(currentPlayer.getName() + " says " + post);
            }

            if (verbose) {
                System.out.println(currentPlayer.getName() + " played " + move + " as " + currentPlayer.getSymbol());
                printBoard();
            }

            Optional<CellState> maybeWinner = board.checkWinner();
            if (maybeWinner.isPresent()) {
                CellState winnerSymbol = maybeWinner.get();
                if (verbose) {
                    System.out.println("We have a winner: " + winnerSymbol);
                }
                onGameEnd(winnerSymbol);
                break;
            }
            if (board.isFull()) {
                if (verbose) {
                    System.out.println("It's a TIE!");
                }
                onGameEnd(null);
                break;
            }

            swapPlayers();
        }
    }

    private void onGameEnd(CellState winnerSymbol) {
        if (winnerSymbol == null) {
            System.out.println("Result: TIE!");
            String tie1 = dialogueManager.getLine(currentPlayer, otherPlayer, DialogueEvent.TIE, null);
            String tie2 = dialogueManager.getLine(otherPlayer, currentPlayer, DialogueEvent.TIE, null);
            if (tie1.isEmpty() && showDialogueToConsole) System.out.println(currentPlayer.getName() + "says: " + tie1);
            if (tie2.isEmpty() && showDialogueToConsole) System.out.println(otherPlayer.getName() + "says: " + tie2);
            return;
        }
        Player winner = (currentPlayer.getSymbol()== winnerSymbol) ? currentPlayer : otherPlayer;
        Player loser = (winner== currentPlayer) ? otherPlayer : currentPlayer;

        System.out.println("Congrats " + winner.getName() + "! You won as" + winner.getSymbol());

        String win = dialogueManager.getLine(winner, loser, DialogueEvent.WIN, null);
        if(!win.isEmpty() && showDialogueToConsole) System.out.println(winner.getName() + " says: " + win);

        String lose = dialogueManager.getLine(loser, winner, DialogueEvent.LOSE, null);
        if(!lose.isEmpty() && showDialogueToConsole) System.out.println(loser.getName() + " says: " + lose);
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








//        String block = dialogueManager.getLine(currentPlayer , otherPlayer , dialogueEvent.BLOCK, move);
//        if (!block.isEmpty() && showDialogueToConsole){
//            System.out.println(currentPlayer.getName() + "Is saying: " + block);
//        }



