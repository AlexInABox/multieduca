import java.net.*;
import java.io.*;

class ClientHander extends Thread{
    private Socket clientSocket;

    public ClientHandeler(Socket socket){
    
    this.clientSocket = socket;
    
    }
}
