package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

//aenderungen:
//-arbeiten an verbindung der Teile: Niklas Bamberg -02.03.2023

public class SpielLaeuftHostController {

    private Stage stage;
    private Scene scene;
    private static ArrayList<RunnableThread> threadList;
    private static int roundIndex = 0;

    public SpielLaeuftHostController() throws IOException {
        threadList = SpielStartenHostController.getThreadList();
    }

    // Networking-methode (wird durch Buttonclick aufgerufen)
    public void startRound(ActionEvent event) {
        try {
            if (roundIndex < HostscreenController.getQuiz().getLength()) {
                for (RunnableThread thread : threadList) {
                    thread.sendQuestion(roundIndex);
                }
            } else {
                for (RunnableThread thread : threadList) {
                    thread.endGame();
                }
                switchToHostscreen(event);
            }
            roundIndex++;
        } catch (Exception e) {
            System.out.println("Fehler beim Senden der Frage");
            e.printStackTrace();
        }
    }

    public void switchToHostscreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../rsc/Hostscreen.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
