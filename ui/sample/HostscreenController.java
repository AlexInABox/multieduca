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

//aenderungen:
//-arbeiten an verbindung der Teile: Niklas Bamberg -02.03.2023

public class HostscreenController {

    private Stage stage;
    private Scene scene;
    private static Quiz quiz;

    public HostscreenController() throws IOException {

    }
    
    public void switchToStartpage(ActionEvent event) throws IOException {
        switchScreen("../rsc/Startscreen.fxml", event);
    }

    public void switchToSpielErstellen(ActionEvent event) throws IOException {
        switchScreen("../rsc/SpielErstellen.fxml", event);
    }

    public void openFile(ActionEvent event) throws IOException {
        //Quiz-Objekt aus Datei laden:
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Quiz Öffnen");
        File quizDatei = fileChooser.showOpenDialog(stage);
        quiz = new Quiz(quizDatei);
        String quizName = quizDatei.getName();

        //umschalten auf Beitritts-screen
        switchScreen("../rsc/SpielStartenHost.fxml", event);
        SpielStartenHostController controller = new SpielStartenHostController(quizName);
    }

    public static Quiz getQuiz() {
        return quiz;
    }

    //hilfsmethode fuer screenwechsel:
    void switchScreen(String fxmlName, ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlName));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
