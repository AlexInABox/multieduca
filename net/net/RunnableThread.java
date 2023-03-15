/**
* Autor: Alexander Betke, Jonas Lossin, Rosan Sharma, Maximilian Gombala, Niklas Bamberg
* Datum: 16.02.2023
*
* Zweck: Ein RunnableThread soll sich mit einem Spieler (genauer: mit einem client) verbinden und mit ihm kommunizieren.
*        Da sich mit dem Spielhost aber mehr als nur ein Spieler verbinden koennen soll und parallel mit allen Spielern
*        kommuniziert werden muss, wird hier auf sog. Threads zurueckgegriffen. Diese Threads werden in der Klasse host
*        erstellt und von dort aus werden auch die verschiedenen Methoden zur Kommunikation, die diese Klasse bereitstellt,
*        aufgerufen.
*
* Change-Log:
* 06.03: Ergaenzungen für UI-Aenderungen, Niklas Bamberg
* 09.03: Unterstützung mehrerer gegebener Antworten, Niklas Bamberg
* 11.03: Diverse Fehlerbehebungen, Alexander Betke
* 11.03: kleine Anpassungen in getAnswer(), Niklas Bamberg
* 12.03: hinzufuegen des sendes einer spieler,punkte-Map, Niklas Bamberg
* 15.03: Finale Auskommentierung, Alexander Betke
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

    //hier wird mit Uebergabe des RunnableThread-Objekts (mit 'this') ein neuer Thread erstellt und gestartet.
    public void start() {
        Thread t = new Thread(this);
        t.start();
    }

    //die folgenden Methoden realisieren die eigentliche Kommunikation.
    //soll etwas an den Client gesendet werden, geschieht das mit pr.println(). Damit der Client auch mit
    //dem erhaltenen umgehen kann, wird vor der eigentlichen Versendung der Daten, das entsprehende Ereignis
    //definiert, z.B. "START ROUND" oder "RESULT" zum Start einer neuen Runde oder zum Senden der Ergebnisse.

    //Diese Methode sendet die aktuelle Frage in Form eines JSON-Objekts an den Spieler.
    //Somit wird eine Fragerunde gestartet und die Spieler gelangen in einen entsprechenden Bildschirm.
    public void sendQuestion(int roundIndex) throws IOException, JSONException, InterruptedException {
        JSONObject frage = quiz.getFrage(roundIndex);
        pr.println("START ROUND");
        pr.println(frage.toString());
        pr.flush();
    }

    //Diese Methode wartet auf die Antwort des Spielers und wertet diese dann, in Form von Punkten, aus.
    public void getAnswer(int roundIndex) throws IOException, JSONException, InterruptedException {
        JSONObject frage = quiz.getFrage(roundIndex);
        //hier geschieht das Auslesen der Antworten und die Umwandlung in ein entsprechendes int-Array
        String[] antwortString = bf.readLine().split(" "); //die readLine() Methode wartet auf eine Antwort des Spielers.
        int[] antworten = new int[antwortString.length];
        for (int i = 0; i < antworten.length; i++) {
            if (!antwortString[i].equals(""))
                antworten[i] = Integer.parseInt(antwortString[i]);
            else
                antworten[i] = -1;
        }
        double zeit = Double.parseDouble(bf.readLine());
        //die ausgelesenen Antwort-Daten werden genutzt um die Punkte fuer die aktuelle Runde zu berechnen
        int rundenPunkte = Quiz.genPunkte(frage, antworten, zeit);
        punkte += rundenPunkte;
        boolean ergebnis = rundenPunkte > 0;

        //hier werden die Ergebnisse der aktuellen Runde an die Spieler gesendet
        pr.println("RESULT");
        pr.println(ergebnis);
        pr.flush();
    }

    //im Anschluss an die getAnswer() Methode wird diese Methode in der host-Klasse aufgerufen, um dem Spieler
    //den Spielstand, in Form von Bestenlisten, mitzuteilen, damit dieser ein entsprechendes Zwischenranking / Endranking
    //angezeigt bekommt.
    //Die Bestenliste und die Information welcher Spieler wie viele Punkte hat, werden als HashMaps implementiert.
    public void sendBestenliste(HashMap<String, Integer> punkteMap, HashMap<Integer, String> bestenliste) {
        //Umwandlung der HashMap in einen String, der Art: "Name1,Punkte1 Name2,Punkte2 ..."
        String mapString = "";
        String bestenlistenString = "";
        for (String key : punkteMap.keySet())
            mapString += key + "," + punkteMap.get(key) + " ";
        for (int key : bestenliste.keySet())
            bestenlistenString += key + "," + bestenliste.get(key) + " ";
        //senden der generierten Strings 
        pr.println(mapString.substring(0, mapString.length() - 1)); //substring() soll das letzte Leerzeichen entfernen
        pr.println(bestenlistenString.substring(0, bestenlistenString.length() - 1));
        pr.flush();
    }

    //Diese Methode teilt dem Spieler den Namen des Quiz und die Fragenanzahl mit.
    public void sendQuizData() {
        pr.println("QUIZ DATA");
        pr.println(quiz.getLength());
        pr.println(sample.HostscreenController.getName());
        pr.flush();
    }

    //diese Methode teilt dem Spieler das Ende des Spiels mit und sendet ihm die Bestenliste.
    public void endGame(HashMap<String, Integer> punkMap, HashMap<Integer, String> bestenliste) {
        pr.println("END GAME");
        sendBestenliste(punkMap, bestenliste);
        pr.flush();
    }

    //Diese Methode vermittelt dem Spieler, dass das Spiel begonnen hat
    public void startGame() {
        pr.println("START GAME");
        pr.flush();
    }

    //Diese Methode vermittelt dem Spieler, dass die naechste Runde beginnt und die aktuell angezeigten
    //Zwischenrankings nicht mehr angezeigt werden sollen.
    public void endZwischenRanking() {
        pr.println("END ZWISCHENRANKING");
        pr.flush();
    }

    //Wird ausgefuehrt, wenn sich ein neuer Spieler verbindet und teilt dem Spieler alle anderen Spieler mit.
    public void refreshPlayerList(ListView<String> playerList) {
        this.playerList = playerList;
        pr.println("PLAYER LIST");
        pr.println(playerList.getItems().toString());
        pr.flush();
    }

    //Methode zum Abfragen des Spielernamens
    public String getNick() {
        return nick;
    }

    //Methode zum Abfragen der Punkte
    public int getPunkte() {
        return punkte;
    }
}