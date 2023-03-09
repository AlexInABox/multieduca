/*
 * Autor: Basim Bennaji
 * Thema: Methoden des Wartescreens.
 * Erstellungsdatum: 04.03.2023
 * Letzte Aenderung: 04.03.2023 19:13
 * Icons: https://ionic.io/ionicons
 * Change-Log:
 *      Methode zum nachfuellen der Liste mit neuen Spieler muss noch geschrieben werden. 04.03.2023 ~basim
 *      Die get'er und set'er fuer die Labels, bsp. quizFragenAnz, werden erst nach dem Funktionstests des Prgramms implementiert. 04.03.2023 ~basim
 */

package sample;

import java.io.IOException;

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

    @FXML
    private Button backButton;

    @FXML
    private Label hostIPAdresse;

    @FXML
    private ListView<?> playerList;

    @FXML
    private Label quizFragenAnz;

    @FXML
    private Label quizName;

    private client client;

    public void switchToStart(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../rsc/Startscreen.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void initialize() {
        client = StartscreenController.getClient();
        hostIPAdresse.setText(client.getIP());
        quizFragenAnz.setText(client.getQuizLength());
        quizName.setText(client.getQuizName());

        int EVENT;
        do {
            EVENT = net.client.waitForGameStart();
            if (EVENT == 0) { //PLAYER LIST
                //refresh player list
            } else if (EVENT == 1) { // GAME START
                //switch to game window
                break;
            } else if (EVENT == 2) { // GAME END
                //switch to startscreen
                break;
            }
        } while (EVENT == 0);

    }

}
