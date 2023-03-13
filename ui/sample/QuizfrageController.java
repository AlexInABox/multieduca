/*
 * Autor: Basim Bennaji
 * Thema: Screen der Frage
 * Erstellungsdatum: 04.03.2023
 * Letzte Aenderung: 04.03.2023 21:20
 * Icons: https://ionic.io/ionicons
 * Change-Log:
 *      - TODO: Fortschrittbar zeigt den Fortschritt im Quiz grafisch dar
 *      - TODO: Timer fuer verbleibende Zeit
 *      - Beim Auswaehlen einer Antwort werden die Antwortbuttons deaktiviert und es wird auf die weiteren Spieler/Host gewartet. 04.03.2023 20:23 ~basim
 *      - Vorlaeufige Methode fuer timer hinzugefuegt. 04.03.2023 20:45 ~basim
 *      - Set'er fuer Frage und Antwortmoeglichkeiten hinzugefuegt. 04.03.2023 20:51 ~basim
 *      - Antwortbuttons und Frage werden deaktiviert nach Auswahl einer Antwort. Es erscheint eine Bestaetigungsnachricht ueber den Buttons. 04.03.2023 21:20 ~basim
 *      - Entfernen von unnoetigen Teilen, Fehlerbehebungen und einfuegen der eigentlichen Logik dieses Screens, Niklas Bamberg 11.03.2023 
 *      - Anpassungen in warteAufEvent() 13.03.2023 ~Niklas Bamberg
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

     private ArrayList<Integer> gegebeneAntworten = new ArrayList<Integer>();
     private client c;
     private int gebrauchteZeit;
     private long rundenStartZeit;
 
     // vorlaeufige Methode fuer den timer.
     public void timer(int sek) {
        int restSek = sek;
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
 
     @FXML
     void initialize() {
        c = StartscreenController.getClient();
        warteAufEvent();
    }

    public void warteAufEvent() {
        int event = c.listenForEvent();
        switch(event) {
            case 2:
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
                Platform.runLater(new Runnable() {
                    public void run() {
                        switchScreen("../rsc/MidRanking.fxml"); //hier muss statt zum Startscreen zum zwischenrankingscreen gewechselt werden
                    }
                });
                break;
            case 4:
                Platform.runLater(new Runnable() {
                    public void run() {
                        switchScreen("../rsc/Endranking.fxml");
                    }
                });
                break;
        }
    }
            
    
    public void chooseA(ActionEvent e) {
        gebrauchteZeit = (int) (System.currentTimeMillis() - rundenStartZeit)/1000;
        gegebeneAntworten.add(0);
        antA.setDisable(true);
    }

    public void chooseB(ActionEvent e) {
        gebrauchteZeit = (int) (System.currentTimeMillis() - rundenStartZeit)/1000;
        gegebeneAntworten.add(1);
        antB.setDisable(true);
    }

    public void chooseC(ActionEvent e) {
        gebrauchteZeit = (int) (System.currentTimeMillis() - rundenStartZeit)/1000;
        gegebeneAntworten.add(2);
        antC.setDisable(true);
    }

    public void chooseD(ActionEvent e) {
        gebrauchteZeit = (int) (System.currentTimeMillis() - rundenStartZeit)/1000;
        gegebeneAntworten.add(3);
        antD.setDisable(true);
    }

    private void switchScreen (String fxml) {
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