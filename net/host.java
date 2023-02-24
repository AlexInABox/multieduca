import java.net.*;
import java.util.ArrayList;
import java.io.*;

import org.json.*;


public class host {
    private static ServerSocket ss; 

    private static ArrayList<RunnableThread> threadList = new ArrayList<RunnableThread>();

    private static final int PORT = 2594;

    private static Quiz q = new Quiz(new File("test.json"));

    public static void main(String[] args) throws IOException {
        initServer(PORT);
        try{
        Thread.sleep(20000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        startGame();
        
        startRound();
        try{
        Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        endGame();
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
        threadList.remove(threadList.size()-1);
    }

    public static void startRound() {
        //get question object
        
        try{
        for (RunnableThread thread : threadList) {
            thread.sendQuestion(q.getFrage(0).toString());
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void endGame() {
        //
        try{
        for (RunnableThread thread : threadList) {
            thread.endGame();
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}