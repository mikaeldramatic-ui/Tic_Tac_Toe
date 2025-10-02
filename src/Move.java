public class Move {

    public final int row;
    public final int col;

    public Move(int row, int col) {
        this.row;
        this.col;
    }

    public int getRow() { return row;}
    public int getCol() { return col;}

    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }
}
