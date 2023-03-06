package sample;

import java.net.*;
import java.io.*;

import org.json.JSONObject;

import javafx.scene.control.ListView;

/*
* Autor: Alexander Betke, Jonas Lossin, Rosan Sharma, Maximilian Gombala, Niklas Bamberg
* Datum: 2022-02-16
* 
* ...
*/

class RunnableThread implements Runnable {

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
            SpielStartenHostController.initServer(playerList);
            InputStreamReader in = new InputStreamReader(s.getInputStream());
            bf = new BufferedReader(in);
            pr = new PrintWriter(s.getOutputStream());
            System.out.println("New connection established");
            System.out.println("Creating a new thread");

            nick = bf.readLine(); // the first message is the name of the player
            playerList.getItems().add(nick);

            regClient(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
        int antwort = Integer.parseInt(bf.readLine());
        System.out.println(nick + " hat die Antwort " + antwort + " gewählt");
        double zeit = Double.parseDouble(bf.readLine());
        System.out.println(nick + " hat " + zeit + " Sekunden gebraucht");

        punkte = Quiz.genPunkte(frage, antwort, zeit);
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
        pr.println(HostscreenController.getName());
        pr.flush();
    }

    public void endGame() {
        pr.println("END GAME");
        pr.flush();
    }

}