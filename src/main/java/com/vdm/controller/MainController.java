package com.vdm.controller;

import com.vdm.model.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainController {
    @FXML private Button editHotelsButton;
    @FXML private Button editClientsButton;

    @FXML
    public void initialize() {
        if (!UserSession.getInstance().isPrivileged()) {
            editHotelsButton.setDisable(true);
            editClientsButton.setDisable(true);
        }
    }

    @FXML
    public void handleLogout() {
        UserSession.getInstance().clearSession();
    }
}
