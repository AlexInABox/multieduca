/*
 * Autor: Basim Bennaji
 * Thema: Methoden des Wartescreens.
 * Erstellungsdatum: 04.03.2023
 * Letzte Aenderung: 04.03.2023 19:13
 * Icons: https://ionic.io/ionicons
 * Change-Log:
 *      Methode zum nachfuellen der Liste mit neuen Spieler muss noch geschrieben werden.
 *      Die get'er und set'er fuer die Labels, bsp. quizFragenAnz, werden erst nach dem Funktionstests des Prgramms implementiert.
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

    public void switchToStart(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("../rsc/Startscreen.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void initialize() {
        // Die Liste soll mit den Namen der bereits beigetretenen Spieler gefuellt werden. 
    }

}
