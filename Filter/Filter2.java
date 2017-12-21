package Filter;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Filter2 extends Thread {

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

    private String previousState;


    public Filter2(int FilterPort, String serverAdress, int serverPort){
        this.port          = FilterPort;
        this.toAdress      = serverAdress;
        this.toPort        = serverPort;
        this.previousState = "";
    }

    @Override
    public void run() {

        try {
            this.ss = new ServerSocket(port);
            while(true) {
                fromSocket = ss.accept();

                // Connection received : we can now read the message from filter 1
                fromBR = new BufferedReader(new InputStreamReader(fromSocket.getInputStream()));
                String messageFrom = fromBR.readLine();

                if (!previousState.equals(messageFrom)){
                    // Send the change to the server
                    openSocket();
                    this.previousState = messageFrom;
                    this.toDOS.writeBytes(messageFrom);
                    System.out.println("F2: "+messageFrom);
                    closeSocket();
                }

                fromSocket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void closeSocket() {
        try {
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
}