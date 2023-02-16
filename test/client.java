import java.net.*;
import java.io.*;

public class client {
    private static PrintWriter pr;

    public static void main(String[] args) throws IOException {

        Socket s = new Socket("10.16.111.109", 2594);

        System.out.println("Connection established with: " + s.getInetAddress().getHostAddress() + ":" + s.getPort());

        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);

        pr = new PrintWriter(s.getOutputStream());
        System.out.println("Sending name to host");
        pr.println("Alex");
        pr.flush();
        System.out.println("Sent message to host");

        while (true) {

            String str = bf.readLine();
            if (str == null)
                break;
            handleMessage(str);
        }
    }

    public static void handleMessage(String msg) {
        System.out.println("host: " + msg);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendMessage("My Answer is: fancy!");
    }

    public static void sendMessage(String msg) {
        pr.println(msg);
        pr.flush();
    }
}
