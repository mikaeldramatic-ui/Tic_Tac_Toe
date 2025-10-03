public class Main {
    public static void main(String[] args) {

        Board board = new board();

//        Example beneath when yo''l implements HumanPlayer and CpuPlayer

//        Implements by itself
        Player p1 = new HumanPlayer("Alice", CellState.X);
//        Implements by itself
        Player p2 = new CpuPlayer ("Bot", CellState.O, new RandomStrategy());

        GameController game = new GameController(board, p1 , p2, true);
        game.play();

    }
}