import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Board {
    private final CellState[][] grid;
    public static final int SIZE = 3;

    public Board() {
        grid = new CellState[SIZE][SIZE];
        initialize();
    }

    public void initialize() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                grid[r][c] = CellState.EMPTY;
            }
        }
    }

    public boolean setMove(int row, int col, CellState symbol) {
        if (!inBounds(row, col) || symbol == null || symbol == CellState.EMPTY) {
            return false;
        }
        if (!isEmpty(row, col)) {
            return false;
        }
        grid[row][col] = symbol;
        return true;
    }

    public boolean isEmpty(int row, int col) {
        if (!inBounds(row, col)) return false;
        return grid[row][col] == CellState.EMPTY;
    }

    public List<Move> getAvailableMoves() {
        List<Move> moves = new ArrayList<>();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (grid[r][c] == CellState.EMPTY) {
                    moves.add(new Move(r, c));
                }
            }
        }
        return moves;
    }

    public boolean isFull() {
        return getAvailableMoves().isEmpty();
    }

    public Optional<CellState> checkWinner() {

        for (int r = 0; r < SIZE; r++) {
            if (grid[r][0] != CellState.EMPTY &&
                    grid[r][0] == grid[r][1] &&
                    grid[r][1] == grid[r][2]) {
                return Optional.of(grid[r][0]);
            }
        }

        for (int c = 0; c < SIZE; c++) {
            if (grid[0][c] != CellState.EMPTY &&
                    grid[0][c] == grid[1][c] &&
                    grid[1][c] == grid[2][c]) {
                return Optional.of(grid[0][c]);
            }
        }

        if (grid[0][0] != CellState.EMPTY &&
                grid[0][0] == grid[1][1] &&
                grid[1][1] == grid[2][2]) {
            return Optional.of(grid[0][0]);
        }

        if (grid[0][2] != CellState.EMPTY &&
                grid[0][2] == grid[1][1] &&
                grid[1][1] == grid[2][0]) {
            return Optional.of(grid[0][2]);
        }

        return Optional.empty();
    }

    public CellState getCell(int row, int col) {
        if (!inBounds(row, col)) throw new IndexOutOfBoundsException("Cell out of bounds");
        return grid[row][col];
    }

    private boolean inBounds(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }

    public void resetCell(int row, int col) {
        if (inBounds(row, col)) {
            grid[row][col] = CellState.EMPTY;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < SIZE; r++) {
            sb.append(" ");
            for (int c = 0; c < SIZE; c++) {
                sb.append(grid[r][c].toChar());
                if (c < SIZE - 1) sb.append(" | ");
            }
            sb.append(System.lineSeparator());
            if (r < SIZE - 1) sb.append("---+---+---").append(System.lineSeparator());
        }
        return sb.toString();
    }
}