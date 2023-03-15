/**
 * Autor: Alexander Betke
 * Ueberarbeitet: Niklas Bamberg
 * Datum: 2023-02-18
 *
 * Zweck: Dieses Programm ist ein Client, der sich mit einem Server verbindet und mit ihm über das Netzwerk kommuniziert.
 *        Dabei wird ein Objekt, dieser Klasse erstellt, wenn ein Spieler IP und Nutzername eingetragen hat und auf 
 *        einem entsprechenden UI-Bildschirm auf einen Button wie "verbinden" klickt.
 *        Dieses Client-Objekt wird dann von UI-Bildschirmen aus verwendet und stellt zu diesem Zweck diverse Methoden
 *        bereit die Daten an den Spielhost senden und Daten empfangen koennen.
 * 
 * Change-Log:
 * 03.03: Funktionalität für Kmmunikation von Spielerlisten und Punktelisten hinzugefügt, Alexander Betke
 * 06.03: Anfaengliche Aenderungen fuer UI, Niklas Bamberg und Alexander Betke
 * 11.03: Diverse Optimierungen und Fehlerbehebungen, Alexander Betke
 * 11.03: Erstellen einer sendAnswer() Methode, Niklas Bamberg
 * 12.03: Lesen der spieler,punkte-Map und entsprechende get-Methode, Niklas Bamberg
 * 13.03: Hinzufügen der Methodenkommentierung und Vereinheitlichung des Layouts, Rosan Sharma 
 * 13.03: entfernen der waitForGameStart() Methode und andere Kuerzungen, Niklas Bamberg
 * 14.03: Behebung eines Konvertierungsfehlers der Spielerliste in der listenForEvent() Methode, Alexander Betke
 * 15.03: Finale auskommentierung, Alexander Betke
 */
package net;

import java.net.*;

import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class client {

    //Die Kommunikation mit dem Host erfolgt ueber sog. Sockets (hier Socket s).
    private static Socket s;
    //Dabei werden Daten mit einem Printwriter (hier PrintWriter pr) gesendet...
    private static PrintWriter pr;
    //...und mit einem BufferedReader (hier BufferedReader bf) empfangen.
    private static BufferedReader bf;

    //Quiz relevante Variablen
    private static String quizLength;
    private static String quizName;
    private static String nick;
    private static JSONObject question;
    private static boolean answeredRight;
    private static String[] playerList;
    private HashMap<String, Integer> spielerPunkteMap = new HashMap<String, Integer>();
    private HashMap<Integer, String> bestenliste = new HashMap<Integer, String>();
    private int roundIndex = 0;
    //END Quiz relevante Variablen

    //Konstruktor
    public client() {
    }

    // Stellt die Verbindung zum Host her, sendet den Nickname und empfaengt essentielle Metadaten des Quiz
    public boolean establishConnection(String ip, String nickname) {
        try {
            // set the nickname
            nick = nickname;

            s = new Socket();

            s.connect(new InetSocketAddress(ip, 2594), 1000); // Wartet 1000ms auf eine erfolgreiche Verbindung. Bei Timeout wird eine Exception geworfen

            if (s.isConnected()) { //Wenn die Verbindung erfolgreich war:
                pr = new PrintWriter(s.getOutputStream());
                InputStreamReader in = new InputStreamReader(s.getInputStream());
                bf = new BufferedReader(in);

                // send nickname
                pr.println(nick);
                pr.flush();

                getQuizData();

                return true;
            } else { //Wenn die Verbindung nicht erfolgreich war:
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

    // Verarbeitet die einzelnen Signale des Hosts während Spiel und gibt diese als Integer an das GUI zurück
    public int listenForEvent() {
        try {
            /* 
            *  Hier wird auf die Signale des Hosts gewartet und entsprechend reagiert:
            *   PLAYER LIST: die Spielerliste wird empfangen und in ein Array gespeichert
            *   START GAME: das Spiel startet
            *   START ROUND: die Frage wird empfangen und in einem JSONObject gespeichert
            *   RESULT: die Ergebnisse der letzten Runde werden empfangen und in einer Map gespeichert
            *   END GAME: das Spiel endet
            *   END ZWISCHENRANKING: Der Zwischenranking Bildschirm wird geschlossen
            */

            String receivedMessage = bf.readLine();

            if (receivedMessage.equals("PLAYER LIST")) {
                //Empfangen der Spielerliste
                String playerListString = bf.readLine();
                //Konvertieren der Spielerliste in ein Array
                playerList = playerListString.substring(1, playerListString.length() - 1).split(", "); //Das erste und letzte Zeichen sind die eckigen Klammern, welche entfernt werden müssen
                return 0;
            } else if (receivedMessage.equals("START GAME")) {
                return 1;
            } else if (receivedMessage.equals("START ROUND")) {
                // receive the question
                question = new JSONObject(bf.readLine());
                roundIndex++;
                return 2;
            } else if (receivedMessage.equals("RESULT")) {
                // hier werden die Ergebnisse, in Form der Punkte der letzten Runde, einem Boolean ob, man die Frage, zumindest teilweise richtig beantwortet hat und einer der Map mit allen Spielern und dazu gehoerigen Punkten empfangen
                answeredRight = Boolean.parseBoolean(bf.readLine());
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
        //Da die beiden Maps vor dem Versenden durch den Host in Stringform dargestellt werden mussten,
        //muessen sie hier wieder zu den eigentliche Hashmaps konvertiert werden.
        String[] spielerPunkteArray = bf.readLine().split(" ");
        String[] bestenListenArray = bf.readLine().split(" ");
        //Die Maps werden geleert und mit den neuen Werten befuellt.
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

    // Schickt die eigene Antwort und gebrauchte Zeit, bis zum Antworten, an Host
    public void sendAnswer(ArrayList<Integer> antworten, double gebrauchteZeit) {
        try {
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

    // Beeendet das Spiel, schließt alle Verbindungen und setzt alle Variablen auf ihren Anfangswert zurück
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
            spielerPunkteMap.clear();
            bestenliste.clear();
            roundIndex = 0;
        } catch (Exception e) {
            System.out.println("Variablen konnten nicht zurückgesetzt werden.");
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

    public int getRoundIndex() {
        return roundIndex;
    }
}
