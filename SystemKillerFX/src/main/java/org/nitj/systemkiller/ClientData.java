package org.nitj.systemkiller;

import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClientData {
    private final String HOSTNAME;
    private final InetAddress IPADDR;
    private final String MAC;
    private final int PORT;
    private boolean STATE;
    private final String MESSAGE;
    private final long TIMESTAMP;
    public static DatagramSocket serverSocket;

    public ClientData(String host, InetAddress ip, String mac, int port, String message) {
        this.HOSTNAME = host;
        this.IPADDR = ip;
        this.MAC = mac;
        this.PORT = port;
        this.STATE = true;
        this.MESSAGE = message;
        this.TIMESTAMP = System.currentTimeMillis();
    }

    public String getHostName() { return this.HOSTNAME; }

    public InetAddress getIpAddress() { return this.IPADDR; }

    public String getMac() { return this.MAC; }

    public int getPort() { return this.PORT; }

    public boolean getState() { return this.STATE; }

    public String getMessage() { return this.MESSAGE; }

    public long getTimestamp() { return this.TIMESTAMP; }

    public void setState(boolean state) { this.STATE = state; }

}
