import java.net.*;
import java.io.*;

public class client {

    public static void main(String[] args) throws IOException {

        Socket s = new Socket("cloud.alexinabox.de", 4000);

        System.out.println("Connection established");

        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);


        PrintWriter pr = new PrintWriter(s.getOutputStream());
        System.out.println("Sending message to host");
        pr.println("Hello Host, I am client!");
        pr.flush();
        System.out.println("Sent message to host");
        
        while (true) {

            String str = bf.readLine();
            handleMessage(str);
            //wait 5 seconds
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            respondToHost(pr);
        }
    }

    public static void handleMessage(String msg) {
        System.out.println("host: " + msg);
    }

    public static void respondToHost(PrintWriter pr) {
        pr.println("Hello Host, I received your message!");
        pr.flush();
    }
}