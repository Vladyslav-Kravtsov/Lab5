package main.java.Ex1;

import java.io.Serializable;

import java.net.InetAddress;

public class User implements Serializable {
    private static final long serialVersionUID = -1L;
    private InetAddress inetAddress;
    private int port;

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public User(InetAddress inetAddress, int port) {
        this.inetAddress = inetAddress;
        this.port = port;
    }


    @Override
    public String toString() {
        return "User{" +
                "inetAddress=" + inetAddress +
                ", port=" + port +
                '}';
    }
}
