import java.net.*;
import java.io.*;

public class client {

    public static void main(String[] args) throws IOException {

        Socket s = new Socket("https://alexinabox-solid-carnival-pg9jq5qpgrwh7jpq.github.dev", 4000);

        System.out.println("Connection established");

        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);

        System.out.println("Waiting for host to send message");
        PrintWriter pr = new PrintWriter(s.getOutputStream());
        pr.println("Hello Host, I am client!");
        pr.flush();
        while (true) {

            String str = bf.readLine();
            handleMessage(str);
            respondToHost(pr);
        }
    }

    public static void handleMessage(String msg) {
        System.out.println("host: " + msg.toString());
    }

    public static void respondToHost(PrintWriter pr) {
        pr.println("Hello Host, I received your message!");
        pr.flush();
    }
}