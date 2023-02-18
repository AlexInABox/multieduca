import java.net.*;
import java.io.*;

public class host {
    private static int counter = 0;
    private static ServerSocket ss;

    private static RunnableThread[] threadList = new RunnableThread[20];

    public static boolean isStarted = false;

    private static final int PORT = 2594;

    public static void main(String[] args) throws IOException {
        initServer(PORT);
    }

    public static void initServer(int port) throws IOException {
        System.out.println("Starting server on: " + port);
        ss = new ServerSocket(port);

        createThread();

    }

    public static void createThread() {
        RunnableThread t = new RunnableThread(Integer.toString(counter), ss);
        counter += 1;
        try {
            threadList[counter] = t;
        } catch (Exception e) {
            System.out.println("No more people please thanks!!!" + e);
        }
        t.start();
    }

    public static void startGame() {
        isStarted = true;
        // stop the last running thread
        threadList[counter].allowedToRun = false;
    }

    public static void handleMessage(String msg) {
        System.out.println("client: " + msg.toString());
    }

    public static void respondToClient(PrintWriter pr) {
        pr.println("Hello Client, I received your message!");
        pr.flush();
    }

}
