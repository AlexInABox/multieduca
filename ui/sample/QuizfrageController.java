/*
 * Autor: Basim Bennaji
 * Thema: Screen der Frage
 * Erstellungsdatum: 04.03.2023
 * Letzte Aenderung: 04.03.2023 20:11
 * Icons: https://ionic.io/ionicons
 * Change-Log:
 *      - Fortschrittbar zeigt den Fortschritt im Quiz grafisch dar
 *      - Timer fuer verbleibende Zeit
 *      - Beim Auswaehlen einer Antwort werden die Antwortbuttons deaktiviert und es wird auf die weiteren Spieler/Host gewartet
 *      - Vorlaeufige Methode fuer timer hinzugefuegt. 04.03.2023 20:45
 *      - Set'er fuer Frage und Antwortmoeglichkeiten hinzugefuegt. 04.03.2023 20:51
 */

package sample;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class QuizfrageController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button antD;

    @FXML
    private Button antA;

    @FXML
    private Button antB;

    @FXML
    private Button antC;

    @FXML
    private Label frageText;

    @FXML
    private ProgressBar progbarFragen;

    @FXML
    private static Label timer;

    // vorlaeufige Methode fuer den timer.
    public static void timer(int sek) {
        int restSek = sek;
        while (restSek > 0) {
            int min = restSek / 60;
            int sekuebrig = restSek % 60;
            timer.setText(String.format("%02d:%02d%n", min, sekuebrig));
            try {
                Thread.sleep(1000); // sleep for one second
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            restSek--;
        }
    }

    // Set'er fuer die Antwortbuttons
    public void setAntButtons() {
        antA.setText("");
        antB.setText("");
        antC.setText("");
        antD.setText("");
    }

    public void setFrage() {
        frageText.setText("");
    }

    @FXML
    void initialize() {
        // Hier werden die verschiedenen Antwortbuttons, die Frage und Zeit initialisiert.
        setFrage();
        setAntButtons();
    }
}