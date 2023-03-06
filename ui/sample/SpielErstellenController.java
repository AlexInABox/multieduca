package sample;

import javafx.event.ActionEvent;
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
    // private List <Integer> richtigeAntworten = new ArrayList<>();
    private int antwort;

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

    /*
     * public int[] getRichtigeAntworten() {
     * int [] array = new int[4];
     * for(int i = 0; i < richtigeAntworten.size(); i++) array[i] =
     * richtigeAntworten.get(i);
     * return array;
     * }
     */
    public int getZeit() {
        return Integer.parseInt(zeit.getText());
    }

    // Button-Funktionen: richtige Antwort setzen
    public void setA() {
        // A = true;
        // if(antwortA.getText() != "") richtigeAntworten.add(0);
        antwort = 0;
    }

    public void setB() {
        // B = true;
        // if(antwortB.getText() != "") richtigeAntworten.add(1);
        antwort = 1;
    }

    public void setC() {
        // C = true;
        // if(antwortC.getText() != "") richtigeAntworten.add(2);
        antwort = 2;
    }

    public void setD() {
        // D = true;
        // if(antwortD.getText() != "") richtigeAntworten.add(3);
        antwort = 3;
    }

    // eingetragene Fragendaten werden dem Quiz-Objekt hinzugefuegt
    public void addFrage() {
        quiz.addFrage(getFrage(), getAntworten(), antwort, getZeit());
        frage.setText("");
        antwortA.setText("");
        antwortB.setText("");
        antwortC.setText("");
        antwortD.setText("");
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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Quiz Speichern");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Quiz Datei", "*.json"));
        File file = fileChooser.showSaveDialog(stage);
        quiz.save(file);
        switchToHostscreen(event);
    }

}
