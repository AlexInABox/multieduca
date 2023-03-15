/*
 * Autoren: Basim Bennaji, Niklas Bamberg
 * Thema: Darstellung eines Endrankings nach dem Spiel
 * Erstellungsdatum: 05.03.2023
 * Letzte Aenderung: 05.03.2023 13:41
 * Icons: https://ionic.io/ionicons
 * Change-Log:
 *      - Methode zum Wechsel zum Startscreens hinzugefuegt. 05.03.2023 ~basim
 *      - Methoden zum befuellen der Liste hinzugefuegt. 12.03.2023 ~niklas
 */
package sample;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import net.client;

public class EndrankingController {

    private Stage stage;
    private Scene scene;

    //UI-Elemente werden aus FXML-Datei ausgelesen
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button homeButton;

    @FXML
    private ListView<String> playerList;

    //Die Methode initialize wird beim Laden dieses Bildschirms ausgefuehrt und
    //befuellt die Liste des Endrankings mit den Entsprechenden Daten die der erhalten hat.
    @FXML
    void initialize() {
        //Das client Objekt wird mithilfe einer entsprechenden getMethoder initialisiert, da es anders
        //nicht moeglich waere Zugriff darauf zu erhalten, da man den verschiedenen Bildschirm Klassen nichts uebergeben kann.
        client c = StartscreenController.getClient();
        //Ranking-Informationen werden dem client Objekt entnommen
        HashMap<String, Integer> spielerPunkteMap = c.getSpielerPunkteMap();
        HashMap<Integer, String> bestenliste = c.getBestenliste();
        for (Integer i : bestenliste.keySet()) {
            playerList.getItems().add(i + ". " + bestenliste.get(i) + " " + spielerPunkteMap.get(bestenliste.get(i)));
        }
        //Methode des clients zum beenden des Spiels wird aufgerufen, um 
        //dort alle Variablen zurueckzusetzen. (siehe client.java)
        c.gameEnded();
    }

    //Diese Methode wird nach einem Klick auf den Homebutton aufgerufen und wechselt zum Startbildschirm
    public void switchToHome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/rsc/Startscreen.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

}
