import java.util.Optional;

public class GameController {
    private final Board board;
    private Player currentPlayer;
    private Player otherPlayer;
    private final DialogueManager dialogueManager;
    private final boolean showDialougeToConsole;
//    IF true, prints out on board/status to console
    private boolean verbose;

//    Put Constructor

    public GameController(Board board, Player currentPlayer, Player otherPlayer,
                          DialogueManager dialogueManager, boolean showDialougeToConsole, boolean verbose) {
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.otherPlayer = otherPlayer;
        this.dialogueManager = dialogueManager;
        this.showDialougeToConsole = showDialougeToConsole;
        this.verbose = verbose;
    }


//    Starting game loop, method run until winner or tie
//    Will ask currentplayer for a move, trying to put on board and continutes til game is over

    public void play() {
//        keep in mind to reset board before start
        board.initialize();

        if (verbose) {
        System.out.println("Let the game begin!");
        printBoard();
    }

    while (true) {

        String pre = dialogueManager.getLine(currentPlayer, otherPlayer, DialogueEvent.PRE_MOVE,null);
        if (!pre.isEmpty() && showDialougeToConsole) {
            System.out.println(currentPlayer.getName() + " Is saying: " + pre);
        }

        String post = dialogueManager.getLine(currentPlayer, otherPlayer, dialougeEvent.POST_MOVE, move);
        if (!post.isEmpty() && showDialougeToConsole) {
            System.out.println(currentPlayer.getName() + "Is saying: " + post );
        }

        String block = dialogueManager.getLine(currentPlayer , otherPlayer , DialogueEvent.BLOCK, move);
        if (!block.isEmpty() && showDialougeToConsole){
            System.out.println(currentPlayer.getName() + "Is saying: " + block);
        }
//        ask currentplayer to make a move

        Move move = currentPlayer.getMove(board);

//        Try to make move . If invalid - ask for a new move (loop)
        boolean success = board.setMove(move.getRow(), move.getCol(), currentPlayer.getSymbol());

        if (!success) {
//    Invalid move, send message and continues looping that the same player can choose again.
        if (verbose) {
        System.out.println(currentPlayer.getName() + " Invalid move " + move + "Try again!");
    }
//    getMove will call again in the next iteration of while (same player)
    continue;
}

//Move succseeds, show board

        if (verbose) {
            System.out.println(currentPlayer.getName() + " Played " + move + " like " +currentPlayer.getSymbol());
            printBoard();
        }

//Check winner
Optional<CellState> maybeWinner = board.checkWinner();
        if (maybeWinner.isPresent()) {
            CellState winnerSymbol = maybeWinner.get();
            if (verbose) {
                System.out.println("We have a winner!! :" + winnerSymbol);
                System.out.println(currentPlayer.getName() + " Wins!");
            }
            onGameEnd(winnerSymbol);
            break;
        }
//        Check if it's TIE
        if (board.isFull()) {
            if (verbose) {
                System.out.println("The board is full -- it's a TIE!");
            }
            onGameEnd(null);
            break;
        }

//        swap turn
        swapPlayers();
    }
}

//Calls when games finished - winnerSymbol null => TIE

private void onGameEnd(CellState winnerSymbol) {
    if (winnerSymbol == null) {
        System.out.println( " Results : TIE!");
        String tieForp1 = dialogueManager.getLine(currentPlayer, otherPlayer, DialogueEvent.TIE, null);
        if (!tieForp1.isEmpty()) System.out.println(currentPlayer.getName() + "Is saying; " + tieForp1);
        String tieForp2 = dialogueManager.getLine(otherPlayer, otherPlayer, DialogueEvent.TIE, null);
        if (!tieForp2.isEmpty()) System.out.println(currentPlayer.getName() + "Is saying; " + tieForp2);
        return;
    }
//    Can put dialogueManager or update highscore

    Player winner = (currentPlayer.getSymbol() == winnerSymbol) ? currentPlayer : otherPlayer;
    Player loser = (winner== currentPlayer) ? otherPlayer : currentPlayer;
    System.out.println("Congrats "+ winner.getName() + "! You are the winner (" + winner.getSymbol() + ")");

    String win= dialogueManager.getLine(winner,loser,dialogueEvent.WIN, null);
    if(!win.isEmpty()) System.out.println(winner.getName() + "Is saying: "+ win);

    String lose= dialogueManager.getLine(loser,winner,dialogueEvent.LOSE, null);
    if(!lose.isEmpty()) System.out.println(winner.getName() + "Is saying: "+ lose);
    }

//Switch who is the current or otherPlayer
private void swapPlayers() {
    Player tmp = currentPlayer;
    currentPlayer = otherPlayer;
    otherPlayer = tmp;
}

//Helpmethod to print on board thru board.toString()
private void printBoard() {
    System.out.println(board.toString());
}
//Optional getters for testing or UI

public Board getBoard() {
    return board;
}
public Player getCurrentPlayer() {
    return currentPlayer;
}

public Player getOtherPlayer() {
    return otherPlayer;
}

}


