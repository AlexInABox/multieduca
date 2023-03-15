/*
 * Autor: Samuel Hoffleit, Basim Bennaji, Moritz Oehme
 * Thema: 
 * Erstellungsdatum: 023-03-09
 * Letzte Aenderung:
 * Icons: https://ionic.io/ionicons
 * Change-Log:
 * 02.03: Arbeiten an Verbindung der Teile, Niklas Bamberg
 * 13.03: Kleine Aenderungen in initialize() und von roundIndex, Niklas Bamberg
 * 13.03: Behebung von fehlern, welche ein irresponsives verhalten des Programms verursachten, Alexander Betke
 * 15.03: Der naechsteRundeButton ist nun deaktiviert, solange die Runde noch nicht beendet ist, Alexander Betke
 * 15.03: finale Auskommentierung, Alexander Betke
 *
 */
package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import net.host;

import java.io.IOException;

public class SpielLaeuftHostController {

    private Stage stage;
    private Scene scene;
    private static int roundIndex = 1;

    //Variablen fuer Elemente aus FXML-Datei
    @FXML
    private Label frageText;

    @FXML
    public Button nextButton;

    @FXML
    private Label quizName;

    public SpielLaeuftHostController() throws IOException {
    }

    public static void resetRoundIndex() {
        roundIndex = 1;
    }

    // Methode, welche die naechste Runde startet
    public void startRound(ActionEvent event) {
        //Zuerst wird ein neuer Thread erstellt, um die Runde zu starten und zu verhindern, dass das Programm einfriert
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Solange die Runde nicht die letzte ist, wird die Runde gestartet
                    if (roundIndex < HostscreenController.getQuiz().getLength()) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                //lock next round button
                                nextButton.setDisable(true);
                                frageText.setText(
                                        "Frage " + (roundIndex + 1) + "/" + HostscreenController.getQuiz().getLength());
                            }
                        });
                        //Wir beenden das Zwischenranking und starten die naechste Runde
                        host.endZwischenRanking();
                        host.startRound(roundIndex, nextButton);
                    } else {
                        //Wenn die Runde die letzte ist, wird das Spiel beendet
                        host.endGame();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    switchToHostscreen(event);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    roundIndex++;
                } catch (Exception e) {
                    System.out.println("Fehler beim Senden der Frage");
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    //Eine Methode, welche zum Hostscreen wechselt
    public void switchToHostscreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/rsc/Hostscreen.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void initialize() {
        //Der nextButton ist zu Beginn deaktiviert, da die Runde direkt gestartet wird
        nextButton.setDisable(true);
        quizName.setText("Quiz: " + HostscreenController.getName());
        frageText.setText("Frage " + roundIndex + "/" + HostscreenController.getQuiz().getLength());

        //Hier wird die erste Runde gestartet
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                host.startGame();
                host.startRound(0, nextButton);
            }
        });
        thread.start();
    }

}
