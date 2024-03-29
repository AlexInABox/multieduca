/*
 * Autoren: Moritz Oehme, Samuel Hoffleit, Basim Bennaji, Niklas Bamberg
 * Thema: Diese Klasse stellt Methoden bereit, die es dem Host ermoeglichen, ein Quiz-Spiel zu starten.
 * Erstellungsdatum: 23.02.2023
 * Letzte Aenderung: 13.03.2023 23:51
 * Icons: https://ionic.io/ionicons
 * Change-Log:
 * 02.03: Arbeiten an Verbindung der Teile, Niklas Bamberg 
 * 04.03: FXML Elemente mit dem neuen Design wurden hinzugefuegt, Basim Bennaji
 * 04.03: Verbund von Netzwerk und UI, Alexander Betke
 * 06.03: Aktualisierung von UI-Elementen, Niklas Bamberg, Samuel Hoffleit, Moritz Oehme
 * 13.03: Kuerzungen und eine Aenderung in spielStarten(), Niklas Bamberg
 * 13.03: Behebung von fehlern, welche ein irresponsives Verhalten des Programms verursachten, Alexander Betke
 */
package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

import net.*;

public class SpielStartenHostController {

    //Variablen fuer Elemente aus der FXML-Datei
    @FXML
    private Label hostIPAdresse, quizName, quizFragenAnz;

    @FXML
    private Button playButton;

    @FXML
    private ListView<String> playerList;

    private Stage stage;
    private Scene scene;

    // Networking-Variablen
    private static ArrayList<RunnableThread> threadList = new ArrayList<RunnableThread>();

    //Diese Methode wird beim Laden des Bilschirms ausgefuehrt und belegt die UI-Elemente mit den richtigen Daten
    @FXML
    protected void initialize() throws IOException {
        hostIPAdresse.setText("IP-Adresse: " + InetAddress.getLocalHost().getHostAddress());
        quizName.setText(HostscreenController.getName());
        quizFragenAnz.setText("" + HostscreenController.getQuiz().getLength());
        //hier wird der "Server" gestartet, sodass sich Spieler verbinden koennen
        host.initServer(playerList, HostscreenController.getQuiz());
    }

    // Button-Funktion um zu das Spiel zu starten
    public void spielStarten(ActionEvent event) throws IOException {
        //sind keine Spieler vorhanden, wird ein Fehler angezeigt und das Spiel wird nicht gestartet
        if (playerList.getItems().size() == 0) {
            //Draw a popup that informs the user that there are no players connected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText("Keine Spieler verbunden");
            alert.setContentText("Es sind keine Spieler verbunden. Bitte warten Sie bis sich ein Spieler verbindet.");
            alert.showAndWait();
            return;
        }
        //ansonsten wird zum naechsten Bildschirm gewechselt
        SpielLaeuftHostController.resetRoundIndex();
        Parent root = FXMLLoader.load(getClass().getResource("/rsc/SpielLaeuftHost.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Methode um zum Hostscreen zurueck zukehren
    public void switchToHost(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/rsc/Hostscreen.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        host.endGame();
    }

    public static ArrayList<RunnableThread> getThreadList() {
        return threadList;
    }

}
