package server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;

public class ServerFrame extends JFrame {
    private JTextField portField;
    private static JTextArea outputArea;
    private JButton startButton;
    private JButton stopButton;
    private JButton exitButton;

    public ServerFrame() {
        setTitle("ServerFrame");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel portLabel = new JLabel("Working Port:");
        portField = new JTextField(10);
        topPanel.add(portLabel);
        topPanel.add(portField);
        panel.add(topPanel, BorderLayout.NORTH);

        outputArea = new JTextArea();
        startButton = new JButton("Start Server");
        stopButton = new JButton("Stop Server");
        exitButton = new JButton("Exit Server");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(exitButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        add(panel);

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                outputArea.append("Server started on port " + portField.getText() + "\n");
                startServer(Integer.parseInt(portField.getText()));
            }
        });
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                outputArea.append("Server stopped\n");
            }
        });
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        setVisible(true);
    }


    public static JTextArea getTextArea(){
        return outputArea;
    }
    public static void main(String[] args) {
        new ServerFrame();
    }
    public void startServer(int port) {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        StartServ startServ = new StartServ(serverSocket);
        startServ.start();
    }
}
