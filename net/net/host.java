/**
* Autor: Alexander Betke, Jonas Lossin, Rosan Sharma, Maximilian Gombala, Niklas Bamberg
* Datum: 2022-02-13
*
* Zweck: 
*
* Change-Log:
* 12.03: Hinzufuegen von von Erstellen und Senden einer spieler,punkte-Map, Niklas Bamberg
* 13.03: kleinere Kuerzungen, Niklas Bamberg
* 15.03: anpassen der Parameterliste fuer die Funktion startRound, um den naechstenRundeButton zu deaktivieren, Alexander Betke
*/

package net;

import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import data.Quiz;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import sample.SpielLaeuftHostController;

import java.io.*;

public class host {

    //ServerSocket um die Verbindung mit den Spielern aufzubauen
    private static ServerSocket ss;
    //dies ist die oben beschriebene Liste, in der die RunnableThreads gespeichert werden
    private static ArrayList<RunnableThread> threadList = new ArrayList<RunnableThread>();

    //diese beiden Maps werden benutzt, um den aktuellen Spielstand zu, in Form von Bestenlisten zu speichern.
    private static HashMap<String, Integer> punkteMap = new HashMap<String, Integer>();
    private static HashMap<Integer, String> bestenliste = new HashMap<Integer, String>();

    //der Rundenindex gibt an, in der wievielten Runde im Quiz man sich befindet
    private static int roundIndex = 0;

    //Mit dieser Methode wird der ServerSocket initialisiert
    public static void initServer(ListView<String> playerList, Quiz quizArg) throws IOException {
        //dem RunnableThread wird die Playerliste uebergeben, damit er den Playernamen dort hinzufuegen kann
        Quiz quiz = quizArg;
        ss = new ServerSocket(2594);
        //hier wird die Methode createNewThread einmal aufgerufen danach wird diese in RunnableThread aufgerufen
        //unzwar jedes mal wenn sich ein Spieler verbunden hat. Somit wird eine dynamische Anzahl der Spieler
        //moeglich und man muss nicht schon vorher angeben wie viele Spieler maximal teilnehmen duerfen.
        createNewThread(playerList, quiz);
    }

    //Diese Methode erstellt einen neuen RunnableThread und fuegt ihn der Liste hinzu.
    public static void createNewThread(ListView<String> playerList, Quiz quiz) {
        RunnableThread t = new RunnableThread(ss, quiz, playerList);
        threadList.add(t);
        t.start();
    }

    //diese Methode fuehrt einige notwendige Operationen zum Spielstart aus und wird durch
    //entsprechenden Button-Click aufgerufen
    public static void startGame() {
        //schließen des Serversockets, damit keine neuen Spieler mehr beitreten koennen
        try {
            ss.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //der letzte Thread in der Liste wird entfernt, da sich mit diesem kein Spieler verbunden hat
        threadList.remove(threadList.size() - 1);
        //Jeder Thread wird aufgefordert den Spielern den Spielstart zu vermitteln
        try {
            for (RunnableThread thread : threadList) {
                thread.startGame();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startRound(int index, Button nextRoundButton) {
        roundIndex = index;
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
            //unlock next round button for the host
            System.out.println("Unlocking next round button");
            nextRoundButton.setDisable(false);
            roundIndex++;
        } catch (Exception e) {
            System.out.println("Error while starting round");
            nextRoundButton.setDisable(false);
            e.printStackTrace();
        }
    }

    //Diese Methode soll jedem Spieler das ende der Zwischenrankingphase mitteilen
    public static void endZwischenRanking() {
        try {
            for (RunnableThread thread : threadList) {
                thread.endZwischenRanking();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Diese Methode sendet allen Spielern die aktuelle Spielerliste
    public static void refreshPlayerList(ListView<String> playerList) {
        for (RunnableThread thread : threadList) {
            thread.refreshPlayerList(playerList);
        }
    }

    //Diese Methode wird zum Spielende aufgerufen und soll allen Spielern entsprechendes mitteilen
    public static void endGame() {
        try {
            for (RunnableThread thread : threadList) {
                thread.endGame(punkteMap, bestenliste);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //löschen der Liste mit den Threads
        threadList.clear();
    }

    //Methode zum erhalten der Threadliste
    public static ArrayList<RunnableThread> getThreadList() {
        return threadList;
    }

    //Diese Methode generiert ausgehend von einer Map mit Spielern und Punkten eine nach Punkten geordnete Map
    //der Art <Platz, Spielername>
    //Diese dient dann dem Zweck entsprechende Rankings zu ermoeglichen
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