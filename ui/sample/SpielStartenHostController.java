/*
 * Autoren: Moritz Oehme, Samuel Hoffleit, Basim Bennaji, Niklas Bamberg
 * Thema: Methoden des Startscreens.
 * Erstellungsdatum: 23.02.2023 (?)
 * Letzte Aenderung: 04.03.2023 23:51
 * Icons: https://ionic.io/ionicons
 * Change-Log:
 *      - FXML Elemente mit dem neuen Design wurden hinzugefuegt. 04.03.2023 23:51 ~basim
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
import java.net.ServerSocket;
import java.util.ArrayList;

//aenderungen:
//-arbeiten an verbindung der Teile: Niklas Bamberg -02.03.2023

public class SpielStartenHostController {

    @FXML
    private Label hostIPAdresse;

    @FXML
    private Button playButton;

    @FXML
    private ListView<?> playerList;

    @FXML
    private Label quizFragenAnz;

    @FXML
    private Label quizName;

    private Stage stage;
    private Scene scene;
    private static Quiz quiz;

    // Networking-Variablen
    private static ArrayList<RunnableThread> threadList = new ArrayList<RunnableThread>();
    private static ServerSocket ss;

    @FXML
    protected void initialize() throws IOException {
        quiz = HostscreenController.getQuiz();
        hostIPAdresse.setText("IP-Adresse: " + InetAddress.getLocalHost().getHostAddress());
        quizName.setText(HostscreenController.getName());
        quizFragenAnz.setText(""+HostscreenController.getQuiz().getLength());
        ss = new ServerSocket(2594);
        initServer();
    }

    // Button-FUnktion um zu Fragen-Screen zu wechseln
    public void spielStarten(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../rsc/SpielLaeuftHost.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        threadList.remove(threadList.size() - 1);
    }

    public static void initServer() throws IOException {
        RunnableThread t = new RunnableThread(ss, quiz);
        threadList.add(t);
        t.start();
    }

    // Methode um zum Hostscreen zurueck zukehren
    public void switchToHost (ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../rsc/Hostscreen.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public static ArrayList<RunnableThread> getThreadList() {
        return threadList;
    }

}
