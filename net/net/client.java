package net;

import java.net.*;

import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Autor: Alexander Betke
 * Ueberarbeitet: Niklas Bamberg
 * Datum: 2023-02-18
 *
 * Zweck: Dieses Programm ist ein Client, der sich mit einem Server verbindet und mit ihm über das Netzwerk kommuniziert.
 * 
 * 03.03: Funktionalität für Kmmunikation von Spielerlisten und Punktelisten hinzugefügt, Alexander Betke
 * 06.03: Anfaengliche Aenderungen fuer UI, Niklas Bamberg und Alexander Betke
 * 11.03: Diverse Optimierungen und Fehlerbehebungen, Alexander Betke
 * 11.03: Erstellen einer sendAnswer() Methode, Niklas Bamberg
 * 12.03: Lesen der spieler,punkte-Map und entsprechende get-Methode, Niklas Bamberg
 * 13.03: Hinzufügen der Methodenkommentierung und Vereinheitlichung des Layouts, Rosan Sharma 
 * 13.03: entfernen der waitForGameStart() Methode und andere Kuerzungen, Niklas Bamberg
 * 14.01: Behebung eines Konvertierungsfehlers der Spielerliste in der listenForEvent() Methode, Alexander Betke
 */
public class client {
    // dev variables
    // private static final String IP = "localhost";
    // end of dev variables

    // networking related variables
    private static Socket s;
    private static PrintWriter pr;
    private static BufferedReader bf;
    // end of networking related variables

    // game related variables
    private static String quizLength;
    private static String quizName;
    private static String nick;
    private static int rundenPunkte = 0;
    private static JSONObject question;
    private static boolean answeredRight;
    private static String[] playerList;
    private HashMap<String, Integer> spielerPunkteMap = new HashMap<String, Integer>();
    private HashMap<Integer, String> bestenliste = new HashMap<Integer, String>();
    // end of game related variables

    public client() {
    }

    // Startet den Client und führt ihn aus
    public boolean establishConnection(String ip, String nickname) {
        try {
            // set the nickname
            nick = nickname;

            s = new Socket();

            s.connect(new InetSocketAddress(ip, 2594), 1000); // 1000ms timeout -> throws exception if timeout is exceeded

            if (s.isConnected()) { // if the connection was established
                pr = new PrintWriter(s.getOutputStream());
                InputStreamReader in = new InputStreamReader(s.getInputStream());
                bf = new BufferedReader(in);

                // send nickname
                pr.println(nick);
                pr.flush();

                getQuizData();

                return true;
            } else { // if the connection could not be established
                try {
                    s.close();
                } catch (Exception e) {
                    System.out.println("Socket konnte nicht geschlossen werden.");
                }
                return false;
            }
        } catch (Exception e) {
            System.out.println("Es konnte keine Verbindung hergestellt werden!");
            return false;
        }
    }

