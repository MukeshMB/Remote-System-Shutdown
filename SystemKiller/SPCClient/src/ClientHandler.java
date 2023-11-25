import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.TimerTask;

class ClientHandler extends TimerTask {
    private final SPCClient spcClient;

    ClientHandler(SPCClient spcClient) { this.spcClient = spcClient; }

    @Override
    public void run() {
        try {
            if(spcClient.getServerIp() == null) {
                spcClient.addressResolver();
                sendSPCServerDiscoveryPacket();
            } else if(System.currentTimeMillis() - spcClient.getServerTimestamp() >= 20000) {
                spcClient.setServerIp(null);
            } else {
                sendSPCClientAlivePacket();
            }
        } catch (Exception e) {
            if(spcClient.getSocket() != null && spcClient.getSocket().isClosed()) {
                spcClient.createSocket();
            }
            e.printStackTrace();
        }
    }

    void sendSPCServerDiscoveryPacket() throws Exception {
        System.out.println("[+] SERVER DISCOVERY PROTOCOL INITIATED [+]");
        for(NetworkAddress networkAddress : spcClient.getNetworks()) {
            InetAddress broadcastAddress = InetAddress.getByName(networkAddress.getBroadcastAddress());
            System.out.println("[+] Broadcasting Over: " + broadcastAddress + " [+]");
            byte[] messageBytes = (spcClient.getHostname() + "#" + spcClient.getMac() + "#" + SPCClientMessages.serverDiscoveryMessage()).getBytes();
            DatagramPacket packet = new DatagramPacket(messageBytes, messageBytes.length, broadcastAddress, 8090);
            spcClient.getSocket().send(packet);
        }
    }

    void sendSPCClientAlivePacket() throws Exception {
        System.out.println("[+] CLIENT ALIVE PACKET SENT [+]");
        byte[] messageBytes = (spcClient.getHostname() + "#" + spcClient.getMac() + "#" + SPCClientMessages.clientAliveMessage()).getBytes();
        DatagramPacket packet = new DatagramPacket(messageBytes, messageBytes.length, spcClient.getServerIp(), 8090);
        spcClient.getSocket().send(packet);
    }
}
