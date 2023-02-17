import java.net.*;
import java.io.*;

class RunnableThread implements Runnable {
    private Thread t;
    private String threadName;
    private ServerSocket ss;
    private Socket s;

    private static String ip;
    private static String name;

    public static int punktezahl;

    RunnableThread(String name, ServerSocket ss) {
        threadName = name;
        this.ss = ss;
        System.out.println("Creating " + threadName);
    }

    public void run() {
        try {
            s = ss.accept();
            System.out.println("Connection established");
            host.createThread();

            InputStreamReader in = new InputStreamReader(s.getInputStream());
            BufferedReader bf = new BufferedReader(in);

            PrintWriter pr = new PrintWriter(s.getOutputStream());

            name = bf.readLine();

            regClient(s);

            while (host.isStarted) {

                System.out.println("Round started, thread" + name);

            }

        } catch (Exception e) {
            System.out.println("Error! Upsie! Here: " + e);
        }
    }

    private static void regClient(Socket s) {
        ip = s.getInetAddress().toString();
        System.out.println(ip + " " + name);
    }

    public void start() {
        System.out.println("Starting " + threadName);
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }
}
