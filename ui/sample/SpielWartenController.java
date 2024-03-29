/*
 * Autor: Basim Bennaji
 * Thema: Diese Klasse stellt Methoden zur Darstellung eines Warte-Screens fuer einen Spieler, der einem Quiz-Spiel beigetreten ist, bereit.
 * Erstellungsdatum: 04.03.2023
 * Letzte Aenderung: 13.03.2023 19:13
 * Icons: https://ionic.io/ionicons
 * Change-Log:
 * 04.03: Methode zum Nachfuellen der Liste mit neuen Spieler muss noch geschrieben werden, Basim Bennaji
 * 04.03: Die get'er und set'er fuer die Labels, bsp. quizFragenAnz, werden erst nach dem Funktionstests des Prgramms implementiert, Basim Bennaji
 * 10.03: Quizname und Fragenanzahl werden angezeigt, Samuel Hoffleit
 * 11.03: Erweiterung dieser Klasse um die Methoden switchToStart, switchToGame und initialize, Basim Bennaji
 * 13.03: Anpassung in initialize(), Niklas Bamberg
 */
package sample;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import net.client;

public class SpielWartenController {

    private Stage stage;
    private Scene scene;
    //Variablen fuer Elemente aus FXML-Datei
    @FXML
    private Button backButton;

    @FXML
    private Label hostIPAdresse, quizFragenAnz, quizName;

    @FXML
    private ListView<String> playerList;

    private client client;

    //Wechselt zum Start-Screen
    public void switchToStart(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/rsc/Startscreen.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    //Wechselt zum Start-Screen
    private void switchToStart() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/rsc/Startscreen.fxml"));
        stage = (Stage) backButton.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    //Wechselt zu Quiz-Frage-Screen
    private void switchToGame() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/rsc/Quizfrage.fxml"));
        stage = (Stage) backButton.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void initialize() {
        client = StartscreenController.getClient();
        hostIPAdresse.setText("Verbunden mit " + client.getIP());
        quizFragenAnz.setText(client.getQuizLength());
        quizName.setText(client.getQuizName());

        new Thread(() -> {
            try {
                System.out.println("Thread started");
                int EVENT;
                do {
                    EVENT = client.listenForEvent();
                    System.out.println("Event: " + EVENT);
                    switch (EVENT) {
                        case 0:
                            System.out.println("Updating player list");
                            Platform.runLater(new Runnable() {
                                public void run() {
                                    String[] playerListArray = client.getPlayerList();
                                    playerList.getItems().clear();
                                    for (int i = 0; i < playerListArray.length; i++) {
                                        playerList.getItems().add(playerListArray[i]);
                                    }
                                }
                            });

                            break;
                        case 1:
                            //switch to game window
                            Platform.runLater(new Runnable() {
                                public void run() {
                                    // Update UI here.
                                    try {
                                        switchToGame();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            break;
                        case 4:
                            //switch to startscreen
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    // Update UI here.
                                    try {
                                        switchToStart();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            break;
                    }
                } while (EVENT == 0);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }
}
