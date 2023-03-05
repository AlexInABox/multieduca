package net;

import java.net.*;
import java.util.UUID;

import org.json.JSONObject;
import java.io.*;

import java.util.ArrayList;

/**
 * Autor: Alexander Betke
 * Ueberarbeitet: Niklas Bamberg
 * Datum: 2022-02-18
 *
 * Zweck: Dieses Programm ist ein Client, der sich mit einem Server verbindet
 * und
 * mit ihm über das Netzwerk kommuniziert.
 * 
 * 03.03.2021: Funktionalität für kommunikation von Spielerlisten und
 * Punktelisten hinzugefügt.
 * (Alexander Betke)
 */
public class client {
    // dev variables
    private static final String IP = "localhost";
    // end of dev variables

    // networking related variables
    private static Socket s;
    private static PrintWriter pr;
    private static BufferedReader bf;
    // end of networking related variables

    // game related variables
    private static String nick;
    private static int score = 0;
    private static JSONObject question;
    private static int answer = 0;
    private static boolean answeredRight;
    private static double time = 0;
    private static ArrayList<String> playerList = new ArrayList<String>();
    private static ArrayList<String> playerPoints = new ArrayList<String>();
    // end of game related variables

    public static void main(String[] args) { // dev function
        run(IP, UUID.randomUUID().toString().substring(0, 6));
    }

    // Startet den Client und führt ihn aus
    public static void run(String ip, String nickname) {
        try {
            // set the nickname
            nick = nickname;

            establishConnection(ip);
            // send nickname
            pr.println(nick);
            pr.flush();

            while (true) {
                // wait for the round to start
                // if the host send the start signal, the client will prepare to receive the
                // question
                // if the host send the end signal, the client will break the loop and end the

                String receivedMessage = bf.readLine();

                if (receivedMessage.equals("START ROUND")) {
                    // receive the question
                    question = new JSONObject(bf.readLine());
                    System.out.println("Das ist die erhaltene Frage: " + question);

                    // give the question to the function that will **eventually return the answer**
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    answer = 3;
                    // send the answer
                    pr.println(answer);
                    pr.println(time);
                    pr.flush();
                } else if (receivedMessage.equals("RESULT")) {
                    System.out.println("Result received");
                    // receive the result
                    answeredRight = Boolean.parseBoolean(bf.readLine()); // Example: Boolean.parseBoolean("True")
                                                                         // returns true.
                    // Example: Boolean.parseBoolean("yes") returns false.
                    // receive the points
                    score = Integer.parseInt(bf.readLine()); // Example: Integer.parseInt("123") returns 123.

                    // give the result and the points to the function that will handle those+

                } else if (receivedMessage.equals("PLAYER LIST")) {
                    // receive the player list
                    String playerListString = bf.readLine();

                    // split the player list string into an array
                    String[] playerListArray = playerListString.split(",");

                    // clear the player list
                    playerList.clear();

                    // add the players to the player list
                    for (String player : playerListArray) {
                        playerList.add(player);
                    }
                } else if (receivedMessage.equals("PLAYER POINTS")) {
                    // receive the player points
                    String playerPointsString = bf.readLine();

                    // split the player points string into an array
                    String[] playerPointsArray = playerPointsString.split(",");

                    // clear the player points
                    playerPoints.clear();

                    // add the players to the player points
                    for (String player : playerPointsArray) {
                        playerPoints.add(player);
                    }
                } else if (receivedMessage.equals("END GAME")) {
                    break;
                }
            }
            // once the loop is broken, the client will close the connection
            s.close();
            System.out.println("Connection closed");

        } catch (Exception e) {
            dummy.errorOccured(e);
        }
    }

    // Stellt die Verbindung zum Server her
    private static void establishConnection(String ip) throws Exception {
        s = new Socket(ip, 2594);
        pr = new PrintWriter(s.getOutputStream());
        InputStreamReader in = new InputStreamReader(s.getInputStream());
        bf = new BufferedReader(in);
    }

    // Gibt die Liste aller aktiven Spieler zurück
    public static ArrayList getPlayerList() {
        return playerList;
    }

    // Gibt die Liste mit den Punkten aller aktiven Spieler zurück
    public static ArrayList getPlayerPoints() {
        return playerPoints;
    }

    public static JSONObject getQuestion() {
        return question;
    }

    public static boolean answeredRight() {
        return answeredRight;
    }
}
