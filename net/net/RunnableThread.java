
/*
* Autor: Alexander Betke, Jonas Lossin, Rosan Sharma, Maximilian Gombala, Niklas Bamberg
* Zweck: Ein RunnableThread soll sich mit einem Spieler (genauer: mit einem client) verbinden und mit ihm kommunizieren.
*        Da sich mit dem Spielhost aber mehr als nur ein Spieler verbinden koennen soll und parallel mit allen Spielern
*        kommuniziert werden muss, wird hier auf sog. Threads zurueckgegriffen. Diese Threads werden in der Klasse host
*        erstellt und von dort aus werden auch die verschiedenen Methoden zur Kommunikation, die diese Klasse bereitstellt,
*        aufgerufen.
* Erstellungsdatum: 16.02.2023
* Change-Log:
*   Ergaenzungen für UI-Aenderungen, Niklas Bamberg - 06.03
*   Unterstützung mehrerer gegebener Antworten, Niklas Bamberg - 09.03
*   Diverse Fehlerbehebungen, Alexander Betke - 11.03
*   kleine Anpassungen in getAnswer(), Niklas Bamberg - 11.03
*   hinzufuegen des sendes einer spieler,punkte-Map, Niklas Bamberg - 12.03
*/
package net;

import data.Quiz;
import java.net.*;
import java.util.HashMap;
import java.io.*;

import org.json.JSONException;
import org.json.JSONObject;

import javafx.application.Platform;
import javafx.scene.control.ListView;

//Da die Threads die verwendet werden sollen ein sog. Runnable-Referenz-Objekt bekommen muessen, implementiert
//RunnableThread das Runnable-Interface.
public class RunnableThread implements Runnable {

    //Wie in client.java beschrieben, wird die Kommunikation ueber Sockets realisiert. Hier wird außerdem ein ServerSocket
    //verwendet
    private static ServerSocket ss;
    private static Socket s;
    //auch hier dienen BufferedReader und PrintWriter zum lesen bzw schreiben von Daten
    private BufferedReader bf;
    private PrintWriter pr;

    //Diese Variablen speichern die Daten vom verbundenen Spieler
    private String nick;
    public int punkte;

    //Das Quiz mit all seinen Fragen wird gespeichert, um entsprechende Fragen zu versenden und die Antworten zu verarbeiten
    private Quiz quiz;

    //UI-Element zur Anzeige der Spieler im Wartebilschirm
    private ListView<String> playerList;

    //Konstruktor zur Erstellung eines neuen RunnableThreads, dabei muessen ein ServerSocket Objekt, logischerweise
    //das Quiz und das gerade beschriebene UI-Element uebergeben werden
    RunnableThread(ServerSocket server, Quiz quiz, ListView<String> playerList) {
        ss = server;
        this.quiz = quiz;
        this.playerList = playerList;
    }

    //Die Methode run() muss hier, wegen des Runnable-Interfaces, implementiert werden. Sie wird, aehnlich wie eine
    //main-Methode, beim Start eines Threads ausgefuehrt.
    //hier werden oben deklarierten Variablen zur Netzwerk-Kommunikation initialisiert und es findet eine erste
    //Kommunikation mit dem Spieler statt.
    public void run() {
        try {
            //Die Methode accept() wartet, bis sich ein Spieler mit dem Server verbindet. Dann wird der Socket s initialisiert
            s = ss.accept();

            //Dieser Methodenaufruf sorgt dafuer, dass ein neuer RunnableThread erstellt wird, sobald sich ein Spieler ueber
            //accept mit dem aktuellen Thread verbunden hat. Der neue wartet dann ebenfalls in accept().
            //in host.java mehr zu dieser Logik...
            host.createNewThread(playerList, quiz);

            //Initialisierung der Netzwerkvariablen
            InputStreamReader in = new InputStreamReader(s.getInputStream());
            bf = new BufferedReader(in);
            pr = new PrintWriter(s.getOutputStream());

            //der Name des Spielers wird hier ausgelesen und in der UI-Spielerliste hinzugefuegt
            nick = bf.readLine();
            Platform.runLater(() -> playerList.getItems().add(nick));

            sendQuizData();

            //ausserdem werden, ueber die host-Klasse, allen Spielern die neue Spielerliste zugesendet, da es ja
            //einen neuen Spieler gibt.
            host.refreshPlayerList(playerList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //hier mit Uebergabe des RunnableThread-Objekts (mit 'this') ein neuer Thread erstellt und gestartet.
    public void start() {
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
            if (!antwortString[i].equals(""))
                antworten[i] = Integer.parseInt(antwortString[i]);
            else
                antworten[i] = -1;
        }
        double zeit = Double.parseDouble(bf.readLine());
        int rundenPunkte = Quiz.genPunkte(frage, antworten, zeit);
        punkte += rundenPunkte;
        boolean ergebnis = rundenPunkte > 0;

        //hier werden die Ergebnisse der aktuellen Runde an die Spieler gesendet
        pr.println("RESULT");
        pr.println(ergebnis);
        pr.flush();
    }

    //hier wird der Spielstand des gesamten Spiels, in Form einer Bestenliste, an die Spieler gesendet
    public void sendBestenliste(HashMap<String, Integer> punkteMap, HashMap<Integer, String> bestenliste) {
        //Umwandlung der HashMap in einen String, der Art: "Name1,Punkte1 Name2,Punkte2 ..."
        String mapString = "";
        String bestenlistenString = "";
        for (String key : punkteMap.keySet())
            mapString += key + "," + punkteMap.get(key) + " ";
        for (int key : bestenliste.keySet())
            bestenlistenString += key + "," + bestenliste.get(key) + " ";
        pr.println(mapString.substring(0, mapString.length() - 1));
        pr.println(bestenlistenString.substring(0, bestenlistenString.length() - 1));
        pr.flush();
    }

    public void sendQuizData() {
        pr.println("QUIZ DATA");
        pr.println(quiz.getLength());
        pr.println(sample.HostscreenController.getName());
        pr.flush();
    }

    public void endGame(HashMap<String, Integer> punkMap, HashMap<Integer, String> bestenliste) {
        pr.println("END GAME");
        sendBestenliste(punkMap, bestenliste);
        pr.flush();
    }

    public void startGame() {
        pr.println("START GAME");
        pr.flush();
    }

    public void endZwischenRanking() {
        pr.println("END ZWISCHENRANKING");
        pr.flush();
    }

    public void refreshPlayerList(ListView<String> playerList) {
        this.playerList = playerList;
        pr.println("PLAYER LIST");
        pr.println(playerList.getItems().toString());
        pr.flush();
    }

    public String getNick() {
        return nick;
    }

    public int getPunkte() {
        return punkte;
    }
}