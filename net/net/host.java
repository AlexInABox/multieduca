package net;

import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import data.Quiz;
import javafx.scene.control.ListView;

import java.io.*;

/*
 * Autor: Alexander Betke, Jonas Lossin, Rosan Sharma, Maximilian Gombala, Niklas Bamberg
 * Datum: 2022-02-13
 * 
 * hinzufuegen von von Erstellen und Senden einer spieler,punkte-Map, Niklas Bamberg - 12.03
 */

public class host {

    private static ServerSocket ss;
    private static ArrayList<RunnableThread> threadList = new ArrayList<RunnableThread>();
    private static Quiz quiz;
    private static HashMap<String, Integer> punkteMap = new HashMap<String, Integer>();
    private static HashMap<Integer, String> bestenliste = new HashMap<Integer, String>();

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
        System.out.println("");
        System.out.println("Starting round: " + (roundIndex + 1));
        try {
            //senden der Fragen an alle Spieler
            for (RunnableThread thread : threadList) {
                thread.sendQuestion(roundIndex);
            }
            //auslesen von Antworten und erstellen einer nickname, punkte map
            punkteMap.clear();
            for (RunnableThread thread : threadList) {
                thread.getAnswer(roundIndex);
                punkteMap.put(thread.getNick(), thread.getPunkte());
            }
            bestenliste = generiereBestenliste((HashMap<String, Integer>) punkteMap.clone());
            for (RunnableThread thread : threadList) {
                thread.sendBestenliste(punkteMap, bestenliste);
            }
            roundIndex++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void refreshPlayerList(ListView<String> playerList) {
        for (RunnableThread thread : threadList) {
            thread.refreshPlayerList(playerList);
        }
    }

    public static void endGame() {
        try {
            for (RunnableThread thread : threadList) {
                thread.endGame(punkteMap, bestenliste);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<RunnableThread> getThreadList() {
        return threadList;
    }

    static HashMap<Integer, String> generiereBestenliste(HashMap<String, Integer> spielerPunkteMap) {
        HashMap<Integer, String> bestenliste = new HashMap<Integer, String>();
        int i = 1;
        while (spielerPunkteMap.size() > 0) {
            Map.Entry<String, Integer> maxEntry = null;
            for (Map.Entry<String, Integer> entry : spielerPunkteMap.entrySet()) {
                if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                    maxEntry = entry;
                }
            }
            bestenliste.put(i, maxEntry.getKey());
            spielerPunkteMap.remove(maxEntry.getKey());
            i++;
        }
        return bestenliste;
    }
}