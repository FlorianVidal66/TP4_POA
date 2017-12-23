package Server;

import java.io.*;
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
            InputStream is = sock.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            String command = (String) ois.readObject();
            System.out.println("Command: " + command);

            if (command.charAt(0) == 'C') {
                OutputStream os = sock.getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os);
                if (command.contains("getSize")) {
                    oos.writeObject("" + sensors.size());
                }
                if (command.contains("getStateAll")) {
                    oos.writeObject(sendSensorsString());
                }

            } else {
                int indexSensor = Integer.parseInt("" + command.charAt(0)) - 1;
                System.out.println("idx: "+indexSensor);

                if (command.contains("R")) { // Comes from a sensor without filter
                    sensors.set(indexSensor, "" + command.charAt(command.indexOf("P") + 1));
                } else { // Comes from a filter
                    if (command.substring(command.lastIndexOf(":") + 1).contains("NA")) {
                        sensors.set(indexSensor, "0");
                    } else {
                        sensors.set(indexSensor, "1");
                    }
                }
            }
            System.out.println(sensors);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Object sendSensorsString() {
        String message = "";
        for(String s : sensors){
            message = message + s;
        }
        return message;
    }


}

