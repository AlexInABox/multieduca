package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SpielWartenController {

    private Stage stage;
    private Scene scene;

    public SpielWartenController() throws IOException {

    }

    public void switchToStartpage(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("../rsc/Startscreen.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

}