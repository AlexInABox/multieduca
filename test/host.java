import java.net.*;
import java.io.*;

public class host {

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(4000);
        Socket s = ss.accept();

        System.out.println("Connection established");

        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);

        System.out.println("Waiting for client to send message");
        PrintWriter pr = new PrintWriter(s.getOutputStream());
        while (true) {

            String str = bf.readLine();
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