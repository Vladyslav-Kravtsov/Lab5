package server;

import interfaces.Executable;
import main.java.server.ResultImpl;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class StartServ extends Thread{
    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;
    private Socket clientSocket = null;
    private ServerSocket serveSocket;

    public StartServ(ServerSocket servSoc) {
        this.serveSocket = servSoc;
    }
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                clientSocket = serveSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                ois = new ObjectInputStream(clientSocket.getInputStream());
                oos = new ObjectOutputStream(clientSocket.getOutputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    try {
                        String classFile = (String) ois.readObject();
                        classFile = classFile.replaceFirst("client", "server");
                        byte[] b = (byte[]) ois.readObject();
                        FileOutputStream fos = new FileOutputStream(classFile);
                        fos.write(b);
                        Executable ex = (Executable) ois.readObject();
                        double startTime = System.nanoTime();
                        Object output = ex.execute();
                        double endTime = System.nanoTime();
                        double completionTime = endTime - startTime;
                        ResultImpl r = new ResultImpl(output, completionTime);
                        classFile = classFile.replaceFirst("server", "client");
                        oos.writeObject(classFile);
                        FileInputStream fis = new FileInputStream(classFile);
                        byte[] bo = new byte[fis.available()];
                        fis.read(bo);
                        oos.writeObject(bo);
                        oos.writeObject(r);
                        server.ServerFrame.getTextArea().append("Sending....");
                    } catch (SocketException e) {
                        break;
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
