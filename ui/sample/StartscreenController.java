/*
 * Autoren: Moritz Oehme, Samuel Hoffleit, Basim Bennaji
 * Thema: Methoden des Startscreens.
 * Erstellungsdatum: 23.02.2023 (?)
 * Letzte Aenderung: 05.03.2023
 * Icons: https://ionic.io/ionicons
 * Change-Log:
 * 		- Methoden onLoginButtonClick und ipMoeglich wurden erstellt und hinzugefuegt. ~basim 23.02.2023
 *      - Namen- und IP-Adressenfeld sollen ROT markiert werden und schutteln wenn das Feld leer ist. Auch unabhaengig voneinander. ~basim 23.02.2023
 *      - Animationen Folge wurde verbessert. Farbe der Fehlernachrricht (logInfo) eingestellt. Gruene Farbe fuer richtige Eingaben wurde entfernt. ~basim 24.02.2023 16:16
 *      - Methode zur ueberpruefung der Gueltigkeit der eingegebenen IP-Adresse wurde hinzugefügt. ~basim 25.02.2023
 * 		- JAVA-8 versionsinkompatibilitaeten (String.isBlank() => String.isEmpty()) behoben. ~alexander 05.03.2023
 * 		- Shake-Animation fuer den Fall, dass der Host nicht existiert hinzugefuegt und die dazugehoerige Errornachricht konkreter formuliert. ~basim 09.03.2023 20:15
 * 		- Fehlermeldung bei ungueltigem Usernamen hinzugefuegt. ~Alexander Betke 14.03.2023 17:58
 * 		- Versions-Aktualitaet-Ueberpruefung hinzugefuegt (initialize()). ~Alexander Betke 14.03.2023 18:26
 * 		- Fehlermeldung aktualsiert sich jetzt bei erneuter Eingabe. ~Alexander Betke 14.03.2023 19:42
 */
package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import net.client;

import java.io.IOException;
import data.utilities;

/**
 * Autor: Samuel Hoffleit, Basim Bennaji, Moritz Oehme
 * Ueberarbeitet:
 * Datum: 2023-03-09
 *
 * Zweck: 
 */

public class StartscreenController {

	private Stage stage;
	private Scene scene;
	//Variablen fuer Elemente aus FXML-Datei
	@FXML
	private TextField ipAdress;

	@FXML
	private TextField userName;

	@FXML
	private Label logInfo;

	// Methode wird beim klicken ausgefuehrt. Diese ueberprueft die Gueltigkeit der
	// Eingaben.

	// Networking-variable:
	public static client client;

