/*
 * Autor: Basim Bennaji
 * Thema: Objekt des Spielers
 * Erstellungsdatum: 05.03.2023
 * Letzte Aenderung: 05.03.2023 14:02
 * Change-Log:
 *      - Attribute hinzugefuegt. 05.03.2023 14:00
 *      - Get'er und Set'er generiert.  05.03.2023 14:02
 */

package sample;

public class Spieler {

    int punkte;
    String name;

    public int getPunkte() {
        return punkte;
    }
    public void setPunkte(int punkte) {
        this.punkte = punkte;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
