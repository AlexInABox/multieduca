/*
 * Autor: Niklas Bamberg, Basim Bennaji
 * Thema: Diese Klasse stellt Methoden rund um die Beantwort einer Quizfrage bereit. Dazu gehoeren unter anderem das direkte Auswaehlen einer Antwort sowie ein ablaufender Timer.
 * Erstellungsdatum: 04.03.2023
 * Letzte Aenderung: 13.03.2023 21:20
 * Icons: https://ionic.io/ionicons
 * Change-Log:
 * 04.03: Timer fuer verbleibende Zeit, Basim Bennaji
 * 04.03: Beim Auswaehlen einer Antwort werden die Antwortbuttons deaktiviert und es wird auf die weiteren Spieler/Host gewartet, Basim Bennaji
 * 04.03: Vorlaeufige Methode fuer timer hinzugefuegt, Basim Bennaji
 * 04.03: Set'er fuer Frage und Antwortmoeglichkeiten hinzugefuegt, Basim Bennaji
 * 04.03: Antwortbuttons und Frage werden deaktiviert nach Auswahl einer Antwort. Es erscheint eine Bestaetigungsnachricht ueber den Buttons, Basim Bennaji
 * 11.03: Funktionalitaet der Progress Bar gewaehrleistet, Samuel Hoffleit
 * 11.03: Entfernen von unnoetigen Teilen, Fehlerbehebungen und einfuegen der eigentlichen Logik dieses Screens, Niklas Bamberg
 * 13.03: Anpassungen in warteAufEvent(), Niklas Bamberg
 */

package sample;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import net.client;

public class QuizfrageController {

    //UI-Elemente werden aus FXML-Datei ausgelesen
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button antD, antA, antB, antC;

    @FXML
    private Label antGeg, frageText, timer;

    @FXML
    private ProgressBar progbarFragen;

    private Stage stage;
    private Scene scene;

    //in dieser Antworten Liste werden die Antworten gespeichert, die der Spieler ausgewaehlt hat
    private ArrayList<Integer> gegebeneAntworten = new ArrayList<Integer>();
    private client c;

    //Variablen die zur Ermittlung der Antwortzeit dienen
    private int gebrauchteZeit;
    private long rundenStartZeit;

    //Die Methode Timer wird aufgerufen, sobald die Frage angezeigt wird
    //sie hat den zweck einerseits die verbleibende Zeit anzuzeigen und andererseits
    //die Antworten des Spielers zu senden, sobald die Zeit abgelaufen ist.
    public void timer(int sek) {
        int restSek = sek;
        //hier wird der uebergebenen Sekundenzahl entsprechendend lange gewartet
        while (restSek > 0) {
            int min = restSek / 60;
            int sekuebrig = restSek % 60;
            Platform.runLater(new Runnable() {
                public void run() {
                    timer.setText(String.format("%02d:%02d%n", min, sekuebrig));
                }
            });
            try {
                Thread.sleep(1000); // sleep for one second
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            restSek--;
        }
        //ist die Zeit abgelaufen werden alle ausgewaehlten Antworten gesendet
        c.sendAnswer(gegebeneAntworten, gebrauchteZeit);
        warteAufEvent();
    }

    //Methode die bei Wechsel auf diesen Bildschirm aufgerufen wird
    //sie initialisiert das benoetigte client Objekt und wechselt in die folgende Methode
    @FXML
    void initialize() {
        c = StartscreenController.getClient();
        progbarFragen.setProgress((double)(c.getRoundIndex()+1)/(double)Integer.parseInt(c.getQuizLength())); //Progressbar bekommt Wert.
        warteAufEvent();
    }

    //Diese Methode baut auf der listenForEvent() Methode des clients auf.
    //Sie soll auf die moeglichen Ereignisse angemessen reagieren.
    public void warteAufEvent() {
        //in listenForEvent() wird auf Signal des Hosts gewartet und das entsprechende Event
        //wird als int zurueckgegeben
        int event = c.listenForEvent();
        //fuer verschiedene Events wird unterschiedlich reagiert:
        switch (event) {
            case 2:
                //2 bedeutet einen Rundenstart
                //es werden Fragen und Antworten angezeigt und der Timer gestartet
                rundenStartZeit = System.currentTimeMillis();
                Thread zeitThread = new Thread(new Runnable() {
                    public void run() {
                        timer(c.getQuestion().getInt("zeit"));
                    }
                });
                zeitThread.start();
                JSONObject frage = c.getQuestion();
                frageText.setText(frage.getString("text"));
                JSONArray antworten = frage.getJSONArray("antworten");
                antA.setText(antworten.getString(0));
                antB.setText(antworten.getString(1));
                antC.setText(antworten.getString(2));
                antD.setText(antworten.getString(3));
                break;
            case 3:
                //3 bedeutet das Anzeigen des Zwischenrankings
                //das Platform.runLater() ist notwendig, da man sonst keine Aenderungen an UI-Elementen
                //vornehmen kann
                Platform.runLater(new Runnable() {
                    public void run() {
                        //es wird auf den entsprechenden Bildschirm gewechselt
                        switchScreen("/rsc/MidRanking.fxml");
                    }
                });
                break;
            case 4:
                //4 bedeutet das Anzeigen des Endrankings
                Platform.runLater(new Runnable() {
                    public void run() {
                        switchScreen("/rsc/Endranking.fxml");
                    }
                });
                break;
        }
    }

    //die folgenden 4 Methoden werden aufgerufen, sobald ein Spieler eine Antwort auswaehlt
    //dabei wird die entsprechende Antwort in die gegebeneAntworten Liste geschrieben
    //und die Antwortzeit wird gesetzt bzw falls bereits vorhanden ueberschrieben
    public void chooseA(ActionEvent e) {
        gebrauchteZeit = (int) (System.currentTimeMillis() - rundenStartZeit) / 1000;
        gegebeneAntworten.add(0);
        antA.setDisable(true);
    }

    public void chooseB(ActionEvent e) {
        gebrauchteZeit = (int) (System.currentTimeMillis() - rundenStartZeit) / 1000;
        gegebeneAntworten.add(1);
        antB.setDisable(true);
    }

    public void chooseC(ActionEvent e) {
        gebrauchteZeit = (int) (System.currentTimeMillis() - rundenStartZeit) / 1000;
        gegebeneAntworten.add(2);
        antC.setDisable(true);
    }

    public void chooseD(ActionEvent e) {
        gebrauchteZeit = (int) (System.currentTimeMillis() - rundenStartZeit) / 1000;
        gegebeneAntworten.add(3);
        antD.setDisable(true);
    }

    //diese Methode realisiert den Wechsel auf einen anderen Bildschirm unter Angabe
    //der entsprechenden FXML-Datei
    private void switchScreen(String fxml) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource(fxml));
            stage = (Stage) antA.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}