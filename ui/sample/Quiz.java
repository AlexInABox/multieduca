package sample;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

public class Quiz {
    private JSONArray fragen;

    public Quiz() {
        fragen = new JSONArray();
    }

    public Quiz(File quizDatei) {
        try {

            String JSONtext = new String(Files.readAllBytes(Paths.get(quizDatei.getPath())), StandardCharsets.UTF_8);

            fragen = new JSONArray(JSONtext);
        } catch (IOException e) {
            System.out.println("Fehler beim Laden der Quizdatei");
            e.printStackTrace();
        }
    }

    public void addFrage(String text, String[] antworten, int[] loesungen, int zeit) {
        JSONObject frage = new JSONObject();
        frage.put("text", text);
        frage.put("antworten", antworten);
        frage.put("loesungen", loesungen);
        frage.put("zeit", zeit);
        fragen.put(frage);
    }

    public void save(File file) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fragen.write(fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getFrage(int index) {
        return (JSONObject) fragen.get(index);
    }

    public int getLength() {
        return fragen.length();
    }

    // wird von andere Klasse aus aufgerufen
    public static int genPunkte(JSONObject frage, int antwort, double antwortZeit) {
        double output = 0;
        int loesung = frage.getInt("loesung");
        int maxZeit = frage.getInt("zeit");
        if (loesung == antwort) {
            output = 100 - (Math.pow(antwortZeit, 2) / Math.pow(maxZeit, 2) * 50);
        }
        return (int) output;
    }

    public static int gen(JSONObject frage, int [] eingaben, double antwortZeit) {
    	double output = 0;
    	int maxZeit = frage.getInt("zeit");
    	int nAntworten = frage.getJSONArray("antworten").length();
    	int nLoesungen = frage.getJSONArray("loesungen").length();
    	Integer[] loesungen = (Integer[]) frage.getJSONArray("loesungen").toList().toArray();
        double punkteProRichtigeAntwort = (100-(Math.pow(antwortZeit, 2)/Math.pow(maxZeit, 2)*50))/nAntworten;
    	for (int i = 0; i < nAntworten; i++) {
    		
    		boolean istRichtig = false;
    		for (int j = 0; j < nLoesungen; j++) {
    			if (i == loesungen[j].intValue()) {
    				istRichtig = true;
    			}
    		}
    		
    		boolean wurdeAusgewaehlt = false;
    		for (int j = 0; j < eingaben.length; j++) {
    			if (i == eingaben [j]) {
    				wurdeAusgewaehlt = true;
    			}
    		}
    		
    		if ((istRichtig & wurdeAusgewaehlt) || (!istRichtig & !wurdeAusgewaehlt)) {
    			output += punkteProRichtigeAntwort; 
    		}
    	}
    	return (int) output;
    }

}
