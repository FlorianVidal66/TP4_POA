package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Frame extends JFrame implements ActionListener{

    private String adressTo;
    private int    portTo;
    private Socket socket;

    private JPanel panel;
    private int size;


    public Frame(String adressTo, int portTo) {

        this.setTitle("TP4_POA");
        this.setSize(900, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setLayout(new BorderLayout());
        JButton refreshButton = new JButton("Actualiser");
        refreshButton.addActionListener(this);
        this.getContentPane().add(refreshButton, BorderLayout.NORTH);

        // Get the number of sensors
        this.adressTo = adressTo;
        this.portTo = portTo;
        this.size = Integer.parseInt(request("C:getSize"));

        panel = new JPanel();
        GridLayout grid = new GridLayout((size/2)+1,2);
        panel.setLayout(grid);

        this.getContentPane().add(panel, BorderLayout.CENTER);
        this.setVisible(true);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        panel = new JPanel();
        GridLayout grid = new GridLayout((size/2)+1,2);
        panel.setLayout(grid);

        // Get the state from the server
        String response = request("C:getStateAll");

        // Draw the state
        JPanel pan;
        for(int i=0; i < size; i++) {
            pan = new JPanel();
            JLabel label = new JLabel(""+i);
            pan.add(label);
            if(response.charAt(i) == '0') {
                pan.setBackground(Color.green);
            } else if(response.charAt(i) == '1'){
                pan.setBackground(Color.red);
            } else {
                pan.setBackground(Color.gray);
            }
            panel.add(pan);
        }

        this.getContentPane().add(panel, BorderLayout.CENTER);
        this.setVisible(true);
    }

    private String request(String message){
        try {
            this.socket = new Socket(adressTo, portTo);
            OutputStream os = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(message);

            InputStream is = socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            String command = (String) ois.readObject();
            System.out.println("Command: "+command);
            this.socket.close();
            return command;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

}
