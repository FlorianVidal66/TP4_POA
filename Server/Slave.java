package Server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Slave extends Thread {
    private Socket sock;

    public Slave(Socket sock){
        this.sock = sock;
    }

    @Override
    public void run() {
        try {
            BufferedReader fromBR = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            String command = fromBR.readLine();
            System.out.println("Command received from Sensor.Sensor"+ command);

            if (command.charAt(0) == 'C') {
                DataOutputStream toClient = new DataOutputStream(sock.getOutputStream());
                //TODO : Ici il faut mettre le code pour retourner les infos que le client demande
                //toClient.writeBytes(result);



            } else {
                //TODO : Ici il faut gérer la BDD des capteurs et de leur état en fonction du message reçu
                //TODO : Ne pas oublier que le message est différent s'il provient du capteur ou d'un filtre (il faut donc dinstinguer les deux cas)
                /*
                if (je viens du capteur){

                } else {
                     Je viens d'un filtre
                }
                */
            }



            this.sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}