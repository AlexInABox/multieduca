/*
 * Autor: Niklas Bamberg, Samuel Hoffleit, Basim Bennaji, Moritz Oehme
 * Thema: Methoden des Hostscreens.
 * Erstellungsdatum: 27.02.2023
 * Letzte Aenderung: 12.03.2023 13:41
 * Icons: https://ionic.io/ionicons
 * Change-Log:
 *      - 
 */
package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import data.Quiz;

public class HostscreenController {

    private Stage stage;
    private Scene scene;
    private static Quiz quiz;
    private static String quizName;

    public HostscreenController() throws IOException {

    }

    public void switchToStartpage(ActionEvent event) throws IOException {
        switchScreen("../rsc/Startscreen.fxml", event);
    }

    public void switchToSpielErstellen(ActionEvent event) throws IOException {
        switchScreen("../rsc/SpielErstellen.fxml", event);
    }

    public void openFile(ActionEvent event) throws IOException {
        // Quiz-Objekt aus Datei laden:
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Quiz Öffnen");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Quiz Datei", "*.json"));
        File quizDatei = fileChooser.showOpenDialog(stage);
        quiz = new Quiz(quizDatei);
        quizName = quizDatei.getName().replaceAll(".json", "");

        // umschalten auf Beitritts-screen
        switchScreen("../rsc/SpielStartenHost.fxml", event);
    }

    public static Quiz getQuiz() {
        return quiz;
    }

    public static String getName() {
        return quizName;
    }

    // hilfsmethode fuer screenwechsel:
    void switchScreen(String fxmlName, ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlName));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
