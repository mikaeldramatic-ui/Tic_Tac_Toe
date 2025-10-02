public enum CellState {
    EMPTY,
    X,
    O;

//    Helper to write a cell as sign
    public char toChar() {
        switch (this) {
            case X:     return 'X';
            case O:     return 'O';
            default:    return ' ';
        }
    }
}
