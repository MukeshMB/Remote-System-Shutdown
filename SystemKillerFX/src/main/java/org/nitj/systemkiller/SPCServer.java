package org.nitj.systemkiller;

import org.nitj.systemkillerfx.Controller;
import javafx.fxml.FXMLLoader;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Timer;


public class SPCServer {
    private DatagramSocket serverSocket = null;
    private final HashMap<String, ClientData> clientMap = new HashMap<>();
    private Controller controller = null;
    private final Timer timer = new Timer();
    private final ClientHandler clientHandler = new ClientHandler(this);
    private boolean serverState = true;


    public SPCServer() {
        createServerSocket();
    }

     void createServerSocket() {
        try {
            serverSocket = new DatagramSocket(8090);
            ClientData.serverSocket = serverSocket;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    DatagramSocket getServerSocket() { return this.serverSocket; }

    HashMap<String, ClientData> getClientMap() { return this.clientMap; }

    Controller getController() { return this.controller; }

    boolean getServerState() { return this.serverState; }

    public void setController(FXMLLoader fxmlLoader) { this.controller = fxmlLoader.getController(); }

    public void listen() {
        timer.scheduleAtFixedRate(new ServerHandler(this), 0, 10000);
        clientHandler.start();
    }

    public void stopServer() {
        serverState = false;
        timer.cancel();
        serverSocket.close();
    }
}
