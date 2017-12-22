package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Window extends JFrame implements ActionListener {


    private JPanel panel;

    private int size;

    private Socket           socket;
    private DataOutputStream toServer;
    private BufferedReader   fromServer;

    public Window(){
        this.setTitle("TP4_POA");
        this.setSize(900, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setLayout(new BorderLayout());
        JButton refreshButton = new JButton("Actualiser");
        refreshButton.addActionListener(this);
        this.getContentPane().add(refreshButton, BorderLayout.NORTH);

        // Get the number of sensors
        openSocket();
        try {
            toServer.writeBytes("C:getSize");
            String response = fromServer.readLine();
            size = Integer.parseInt(response);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        closeSocket();

        panel = new JPanel();
        GridLayout grid = new GridLayout((size/2)+1,2);
        panel.setLayout(grid);

        this.getContentPane().add(panel, BorderLayout.CENTER);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        panel = new JPanel();
        GridLayout grid = new GridLayout(size/2,2);
        panel.setLayout(grid);

        // Get the state from the server
        openSocket();
        String response ="";
        try {
            toServer.writeBytes("C:getStateAll");
            response = fromServer.readLine();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        closeSocket();

        // Draw the state
        JPanel pan;
        for(int i=0; i < size; i++) {
            pan = new JPanel();
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

    private void closeSocket() {
        try {
            this.toServer.close();
            this.fromServer.close();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openSocket() {
        try {
            this.socket     = new Socket("localhost", 6666);
            this.toServer   = new DataOutputStream(socket.getOutputStream());
            this.fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}