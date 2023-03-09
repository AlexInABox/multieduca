/*
 * Autoren: Moritz Oehme, Samuel Hoffleit, Basim Bennaji
 * Thema: Methoden des Startscreens.
 * Erstellungsdatum: 23.02.2023 (?)
 * Letzte Aenderung: 05.03.2023
 * Icons: https://ionic.io/ionicons
 * Change-Log:
 * 		Methoden onLoginButtonClick und ipMoeglich wurden erstellt und hinzugefuegt ~basim:
 *      Namen- und IP-Adressenfeld sollen ROT markiert werden und schutteln wenn das Feld leer ist. Auch unabhaengig voneinander. ~basim 23.02.2023
 *      Animationen Folge wurde verbessert. Farbe der Fehlernachrricht (logInfo) eingestellt. Gruene Farbe fuer richtige Eingaben wurde entfernt. ~basim 24.02.2023 16:16
 *      Methode zur ueberpruefung der Gueltigkeit der eingegebenen IP-Adresse wurde hinzugefügt. ~basim 25.02.2023
 * 
 * 		JAVA-8 versionsinkompatibilitaeten (String.isBlank() => String.isEmpty()) behoben. ~alexander 05.03.2023
 */

package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.client;

import java.io.IOException;

public class StartscreenController {

	private Stage stage;
	private Scene scene;

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
		if (userName.getText().isEmpty()) {
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
		} // else
			// TODO: Host konnte nicht gefunden werden. Kommt noch!
			// if(!ipMoeglich(ipAdress.getText())) {
			// ipAdress.setStyle(errorStyle);
			// logInfo.setText("IP-Adresse nicht gültig!");
			// logInfo.setStyle(errorMessage);
			// userName.setStyle(sucessStyle);
			// new animatefx.animation.Shake(ipAdress).play();}
		else {
			// Keine Felher.
			client = new client();
			if(client.run(ipAdress.getText(), userName.getText())){
				logInfo.setText("Erfolgreich angemeldet!");
				logInfo.setStyle(sucessMessage);
				userName.setStyle(sucessStyle);
				ipAdress.setStyle(sucessStyle);

				Parent root = FXMLLoader.load(getClass().getResource("../rsc/SpielWarten.fxml"));
				stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			}else{
				//client = new client();
				ipAdress.setStyle(errorStyle);
				logInfo.setText("IP Adresse existiert nicht.");
				logInfo.setStyle(errorMessage);
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

	public void switchToSpielHost(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("../rsc/Hostscreen.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void switchToSpielInfo(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("../rsc/info.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public static client getClient() {
		return client;
	}

	// in onLoginButtonClick eingebaut. ~basim 02.03.2023 22:53
	// public void switchToSpielWarten(ActionEvent event) throws IOException{
	// Parent root =
	// FXMLLoader.load(getClass().getResource("../rsc/SpielWarten.fxml"));
	// stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	// scene = new Scene(root);
	// stage.setScene(scene);
	// stage.show();

	// }
}
