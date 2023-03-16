/*
 * Autor: Samuel Hoffleit, Basim Bennaji, Moritz Oehme
 * Thema: Es handelt sch um eine ausfuehrbare Main-Klasse, die die JavaFX-Anwendung startet.
 * Erstellungsdatum: 09.03.2023
 * Letzte Aenderung: 15.03.2023 18:20
 * Icons: https://ionic.io/ionicons
 * Change-Log:
 * 09.03: Start-Methode erstellt. Samuel Hoffleit, Basim Bennaji, Moritz Oehme
 * 15.03: Logik beim Schließen des Programms hinzugefuegt. ALexander Betke
 */
package sample;

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

//Main erbt hier von der Klasse Application, um eine JavaFX-Anwendung zu erstellen
public class Main extends Application {

    public static void main(String[] args) {
        //es wird die launch() Methode aus Application aufgerufen, welche wiederum die start() Methode aufruft
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //hier wird der erste UI-Bildschirm (der Startscreen) geladen.
        Parent root = FXMLLoader.load(getClass().getResource("/rsc/Startscreen.fxml"));
        primaryStage.setTitle("Knowledge Knockout");
        primaryStage.setScene(new Scene(root, 800, 550));
        primaryStage.setResizable(false);
        primaryStage.show();

        //Sobald der Benutzer das Fenster schließt, wird die showExitAlert() Methode aufgerufen. Hier wird der Benutzer gefragt, ob er das Programm wirklich beenden moechte.
        primaryStage.setOnCloseRequest(event -> {
            System.out.println(utilities.showExitAlert());
            if (utilities.showExitAlert()) {
                Alert alert = createAlertWithOptOut(AlertType.CONFIRMATION, "Programm beenden...",
                        "ACHTUNG!", "Möchten Sie das Programm wirklich beenden?", "Nicht mehr fragen",
                        //Falls der Nutzer die Checkbox "Nicht mehr fragen" aktiviert, erscheint die Meldung nicht mehr, wenn der Benutzer in Zukunft das Fenster schließt.
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

    //Diese Methode erstellt ein Pop-Up-Fenster, mit einer Checkbox. In unserem Kontext erstellt diese Methode ein Pop-Up-Fenster, welches den Benutzer fragt, ob er das Programm wirklich beenden moechte. Plus eine Checkbox, die den Benutzer fragt, ob er diese Meldung in Zukunft nicht mehr sehen moechte.
    public static Alert createAlertWithOptOut(AlertType type, String title, String headerText,
            String message, String optOutMessage, Consumer<Boolean> optOutAction,
            ButtonType... buttonTypes) {
        Alert alert = new Alert(type);
        alert.getDialogPane().applyCss();
        Node graphic = alert.getDialogPane().getGraphic();
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
        alert.getDialogPane().setExpandableContent(new Group());
        alert.getDialogPane().setExpanded(true);
        alert.getDialogPane().setGraphic(graphic);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        return alert;
    }
}
