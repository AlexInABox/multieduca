/*
 * Autor: Basim Bennaji
 * Thema: Methoden des Wartescreens.
 * Erstellungsdatum: 04.03.2023
 * Letzte Aenderung: 04.03.2023 19:13
 * Icons: https://ionic.io/ionicons
 * Change-Log:
 *      - Methode zum Nachfuellen der Liste mit neuen Spieler muss noch geschrieben werden. 04.03.2023 ~basim
 *      - Die get'er und set'er fuer die Labels, bsp. quizFragenAnz, werden erst nach dem Funktionstests des Prgramms implementiert. 04.03.2023 ~basim
 *      - Erweiterung dieser Klasse um die Methoden switchToStart, switchToGame und initialize. 11.03.2023 ~Alexander Betke
 *      - Anpassung in initialize(). 13.03.2023 ~Niklas Bamberg
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

<<<<<<< HEAD
/**
 * Autor: Samuel Hoffleit, Basim Bennaji, Moritz Oehme
 * Ueberarbeitet:
 * Datum: 2023-03-09
 *
 * Zweck: 
 */

=======
>>>>>>> b8f1d104fd924443363c879619a84d6c22c1f29c
public class SpielWartenController {

    private Stage stage;
    private Scene scene;

    @FXML
    private Button backButton;

    @FXML
    private Label hostIPAdresse, quizFragenAnz, quizName;

    @FXML
    private ListView<String> playerList;

    private client client;

    public void switchToStart(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/rsc/Startscreen.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void switchToStart() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/rsc/Startscreen.fxml"));
        stage = (Stage) backButton.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

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
