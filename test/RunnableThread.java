import java.net.*;
import java.io.*;

class RunnableThread implements Runnable {
    public boolean allowedToRun = true; // this is the variable that will be used to stop the thread

    private Thread t;
    private ServerSocket ss;
    private Socket s;

    private static String ip;
    private static String nick;

    public static int punkte;
    private static int antwort;
    private static boolean ergebnis;
    private static double zeit;

    private InputStreamReader in;
    private BufferedReader bf;
    private PrintWriter pr;

    RunnableThread(ServerSocket ss) {
        this.ss = ss;
    }

    public void run() {
        try {
            s = ss.accept();

            in = new InputStreamReader(s.getInputStream());
            bf = new BufferedReader(in);

            pr = new PrintWriter(s.getOutputStream());

            if (allowedToRun) {

                System.out.println("Connection established");
                System.out.println("Creating a new thread");

                host.createThread();

                nick = bf.readLine(); // the first message is the name of the player

                regClient(s);

            } else {

                pr.println("Game has started, you are not allowed to connect anymore");
                pr.flush();
                s.close();
                System.out.println("Thread does not accept any more connections");
            }
        } catch (Exception e) {
            System.out.println("Error! Upsie! Here: " + e);
        }
    }

    private static void regClient(Socket s) {
        ip = s.getInetAddress().toString();
        System.out.println(ip + " " + nick);
    }

    public void start() {
        System.out.println("Starting new Thread");
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }

    public void sendQuestion(String question) throws IOException {
        pr.println("START ROUND");
        pr.println(question);
        pr.flush();
        antwort = Integer.parseInt(bf.readLine());
        System.out.println(antwort);
        zeit = Double.parseDouble(bf.readLine());
        System.out.println(zeit);

        sendResult();
    }

    public void sendResult() {
        punkte = dummy.getResult(antwort, 2);
        System.out.println("Client bekommt: " + punkte + " Punkte");
        ergebnis = punkte > 0;
        System.out.println("Client bekommt: " + ergebnis + " als Ergebnis");
        pr.println("RESULT");
        pr.println(ergebnis);
        pr.println(punkte);
    }

    public void endGame() {
        pr.println("END GAME");
    }
}