    // Verarbeitet die einzelnen Signale des Hosts während Spiel
    public int listenForEvent() {
        try {
            // wait for the round to start
            // if the host sends the start signal, the client will prepare to receive the
            // question
            // if the host send the end signal, the client will break the loop and end the

            String receivedMessage = bf.readLine();

            if (receivedMessage.equals("PLAYER LIST")) {
                //receive the player list
                String playerListString = bf.readLine();
                //split the player list string into an array
                playerList = playerListString.substring(1, playerListString.length() - 1).split(", "); //remove the brackets from the string and split it into an array
                return 0;
            } else if (receivedMessage.equals("START GAME")) {
                return 1;
            } else if (receivedMessage.equals("START ROUND")) {
                // receive the question
                question = new JSONObject(bf.readLine());
                return 2;
            } else if (receivedMessage.equals("RESULT")) {
                // hier werden die Ergebnisse, in Form der Punkte der letzten Runde, einem Boolean ob, man die Frage, zumindest teilweise richtig beantwortet hat und einer der Map mit allen Spielern und dazu gehoerigen Punkten empfangen
                answeredRight = Boolean.parseBoolean(bf.readLine());
                rundenPunkte = Integer.parseInt(bf.readLine());
                // erstellen der SpielerPunkteMap und der bestenliste
                leseBestenliste();
                return 3;
            } else if (receivedMessage.equals("END GAME")) {
                leseBestenliste();
                s.close();
                return 4;
            } else if (receivedMessage.equals("END ZWISCHENRANKING")) {
                return 5;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 5;
    }

    // Liest die empfangenen Bestenlisten fügt sie im Spieler-Punkte Format in die jeweiligen Hashmaps ein
    public void leseBestenliste() throws IOException {
        String[] spielerPunkteArray = bf.readLine().split(" ");
        String[] bestenListenArray = bf.readLine().split(" ");
        spielerPunkteMap.clear();
        bestenliste.clear();
        for (String spielerPunkteEintrag : spielerPunkteArray) {
            String[] spielerPunkteEintragSplitted = spielerPunkteEintrag.split(",");
            spielerPunkteMap.put(spielerPunkteEintragSplitted[0], Integer.parseInt(spielerPunkteEintragSplitted[1]));
        }
        for (String bestenListenEintrag : bestenListenArray) {
            String[] bestenListenEintragSplitted = bestenListenEintrag.split(",");
            bestenliste.put(Integer.parseInt(bestenListenEintragSplitted[0]), bestenListenEintragSplitted[1]);
        }
    }

    // Schickt eigene Antwort und gebrauchte Zeit bis zum Antworten an Host 
    public void sendAnswer(ArrayList<Integer> antworten, double gebrauchteZeit) {
        try {
            // send the answer
            String antwortenString = "";
            for (int i = 0; i < antworten.size(); i++) {
                if (i < (antworten.size() - 1))
                    antwortenString += antworten.get(i).intValue() + " ";
                else
                    antwortenString += antworten.get(i).intValue();
            }
            pr.println(antwortenString);
            pr.println(gebrauchteZeit);
            pr.flush();
        } catch (Exception e) {
            System.out.println("Fehler beim Senden der Antwort");
        }
    }

    // Empfängt alle Metadaten des Quiz
    private static void getQuizData() {
        try {
            if (bf.readLine().equals("QUIZ DATA")) {
                quizLength = bf.readLine();
                quizName = bf.readLine();
            }
        } catch (Exception e) {
            System.out.println("QUIZ DATA konnte nicht gelesen werden.");
        }
    }

    // Gibt die Anzahl der Fragen im Quiz zurück
    public String getQuizLength() {
        return quizLength;
    }

    // Gibt den Namen des Quiz zurück
    public String getQuizName() {
        return quizName;
    }

    // Gibt die IP des Servers zurück
    public String getIP() {
        return s.getInetAddress().getHostAddress();
    }

    // Gibt die Liste aller aktiven Spieler zurück
    public String[] getPlayerList() {
        return playerList;
    }

    // Gibt aktuelle Frage zurück
    public JSONObject getQuestion() {
        return question;
    }

    // Gibt Antwort, ob aktuelle Frage richtig beantwortet wurde
    public boolean answeredRight() {
        return answeredRight;
    }

    // Gibt eigene Punkte für die aktuelle Runde zurück
    public int getRundenPunkte() {
        return rundenPunkte;
    }

    // Gibt Hashmap mit allen Spieler im Spieler-Punkte Format zurück
    public HashMap<String, Integer> getSpielerPunkteMap() {
        return spielerPunkteMap;
    }

    // Gibt Hashmap mit Bestenliste zurück
    public HashMap<Integer, String> getBestenliste() {
        return bestenliste;
    }

    // Gibt eigenen Nickname zurück
    public String getName() {
        return nick;
    }

    public void gameEnded() {
        try {
            s.close();
        } catch (Exception e) {
            System.out.println("Socket konnte nicht geschlossen werden.");
        }
        //reset all variables
        try {
            bf.close();
            pr.close();
        } catch (Exception e) {
            System.out.println("BufferedReader und PrintWriter konnten nicht geschlossen werden.");
        }
        try {
            nick = "";
            quizLength = "";
            quizName = "";
            playerList = new String[0];
            question = new JSONObject();
            answeredRight = false;
            rundenPunkte = 0;
            spielerPunkteMap.clear();
            bestenliste.clear();
        } catch (Exception e) {
            System.out.println("Variablen konnten nicht zurückgesetzt werden.");
        }
    }
}
