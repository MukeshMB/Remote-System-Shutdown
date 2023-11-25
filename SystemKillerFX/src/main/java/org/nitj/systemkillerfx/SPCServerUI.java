package org.nitj.systemkillerfx;

import org.nitj.systemkiller.SPCServer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SPCServerUI extends Application {
    private double x, y;

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(SPCServerUI.class.getResource("design.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("SPCServer");
            stage.setScene(scene);

            scene.getRoot().setOnMousePressed(event -> {
                x = event.getSceneX();
                y = event.getSceneY();
            });
            scene.getRoot().setOnMouseDragged(event -> {
                stage.setX(event.getScreenX() - x);
                stage.setY(event.getScreenY() - y);
            });

            SPCServer spcServer = new SPCServer();
            spcServer.setController(fxmlLoader);
            spcServer.listen();

            stage.show();
            stage.setOnCloseRequest(event -> {
                System.out.println(event);
                spcServer.stopServer();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}