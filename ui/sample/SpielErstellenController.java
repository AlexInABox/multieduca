package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//aenderungen:
//-arbeiten an verbindung der Teile: Niklas Bamberg -02.03.2023

public class SpielErstellenController {

    private Stage stage;
    private Scene scene;
    private Quiz quiz = new Quiz();

    public SpielErstellenController() throws IOException {

    }
    @FXML
    TextField frage, antwortA, antwortB, antwortC, antwortD, zeit;

    @FXML
    CheckBox boxA, boxB, boxC, boxD;

    private List <Integer> richtigeAntworten = new ArrayList<>();

    // Informationen für .json Datei
    public String getFrage() {
        return frage.getText();
    }

    public String[] getAntworten() {
        String[] array = new String[4];
        array[0] = antwortA.getText();
        array[1] = antwortB.getText();
        array[2] = antwortC.getText();
        array[3] = antwortD.getText();
        return array;
    }

    public int[] getRichtigeAntworten() {
        int [] array = new int[richtigeAntworten.size()];
        for(int i = 0; i < richtigeAntworten.size(); i++) array[i] = richtigeAntworten.get(i);
        return array;
    }
    
    public int getZeit() {
        return Integer.parseInt(zeit.getText());
    }

    // Button-Funktionen: richtige Antwort setzen
    public void setA() {
        //hier stand noch antwortA.getText() != "" zusaetzlich in der if, allerings finde ich,
        //dass man auch eine richtige Antwort setzen koennen sollte, ohne eine Antwort eingegeben zu haben.
        if(boxA.isSelected()) richtigeAntworten.add(0);
        else richtigeAntworten.remove((Integer) 0);
    }

    public void setB() {
        if(boxB.isSelected()) richtigeAntworten.add(1);
        else richtigeAntworten.remove((Integer) 1);
    }

    public void setC() {
        if(boxC.isSelected()) richtigeAntworten.add(2);
        else richtigeAntworten.remove((Integer) 2);
    }

    public void setD() {
        if(boxD.isSelected()) richtigeAntworten.add(3);
        else richtigeAntworten.remove((Integer) 3);
    }

    // eingetragene Fragendaten werden dem Quiz-Objekt hinzugefuegt
    public void addFrage() {
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
    }

    public void switchToHostscreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../rsc/Hostscreen.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Quiz wird als Datei gespeichert
    public void saveGame(ActionEvent event) throws IOException {
        if(quiz.getLength() != 0){
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Quiz Speichern");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Quiz Datei", "*.json"));
            File file = fileChooser.showSaveDialog(stage);
            quiz.save(file);
            switchToHostscreen(event);
        }
    }

}
