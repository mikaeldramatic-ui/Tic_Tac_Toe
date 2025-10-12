public abstract class Player {
    private final String name;

    private final CellState symbol;

    public Player(String name, CellState symbol) {
        if (symbol == CellState.EMPTY) {
            throw new IllegalArgumentException("Player must have symbol X or O, not EMPTY.");
        }
                this.name = name;
                this.symbol = symbol;
    }


    public abstract  Move getMove(Board board);

    public String getName() {
        return name;
    }

    public CellState getSymbol() {
        return symbol;
    }

    @Override
            public String toString() {
        return name + " (" + symbol + ")";
    }
}
