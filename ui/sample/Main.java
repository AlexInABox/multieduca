package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Autor: Samuel Hoffleit, Basim Bennaji, Moritz Oehme
 * Ueberarbeitet:
 * Datum: 2023-03-09
 *
 * Zweck: 
 */

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../rsc/Startscreen.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 640, 400));
        primaryStage.setResizable(false);
        primaryStage.show();

        stage.setOnCloseRequest(event -> beenden(stage));
    }

    //Pop-Up Fenster zum Bestätigen, wenn Spiel über x beendet wird
    public void beenden(Stage stage){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Beenden");
        alert.setHeaderText("Du bist dabei, das Spiel zu beenden.");
        alert.setContentText("Willst du fortfahren?");
        if(alert.showAndWait().get() == ButtonType.OK){
            stage.close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
