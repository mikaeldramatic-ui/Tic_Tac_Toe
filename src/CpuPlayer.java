import java.util.List;
import java.util.Random;

public class CpuPlayer extends Player {
    private final Random random;
    private final Difficulty difficulty;

    public CpuPlayer(String name, CellState symbol, Difficulty difficulty) {
        super(name, symbol);
        this.random = new Random();
        this.difficulty = difficulty;
    }


    @Override
    public Move getMove(Board board) {
        switch (difficulty) {
            case EASY:
                return randomMove(board);

            case MEDIUM:
                Move winMove = findWinningMove(board, getSymbol());
                if (winMove != null) {
                    System.out.println(getName() + "Normal");
                    return winMove;
                }
                return randomMove(board);

            case HARD:
                Move winning = findWinningMove(board, getSymbol());
                if (winning != null) {
                    System.out.println(getName() + " WRECKLESS!");
                    return winning;
                }

//                Blocking opponent
                CellState opponent = (getSymbol() == CellState.X) ? CellState.O : CellState.X;
                Move block = findWinningMove(board, opponent);
                if (block != null) {
                    System.out.println(getName() + "Blocking Opponent!");
                    return block;
                }
//                Otherwise random
                return randomMove(board);

            default:
                return randomMove(board);
        }
    }

    private Move randomMove(Board board) {
        List<Move> availableMoves = board.getAvailableMoves();
        return availableMoves.get(random.nextInt(availableMoves.size()));
    }

    //    Helpmethod to see if player can win in next move
    private Move findWinningMove(Board board, CellState symbol) {
        for (Move move : board.getAvailableMoves()) {
//            Try move temporary
            board.setMove(move.getRow(), move.getCol(), symbol);
            boolean wins = board.checkWinner().isPresent() &&
                    board.checkWinner().get() == symbol;

//            Undo move (reset cell to empty)
            board.resetCell(move.getRow(), move.getCol());
            if (wins) {
                return move;
            }

        }
        return null;
    }
}



