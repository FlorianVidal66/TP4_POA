package Filter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Filter2 extends Thread {

    private int    port;
    private String toAdress;
    private int    toPort;

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
            ServerSocket ss = new ServerSocket(port);
            Socket toSocket = new Socket(toAdress, toPort);
            while(true) {
                Socket fromSocket = ss.accept();

                // Connection received : we can now read the message from filter 1
                InputStream is = fromSocket.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(is);
                String messageFrom = (String) ois.readObject();

                if (!previousState.equals(messageFrom)){
                    // Send the change to the server
                    this.previousState = messageFrom;
                    OutputStream os = toSocket.getOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(os);
                    oos.writeObject(messageFrom);
                    System.out.println("F2: "+messageFrom);

                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

}