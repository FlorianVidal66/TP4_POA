package Filter;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Filter1 extends Thread {

    private int port;
    private ServerSocket ss;

    private Socket fromSocket;
    private String fromAdress;
    private int    fromPort;
    private BufferedReader fromBR;

    private Socket toSocket;
    private String toAdress;
    private int    toPort;
    private DataOutputStream toDOS;


    public Filter1(int FilterPort, String serverAdress, int serverPort){
        this.port     = FilterPort;
        this.toAdress = serverAdress;
        this.toPort   = serverPort;
    }

    @Override
    public void run() {

        try {
            this.ss = new ServerSocket(port);
            while(true) {
                fromSocket = ss.accept();

                // Connection received : we can now read the message from the sensor
                fromBR = new BufferedReader(new InputStreamReader(fromSocket.getInputStream()));
                String messageFrom = fromBR.readLine();

                // Send the appropriate message to the output entity (it could be Filter.Filter2 or Server.Server)
                openSocket();
                String messageTo = decode(messageFrom);
                this.toDOS.writeBytes(messageTo);
                closeSocket();
                System.out.println("F1: " + messageTo);
            }

        } catch (IOException e) {
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

    private void closeSocket() {
        try {
            this.fromSocket.close();
            this.toSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openSocket() {
        try {
            this.toSocket = new Socket(toAdress, toPort);
            this.toDOS = new DataOutputStream(toSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
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
