package echoServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ReceiverThread extends Thread{
    private DatagramSocket socket;
    private volatile boolean stopped = false;
    ReceiverThread(DatagramSocket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        byte[] buffer = new byte[65507];
        while (true) {
            if (stopped)
                return;
            DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
            try {
                socket.receive(dp);
                String s = new String(dp.getData(), 0, dp.getLength(), "UTF-8");
                System.out.println(s);
                if (s.equals(".")) halt();
                Thread.yield();
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }
    }
    public void halt() {
        this.stopped = true;
    }

}
