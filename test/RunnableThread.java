import java.net.*;
import java.io.*;

class RunnableThread implements Runnable {
    private Thread t;
    private String threadName;
    private Socket s;

    private static String ip;
    private static String name;

    RunnableThread(String name, Socket s) {
        threadName = name;
        s = this.s;
        System.out.println("Creating " + threadName);
    }

    public void run() {
        try {
            InputStreamReader in = new InputStreamReader(s.getInputStream());
            BufferedReader bf = new BufferedReader(in);

            System.out.println("Connection established");

            PrintWriter pr = new PrintWriter(s.getOutputStream());

            name = bf.readLine();

            regClient(s);

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
