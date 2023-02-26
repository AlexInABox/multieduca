import java.net.*;
import java.util.ArrayList;
import java.io.*;

/*
 * Autor: Alexander Betke, Jonas Lossin, Rosan Sharma, Maximilian Gombala, Niklas Bamberg
 * Datum: 2022-02-13
 * 
 * ...
 */

public class host {

    private static ServerSocket ss;
    private static ArrayList<RunnableThread> threadList = new ArrayList<RunnableThread>();
    private static Quiz q = new Quiz(new File("test.json"));

    public static void main(String[] args) throws IOException, InterruptedException {
        ss = new ServerSocket(2594);
        initServer();
        Thread.sleep(10000);
        threadList.remove(threadList.size()-1);
        startRound();
        endGame();
    }

    public static void initServer() throws IOException {
        RunnableThread t = new RunnableThread(ss, q);
        threadList.add(t);
        t.start();
    }

    public static void startRound() {
        try{
            for (RunnableThread thread : threadList) {
                thread.sendQuestion(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void endGame() {
        try{
            for (RunnableThread thread : threadList) {
                thread.endGame();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}