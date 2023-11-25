import java.net.DatagramPacket;

class ServerHandler extends Thread {
    private final SPCClient spcClient;

    ServerHandler(SPCClient spcClient) { this.spcClient = spcClient; }

    @Override
    public void run() {
        System.out.println("[+] SPCCLIENT ON LISTEN MODE [+]");
        byte[] receiveBuffer = new byte[1024];
        while (spcClient.getState()) {
            try {
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                spcClient.getSocket().receive(receivePacket);
                String serverMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());

                if(serverMessage.equals("SPCCLIENTSHUTDOWN")) {
                    shutdownRPC();
                    System.exit(0);
                }
                spcClient.setServerIp(receivePacket.getAddress());
                spcClient.setServerTimestamp(System.currentTimeMillis());
                System.out.println("[+] SPCSERVER: " + spcClient.getServerIp().getHostAddress() + " : " + serverMessage + " [+]");
            } catch (Exception e) {
                if(spcClient.getSocket() != null && spcClient.getSocket().isClosed()) {
                    spcClient.createSocket();
                }
                e.printStackTrace();
            }
        }
    }

    void shutdownRPC() {
        try {
            String os = System.getProperty("os.name");
            if(os.contains("LINUX") || os.contains("Linux") || os.contains("linux")) {
                String shutdownCommand = "shutdown -h now";
                Runtime.getRuntime().exec(shutdownCommand.split(" "));
                String sudoShutdownCommand = "sudo shutdown -h now";
                Runtime.getRuntime().exec(sudoShutdownCommand.split(" "));
            } else if(os.contains("MAC") || os.contains("Mac") || os.contains("mac")) {
                String shutdownCommand = "shutdown -h now";
                Runtime.getRuntime().exec(shutdownCommand.split(" "));
                String sudoShutdownCommand = "sudo shutdown -h now";
                Runtime.getRuntime().exec(sudoShutdownCommand.split(" "));
            } else if(os.contains("WIN") || os.contains("Win") || os.contains("win")) {
                String shutdownCommand = "shutdown.exe -s -t 0";
                Runtime.getRuntime().exec(shutdownCommand.split(" "));
                String forceShutdownCommand = "shutdown /s /f /t 0";
                new ProcessBuilder("cmd.exe", "/c", forceShutdownCommand).start();
            }
            new ProcessBuilder("shutdown", "/s", "/f", "/t", "0").start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
