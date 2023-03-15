/*
* Autoren: Niklas, Felix, Till, Alexander H.
* Thema: Datentyp zur Repraesentation von Quizzen
* Erstellungsdatum: 09.02.2023
* Letzte Aenderung: 06.03.2023 
* Change-log:
*  09.02: Importierung aller wesentlichen Klassen
*         Implementierung der Funktion 'save' 
*         Implementierung der Konstruktoren 
*         Implementierung der Funktion 'genPunkte'
*           - Niklas, Felix, Till, Alexander H.
*  14.02: Implementierung der Funktionen 'addFrage' und 'getFrage'
*           - Niklas, Felix
*  16.02: Implementierung der Funktion 'getLength'
*           - Niklas
*  06.03: Ueberarbeitung der Funktion 'genPunkte' (Beruecksichtigt nun die Mehrfachauswahl von Antworten)
*           - Felix
*/

package data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

// import von den Datentypen JSONArray und JSONObjact zur Repraesentation der im Quiz gespeicherten Daten im JSON-Format.
import org.json.JSONArray;
import org.json.JSONObject;

public class Quiz {
    // Attribut zum Speichern aller im Quiz enthaltenen Fragen als JSONArray (Ein Quizz besteht aus fragen)
    private JSONArray fragen;

    // Konstruktor zum erstellen eines leeren Quiz 
    public Quiz() {
        fragen = new JSONArray();
    }

    // Konstruktor zum Erstellen eines Quiz' aus einem vorhandenen JSON-Dokument
    public Quiz(File quizDatei) {
        try {
            
            // Wenn Vorhanden, wird aus dem eingegebenen Dokument der Inhalt ausgelesen und im String JSONtext gespeichert
            String JSONtext = new String(Files.readAllBytes(Paths.get(quizDatei.getPath())), StandardCharsets.UTF_8);
            
            // Aus JSONtext wird dann das Attribut fragen generiert
            fragen = new JSONArray(JSONtext);
        } catch (IOException e) {
            // Fehlermanagement
            System.err.println("Fehler beim Laden der Quizdatei");
            e.printStackTrace();
        }
    }

    /* 
    * Funktion, um dem Quiz eine Frage hinzuzufuegen
    * 
    * String text: ausformulierte Frage 
    * String[] antworten: Antwortmoeglichkeiten
    * int[] loesungen: Indexe der richtigen Antworten in 'antworten'
    * int zeit: Zeit, die zur Beantwortung der Frage zur Verfuegung steht
    */
    public void addFrage(String text, String[] antworten, int[] loesungen, int zeit) {
        
        // Neues JSONObject 'frage' zum Speichern der neuen Frage
        JSONObject frage = new JSONObject();
        
        // Parameter werden mit einem key versehen und 'frage' hinzugefuegt
        frage.put("text", text);
        frage.put("antworten", new JSONArray(antworten));
        frage.put("loesungen", new JSONArray(loesungen));
        frage.put("zeit", zeit);
        
        // Die neue Frage wird dem gesamten Quiz hinzugefuegt
        fragen.put(frage);
    }

    // Funktion zum speichern des Quiz' im Zielort 'file'
    public void save(File file) {
        try {
            
            // Konstruktor der importierten Klasse FileWriter bekommt den Zielort der Datei
            FileWriter fileWriter = new FileWriter(file);
            
            // JSONArray besitzt eine Funktion zum Schreiben des eigenen inhalts in das Dokument, welches im 'fileWriter' referiert wird
            fragen.write(fileWriter);
            
            // Der FileWriter-Stream wird fuer 'fileWriter' geschlossen
            fileWriter.close();
        } catch (IOException e) {
            
            // Fehlermanagement
            e.printStackTrace();
        }
    }

    // Gibt die Frage bei dem Index 'index' zurueck
    public JSONObject getFrage(int index) {
        return (JSONObject) fragen.get(index);
    }

    // Gibt die Anzahl der Fragen im Quiz zurueck
    public int getLength() {
        return fragen.length();
    }

    /*
    * Funktion zur Punktberechnung
    *
    * JSONObject frage: Beantwortete Frage
    * int[] eingaben: Indexe der vom Benutzer als richtig ausgewaehlten Antwortmoeglichkeiten
    * double antwortZeit: Zeit, die der Benutzer zum Beantworten der Frage gebraucht hat
    */
    public static int genPunkte(JSONObject frage, int[] eingaben, double antwortZeit) {
        
        // Speichert die Punktzahl, die der Benutzer fuer die Beantwortung der Frage bekommt
        double output = 0;
        
        // Die Zeit, die fuer die Beantwortung der Frage zur Verfuegung stand 
        int maxZeit = frage.getInt("zeit");
        
        // Die Anzahl der Antwortmoeglichkeiten 
        int nAntworten = frage.getJSONArray("antworten").length();
        
        // Die Indexe der richtigen Antworten
        JSONArray loesungen = frage.getJSONArray("loesungen");
        
        /* Berechnung der Punktzahl. 
        *
        * Wenn der Benutzer eine Antwortmoeglichkeit zurecht angeklickt hat, bekommt er dafuer Punkte.
        * Die Punkte entsprechen jeweils maximal 100/AnzahDerGegebenenAntworten.
        * Die Punkte nehmen exponentiell mit der zur Beantwortung der Frage benoetigten Zeit ab. Maximal kann das die Punktzahl halbieren
        */
        double punkteProRichtigeAntwort = (100 - (Math.pow(antwortZeit, 2) / Math.pow(maxZeit, 2) * 50)) / eingaben.length;
        
        // Durchlauuft alle Antwortmoeglichkeiten 
        for (int i = 0; i < nAntworten; i++) {
            
            // Variable gibt an, ob die aktuelle Antwortmoeglichkeit richtig ist (ausgewahlt werden muesste)
            boolean istRichtig = false;
            
            // Schleife gleicht den Indexwert der aktuellen Antwortmoeglichkeit i mit dem Indexwerten der richtigen Antwortmoeglichkeiten ('loesungen') ab
            for (Object loesung : loesungen) {
                if (i == (int) loesung) {
                    
                    // istRichtig wird auf True gesetzt, wenn sich die aktuelle Antwortmoeglichkeit in den Loesungen befindet
                    istRichtig = true;
                }
            }
            
            // Variable gibt an, ob die aktuelle Antwortmoeglichkeit vom Benutzer ausgewahlt wurde
            boolean wurdeAusgewaehlt = false;
            
            // Selbes Konzept wie oben
            for (int j = 0; j < eingaben.length; j++) {
                if (i == eingaben[j]) {
                    wurdeAusgewaehlt = true;
                }
            }
            
            // Wenn die aktuelle Antwortmoeglichkeit richtig ist und ausgewahlt wurde...
            if ((istRichtig & wurdeAusgewaehlt)) {
                
                // ... wird 'output' um den Wert 'punkteProRichtigeAntwort' erhoeht
                output += punkteProRichtigeAntwort;
            }
        }
        
        // Die berechnete Punktzahl wird zurueckgegeben
        return (int) output;
    }

}