package org.nitj.systemkiller;

import javafx.application.Platform;
import java.net.DatagramPacket;
import java.util.TimerTask;

class ServerHandler extends TimerTask {
    private final SPCServer spcServer;

    ServerHandler(SPCServer spcServer) {
        this.spcServer = spcServer;
    }

    @Override
    public void run() {
        for (String key : spcServer.getClientMap().keySet()) {
            ClientData clientData = spcServer.getClientMap().get(key);
            String message = SPCServerMessages.serverAliveMessage();
            DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), clientData.getIpAddress(), clientData.getPort());
            try {
                spcServer.getServerSocket().send(packet);
                Platform.runLater(() -> spcServer.getController().updateServerMessage("[+] SERVER ALIVE TO: " + clientData.getHostName() + "[+]"));
            } catch (Exception e) {
                if(spcServer.getServerSocket()!=null && spcServer.getServerSocket().isClosed() && spcServer.getServerState()){
                    spcServer.createServerSocket();
                }
                e.printStackTrace();
            }
            if(System.currentTimeMillis() - clientData.getTimestamp() > 10000) {
                clientData.setState(false);
                Platform.runLater(() -> spcServer.getController().updateItemState(clientData));
            }
        }
    }
}