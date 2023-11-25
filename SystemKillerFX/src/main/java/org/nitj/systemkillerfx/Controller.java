package org.nitj.systemkillerfx;

import org.nitj.systemkiller.ClientData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.HashMap;

public class Controller {

    @FXML
    private VBox pnItems;

    @FXML
    private Button btnShutdownAll;

    @FXML
    private Button btnAboutUs;

    @FXML
    private TextField filterClient;

    @FXML
    private Label sl1;

    @FXML
    private Label sl2;

    @FXML
    private Label cl1;

    @FXML
    private Label cl2;

    @FXML
    private Label totalClients;

    @FXML
    private Label aliveClients;

    @FXML
    private Label deadClients;

    private final HashMap<String, FXMLLoader> mapItems = new HashMap<>();

    public void addItem(String key, ClientData clientData) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(SPCServerUI.class.getResource("Item.fxml"));
            mapItems.put(key, fxmlLoader);

            Node node = fxmlLoader.load();
            node.setOnMouseEntered(event -> node.setStyle("-fx-background-color : #0A0E3F"));
            node.setOnMouseExited(event -> node.setStyle("-fx-background-color : #02030A"));

            ItemController itemController = fxmlLoader.getController();
            itemController.updateProperty(clientData);

            totalClients.setText(Integer.toString(Integer.parseInt(totalClients.getText()) + 1));
            aliveClients.setText(Integer.toString(Integer.parseInt(aliveClients.getText()) + 1));
            pnItems.getChildren().add(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isAdded(String key) {
        return mapItems.containsKey(key);
    }

    public void handleClicks(ActionEvent e) {
        if (e.getSource() == btnShutdownAll) {
            for(String key : mapItems.keySet()) {
                ItemController itemController = mapItems.get(key).getController();
                itemController.initiateShutdown();
                updateServerMessage("[+] " + itemController.getHostName() + ": SHUTDOWN [+]");
            }
        }
        if (e.getSource() == btnAboutUs) {
            System.out.println("ABOUT US");
        }
    }

    public void handleKeyPressed(KeyEvent event) {
        if(event.getSource() == filterClient) {
            System.out.println(filterClient.getText());
        }
    }

    public void updateItemState(ClientData clientData) {
        ItemController itemController = mapItems.get(clientData.getMac()).getController();
        int num = 0;
        if(itemController.isUp() && !clientData.getState()) {
            num = -1;
        } else if(!itemController.isUp() && clientData.getState()){
            num = 1;
        }
        aliveClients.setText(Integer.toString(Integer.parseInt(aliveClients.getText()) + num));
        deadClients.setText(Integer.toString(Integer.parseInt(deadClients.getText()) - num));
        itemController.updateClientData(clientData);
        itemController.updateState(clientData.getState());
    }

    public void updateServerMessage(String message) {
        sl1.setText(sl2.getText());
        sl2.setText(message);
    }

    public void updateClientMessage(String message) {
        cl1.setText(cl2.getText());
        cl2.setText(message);
    }
}
