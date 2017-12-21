package Sensor;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

public class Sensor extends Thread{

    private int    id;
    private int    rangeMax;
    private long   rate;
    private String opt;
    private Random rd;

    private String adressTo;
    private int    portTo;
    private Socket socket;
    private DataOutputStream toServer;


    public Sensor(int id, int rangeMax, long rate, String opt, String adressTo, int portTo) {
        this.id = id;
        this.rangeMax = rangeMax;
        this.rate = rate;
        this.opt = opt;
        this.adressTo = adressTo;
        this.portTo = portTo;
        this.rd = new Random();
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(rate);
                sendMessage();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessage() {
        openSocket();
        try {
            String message = id+":R"+rangeMax+"_P"+rd.nextInt(2)+":"+opt;
            System.out.println("Sensor "+id+" : "+message);
            this.toServer.writeBytes(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        closeSocket();
    }

    private void closeSocket() {
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openSocket() {
        try {
            this.socket = new Socket(adressTo, portTo);
            this.toServer = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connectionFromFilter(String filterAddress, int filterPort){
        this.adressTo = filterAddress;
        this.portTo = filterPort;
    }
}
