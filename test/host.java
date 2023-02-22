import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class host {
    private static ServerSocket ss;

    private static ArrayList<RunnableThread> threadList = new ArrayList<RunnableThread>();

    private static final int PORT = 2594;

    public static void main(String[] args) throws IOException {
        initServer(PORT);
    }

    public static void initServer(int port) throws IOException {
        ss = new ServerSocket(port);

        createThread();
    }

    public static void createThread() {
        RunnableThread t = new RunnableThread(ss);
        try {
            threadList.add(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        t.start();
    }

    public static void startGame() {
        // stop the last running thread
        threadList.get(threadList.size()-1).allowedToRun = false;
    }

    public static void startRound() {
        //
        try{
        for (RunnableThread thread : threadList) {
            thread.sendQuestion("coole Frage");
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}