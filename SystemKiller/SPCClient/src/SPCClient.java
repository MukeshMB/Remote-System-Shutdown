import java.net.InetAddress;
import java.net.DatagramSocket;
import java.net.NetworkInterface;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Timer;

public class SPCClient {
    private InetAddress serverIp = null;
    private final ArrayList<NetworkAddress> networks = new ArrayList<>();
    private DatagramSocket socket;
    private long serverTimestamp = System.currentTimeMillis();
    private String hostname = null;
    private String mac = null;
    private boolean state = true;
    final Timer timer = new Timer();

    SPCClient() {
        addressResolver();
        createSocket();
    }

    void createSocket() {
        try {
            socket = new DatagramSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void addressResolver() {
        try {
            networks.clear();
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while(networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if(networkInterface.isUp()) {
                    Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress address = addresses.nextElement();
                        if (address instanceof Inet4Address && !address.isLoopbackAddress()) {
                            short subnet = networkInterface.getInterfaceAddresses().get(0).getNetworkPrefixLength();
                            networks.add(new NetworkAddress(address.getHostAddress(), subnet));
                            if(hostname == null) {
                                hostname = address.getHostName();
                            }
                            if(mac == null) {
                                byte[] _mac = networkInterface.getHardwareAddress();
                                StringBuilder sb = new StringBuilder();
                                for (int i = 0; i < _mac.length; i++) {
                                    sb.append(String.format("%02X", _mac[i]));
                                    if (i < _mac.length - 1) {
                                        sb.append(":");
                                    }
                                }
                                mac = sb.toString();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    final InetAddress getServerIp() { return this.serverIp; }

    final DatagramSocket getSocket() { return this.socket; }

    final ArrayList<NetworkAddress> getNetworks() { return this.networks; }

    final long getServerTimestamp() { return this.serverTimestamp; }

    final String getHostname() { return this.hostname; }

    final String getMac() { return this.mac; }
    final boolean getState() { return this.state; }

    void setServerIp(final InetAddress ip) { this.serverIp = ip; }

    void setServerTimestamp(long timestamp) { this.serverTimestamp = timestamp; }

    void startClient() {
        new ServerHandler(this).start();
        timer.scheduleAtFixedRate(new ClientHandler(this), 0, 5000);
    }

    void closeClient() {
        state = false;
        timer.cancel();
    }

    public static void main(String[] args) {
        SPCClient spcClient = new SPCClient();
        spcClient.startClient();
        Runtime.getRuntime().addShutdownHook(new Thread(spcClient::closeClient));
    }
}
