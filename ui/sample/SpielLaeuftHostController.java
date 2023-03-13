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
import net.RunnableThread;
import net.host;

import java.io.IOException;
import java.util.ArrayList;

//aenderungen:
//-arbeiten an verbindung der Teile: Niklas Bamberg -02.03.2023
//kleine Aenderungen in initialize() und von roundIndex: Niklas Bamberg - 13.03.2023

public class SpielLaeuftHostController {

    private Stage stage;
    private Scene scene;
    private static int roundIndex = 1;

    @FXML
    private Label frageText;

    @FXML
    private Button nextButton;

    @FXML
    private Label quizName;

    public SpielLaeuftHostController() throws IOException {
    }

    public void startRound(ActionEvent event) {
        try {
            if (roundIndex < HostscreenController.getQuiz().getLength()) {
                frageText.setText("Frage " + (roundIndex + 1) + "/" + HostscreenController.getQuiz().getLength());
                host.endZwischenRanking();
                host.startRound();
            } else {
                host.endGame();
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

    @FXML
    void initialize() {
        quizName.setText("Quiz: " + HostscreenController.getName());
        frageText.setText("Frage " + (roundIndex + 1) + "/" + HostscreenController.getQuiz().getLength());
    }

}
