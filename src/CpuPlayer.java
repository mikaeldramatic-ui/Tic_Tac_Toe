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
        System.out.println("[DEBUG CPU] " + getName() + " difficulty = " + difficulty);

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
                    System.out.println("[DEBUG CPU] " + getName() + " found winning move " + winMove);
                    return winMove;
                }
                return randomMove(avail);
            }

            case HARD: {
                // 1) Försök vinna
                Move winning = findWinningMove(board, getSymbol());
                if (winning != null) {
                    System.out.println("[DEBUG CPU] " + getName() + " plays winning move " + winning);
                    return winning;
                }

                // 2) Blockera motståndaren
                CellState opponent = (getSymbol() == CellState.X) ? CellState.O : CellState.X;
                Move block = findWinningMove(board, opponent);
                if (block != null) {
                    System.out.println("[DEBUG CPU] " + getName() + " blocks opponent at " + block);
                    return block;
                }

                // 3) Ta mitten om ledig
                Move center = centerMove(board);
                if (center != null) {
                    System.out.println("[DEBUG CPU] " + getName() + " takes center at " + center);
                    return center;
                }

                // 4) Ta ett hörn om möjligt
                Move corner = pickCorner(board);
                if (corner != null) {
                    System.out.println("[DEBUG CPU] " + getName() + " takes corner at " + corner);
                    return corner;
                }

                // 5) Fallback: random
                return randomMove(avail);
            }

            default:
                return randomMove(avail);
        }
    }

    // Välj slumpmässigt från tillgängliga moves
    private Move randomMove(List<Move> avail) {
        return avail.get(random.nextInt(avail.size()));
    }

    // Testa varje möjlig move: sätt temporärt, kolla winner, ångra
    private Move findWinningMove(Board board, CellState symbol) {
        for (Move m : board.getAvailableMoves()) {
            board.setMove(m.getRow(), m.getCol(), symbol);
            Optional<CellState> maybeWinner = board.checkWinner();
            boolean wins = maybeWinner.isPresent() && maybeWinner.get() == symbol;
            board.resetCell(m.getRow(), m.getCol()); // återställ
            if (wins) return m;
        }
        return null;
    }

    // Ta mitten om ledig
    private Move centerMove(Board board) {
        int mid = Board.SIZE / 2;
        if (board.isEmpty(mid, mid)) return new Move(mid, mid);
        return null;
    }

    // Plocka ett ledigt hörn (i ordning)
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