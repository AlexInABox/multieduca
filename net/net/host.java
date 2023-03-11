package net;

import java.net.*;
import java.util.ArrayList;

import org.json.JSONObject;

import data.Quiz;
import javafx.scene.control.ListView;

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
    private static Quiz quiz;

    private static int roundIndex = 0;

    /*public static void main(String[] args) throws IOException, InterruptedException {
    
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
    */

    public static void initServer(ListView<String> playerList, Quiz quizArg) throws IOException {
        //dem RunnableThread wird die Playerliste uebergeben, damit er den Playernamen dort hinzufuegen kann
        quiz = quizArg;
        ss = new ServerSocket(2594);
        RunnableThread t = new RunnableThread(ss, quiz, playerList);
        threadList.add(t);
        t.start();
    }

    public static void createNewThread(ListView<String> playerList) {
        RunnableThread t = new RunnableThread(ss, quiz, playerList);
        threadList.add(t);
        t.start();
    }

    public static void startGame() {
        threadList.remove(threadList.size() - 1);
        System.out.println("Starting game with " + threadList.size() + " players");

        try {
            for (RunnableThread thread : threadList) {
                thread.startGame();
            }
        } catch (Exception e) {
            System.out.println("Error while starting game");
            e.printStackTrace();
        }
        System.out.println("");
        System.out.println("Game started");
    }

    public static void startRound() {
        JSONObject frage = quiz.getFrage(roundIndex);
        System.out.println("");
        System.out.println("Starting round: " + (roundIndex + 1));
        try {
            for (RunnableThread thread : threadList) {
                thread.sendQuestion(roundIndex);
            }
            roundIndex++;

            //wartet solange, bis die Zeit abgelaufen ist
            Thread.sleep(frage.getInt("zeit") * 1000);

            for (RunnableThread thread : threadList) {
                thread.getAnswer(roundIndex);
            }
        } catch (Exception e) {
            System.out.println("Error while starting round");
            e.printStackTrace();
        }
        System.out.println("");
        System.out.println("Round started");
    }

    public static void refreshPlayerList(ListView<String> playerList) {
        for (RunnableThread thread : threadList) {
            thread.refreshPlayerList(playerList);
        }
    }

    public static void endGame() {
        try {
            for (RunnableThread thread : threadList) {
                thread.endGame();
            }
        } catch (Exception e) {
            System.out.println("Error while ending game");
            e.printStackTrace();
        }
        System.out.println("");
        System.out.println("Game ended. All players disconnected ");
    }

    public static ArrayList<RunnableThread> getThreadList() {
        return threadList;
    }
}