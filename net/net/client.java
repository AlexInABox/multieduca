package net;

import java.net.*;

import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Autor: Alexander Betke
 * Ueberarbeitet: Niklas Bamberg
 * Datum: 2023-02-18
 *
 * Zweck: Dieses Programm ist ein Client, der sich mit einem Server verbindet
 * und
 * mit ihm über das Netzwerk kommuniziert.
 * 
 * 03.03.2023: Funktionalität für kommunikation von Spielerlisten und
 * Punktelisten hinzugefügt.
 * (Alexander Betke)
 * 
 * 06.03: Anfaengliche Aenderungen fuer UI, Niklas Bamberg und Alexander Betke
 * 
 * 11.03: Diverse Optimierungen und Fehlerbehebungen, Alexander Betke
 * 11.03 erstellen einer sendAnswer() Methode, Niklas Bamberg
 * 12.03 lesen der spieler,punkte-Map und entsprechende get-Methode, Niklas Bamberg
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
    private static String[] playerPoints;
    private HashMap<String, Integer> spielerPunkteMap = new HashMap<String, Integer>();
    // end of game related variables

    /*
     * public static void main(String[] args) { // dev function
     * run(IP, UUID.randomUUID().toString().substring(0, 6));
     * }
     */

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

    public  int waitForGameStart() {
        try {
            String receivedMessage = bf.readLine();

            //while waiting for the round to start the host can send either one of the following messages
            //PLAYER LIST
            //or
            //START ROUND
            //or
            //END GAME
            //if the host sends the PLAYER LIST signal the client will handle the incomming messages and return 0 to indicate that the client is still waiting for the game to start
            //if the host sends the START ROUND signal the client will handle the incomming messages and return 1 to indicate that the game has started
            //if the host sends the END GAME signal the client will handle the incomming messages and return 2 to indicate that the game has ended

            if (receivedMessage.equals("PLAYER LIST")) {
                //receive the player list
                String playerListString = bf.readLine();
                //split the player list string into an array
                playerList = playerListString.substring(1, playerListString.length() - 1).split(","); //remove the brackets from the string and split it into an array
                return 0;
            } else if (receivedMessage.equals("START GAME")) {
                return 1;
            } else if (receivedMessage.equals("END GAME")) {
                return 2;
            }
        } catch (Exception e) {
            System.out.println("Fehler beim Warten auf Spielstart");
        }
        return 3;
    }

    public int listenForEvent() {
        try {
            // wait for the round to start
            // if the host send the start signal, the client will prepare to receive the
            // question
            // if the host send the end signal, the client will break the loop and end the

            String receivedMessage = bf.readLine();

            if (receivedMessage.equals("START ROUND")) {
                // receive the question
                question = new JSONObject(bf.readLine());
                return 0;
            } else if (receivedMessage.equals("RESULT")) {
                System.out.println("Result received");
                //hier werden die Ergebnisse, in Form der Punkte der letzten Runde, einem Boolean ob, man die Frage, zumindest teilweise richtig beantwortet hat und einer der Map mit allen Spielern und dazu gehoerigen Punkten empfangen
                answeredRight = Boolean.parseBoolean(bf.readLine());
                rundenPunkte = Integer.parseInt(bf.readLine());
                //erstellen der SpielerPunkteMap
                String spielerPunkteString = bf.readLine();
                String[] spielerPunkteArray = spielerPunkteString.split(" ");
                for (String spielerPunkte : spielerPunkteArray) {
                    String[] spielerPunkteSplitted = spielerPunkte.split(",");
                    spielerPunkteMap.put(spielerPunkteSplitted[0], Integer.parseInt(spielerPunkteSplitted[1]));
                }
                return 1;
            }  
            //hier war mal eine else-if fuer "PLAYER LIST", ich glaube allerdings, dass diese in dieser Phase gar nicht mehr gesendet wird
            else if (receivedMessage.equals("PLAYER POINTS")) {
                // receive the player points
                String playerPointsString = bf.readLine();
                // split the player points string into an array
                playerPoints = playerPointsString.split(",");
                return 2;
            } else if (receivedMessage.equals("END GAME")) {
                s.close();
                System.out.println("Connection closed");
                return 3;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 4;
    }

    public void sendAnswer(ArrayList<Integer> antworten, double gebrauchteZeit) {
        try {
            // send the answer
            String antwortenString = "";
            for (int i = 0; i < antworten.size(); i++) {
                if (i < (antworten.size()-1)) antwortenString += antworten.get(i).intValue() + " ";
                else antwortenString += antworten.get(i).intValue();
            }
            pr.println(antwortenString);
            pr.println(gebrauchteZeit);
            pr.flush();
        } catch (Exception e) {
            System.out.println("Fehler beim Senden der Antwort");
        }
    }

    //Empfängt alle Metadaten des Quiz
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

    // Gibt die Liste mit den Punkten aller aktiven Spieler zurück
    public String[] getPlayerPoints() {
        return playerPoints;
    }

    public JSONObject getQuestion() {
        return question;
    }

    public boolean answeredRight() {
        return answeredRight;
    }

    public int getRundenPunkte() {
        return rundenPunkte;
    }

    public HashMap<String, Integer> getSpielerPunkteMap() {
        return spielerPunkteMap;
    }
}
