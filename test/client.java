import java.net.*;
import java.io.*;

public class client {
    // dev variables
    private static final String IP = "localhost";
    // end of dev variables
    // pre defined variables
    private static final int PORT = 2594;
    // end of pre defined variables

    // networking related variables
    private static Socket s;
    private static PrintWriter pr;
    private static InputStreamReader in;
    private static BufferedReader bf;
    // end of networking related variables

    // game related variables
    private static String nick = "Lorem Ipsum";
    private static int punktezahl = 0;
    private static String question; // the question and the answer are both provided in the JSON format
    private static int answer = 0;
    private static boolean result;
    // end of game related variables

    public static void main(String[] args) {
        run(IP);
    }

    public static void run(String ip) {
        try {
            establishConnection(ip);
            registerClient();

            while (true) {
                // wait for the round to start
                // if the host send the start signal, the client will prepare to receive the
                // question
                // if the host send the end signal, the client will break the loop and end the

                if (bf.readLine().equals("START ROUND")) {
                    // receive the question
                    question = bf.readLine();
                    // give the question to the function that will **eventually return the answer**
                    answer = dummy.roundStarted(question);
                    // send the answer
                    pr.println(answer);
                    pr.flush();
                } else if (bf.readLine().equals("RESULT")) {
                    // receive the result
                    result = Boolean.parseBoolean(bf.readLine()); // Example: Boolean.parseBoolean("True") returns true.
                                                                  // Example: Boolean.parseBoolean("yes") returns false.
                    // receive the points
                    punktezahl = Integer.parseInt(bf.readLine()); // Example: Integer.parseInt("123") returns 123.

                    // give the result and the points to the function that will handle those
                    dummy.roundEnded(result, punktezahl);

                } else if (bf.readLine().equals("END GAME")) {
                    break;
                }
            }
            // once the loop is broken, the client will close the connection
            s.close();
            System.out.println("Connection closed");
        } catch (Exception e) {
            System.out.println("OhNo, an error occured: " + e);
        }
    }

    private static void establishConnection(String ip) throws Exception {
        s = new Socket(ip, PORT);
        pr = new PrintWriter(s.getOutputStream());
        in = new InputStreamReader(s.getInputStream());
        bf = new BufferedReader(in);
    }

    private static void registerClient() throws Exception {
        // send the nickname
        pr.println(nick);
        pr.flush();
    }
}
