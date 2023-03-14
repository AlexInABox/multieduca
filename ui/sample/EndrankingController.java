/*
 * Autoren: Basim Bennaji, Niklas Bamberg
 * Thema: Methoden des Endrankings.
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

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button homeButton;

    @FXML
    private ListView<String> playerList;

    @FXML
    void initialize() {
        client c = StartscreenController.getClient();
        HashMap<String, Integer> spielerPunkteMap = c.getSpielerPunkteMap();
        HashMap<Integer, String> bestenliste = c.getBestenliste();
        for (Integer i : bestenliste.keySet()) {
            playerList.getItems().add(i + ". " + bestenliste.get(i) + " " + spielerPunkteMap.get(bestenliste.get(i)));
        }
        c.gameEnded();
    }

    // Methode aus InfoController ~basim
    public void switchToHome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../rsc/Startscreen.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

}
