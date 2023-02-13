import java.net.*;
import java.io.*;

public class host {

    private static final int PORT = 2594;

    public static void main(String[] args) throws IOException {
        initServer(PORT);
    }

    public static void initServer(int port) throws IOException {
        System.out.println("Starting server on: " + port);
        ServerSocket ss = new ServerSocket(port);
        Socket s = ss.accept();

        RunnableThread t1 = new RunnableThread("myThread", s);
        t1.start();

    }

    public static void maini(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(4000);
        Socket s = ss.accept();

        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);

        System.out.println("Connection established");

        System.out.println("Waiting for client to send message");
        PrintWriter pr = new PrintWriter(s.getOutputStream());

        while (true) {
            String str = bf.readLine();
            if (str == null)
                break;

            handleMessage(str);
            respondToClient(pr);

        }

    }

    public static void handleMessage(String msg) {
        System.out.println("client: " + msg.toString());
    }

    public static void respondToClient(PrintWriter pr) {
        pr.println("Hello Client, I received your message!");
        pr.flush();
    }

}
