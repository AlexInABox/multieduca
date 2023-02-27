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

public class HostscreenController {

    private Stage stage;
    private Scene scene;

    public HostscreenController() throws IOException {

    }

    public void switchToStartpage(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("../rsc/Startscreen.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    public void switchToSpielErstellen(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("../rsc/SpielErstellen.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void openFile(ActionEvent event) throws IOException{
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Quiz Öffnen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Quizdatei", "*.json")
        );
        fileChooser.showOpenDialog(stage);
    }

}
