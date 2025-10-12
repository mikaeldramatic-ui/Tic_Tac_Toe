import java.util.Scanner;

public class Welcome {
    public static String show(Scanner scanner) {

        printBanner();
        System.out.println();
        System.out.println("Welcome brave Sir Knight!");
        System.out.println("Get ready for battle and start typing your name to begin.");
        System.out.println();
        System.out.println("Enter your name Sir Knight: ");
        return scanner.nextLine().trim();
    }
    private static void printBanner() {
        System.out.println("========================================");
        System.out.println("||         #                            ||");
        System.out.println("||         #           TIC              ||");
        System.out.println("||         #              TAC           ||");
        System.out.println("||      ^^^^^^^         KNIGHTS         ||");
        System.out.println("||        |||                           ||");
        System.out.println("||        |||                           ||");
        System.out.println("||        |||                           ||");
        System.out.println("||        |||             By:           ||");
        System.out.println("||        |||            Mikael         ||");
        System.out.println("||        |||            Engvall        ||");
        System.out.println("||    -----------                       ||");
        System.out.println("||   | Excalibur |                      ||");
        System.out.println("||    -----------                       ||");
        System.out.println("=========================================");
    }

}




