import java.util.Optional;

public class GameController {
    private final Board board;
    private Player currentPlayer;
    private Player otherPlayer;
//    IF true, prints out on board/status to console
    private boolean verbose;

//    Put Constructor

    public GameController(Board board, Player player1, Player player2, boolean verbose) {
        this.board = board;
        this.currentPlayer = player1;
        this.otherPlayer = player2;
        this.verbose = verbose;
    }

//    Starting game loop, method run until winner or tie
//    Will ask currentplayer for a move, trying to put on board and continutes til game is over

    public void play() {
//        keep in mind to reset board before start
        board.initialize();

        if (verbose) {
        System.out.println("Let the game begins!");
        printBoard();
    }

    while (true) {
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
    } else {
//        Identifies which player won
        Player winner = (currentPlayer.getSymbol() == winnerSymbol) ? currentPlayer : otherPlayer;
        System.out.println("Congrats " + winner.getName() + " You are the winner " + winner.getSymbol());
    }

//    Can put dialogueManager or update highscore
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


