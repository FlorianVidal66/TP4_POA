package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server {

    public static void main (String[] args) {
        int port = 6666;
        int nbSensor = 3;
        ArrayList<String> sensors = new ArrayList<>();
        for (int i = 0; i < nbSensor; i++) {
            sensors.add("_");
        }
        ServerSocket servSock ;
        try {
            servSock = new ServerSocket(port);
            System.out.println("Server.Server started !");
            while(true) {
                // Hand down the incoming request to a slave
                Slave slave = new Slave(servSock.accept(), sensors, "localhost", 6666);
                slave.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
