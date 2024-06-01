package main.java.client;

import interfaces.Result;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class ClientFrame extends JFrame{
    private JTextField ipField, portField, numberField;
    private JButton calculateButton, clearButton, exitButton;
    private JTextArea resultArea;
    public ClientFrame() {
        setTitle("ClientFrame");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ipField = new JTextField(10);
        portField = new JTextField(10);
        numberField = new JTextField(10);
        calculateButton = new JButton("Calculate");
        clearButton = new JButton("Clear Result");
        exitButton = new JButton("Exit Program");
        resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(new JLabel("IP Address:"));
        inputPanel.add(ipField);
        inputPanel.add(new JLabel("Port:"));
        inputPanel.add(portField);
        inputPanel.add(new JLabel("Number:"));
        inputPanel.add(numberField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(calculateButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(exitButton);

        add(inputPanel, BorderLayout.NORTH);
        add(resultArea, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        calculateButton.addActionListener(arg0->{
            try {
                calculate(ipField.getText(),Integer.parseInt(portField.getText()));
            }catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);}
        });
        clearButton.addActionListener(arg0->{
            resultArea.setText("");
        });
        exitButton.addActionListener(arg0->{
            dispose();
        });
    }
    public static void main(String[] args) {
        ClientFrame gui = new ClientFrame();
        gui.setVisible(true);
    }
    public void calculate(String host, int port) throws IOException, ClassNotFoundException {
        Socket client = new Socket(host, port);
        ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
        String classFile = "target/classes/client/Factorial.class";
        out.writeObject(classFile);
        FileInputStream fis = new FileInputStream(classFile);
        byte[] b = new byte[fis.available()];
        fis.read(b);
        out.writeObject(b);
        int num = Integer.parseInt(numberField.getText());
        client.Factorial aJob = new client.Factorial(num);
        out.writeObject(aJob);

        ObjectInputStream in = new ObjectInputStream(client.getInputStream());

        classFile = (String) in.readObject();
        b = (byte[]) in.readObject();
        FileOutputStream fos = new FileOutputStream(classFile);
        fos.write(b);
        Result r = (Result) in.readObject();

        resultArea.append("result = " + r.output() + ", time taken = " + r.scoreTime() + "ns");

    }
}
