package sample;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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
            // String JSONtext = Files.readString(Path.of(quizDatei.getPath()));
            String JSONtext = new String(Files.readAllBytes(Paths.get(quizDatei.getName())), StandardCharsets.UTF_8);

            fragen = new JSONArray(JSONtext);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addFrage(String text, String[] antworten, int loesung, int zeit) {
        JSONObject frage = new JSONObject();
        frage.put("text", text);
        frage.put("antworten", antworten);
        frage.put("loesung", loesung);
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

}
