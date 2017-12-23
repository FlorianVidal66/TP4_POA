package Filter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Filter1 extends Thread {

    private int    port;
    private String toAdress;
    private int    toPort;

    public Filter1(int FilterPort, String serverAdress, int serverPort){
        this.port     = FilterPort;
        this.toAdress = serverAdress;
        this.toPort   = serverPort;
    }

    @Override
    public void run() {

        try {
            ServerSocket ss = new ServerSocket(port);
            while(true) {
                Socket toSocket = new Socket(toAdress, toPort);
                Socket fromSocket = ss.accept();

                // Connection received : we can now read the message from the sensor
                InputStream is = fromSocket.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(is);
                String messageFrom = (String) ois.readObject();

                // Send the appropriate message to the output entity (it could be Filter.Filter2 or Server.Server)
                String messageTo = decode(messageFrom);
                OutputStream os = toSocket.getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os);
                oos.writeObject(messageTo);
                System.out.println("F1: " + messageTo);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    /**
     * Check the position of the vehicle according to the data and return the corresponding message
     */
    private String decode(String message){
        int index = message.indexOf(":");
        String sensorID = message.substring(0,index);
        String isOccupied = String.valueOf(message.charAt(index+7));
        if (isOccupied.equals("1")){
            return sensorID + ":DETECTED";
        } else {
            return sensorID + ":NA";
        }
    }


    /**
     * Update the output entity from the server to the Filter.Filter2
     */
    public void connectionFromFilter(String filterAddress, int filterPort){
        this.toAdress = filterAddress;
        this.toPort = filterPort;
    }
}
