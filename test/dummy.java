public class dummy {

    public static void startGame() {
        System.out.println("Game Started");
    }

    public static void endGame() {
        System.out.println("Game Ended");
    }

    public static int roundStarted(String question) {
        System.out.println("Round Started");
        System.out.println("Question: " + question);
        return 3; // this can be a value between 1 and 4
    }

    public static void roundEnded(boolean result, int points) {
        System.out.println("Round Ended");
        System.out.println("Result: " + result);
        System.out.println("Points: " + points);
    }
}
