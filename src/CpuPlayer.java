import java.util.List;
import java.util.Optional;
import java.util.Random;

public class CpuPlayer extends Player {
    private final Random random = new Random();
    private final Difficulty difficulty;

    public CpuPlayer(String name, CellState symbol, Difficulty difficulty) {
        super(name, symbol);
        this.difficulty = difficulty;
    }

    @Override
    public Move getMove(Board board) {

        List<Move> avail = board.getAvailableMoves();
        if (avail.isEmpty()) {
            throw new IllegalStateException("CPU " + getName() + " asked for move but no available moves.");
        }

        switch (difficulty) {
            case EASY:
                return randomMove(avail);

            case MEDIUM: {
                Move winMove = findWinningMove(board, getSymbol());
                if (winMove != null) {
                    return winMove;
                }
                return randomMove(avail);
            }

            case HARD: {

                Move winning = findWinningMove(board, getSymbol());
                if (winning != null) {
                    return winning;
                }


                CellState opponent = (getSymbol() == CellState.X) ? CellState.O : CellState.X;
                Move block = findWinningMove(board, opponent);
                if (block != null) {
                    return block;
                }


                Move center = centerMove(board);
                if (center != null) {
                    return center;
                }


                Move corner = pickCorner(board);
                if (corner != null) {
                    return corner;
                }

                return randomMove(avail);
            }

            default:
                return randomMove(avail);
        }
    }

    private Move randomMove(List<Move> avail) {
        return avail.get(random.nextInt(avail.size()));
    }

    private Move findWinningMove(Board board, CellState symbol) {
        for (Move m : board.getAvailableMoves()) {
            board.setMove(m.getRow(), m.getCol(), symbol);
            Optional<CellState> maybeWinner = board.checkWinner();
            boolean wins = maybeWinner.isPresent() && maybeWinner.get() == symbol;
            board.resetCell(m.getRow(), m.getCol());
            if (wins) return m;
        }
        return null;
    }


    private Move centerMove(Board board) {
        int mid = Board.SIZE / 2;
        if (board.isEmpty(mid, mid)) return new Move(mid, mid);
        return null;
    }


    private Move pickCorner(Board board) {
        int[][] corners = {
                {0, 0},
                {0, Board.SIZE - 1},
                {Board.SIZE - 1, 0},
                {Board.SIZE - 1, Board.SIZE - 1}
        };
        for (int[] c : corners) {
            if (board.isEmpty(c[0], c[1])) return new Move(c[0], c[1]);
        }
        return null;
    }
}