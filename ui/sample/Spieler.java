/*
 * Autor: Basim Bennaji
 * Thema: Objekt des Spielers
 * Erstellungsdatum: 05.03.2023
 * Letzte Aenderung: 05.03.2023 14:02
 * Change-Log:
 * 05.03: Attribute hinzugefuegt, Basim Bennaji
 * 05.03: Get'er und Set'er generiert, Basim Bennaji
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
