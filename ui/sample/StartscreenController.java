package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartscreenController {

    private Stage stage;
    private Scene scene;

    public StartscreenController() throws IOException{

    }

    public void switchToSpielHost(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("../rsc/Hostscreen.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    public void switchToSpielWarten(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("../rsc/SpielWarten.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
}
