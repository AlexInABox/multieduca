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

/*
 * Autoren: Moritz Oehme, Niklas Bamberg, Basim Bennaji
 * Thema: Erstellen eines neuen Quiz bzw. der Datei, in der es gespeichert wird
 * Erstellungsdatum: 09.02.2023
 * Letzte Aenderung: 10.03.2023
 * Change-Log:
 * 
 */
//aenderungen:
//-arbeiten an verbindung der Teile

/**
 * Autor: Moritz Oehme
 * Ueberarbeitet:
 * Datum: 2023-03-09
 *
 * Zweck: 
 */

public class SpielErstellenController {

    private Stage stage;
    private Scene scene;
    private Quiz quiz = new Quiz();

    //UI Elemente aus FXML-Datei
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

    // Informationen für .json Datei
    //Gibt die Quizfrage als String zurück
    public String getFrage() {
        return frage.getText();
    }

    //Gibt die 4 möglichen Antworten in einem Array zurück
    public String[] getAntworten() {
        String[] array = new String[4];
        array[0] = antwortA.getText();
        array[1] = antwortB.getText();
        array[2] = antwortC.getText();
        array[3] = antwortD.getText();
        return array;
    }

    //Gibt die richtigen Antworten in einem Array zurück
    //Antworten sind von 0-3 durchnummeriert
    //Nummern der richtigen Antworten werden dem Array hinzugefügt
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
    // Werden ausgeführt, wenn Checkboxen neben den Antwrten ausgewählt werden
    public void setA() {
        //Wenn die Box neben der Antwort A ausgewählt ist, wird diese den richtigen Antworten hinzugefügt
        if (boxA.isSelected())
            richtigeAntworten.add(0);
        else
            richtigeAntworten.remove((Integer) 0);
    }

    public void setB() {
        //Wenn die Box neben der Antwort B ausgewählt ist, wird diese den richtigen Antworten hinzugefügt
        if (boxB.isSelected())
            richtigeAntworten.add(1);
        else
            richtigeAntworten.remove((Integer) 1);
    }

    public void setC() {
        //Wenn die Box neben der Antwort C ausgewählt ist, wird diese den richtigen Antworten hinzugefügt
        if (boxC.isSelected())
            richtigeAntworten.add(2);
        else
            richtigeAntworten.remove((Integer) 2);
    }

    public void setD() {
        //Wenn die Box neben der Antwort D ausgewählt ist, wird diese den richtigen Antworten hinzugefügt
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

        // ~basim 10.03.2023
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
    //Wird über den "zurück" Button ausgeführt
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