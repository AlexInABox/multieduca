import java.net.*;
import java.io.*;

class RunnableThread implements Runnable {
    public boolean allowedToRun = true; // this is the variable that will be used to stop the thread

    private Thread t;
    private String threadName;
    private ServerSocket ss;
    private Socket s;

    private static String ip;
    private static String nick;

    public static int punktezahl;

    RunnableThread(String name, ServerSocket ss) {
        threadName = name;
        this.ss = ss;
        System.out.println("Creating " + threadName);
    }

    public void run() {
        try {
            s = ss.accept();

            InputStreamReader in = new InputStreamReader(s.getInputStream());
            BufferedReader bf = new BufferedReader(in);

            PrintWriter pr = new PrintWriter(s.getOutputStream());

            if (allowedToRun) {

                System.out.println("Connection established");
                System.out.println("Creating a new thread");

                host.createThread();

                nick = bf.readLine(); // the first message is the name of the player

                regClient(s);

                while (host.isStarted) {

                    System.out.println("Round started, thread" + nick);

                }
            } else {

                pr.println("Game has started, you are not allowed to connect anymore");
                pr.flush();
                s.close();
                System.out.println("Thread " + threadName + " does not accept any more connections");
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
        System.out.println("Starting " + threadName);
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }
}
