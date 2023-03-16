package data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Autor: Alexander Betke
 * Datum: 2023-03-14
 *
 * Zweck: Diese Klasse beinhaltet Methoden, fuer die Pruefung auf ein Update und die Anzeige einer
 *        Meldung wenn man das Programm schliesst.
 * 
 * Change-Log:
 * 2023-03-14: Alexander Betke: checkForUpdate() hinzugefügt
 * 2023-03-14: Alexander Betke: getCurrentVersion() und getLatestVersion() hinzugefügt
 */

public class utilities {

    //Diese Methode liest die eigene Version aus der Datei "version.txt" aus und gibt sie als String zurueck.
    public static String getCurrentVersion() {
        try {
            // get current version from file
            FileReader fr = new FileReader("data/data/misc/version.txt");
            BufferedReader br = new BufferedReader(fr);
            String currentVersion = br.readLine();
            System.out.println("Current version: " + currentVersion);
            br.close();
            return currentVersion;
        } catch (Exception e) {
            System.out.println("Error while getting current version: " + e);
            return "Null";
        }
    }

    //Diese Methode liest die neueste Version der Datei von GitHub aus und gibt sie als String zurueck.
    public static String getLatestVersion() {
        try {
            // get latest version
            URL url = new URL(
                    "https://raw.githubusercontent.com/AlexInABox/multieduca/main/data/data/misc/version.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String latestVersion = in.readLine();
            in.close();
            return latestVersion;
        } catch (Exception e) {
            System.out.println("Error while getting latest version: " + e);
            return "Error";
        }
    }

    //vergleicht die beiden Versionen und gibt true zurueck, wenn eine neue Version verfuegbar ist.
    public static boolean checkForUpdate() {
        String currentVersion = getCurrentVersion();
        String latestVersion = getLatestVersion();
        if (currentVersion.equals(latestVersion)) {
            return false;
        } else {
            return true;
        }
    }

    //gibt zurueck, ob der Benutzer die Meldung beim Schliessen des Programms sehen will oder nicht.
    //true = ja, false = nein
    public static boolean showExitAlert() {
        try {
            // get optout value from file
            FileReader fr = new FileReader("data/data/misc/optout.txt");
            BufferedReader br = new BufferedReader(fr);
            Boolean optout = Boolean.valueOf(br.readLine());
            br.close();
            return optout;
        } catch (Exception e) {
            return false;
        }
    }

    //legt fest, ob der Benutzer die Meldung beim Schliessen des Programms sehen will oder nicht.
    public static void showExitAlert(Boolean optout) {
        try {
            //write optout value to file
            FileWriter fw = new FileWriter("data/data/misc/optout.txt");
            fw.write(optout.toString());
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
