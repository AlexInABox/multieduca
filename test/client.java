import java.net.*;
import java.io.*;

public class client {

    public static void main(String[] args) throws IOException {
        Socket s = new Socket("localhost", 8080);

        System.out.println("Connection established");

        PrintWriter pr = new PrintWriter(s.getOutputStream());
        pr.println("Hello Host, im the client!");
        pr.flush();

    }
}