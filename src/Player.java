public class abstract Player {
    private final String name;
//    X or O
    private final CellState symbol;

    public Player(String name, CellState symbol) {
        if (symbol == CellState.EMPTY) {
            throw new IllegalArgumentException("Player must have symbol X or O, not EMPTY.");
        }
                this.name;
                this.symbol;
    }

//    Shall return a move (row or colon) based on actual board
//    Implements by HumanPlayer (Read input/ UI) and CPU Ai
//    Method to call board.getAvailableMoves() but NOT update boardgame

    public abstract  Move getMove(Board board);

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
            public String toString() {
        return name + " (" + symbol + ")";
    }
}
