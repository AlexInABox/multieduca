package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import net.host;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../rsc/Startscreen.fxml"));
        primaryStage.setTitle("Quiz Game");
        primaryStage.setScene(new Scene(root, 800, 550));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop() {
        // executed when the application shuts down
        System.out.println("Application closing...");
        try {
            host.endGame();
        } catch (Exception e) {
            System.out.println("No game running");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
