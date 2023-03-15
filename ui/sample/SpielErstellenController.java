/*
 * Autoren: Moritz Oehme, Niklas Bamberg, Basim Bennaji
 * Thema: Diese Klasse stellt Methoden zur Erstellung eines neuen Quiz bzw. der Datei, in der ein neues Quiz gespeichert wird, bereit.
 * Erstellungsdatum: 09.02.2023
 * Letzte Aenderung: 10.03.2023
 * Icons: https://ionic.io/ionicons
 * Change-Log:
 * 14.02: Hinzufuegen der Variablen der UI-Elemente, Moritz Oehme
 * 28.02: Get-Methoden zum zurueckgeben der Informationen fuer die Quiz-Datei, Moritz Oehme
 * 28.02: Set-Methoden zum Auswaehlen der korrekten Antworten, Moritz Oehme
 * 02.03: Arbeiten an Verbindung der Teile, Niklas Bamberg
 * 10.03: Anpassen der Animation beim Hinzufuegen einer Frage, Basim Bennaji
 */
package sample;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import data.Quiz;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SpielErstellenController {

    private Stage stage;
    private Scene scene;
    private Quiz quiz = new Quiz();

    //Variablen fuer UI Elemente aus FXML-Datei
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField frage, antwortA, antwortB, antwortC, antwortD;

    @FXML
    private CheckBox boxA, boxB, boxC, boxD;

    @FXML
    private Label msg;

    @FXML
    private TextField zeit;

    private List<Integer> richtigeAntworten = new ArrayList<>();

    // Informationen fuer .json Datei
    //Gibt die Quizfrage als String zurueck
    public String getFrage() {
        return frage.getText();
    }

    //Gibt die 4 moeglichen Antworten in einem Array zurueck
    public String[] getAntworten() {
        String[] array = new String[4];
        array[0] = antwortA.getText();
        array[1] = antwortB.getText();
        array[2] = antwortC.getText();
        array[3] = antwortD.getText();
        return array;
    }

    //Gibt die richtigen Antworten in einem Array zurueck
    //Antworten sind von 0-3 durchnummeriert
    //Nummern der richtigen Antworten werden dem Array hinzugefuegt
    public int[] getRichtigeAntworten() {
        int[] array = new int[richtigeAntworten.size()];
        for (int i = 0; i < richtigeAntworten.size(); i++)
            array[i] = richtigeAntworten.get(i);
        return array;
    }

    //Liefert die Zeit, die bereitgestellt wird, um eine Frage zu beantworten
    public int getZeit() {
        return Integer.parseInt(zeit.getText());
    }

    // Button-Funktionen: richtige Antwort setzen
    // Werden ausgefuehrt, wenn Checkboxen neben den Antwrten ausgewaehlt werden
    public void setA() {
        //Wenn die Box neben der Antwort A ausgewaehlt ist, wird diese den richtigen Antworten hinzugefuegt
        if (boxA.isSelected())
            richtigeAntworten.add(0);
        else
            richtigeAntworten.remove((Integer) 0);
    }

    public void setB() {
        //Wenn die Box neben der Antwort B ausgewaehlt ist, wird diese den richtigen Antworten hinzugefuegt
        if (boxB.isSelected())
            richtigeAntworten.add(1);
        else
            richtigeAntworten.remove((Integer) 1);
    }

    public void setC() {
        //Wenn die Box neben der Antwort C ausgewaehlt ist, wird diese den richtigen Antworten hinzugefuegt
        if (boxC.isSelected())
            richtigeAntworten.add(2);
        else
            richtigeAntworten.remove((Integer) 2);
    }

    public void setD() {
        //Wenn die Box neben der Antwort D ausgewaehlt ist, wird diese den richtigen Antworten hinzugefuegt
        if (boxD.isSelected())
            richtigeAntworten.add(3);
        else
            richtigeAntworten.remove((Integer) 3);
    }

    @FXML
    void addFrage() {
        //eigentliches Hinzufuegen der Frage
        quiz.addFrage(getFrage(), getAntworten(), getRichtigeAntworten(), getZeit());

        //UI und Variablen zuruecksetzen:
        frage.setText("");
        antwortA.setText("");
        antwortB.setText("");
        antwortC.setText("");
        antwortD.setText("");
        boxA.setSelected(false);
        boxB.setSelected(false);
        boxC.setSelected(false);
        boxD.setSelected(false);
        richtigeAntworten.clear();

        msg.setVisible(true);
        new animatefx.animation.FadeIn(msg).play();
        new animatefx.animation.FadeOut(msg).play();
    }

    //Speichern des Quiz als json-Datei
    @FXML
    public void saveGame(ActionEvent event) throws IOException {
        if (quiz.getLength() != 0) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Quiz Speichern");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Quiz Datei", "*.json"));
            File file = fileChooser.showSaveDialog(stage);
            quiz.save(file);
            switchToHostscreen(event);
        }
    }

    //UI Wechsel zum vorherigen Screen(Hostscreen)
    //Wird ueber den "zurueck" Button ausgefuehrt
    @FXML
    public void switchToHostscreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/rsc/Hostscreen.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void initialize() {

    }

}