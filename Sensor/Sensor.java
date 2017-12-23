package Sensor;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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
        try {
            while (true){
                socket = new Socket(adressTo, portTo);
                try {
                    Thread.sleep(rate);
                    sendMessage();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        try {

            String message = id+":R"+rangeMax+"_P"+rd.nextInt(2)+":"+opt;
            System.out.println("Sensor "+id+" : "+message);

            OutputStream os = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void connectionFromFilter(String filterAddress, int filterPort){
        this.adressTo = filterAddress;
        this.portTo = filterPort;
    }
}
