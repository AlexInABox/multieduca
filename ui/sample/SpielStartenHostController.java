package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

//aenderungen:
//-arbeiten an verbindung der Teile: Nila Bamberg -02.03.2023

public class SpielStartenHostController {

    private Stage stage;
    private Scene scene;
    private static Quiz quiz;

    // Networking-Variablen
    private static ArrayList<RunnableThread> threadList = new ArrayList<RunnableThread>();
    private static ServerSocket ss;

    @FXML
    private Label ipLabel, nameLabel;

    @FXML
    protected void initialize() throws IOException {
        quiz = HostscreenController.getQuiz();
        // ipLabel.setText("IP-Adresse: " + "");
        // nameLabel.setText(quizName);
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

    public static ArrayList<RunnableThread> getThreadList() {
        return threadList;
    }
}
