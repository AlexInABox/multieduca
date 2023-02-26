import java.net.*;
import java.io.*;

import org.json.JSONObject;

/*
* Autor: Alexander Betke, Jonas Lossin, Rosan Sharma, Maximilian Gombala, Niklas Bamberg
* Datum: 2022-02-16
* 
* ...
*/

class RunnableThread implements Runnable {

    private static ServerSocket ss;
    private static Socket s;

    private static String nick;

    public static int punkte;

    private Quiz quiz;

    private BufferedReader bf;
    private PrintWriter pr;

    RunnableThread(ServerSocket server, Quiz quiz) {
        ss = server;
        this.quiz = quiz;
    }

    public void run() {
        try {
            s = ss.accept();
            host.initServer();
            InputStreamReader in = new InputStreamReader(s.getInputStream());
            bf = new BufferedReader(in);
            pr = new PrintWriter(s.getOutputStream());
            System.out.println("Connection established");
            System.out.println("Creating a new thread");

            nick = bf.readLine(); // the first message is the name of the player

            regClient(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void regClient(Socket s) {
        String ip = s.getInetAddress().toString();
        System.out.println(ip + " " + nick);
    }

    public void start() {
        System.out.println("Starting new Thread");
        Thread t = new Thread(this);
        t.start(); 
    }

    public void sendQuestion(int index) throws IOException {
        JSONObject frage = quiz.getFrage(index);
        pr.println("START ROUND");
        pr.println(frage.toString());
        pr.flush();
        int antwort = Integer.parseInt(bf.readLine());
        System.out.println(antwort);
        double zeit = Double.parseDouble(bf.readLine());
        System.out.println(zeit);

        punkte = genPunkte(frage, antwort, zeit);
        System.out.println("Client bekommt: " + punkte + " Punkte");
        boolean ergebnis = punkte > 0;
        System.out.println("Client bekommt: " + ergebnis + " als Ergebnis");
        pr.println("RESULT");
        pr.println(ergebnis);
        pr.println(punkte);
        pr.flush();
    }

    public void endGame() {
        pr.println("END GAME");
        pr.flush();
    }

    private static int genPunkte(JSONObject frage, int antwort, double antwortZeit) {
		double output = 0;
		int loesung = frage.getInt("loesung");
		int maxZeit = frage.getInt("zeit");
		if (loesung == antwort) {
			 output = 100-(Math.pow(antwortZeit, 2)/Math.pow(maxZeit, 2)*50); 
		}
		return (int) output;
	}
}