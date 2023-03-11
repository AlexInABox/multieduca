package net;

import data.*;
import data.Quiz;
import sample.*;
import java.net.*;
import java.io.*;

import org.json.JSONException;
import org.json.JSONObject;

import javafx.scene.control.ListView;

/*
* Autor: Alexander Betke, Jonas Lossin, Rosan Sharma, Maximilian Gombala, Niklas Bamberg
* Datum: 2022-02-16
* 
* ...
*Ergaenzungen für UI-Aenderungen, Niklas Bamberg - 06.03
*Unterstützung mehrerer gegebener Antworten, Niklas Bamberg - 09.03
*Diverse Fehlerbehebungen, Alexander Betke - 11.03
*kleine Anpassungen in getAnswer(), Niklas Bamberg - 11.03
*/

public class RunnableThread implements Runnable {

    private static ServerSocket ss;
    private static Socket s;

    private static String nick;
    //UI-Element
    private ListView<String> playerList;

    public static int punkte;

    private Quiz quiz;

    private BufferedReader bf;
    private PrintWriter pr;

    RunnableThread(ServerSocket server, Quiz quiz, ListView<String> playerList) {
        ss = server;
        this.quiz = quiz;
        this.playerList = playerList;
    }

    public void run() {
        try {
            s = ss.accept();
            host.createNewThread(playerList);

            InputStreamReader in = new InputStreamReader(s.getInputStream());
            bf = new BufferedReader(in);
            pr = new PrintWriter(s.getOutputStream());
            System.out.println("New connection established");
            System.out.println("Creating a new thread");

            nick = bf.readLine(); // the first message is the name of the player
            System.out.println("Trying to add " + nick + " to the player list");
            playerList.getItems().add(nick);
            System.out.println("Added " + nick + " to the player list");
            sendQuizData();

            regClient(s);
            host.refreshPlayerList(playerList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //ueberfluessig
    private static void regClient(Socket s) {
        String ip = s.getInetAddress().toString();
        System.out.println("Client \"" + nick + "\" connected from " + ip);
    }

    public void start() {
        System.out.println("Starting new Thread");
        Thread t = new Thread(this);
        t.start();
    }

    public void sendQuestion(int roundIndex) throws IOException, JSONException, InterruptedException {
        JSONObject frage = quiz.getFrage(roundIndex);
        pr.println("START ROUND");
        pr.println(frage.toString());
        pr.flush();
    }

    public void getAnswer(int roundIndex) throws IOException, JSONException, InterruptedException {
        JSONObject frage = quiz.getFrage(roundIndex);
        //Auslesen des Antwort-Arrays
        String[] antwortString = bf.readLine().split(" ");
        int[] antworten = new int[antwortString.length];
        for (int i = 0; i < antworten.length; i++) {   
            if (antwortString[i].equals("")) antworten[i] = Integer.parseInt(antwortString[i]);
            else antworten[i] = -1;
        }
        double zeit = Double.parseDouble(bf.readLine());
        int rundenPunkte = Quiz.genPunkte(frage, antworten, zeit);
        punkte += rundenPunkte;
        boolean ergebnis = rundenPunkte > 0;
        pr.println("RESULT");
        pr.println(ergebnis);
        pr.println(punkte);
        pr.flush();
    }

    public void sendQuizData() {
        pr.println("QUIZ DATA");
        pr.println(quiz.getLength());
        pr.println(sample.HostscreenController.getName());
        pr.flush();
    }

    public void endGame() {
        pr.println("END GAME");
        pr.flush();
    }

    public void startGame() {
        pr.println("START GAME");
        pr.flush();
    }

    public void refreshPlayerList(ListView<String> playerList) {
        this.playerList = playerList;
        pr.println("PLAYER LIST");
        pr.println(playerList.getItems().toString());
        pr.flush();
    }
}