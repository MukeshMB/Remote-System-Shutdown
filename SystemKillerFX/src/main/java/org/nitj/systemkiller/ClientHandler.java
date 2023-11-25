package org.nitj.systemkiller;

import javafx.application.Platform;
import java.net.DatagramPacket;
import java.net.InetAddress;

class ClientHandler extends Thread {
    private final SPCServer spcServer;

    ClientHandler(SPCServer spcServer) {
        this.spcServer = spcServer;
    }

    @Override
    public void run() {
        System.out.println("[+] SPCServer Started Listening For Clients [+]");
        byte[] receiveBuffer = new byte[1024];

        while(spcServer.getServerState()) {
            try {
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                spcServer.getServerSocket().receive(receivePacket);

                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                String clientMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());

                String host =  clientMessage.split("#")[0];
                String mac = clientMessage.split("#")[1];
                clientMessage = clientMessage.split("#")[2];

                spcServer.getClientMap().remove(mac);
                spcServer.getClientMap().put(mac, new ClientData(host, clientAddress, mac, clientPort, clientMessage));
                Platform.runLater(() -> handleClient(spcServer.getClientMap().get(mac)));
            } catch (Exception e) {
                if(spcServer.getServerSocket()!=null && spcServer.getServerSocket().isClosed() && spcServer.getServerState()){
                    spcServer.createServerSocket();
                }
                e.printStackTrace();
            }
        }
    }

    void handleClient(ClientData clientData) {
        if(clientData.getMessage().equals("SPCSERVER?")) {
            sendServerHelloPacket(clientData);
        }
        if (!spcServer.getController().isAdded(clientData.getMac())) {
            spcServer.getController().addItem(clientData.getMac(), clientData);
        }
        spcServer.getController().updateItemState(clientData);
        spcServer.getController().updateClientMessage("[+] " + clientData.getHostName() + ": " + clientData.getMessage());
    }

    void sendServerHelloPacket(ClientData clientData) {
        String message = SPCServerMessages.serverHelloMessage();
        DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), clientData.getIpAddress(), clientData.getPort());
        try {
            spcServer.getServerSocket().send(packet);
            spcServer.getController().updateServerMessage("[+] HELLO " + clientData.getHostName() + " [+]");
        } catch (Exception e) {
            if(spcServer.getServerSocket()!=null && spcServer.getServerSocket().isClosed() && spcServer.getServerState()) {
                spcServer.createServerSocket();
            }
            e.printStackTrace();
        }
    }
}
