package com.vdm.controller;

import com.vdm.model.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

public class MainController {
    @FXML private Button editHotelsButton;
    @FXML private Button editClientsButton;

    @FXML
    public void initialize() {
        if (!UserSession.getInstance().isPrivileged()) {
            editHotelsButton.setDisable(true); 
        }
        
        if (!UserSession.getInstance().canManageClients()) {
            editClientsButton.setDisable(true);
        }
    }

    @FXML
    public void handleLogout(javafx.event.ActionEvent event) throws Exception {
        UserSession.getInstance().clearSession();
        loadView("/view/auth.fxml", event);
    }

    @FXML
    public void handleManageHotels(javafx.event.ActionEvent event) throws Exception {
        loadView("/view/hotel.fxml", event);
    }

    @FXML
    public void handleManageClients(javafx.event.ActionEvent event) throws Exception {
        loadView("/view/client.fxml", event);
    }

    private void loadView(String fxml, javafx.event.ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        javafx.scene.Scene scene = new javafx.scene.Scene(loader.load());
        javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    }

