/*
 * Autor: Basim Bennaji
 * Thema: Ranking zwischen den Fragen
 * Erstellungsdatum: 
 * Letzte Aenderung:
 * Icons: https://ionic.io/ionicons
 * Change-Log:
 *      -
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

    String successStyle = String.format(
				"-fx-background-color: #03C988; -fx-background-radius: 20");

    String errorStyle = String.format(
				"-fx-background-color: #C92403; -fx-background-radius: 20");
    //Variablen fuer Elemente aus FXML-Datei
    @FXML
    private SVGPath ergebnisC, ergebnisF;

    @FXML
    private Pane ergebnisPane;

    @FXML
    private Label ergebnisText, player1, player2, player3, playerPunkte1, playerPunkte2, playerPunkte3, position, punkte;


    private client c;

    
    @FXML
    void initialize() {
        c = StartscreenController.getClient();
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
        position.setText(bestenliste.entrySet().stream().filter(entry -> entry.getValue().equals(c.getName())).map(Map.Entry::getKey).findFirst().get().toString());
        punkte.setText(spielerPunkteMap.get(c.getName()).toString());
        new Thread(() -> {
            int event = c.listenForEvent();
            switch(event) {
                case 5:
                    Platform.runLater(() -> {
                        switchScreen("../rsc/Quizfrage.fxml");
                    });
                    break;
                case 4:
                    Platform.runLater(() -> {
                        switchScreen("../rsc/Endranking.fxml");
                    });
                    break;
            }
        }).start();
    }

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

