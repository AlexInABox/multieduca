/*
 * Autoren: Moritz Oehme, Samuel Hoffleit, Basim Bennaji, Niklas Bamberg
 * Thema: Methoden des Startscreens.
 * Erstellungsdatum: 23.02.2023 (?)
 * Letzte Aenderung: 04.03.2023 23:51
 * Icons: https://ionic.io/ionicons
 * Change-Log:
 *      - FXML Elemente mit dem neuen Design wurden hinzugefuegt. 04.03.2023 23:51 ~basim
 *      - Verbund von Netzwerk und UI. 04.03.2023 23:51 ~Alexander Betke
 */
package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

import net.*;

//aenderungen:
//-arbeiten an verbindung der Teile: Niklas Bamberg -02.03.2023
//-aktualisierung von UI-Elementen: Niklas Bamberg, Samuel Hoffleit, Moritz Oehme -06.03.2023
//-kuerzungen und eine aendeung in spielStarten(): Niklas Bamberg -13.03.2023
//-behebung von fehlern, welche ein irresponsives verhalten des Programms verursachten: Alexander Betke -13.03.2023

/**
 * Autor: Samuel Hoffleit, Basim Bennaji, Moritz Oehme
 * Ueberarbeitet:
 * Datum: 2023-03-09
 *
 * Zweck: 
 */

public class SpielStartenHostController {

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

    @FXML
    protected void initialize() throws IOException {
        hostIPAdresse.setText("IP-Adresse: " + InetAddress.getLocalHost().getHostAddress());
        quizName.setText(HostscreenController.getName());
        quizFragenAnz.setText("" + HostscreenController.getQuiz().getLength());
        host.initServer(playerList, HostscreenController.getQuiz());
    }

    // Button-FUnktion um zu Fragen-Screen zu wechseln
    public void spielStarten(ActionEvent event) throws IOException {
        SpielLaeuftHostController.resetRoundIndex();
        Parent root = FXMLLoader.load(getClass().getResource("/rsc/SpielLaeuftHost.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                host.startGame();
                host.startRound(0);
            }
        });
        thread.start();
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
