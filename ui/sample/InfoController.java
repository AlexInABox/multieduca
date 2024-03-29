/*
 * Autor: Basim Bennaji
 * Thema: Diese Klassse ermoeglicht es, alle Mitarbeiter des Projekts darzustellen.
 * Erstellungsdatum: 03.03.2023
 * Letzte Aenderung: 04.03.2023 01:20
 * Icons: https://ionic.io/ionicons
 * Change-Log:
 * 04.03: Funktion switchToHome hinzugefuegt, Basim Bennaji
 * 04.03: Labels werden mit den Namen der Autoren initializiert, Basim Bennaji
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
import javafx.stage.Stage;

import data.utilities;

public class InfoController {

    private Stage stage;
    private Scene scene;
    //Variablen fuer Elemente aus FXML-Datei
    @FXML
    private Button homebtn;

    @FXML
    private Label people1;

    @FXML
    private Label people2;

    @FXML
    private Label version;

    //Methode zur Rueckkehr zum Startscreen
    //wird nach einem entsprechenden Button-Click ausgefuehrt
    public void switchToHome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/rsc/Startscreen.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    //Wird ausgefuehrt, wenn Screen geoeffnet wird
    //Zeigt Namensliste als Text in Labels an
    @FXML
    void initialize() {

        version.setText(utilities.getCurrentVersion());
        people2.setText(
                "Basim Bennaji \n Till Dietrich \n Emil Habermann \n Alexander Horns \n Moritz Oehme \n Felix Trautwein");
        people1.setText(
                "Niklas Bamberg \n Alexander Betke \n Maximilian Gombala \n Samuel Hoffleit \n Jonas Lossin \n Rosan Sharma");

    }

}