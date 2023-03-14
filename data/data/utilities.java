package data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Autor: Alexander Betke
 * Datum: 2023-03-14
 *
 * Zweck: Diese Klasse beinhaltet Methoden, die von mehreren Klassen verwendet werden.
 * 
 * Change-Log:
 * 2023-03-14: Alexander Betke: checkForUpdate() hinzugefügt
 * 2023-03-14: Alexander Betke: getCurrentVersion() und getLatestVersion() hinzugefügt
 */

public class utilities {

    public static String getCurrentVersion() {
        try {
            // get current version from file
            FileReader fr = new FileReader("version.txt");
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

    public static String getLatestVersion() {
        try {
            // get latest version
            URL url = new URL("https://raw.githubusercontent.com/AlexInABox/multieduca/main/version.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String latestVersion = in.readLine();
            in.close();
            return latestVersion;
        } catch (Exception e) {
            System.out.println("Error while getting latest version: " + e);
            return "Error";
        }
    }

    public static boolean checkForUpdate() {
        String currentVersion = getCurrentVersion();
        String latestVersion = getLatestVersion();
        if (currentVersion.equals(latestVersion)) {
            System.out.println("No update available");
            return false;
        } else {
            System.out.println("Update available");
            return true;
        }
    }
}
