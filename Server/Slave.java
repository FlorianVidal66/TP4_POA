package Server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class Slave extends Thread {
    private Socket sock;
    private ArrayList<String> sensors;

public Slave(Socket sock, ArrayList<String> sensors){
        this.sock = sock;
        this.sensors = sensors;
    }

    @Override
    public void run() {
        try {
            BufferedReader fromBR = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            String command = fromBR.readLine();
            System.out.println("Command received from Sensor.Sensor    "+ command);

            if (command.charAt(0) == 'C') {
                DataOutputStream toClient = new DataOutputStream(sock.getOutputStream());
                //TODO : Ici il faut mettre le code pour retourner les infos que le client demande
                //toClient.writeBytes(result);
            } else {
                int indexSensor = Integer.parseInt(""+command.charAt(0)) - 1;
                //TODO : Ici il faut gérer la BDD des capteurs et de leur état en fonction du message reçu
                //TODO : Ne pas oublier que le message est différent s'il provient du capteur ou d'un filtre (il faut donc dinstinguer les deux cas)
                if (command.contains("R")){ //if become to the sensor
                    sensors.set(indexSensor, ""+command.charAt(command.lastIndexOf("P") + 1));
                } else { // become to a filter
                    if (command.substring(command.lastIndexOf(":") + 1).contains("NA"))
                    {
                        sensors.set(indexSensor, "0");
                    } else {
                        sensors.set(indexSensor, "1");
                    }
                }
            }
            System.out.println(sensors);
            this.sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}