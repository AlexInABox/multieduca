/*
 * Autor: Niklas Bamberg, Basim Bennaji
 * Thema: Diese Klasse stellt Methoden zur Darstellung eines Rankings zwischen den einzelen Fragen bereit.
 * Erstellungsdatum: 06.03.2023
 * Letzte Aenderung: 09.03.2023 11:14
 * Icons: https://ionic.io/ionicons
 * Change-Log:
 * 06.03: Uebetragung der Ranking Daten in UI-Elemente durch initialize-Methode, Niklas Bamberg
 * 09.03: Anpassung des graphischen Designs zur Korrektheit der Antwort, Basim Bennaji
 */
package sample;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import net.client;

public class MidRanking {

    //Komponenten fuer graphisches Design
    //Style, wenn Antwort richtig
    String successStyle = String.format(
            "-fx-background-color: #03C988; -fx-background-radius: 20");
    //Style, wenn Antwort falsch
    String errorStyle = String.format(
            "-fx-background-color: #C92403; -fx-background-radius: 20");

    //UI-Elemente werden aus FXML-Datei ausgelesen
    @FXML
    private SVGPath ergebnisC, ergebnisF;

    @FXML
    private Pane ergebnisPane;

    @FXML
    private Label ergebnisText, player1, player2, player3, playerPunkte1, playerPunkte2, playerPunkte3, position,
            punkte;

    //auch hier wird das client Objekt benoetigt
    private client c;

    //In der zum Laden ausgefuehrten initialize()-Methode werden dem client die vom Host erhaltenen Ranking-Daten
    //entnommen und in die entsprechenden UI-Elemente eingetragen.
    @FXML
    void initialize() {
        //client Objekt wird aus StartscreenController bezogen
        c = StartscreenController.getClient();
        //im folgenden geschieht das Auslesen und Eintragen der Daten in die UI-Elemente
        setzeRueckmeldung(c.answeredRight());
        HashMap<String, Integer> spielerPunkteMap = c.getSpielerPunkteMap();
        HashMap<Integer, String> bestenliste = c.getBestenliste();
        player1.setText(bestenliste.get(1));
        playerPunkte1.setText(spielerPunkteMap.get(bestenliste.get(1)).toString());
        if (bestenliste.size() > 1) {
            player2.setText(bestenliste.get(2));
            playerPunkte2.setText(spielerPunkteMap.get(bestenliste.get(2)).toString());
        }
        if (bestenliste.size() > 2) {
            player3.setText(bestenliste.get(3));
            playerPunkte3.setText(spielerPunkteMap.get(bestenliste.get(3)).toString());
        }
        position.setText(bestenliste.entrySet().stream().filter(entry -> entry.getValue().equals(c.getName()))
                .map(Map.Entry::getKey).findFirst().get().toString());
        punkte.setText(spielerPunkteMap.get(c.getName()).toString());

        //ist das Abgeschlossen, so wird, wie in QuizfrageController.java mit der listenForEvent()
        //Methode auf Nachricht vom Host gewartet und entsprechend reagiert.
        //das warten geschieht parallel in einem neuen Thread, da JavaFX aufgrund des wartens sonst die UI einfriert.
        //hier wird wie in QuizfrageController.java erneut auf Plattform.runLater() zurueckgegriffen, um
        //das Aendern der UI-Elemente zu ermoeglichen.
        new Thread(() -> {
            int event = c.listenForEvent();
            switch (event) {
                case 5:
                    //case 5 bedeutet, dass eine neue Frage gestellt wurde, daher wird zum Fragenfenster gewechselt...
                    Platform.runLater(() -> {
                        switchScreen("/rsc/Quizfrage.fxml");
                    });
                    break;
                case 4:
                    //case 4 bedeutet, dass das Spiel beendet wurde, daher wird zum Endranking gewechselt...
                    Platform.runLater(() -> {
                        switchScreen("/rsc/Endranking.fxml");
                    });
                    break;
            }
        }).start();
    }

    //Diese Methode wechselt unter Angabe des Pfades zur FXML-Datei zum entsprechenden Fenster
    void switchScreen(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = (Stage) player1.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Diese Methode aendert die UI-Elemente entsprechend der Richtigkeit der Antwort des Spielers
    void setzeRueckmeldung(boolean antowrt) {
        if (antowrt) {
            // Format fuer richtige Antwort, gruen ~basim 14.03.2023 19:39
            ergebnisPane.setStyle(successStyle);
            ergebnisText.setText("korrekt!");
        } else {
            // Format fuer falsche Antwort, rot und mit einem "X" ~basim 14.03.2023 19:39
            ergebnisPane.setStyle(errorStyle);
            ergebnisText.setText("falsch!");
            ergebnisC.setVisible(false);
            ergebnisF.setVisible(true);
        }
    }
}
