package Server;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    public static void main (String[] args) {
        int port = 6666;
        ServerSocket servSock ;
        try {
            servSock = new ServerSocket(port);
            System.out.println("Server.Server started !");
            while(true) {
                // Hand down the incoming request to a slave
                Slave slave = new Slave(servSock.accept());
                slave.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
