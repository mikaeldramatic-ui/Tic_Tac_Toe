import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class StoryManager {

    private final List<Scene> scenes;
    private final DialogManager dialogManager;
    private final boolean showDialog;
    private final Scanner scanner;

    public StoryManager(DialogManager dialogManager, boolean showDialog, Scanner scanner) {
        this.dialogManager = dialogManager;
        this.showDialog = showDialog;
        this.scanner= scanner;
        this.scenes = defaultScenes();
    }

    private List<Scene> defaultScenes() {
        List<Scene> list= new ArrayList<>();
        list.add(new Scene("The Armory","Before going out to battle you'll need to arm yourself up, You'll see Excalibur and wants it"));
        list.add(new Scene("The Stables", "You need to ride with a horse, You'll see Arthurs famouse horse Llamrei"));
        list.add(new Scene("The Battlefield", "Out in the fields there´s a big battle going on, LETS FIGHT!"));
        list.add(new Scene("The enemies are fighting well"," but then...Mildred approaches, FIGHT!"));
        return list;
    }

    public void startCampaign(Player p1, Player p2) {
        System.out.println("  === CAMPAIGN START: TIC TAC KNIGHTS === ");
        int scoreP1 = 0;
        int scoreP2 = 0;

        Player starter= p1;

        for (int i = 0; i < scenes.size(); i++) {
            Scene s = scenes.get(i);
            System.out.println("---- SCENE " + (i+ 1) + " : " + s.getName() + " ----");
            System.out.println(s.getDescription());
            promptEnterToContinue();

            Player other= (starter == p1) ? p2 : p1;

            Board board = new Board();
            GameController game = new GameController(board, starter,other, true, dialogManager, showDialog);

            Optional<CellState> result= game.playSingleRound();

            if (result.isPresent()) {
            CellState winnerSymbol = result.get();
            if (winnerSymbol == p1.getSymbol()) {
            scoreP1++;
                System.out.println(p1.getName() + " wins scene " + (i +1) +"!");
                starter = p1;
            } else if (winnerSymbol == p2.getSymbol()) {
            scoreP2++;
                System.out.println(p2.getName() + " wins scene " + (i +1) +"!");
            starter = p2;
            }
            } else {
                System.out.println("Scene " + (i+1) + "Ended in a TIE (No points). ");
            }
            System.out.println("SCORE: " + p1.getName() + " " + scoreP1 + " - " + p2.getName() + " " + scoreP2);
            promptEnterToContinue();
        }

        if (scoreP1 == scoreP2) {
            System.out.println("Scores are TIED! Sudden-Death match to decide who challenges the dragon.");
            promptEnterToContinue();

            while (scoreP1==scoreP2) {
                Board board = new Board();
                GameController game = new GameController(board, p1,p2,true,dialogManager,showDialog);
                Optional<CellState> maybeWinner = game.playSingleRound();

                if (maybeWinner.isPresent()) {
                    CellState winnerSymbol = maybeWinner.get();
                    if (winnerSymbol== p1.getSymbol()) scoreP1++;
                    else if (winnerSymbol== p2.getSymbol()) scoreP2++;
                } else {
                    System.out.println("Sudden-death ends with a TIE - Play again!");
                }
            }
        }

        Player challenger = (scoreP1 > scoreP2) ? p1 : p2;
        int finalScore = (challenger==p1)? scoreP1 : scoreP2;
        boolean campaignVictory=false;
        HighscoreManager hs= new HighscoreManager();
        hs.saveEntry(challenger.getName(), finalScore,campaignVictory);

        List<HighscoreManager.HighscoreEntry> top = hs.loadTop(5);
        System.out.println("=== HIGHSCORE ===");
        for (HighscoreManager.HighscoreEntry e : top){
            System.out.println(e);
            System.out.println();
        }
        System.out.println();
        System.out.println(" Campaign scores:");
        System.out.println(p1.getName() + ": " + scoreP1);
        System.out.println(p2.getName() + ": " + scoreP2);
        System.out.println(" Mildred has been defeated, but then the dragon landed in front of " + challenger.getName());
        promptEnterToContinue();

        System.out.println(" --- BOSS: The Dragon! --- ");
        System.out.println("PREPARE FOR FINAL BATTLE!");
        promptEnterToContinue();

        CellState challengerSymbol = challenger.getSymbol();
        CellState bossSymbol = (challengerSymbol == CellState.X) ? CellState.O : CellState.X;
        Player boss = new CpuPlayer("Dragon", bossSymbol,Difficulty.HARD);

        Board bossBoard = new Board();
        GameController bossGame= new GameController(bossBoard, challenger, boss, true, dialogManager, showDialog);
        Optional<CellState> bossWinner = bossGame.playSingleRound();


        if (bossWinner.isPresent()) {
            CellState w = bossWinner.get();
            if (w== challenger.getSymbol()){
                System.out.println(" " + challenger.getName() + "Slayed the Dragon, CONGRATULATIONS!");
                campaignVictory = true;
            } else {
                System.out.println("Dragon shots fire and burned " + challenger.getName() + ". GAME OVER");
            }
        } else {
            System.out.println(" The Final Bossmatch ended with a TIE!, Campaign ends inconclusively");
        }

        System.out.println(" === CAMPAIGN RESULTS ===");
        System.out.println(p1.getName() + ": " + scoreP1);
        System.out.println(p2.getName() + ": " + scoreP2);
        System.out.println("Campaign challenger: " + challenger.getName());
        System.out.println("Campaign victory: " + ((campaignVictory) ? "YES" : "NO"));
    }

    private void promptEnterToContinue() {
        System.out.println(" Press enter to continue");
        scanner.nextLine();
    }

    private static class Scene {
        private final String name;
        private final String description;

        public Scene(String name, String description) {
            this.name = name;
            this.description = description;
        }
        public String getName () {return name;}
        public String getDescription() {return description;}
    }
}


