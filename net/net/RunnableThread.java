package net;

import data.*;
import data.Quiz;
import sample.*;
import java.net.*;
import java.io.*;

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

    public void sendQuestion(int roundIndex) throws IOException {
        JSONObject frage = quiz.getFrage(roundIndex);
        pr.println("START ROUND");
        pr.println(frage.toString());
        pr.flush();
        //Auslesen des Antwort-Arrays
        String[] antwortString = bf.readLine().split(" ");
        int[] antwort = new int[antwortString.length];
        for (int i = 0; i < antwort.length; i++) {
            antwort[i] = Integer.parseInt(antwortString[i]);
        }

        double zeit = Double.parseDouble(bf.readLine());
        System.out.println(nick + " hat " + zeit + " Sekunden gebraucht");

        punkte += Quiz.genPunkte(frage, antwort, zeit);
        System.out.println(nick + " bekommt: " + punkte + " Punkte");
        boolean ergebnis = punkte > 0;
        if (ergebnis) {
            System.out.println(nick + " hat die Frage RICHTIG beantwortet");
        } else {
            System.out.println(nick + " hat die Frage FALSCH beantwortet");
        }
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