	@FXML
	protected void onLoginButtonClick(ActionEvent event) throws InterruptedException, IOException {
		logInfo.setText("");

		// Formatierung fuer falsche und korrekte Eingaben
		String sucessMessage = String.format("-fx-text-fill: #03C988");
		String errorMessage = String.format("-fx-text-fill: #C92403");
		String sucessStyle = String.format(
				"-fx-border-color: #1C82AD; -fx-border-radius: 10; -fx-background-radius: 10; -fx-background-color: #1C82AD; -fx-text-fill: #FFFFFF");
		String errorStyle = String.format(
				"-fx-border-color: #C92403; -fx-border-radius: 10; -fx-background-radius: 10; -fx-background-color: #1C82AD; -fx-text-fill: #C92403");

		// Die folgenden if-Abfragen behandeln die verschiedenen Fehler-Moeglichkeiten
		// beim Anmelden.
		// Dabei schuettelt das jeweilige Feld und wird Rot markiert. Dazu wird eine
		// Fehlermeldung unter den Feldern angezeigt.

		// Name und IP-Adresse fehlen.
		if (userName.getText().isEmpty() && ipAdress.getText().isEmpty()) {
			logInfo.setText("Name und IP-Adresse fehlen!");
			logInfo.setStyle(errorMessage);
			userName.setStyle(errorStyle);
			ipAdress.setStyle(errorStyle);
			new animatefx.animation.Shake(userName).play();
			new animatefx.animation.Shake(ipAdress).play();
		} else
		// Name nicht eigegeben und IP-Adresse nicht gueltig.
		if (userName.getText().isEmpty() && !ipMoeglich(ipAdress.getText())) {
			userName.setStyle(errorStyle);
			logInfo.setText("Name fehlt und IP-Adresse nicht gültig!");
			logInfo.setStyle(errorMessage);
			ipAdress.setStyle(errorStyle);
			new animatefx.animation.Shake(userName).play();
			new animatefx.animation.Shake(ipAdress).play();
		} else
		// Name nicht eigegeben.
		if (userName.getText().trim().isEmpty()) {
			userName.setStyle(errorStyle);
			logInfo.setText("Name fehlt!");
			logInfo.setStyle(errorMessage);
			ipAdress.setStyle(sucessStyle);
			new animatefx.animation.Shake(userName).play();
		} else
		// IP-Adresse nicht eigegeben.
		if (ipAdress.getText().isEmpty()) {
			ipAdress.setStyle(errorStyle);
			logInfo.setText("IP-Adresse fehlt!");
			logInfo.setStyle(errorMessage);
			userName.setStyle(sucessStyle);
			new animatefx.animation.Shake(ipAdress).play();
		} else
		// IP-Adresse nicht gueltig.
		if (!ipMoeglich(ipAdress.getText())) {
			ipAdress.setStyle(errorStyle);
			logInfo.setText("IP-Adresse nicht gültig!");
			logInfo.setStyle(errorMessage);
			userName.setStyle(sucessStyle);
			new animatefx.animation.Shake(ipAdress).play();
		} else
		//Name ist zu kurz (mindestens 3 Zeichen) oder zu lang (maximal 15 Zeichen).
		if (userName.getText().length() < 3 || userName.getText().length() > 15) {
			userName.setStyle(errorStyle);
			logInfo.setText("Name muss zwischen 3 und 15 Zeichen lang sein!");
			logInfo.setStyle(errorMessage);
			ipAdress.setStyle(sucessStyle);
			new animatefx.animation.Shake(userName).play();
		} else
		//Name enthaelt Leerzeichen oder Komma.
		if (userName.getText().contains(" ") || userName.getText().contains(",")) {
			userName.setStyle(errorStyle);
			logInfo.setText("Name darf keine Leerzeichen oder Kommas enthalten!");
			logInfo.setStyle(errorMessage);
			ipAdress.setStyle(sucessStyle);
			new animatefx.animation.Shake(userName).play();
		}

		else {
			// Keine Fehler.
			client = new client();
			if (client.establishConnection(ipAdress.getText(), userName.getText())) {
				logInfo.setText("Erfolgreich angemeldet!");
				logInfo.setStyle(sucessMessage);
				userName.setStyle(sucessStyle);
				ipAdress.setStyle(sucessStyle);

				Parent root = FXMLLoader.load(getClass().getResource("/rsc/SpielWarten.fxml"));
				stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			} else {
				// Host konnte nicht gefunden werden.
				//client = new client(); 
				ipAdress.setStyle(errorStyle);
				logInfo.setText("Host konnte nicht gefunden werden!");
				logInfo.setStyle(errorMessage);
				new animatefx.animation.Shake(ipAdress).play();
			}
		}
	}

	// Methode zum ueberpruefen der Gueltigkeit der eingegebene IP-Adresse.
	public static boolean ipMoeglich(String ip) {

		// Teilt IP-Adresse auf. Jeweils an den Punkten.
		String[] viertel = ip.split("\\.");

		// Ueberprueft ob die Adresse aus 4 Teilen besteht.
		if (viertel.length != 4) {
			return false;
		}

		// Prueft ob die Einzelteile im Zahlenbereich 0 bis 255 liegen.
		for (String part : viertel) {
			try {
				int value = Integer.parseInt(part);
				if (value < 0 || value > 255) {
					return false;
				}
			} catch (NumberFormatException e) {
				return false;
			}
		}

		return true;
	} // Ende der Gueltigkeitsueberpruefung.

	public StartscreenController() throws IOException {

	}
	
	// Methode um zum Hostfenster zu wechseln
	public void switchToSpielHost(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/rsc/Hostscreen.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	// Methode um zum Infofenster zu wechseln
	public void switchToSpielInfo(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/rsc/info.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public static client getClient() {
		return client;
	}

	// check for new version and notify user on startup
	@FXML
	void initialize() {
		Platform.runLater(new Runnable() {
			public void run() {
				try {
					if (utilities.checkForUpdate()) {
						// show update notification
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Update verfügbar");
						alert.setHeaderText("Es ist ein Update verfügbar!");
						alert.setContentText(
								"Bitte laden Sie die neuste Version von https://github.com/alexinabox/multieduca/releases herunter.");
						alert.showAndWait();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	// in onLoginButtonClick eingebaut. ~basim 02.03.2023 22:53
	// public void switchToSpielWarten(ActionEvent event) throws IOException{
	// Parent root =
	// FXMLLoader.load(getClass().getResource("/rsc/SpielWarten.fxml"));
	// stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	// scene = new Scene(root);
	// stage.setScene(scene);
	// stage.show();

	// }
}
