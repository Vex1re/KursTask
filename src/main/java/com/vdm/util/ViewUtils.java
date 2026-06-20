package com.vdm.util;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.io.IOException;

public class ViewUtils {
    public static void loadView(String fxml, ActionEvent event) throws IOException {
        URL resource = ViewUtils.class.getResource(fxml);
        if (resource == null) {
            throw new IOException("Cannot find resource: " + fxml);
        }
        FXMLLoader loader = new FXMLLoader(resource);
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
