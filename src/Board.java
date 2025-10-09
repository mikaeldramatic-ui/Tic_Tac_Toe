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

    // Initiera brädet med EMPTY
    public void initialize() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                grid[r][c] = CellState.EMPTY;
            }
        }
    }

    // Försök göra ett drag; returnerar true om lyckat
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

    // Är en cell tom?
    public boolean isEmpty(int row, int col) {
        if (!inBounds(row, col)) return false;
        return grid[row][col] == CellState.EMPTY;
    }

    // Lista alla tillgängliga drag
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

    // Är brädet fullt?
    public boolean isFull() {
        return getAvailableMoves().isEmpty();
    }

    // Kolla vinnare: returnerar Optional.of(X/O) eller Optional.empty()
    public Optional<CellState> checkWinner() {
        // Horisontella rader
        for (int r = 0; r < SIZE; r++) {
            if (grid[r][0] != CellState.EMPTY &&
                    grid[r][0] == grid[r][1] &&
                    grid[r][1] == grid[r][2]) {
                return Optional.of(grid[r][0]);
            }
        }

        // Vertikala kolumner
        for (int c = 0; c < SIZE; c++) {
            if (grid[0][c] != CellState.EMPTY &&
                    grid[0][c] == grid[1][c] &&
                    grid[1][c] == grid[2][c]) {
                return Optional.of(grid[0][c]);
            }
        }

        // Diagonal (topp-left -> bot-right)
        if (grid[0][0] != CellState.EMPTY &&
                grid[0][0] == grid[1][1] &&
                grid[1][1] == grid[2][2]) {
            return Optional.of(grid[0][0]);
        }

        // Diagonal (topp-right -> bot-left)
        if (grid[0][2] != CellState.EMPTY &&
                grid[0][2] == grid[1][1] &&
                grid[1][1] == grid[2][0]) {
            return Optional.of(grid[0][2]);
        }

        return Optional.empty();
    }

    // Hämta en cell (eller kasta om out of bounds)
    public CellState getCell(int row, int col) {
        if (!inBounds(row, col)) throw new IndexOutOfBoundsException("Cell out of bounds");
        return grid[row][col];
    }

    // Hjälpmetod: kontroll av bounds
    private boolean inBounds(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }

    // Återställ en cell till EMPTY (används av AI-simulering)
    public void resetCell(int row, int col) {
        if (inBounds(row, col)) {
            grid[row][col] = CellState.EMPTY;
        }
    }

    // Enkel textrepresentation (för konsol)
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