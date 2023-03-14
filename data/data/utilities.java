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
 */

public class utilities {
    public static boolean checkForUpdate() {
        try {
            // get current version from file
            FileReader fr = new FileReader("version.txt");
            BufferedReader br = new BufferedReader(fr);
            String currentVersion = br.readLine();
            System.out.println("Current version: " + currentVersion);
            br.close();

            // get latest version
            URL url = new URL("https://raw.githubusercontent.com/AlexInABox/multieduca/main/version.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String latestVersion = in.readLine();
            in.close();
            // compare versions
            if (currentVersion.equals(latestVersion)) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error while checking for update: " + e);
            return false;
        }
    }
}
