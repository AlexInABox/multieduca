import org.json.JSONObject;

public class PunkteTest {
    public static int genPunkte(JSONObject frage, int antwort, double antwortZeit) {
		double output = 0;
		int loesung = frage.getInt("loesung");
		int maxZeit = frage.getInt("zeit");
		if (loesung == antwort) {
			 output = 100-(Math.pow(antwortZeit, 2)/Math.pow(maxZeit, 2)*50); 
		}
		return (int) output;
	}
}
