package sample;

import java.io.FileReader;
import java.util.function.Consumer;

import data.utilities;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;
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
        Parent root = FXMLLoader.load(getClass().getResource("/rsc/Startscreen.fxml"));
        primaryStage.setTitle("Multieduca");
        primaryStage.setScene(new Scene(root, 800, 550));
        primaryStage.setResizable(false);
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            System.out.println(utilities.showExitAlert());
            if (utilities.showExitAlert()) {
                Alert alert = createAlertWithOptOut(AlertType.CONFIRMATION, "Programm beenden...",
                        "ACHTUNG!", "Möchten Sie das Programm wirklich beenden?", "Nicht mehr fragen",
                        (optOut) -> {
                            if (optOut) {
                                utilities.showExitAlert(false);
                                System.out.println("Opted out");
                            } else {
                                utilities.showExitAlert(true);
                                System.out.println("Opted in");
                            }
                        }, ButtonType.YES, ButtonType.NO);
                alert.setTitle("Programm beenden...");
                alert.setHeaderText("ACHTUNG!");
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
            } else {
                host.endGame();
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

    public static Alert createAlertWithOptOut(AlertType type, String title, String headerText,
            String message, String optOutMessage, Consumer<Boolean> optOutAction,
            ButtonType... buttonTypes) {
        Alert alert = new Alert(type);
        // Need to force the alert to layout in order to grab the graphic,
        // as we are replacing the dialog pane with a custom pane
        alert.getDialogPane().applyCss();
        Node graphic = alert.getDialogPane().getGraphic();
        // Create a new dialog pane that has a checkbox instead of the hide/show details button
        // Use the supplied callback for the action of the checkbox
        alert.setDialogPane(new DialogPane() {
            @Override
            protected Node createDetailsButton() {
                CheckBox optOut = new CheckBox();
                optOut.setText(optOutMessage);
                optOut.setOnAction(e -> optOutAction.accept(optOut.isSelected()));
                return optOut;
            }
        });
        alert.getDialogPane().getButtonTypes().addAll(buttonTypes);
        alert.getDialogPane().setContentText(message);
        // Fool the dialog into thinking there is some expandable content
        // a Group won't take up any space if it has no children
        alert.getDialogPane().setExpandableContent(new Group());
        alert.getDialogPane().setExpanded(true);
        // Reset the dialog graphic using the default style
        alert.getDialogPane().setGraphic(graphic);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        return alert;
    }
}
