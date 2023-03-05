package net;

import java.net.*;
import java.util.ArrayList;

import data.Quiz;

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

    private static int roundIndex = 0;

    public static void main(String[] args) throws IOException, InterruptedException {

        ss = new ServerSocket(2594);
        initServer();
        try {
            Thread.sleep(10000);
            startGame();
            startRound();
            Thread.sleep(5000);
            startRound();
            Thread.sleep(5000);
            endGame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initServer() throws IOException {
        RunnableThread t = new RunnableThread(ss, q);
        threadList.add(t);
        t.start();
    }

    public static void startGame() {
        threadList.remove(threadList.size() - 1);
        System.out.println("Starting game with " + threadList.size() + " players");
    }

    public static void startRound() {
        System.out.println("");
        System.out.println("Starting round: " + (roundIndex + 1));
        try {
            for (RunnableThread thread : threadList) {
                thread.sendQuestion(roundIndex);
            }
            roundIndex++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void endGame() {
        try {
            for (RunnableThread thread : threadList) {
                thread.endGame();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("");
        System.out.println("Game ended. All players disconnected ");
    }
}