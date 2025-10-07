import java.util.Optional;

public class GameController {
    private final Board board;
    private Player currentPlayer;
    private Player otherPlayer;
    private final DialogueManager dialogueManager;
    private final boolean showDialogueToConsole;
    private boolean verbose;

    public GameController(Board board) {
        this.board = board;
        showDialogueToConsole = true;
        dialogueManager = dialogueManager;
    }

//    Put Constructor


//    Starting game loop, method run until winner or tie
//    Will ask currentplayer for a move, trying to put on board and continues til game is over

    public void play() {
//        keep in mind to reset board before start
        board.initialize();

        if (verbose) {
        System.out.println("Let the game begin!");
        printBoard();
    }

    while (true) {

        String pre = dialogueManager.getLine(currentPlayer, otherPlayer, DialogueEvent.PRE_MOVE,null);
        if (!pre.isEmpty() && showDialogueToConsole) {
            System.out.println(currentPlayer.getName() + " Is saying: " + pre);
        }
        Move move = currentPlayer.getMove(board);

        boolean success = board.setMove(move.getRow(), move.getCol(), currentPlayer.getSymbol());

        if (!success) {
            if (verbose) {
                System.out.println(currentPlayer.getName() + "Invalid move" + move + "try again!");
            }
            String Illegal = dialogueManager.getLine(currentPlayer, otherPlayer, DialogueEvent.ILLEGAL_MOVE, move);
            if (!Illegal.isEmpty()&& showDialogueToConsole) {
                System.out.println(currentPlayer.getName() + "Is saying; " + Illegal);
            }
            continue;
        }

        if (verbose) {
            System.out.println(currentPlayer.getName() + " Played "+ move +" as " + currentPlayer.getSymbol());
            printBoard();
        }

        String post = dialogueManager.getLine(currentPlayer, otherPlayer, DialogueEvent.POST_MOVE, move);
        if (!post.isEmpty() && showDialogueToConsole) {
            System.out.println(currentPlayer.getName() + "Is saying: " + post );
        }

        Optional<CellState> maybeWinner = board.checkWinner();
        if(maybeWinner.isPresent()) {
            CellState winnerSymbol = maybeWinner.get();
            if (verbose) {
                System.out.println("We have a winner!: " + winnerSymbol);
                System.out.println(currentPlayer.getName() + " wins! ");
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
        swapPlayers();
    }

    }
    //Calls when games finished - winnerSymbol null => TIE

    private void onGameEnd(CellState winnerSymbol) {
        if (winnerSymbol == null) {
            System.out.println( " Results : TIE!");
            String tieForP1 = dialogueManager.getLine(currentPlayer, otherPlayer, DialogueEvent.TIE, null);
            if (!tieForP1.isEmpty()) System.out.println(currentPlayer.getName() + "Is saying; " + tieForP1);
            String tieForP2 = dialogueManager.getLine(otherPlayer, otherPlayer, DialogueEvent.TIE, null);
            if (!tieForP2.isEmpty()) System.out.println(currentPlayer.getName() + "Is saying; " + tieForP2);
            return;
        }

        Player winner = (currentPlayer.getSymbol() == winnerSymbol) ? currentPlayer : otherPlayer;
        Player loser = (winner== currentPlayer) ? otherPlayer : currentPlayer;
        System.out.println("Congrats "+ winner.getName() + "! You are the winner (" + winner.getSymbol() + ")");

        String win= dialogueManager.getLine(winner,loser,DialogueEvent.WIN, null);
        if(!win.isEmpty()) System.out.println(winner.getName() + "Is saying: "+ win);

        String lose= dialogueManager.getLine(loser,winner,DialogueEvent.LOSE, null);
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


//        String block = dialogueManager.getLine(currentPlayer , otherPlayer , dialogueEvent.BLOCK, move);
//        if (!block.isEmpty() && showDialogueToConsole){
//            System.out.println(currentPlayer.getName() + "Is saying: " + block);
//        }



