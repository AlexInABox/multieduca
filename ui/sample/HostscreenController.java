/*
 * Autor: Niklas Bamberg, Samuel Hoffleit, Basim Bennaji, Moritz Oehme
 * Thema: Diese Klasse stellt Methoden zur Bedienung des Hostscreens bereit. Daz gehoert das Laden einer Quiz-Datei und das Weiterleiten auf den Screen zum Erstellen eines neuen Quiz.
 * Erstellungsdatum: 27.02.2023
 * Letzte Aenderung: 12.03.2023 13:41
 * Icons: https://ionic.io/ionicons
 * Change-Log:
 * 27.02: Screen-Wechsel Methoden hinzugefuegt
 * 09.03: Get-methoden hinzugefuegt
 * 12.03: openFile Methode zum Quiz-Laden hinzugefuegt 
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
    //Wechselt zurueck zum Screen der Startseite
    public void switchToStartpage(ActionEvent event) throws IOException {
        switchScreen("/rsc/Startscreen.fxml", event);
    }
    //Wechselt zum Screen zum Spiel-Erstellen
    public void switchToSpielErstellen(ActionEvent event) throws IOException {
        switchScreen("/rsc/SpielErstellen.fxml", event);
    }
    //Oeffnet ein Fenster, mit welchem eine Quiz-Datei aus dem Dateien-Manager ins Spiel geladen werden kann
    public void openFile(ActionEvent event) throws IOException {
        // Quiz-Objekt aus Datei laden:
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Quiz Öffnen");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Quiz Datei", "*.json"));
        File quizDatei = fileChooser.showOpenDialog(stage);
        quiz = new Quiz(quizDatei);
        quizName = quizDatei.getName().replaceAll(".json", ""); //Sorgt dafuer, dass die Datei-Endung .son nicht als Quizname im Spiel angezeigt wird

        // umschalten auf Beitritts-screen
        switchScreen("/rsc/SpielStartenHost.fxml", event);
    }
    //Liefert das Quizobjekt
    public static Quiz getQuiz() {
        return quiz;
    }
    //Liefert den Quiznamen als String
    public static String getName() {
        return quizName;
    }

    //Hilfsmethode fuer Screen-Wechsel
    void switchScreen(String fxmlName, ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlName));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
