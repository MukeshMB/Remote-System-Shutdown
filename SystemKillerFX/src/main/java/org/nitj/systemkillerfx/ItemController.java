package org.nitj.systemkillerfx;

import org.nitj.systemkiller.ClientData;
import org.nitj.systemkiller.SPCServerMessages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import java.net.DatagramPacket;

public class ItemController {

    @FXML
    private Button btnShutdown;

    @FXML
    private Label hostNameLabel;

    @FXML
    private Label ipAddressLabel;

    @FXML
    private Button btnActive;

    private ClientData clientData;

    public void handleClicks(ActionEvent e) {
        if (e.getSource() == btnShutdown) {
            initiateShutdown();
        }
    }

    public void initiateShutdown() {
        String message = SPCServerMessages.clientShutdownMessage();
        DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), clientData.getIpAddress(), clientData.getPort());
        try {
            ClientData.serverSocket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        btnShutdown.setBackground(Background.fill(Color.RED));
        clientData.setState(false);
    }

    public void updateState(boolean state) {
        if(state) {
            btnShutdown.setBackground(Background.fill(Color.GREEN));
            btnActive.setText("Alive");
        } else {
            btnShutdown.setBackground(Background.fill(Color.RED));
            btnActive.setText("Dead");
        }
    }

    public void updateProperty(ClientData clientData) {
        hostNameLabel.setText(clientData.getHostName());
        ipAddressLabel.setText(clientData.getIpAddress().getHostAddress());
        this.clientData = clientData;
        updateState(clientData.getState());
    }

    public boolean isUp() {
        return btnActive.getText().equals("Alive");
    }

    public String getHostName() { return clientData.getHostName(); }

    public void updateClientData(ClientData clientData) { this.clientData = clientData; }

}
