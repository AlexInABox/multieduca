package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import net.host;

/**
 * Autor: Samuel Hoffleit, Basim Bennaji, Moritz Oehme
 * Ueberarbeitet:
 * Datum: 2023-03-09
 *
 * Zweck: 
 */

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../rsc/Startscreen.fxml"));
        primaryStage.setTitle("Quiz Game");
        primaryStage.setScene(new Scene(root, 800, 550));
        primaryStage.setResizable(false);
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Möchtest du das Programm wirklich beenden? Jedglicher Fortschritt geht dabei verloren!",
                    ButtonType.YES,
                    ButtonType.NO);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.NO) {
                event.consume();
            } else {
                try {
                    // executed when the application shuts down
                    host.endGame();
                } catch (Exception e) {
                    System.out.println("No game running");
                }
            }
        });

    }

    /*@Override
    public void stop() {
        // executed when the application shuts down
        System.out.println("Application closing...");
        try {
            host.endGame();
        } catch (Exception e) {
            System.out.println("No game running");
        }
    }
    */

    public static void main(String[] args) {
        launch(args);
    }
}
